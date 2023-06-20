package server.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.auth.User;
import common.command.core.CommandType;
import common.command.core.Commandable;
import common.connection.*;
import common.data.*;

import common.exceptions.*;
import server.auth.UserManager;
import server.collection.CollectionManager;
import server.collection.LabWorkCollectionManager;
import server.commands.ServerCommandManager;
import server.database.DatabaseHandler;
import server.database.LabWorkDatabaseManager;
import server.database.UserDatabaseManager;
import server.exceptions.ServerOnlyCommandException;
import server.log.Log;

/**
 * server class
 */
public class Server {
    public final int BUFFER_SIZE = 10240;
    public final int MAX_CLIENTS = 10;

    private LabWorkCollectionManager collectionManager;
    private ServerCommandManager commandManager;
    private DatabaseHandler databaseHandler;
    private UserManager userManager;

    private int port;
    private DatagramChannel channel;

    private ExecutorService receiverThreadPool;
    private ExecutorService senderThreadPool;
    private ExecutorService requestHandlerThreadPool;

    private Queue<Map.Entry<InetSocketAddress, Request>> requestQueue;
    private Queue<Map.Entry<InetSocketAddress, Response>> responseQueue;
    private volatile boolean running;

    private Selector selector;

    private User hostUser;

    private void init(int p, Properties properties) throws ConnectionException, DatabaseException {
        running = true;
        port = p;

        hostUser = null;
        receiverThreadPool = Executors.newFixedThreadPool(MAX_CLIENTS);
        senderThreadPool = Executors.newFixedThreadPool(MAX_CLIENTS);
        requestHandlerThreadPool = Executors.newCachedThreadPool();

        requestQueue = new ConcurrentLinkedQueue<>();
        responseQueue = new ConcurrentLinkedQueue<>();

        databaseHandler = new DatabaseHandler(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
        userManager = new UserDatabaseManager(databaseHandler);
        collectionManager = new LabWorkDatabaseManager(databaseHandler, userManager);
        commandManager = new ServerCommandManager(this);


        try {
            collectionManager.deserializeCollection("");
        } catch (CollectionException e) {
            Log.logger.error(e.getMessage());
        }
        host(port);
        Log.logger.info("starting server");
    }

    private void host(int port) throws ConnectionException {
        try {
            if (channel != null && channel.isOpen()) channel.close();
            this.port = port;
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
        } catch (AlreadyBoundException e) {
            throw new PortAlreadyInUseException();
        } catch (IllegalArgumentException e) {
            throw new InvalidPortException();
        } catch (IOException e) {
            throw new ConnectionException("something went wrong during server initialization");
        }
    }

    public Server(int p, Properties properties) throws ConnectionException, DatabaseException {
        init(p, properties);
    }

    /**
     * receives request from client
     *
     * @throws ConnectionException
     * @throws InvalidDataException
     */
    public void receive() throws ConnectionException, InvalidDataException{
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
        InetSocketAddress clientAddress = null;
        Request request = null;
        try {
            clientAddress = (InetSocketAddress) channel.receive(buf);
            if (clientAddress == null) return; //no data to read
            Log.logger.info("received request from " + clientAddress.toString());
        } catch (ClosedChannelException e) {
            throw new ClosedConnectionException();
        } catch (IOException e) {
            throw new ConnectionException("something went wrong during receiving request");
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buf.array()));
            request = (Request) objectInputStream.readObject();
        } catch (ClassNotFoundException | ClassCastException | IOException e) {
            throw new InvalidReceivedDataException();
        }

        requestQueue.offer(new AbstractMap.SimpleEntry<>(clientAddress, request));
    }

    /**
     * sends response
     *
     * @param clientAddress
     * @param response
     * @throws ConnectionException
     */
    public void send(InetSocketAddress clientAddress, Response response) throws ConnectionException {
        if (clientAddress == null) throw new InvalidAddressException("no client address found");
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(response);
            channel.send(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()), clientAddress);
            Log.logger.info("sent response to " + clientAddress.toString());
        } catch (IOException e) {
            throw new ConnectionException("something went wrong during sending response");
        }
    }

    /**
     * runs server
     */
    public void run() {
        Thread threadServer = new Thread(() -> {
            while (running) {
                try {
                    selector.select();
                } catch (IOException e) {
                    continue;
                }
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isReadable()) {
                        receiverThreadPool.submit(new Receiver());
                        continue;
                    }
                    if (key.isWritable() && responseQueue.size() > 0) {
                        senderThreadPool.submit(new Sender(responseQueue.poll()));
                    }
                }
                if (requestQueue.size() > 0) {
                    requestHandlerThreadPool.submit(new RequestHandler(requestQueue.poll()));
                }
            }
        });
        threadServer.start();
        commandManager.consoleMode();
    }

    /**
     * close server and connection
     */
    public void close(){
        try{
            running = false;
            receiverThreadPool.shutdown();
            requestHandlerThreadPool.shutdown();
            senderThreadPool.shutdown();
            databaseHandler.closeConnection();
            channel.close();
        } catch (IOException e) {
            Log.logger.error("cannot close channel");
        }
    }

    public Commandable getCommandManager(){
        return commandManager;
    }

    public CollectionManager<LabWork> getCollectionManager() {
        return collectionManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public User getHostUser() {
        return hostUser;
    }

    public void setHostUser(User usr) {
        hostUser = usr;
    }

    private class Receiver implements Runnable {
        public void run() {
            try {
                receive();
            } catch (ConnectionException | InvalidDataException e) {
                Log.logger.error(e.getMessage());
            }
        }
    }

    private void handleRequest(InetSocketAddress address, Request request) {
        AnswerMsg answerMsg = new AnswerMsg();
        try {

            LabWork labWork = request.getLabWork();

            if (labWork != null) {
                labWork.setCreationDate(new Date());
            }
            request.setStatus(Request.Status.RECEIVED_BY_SERVER);

            if (commandManager.getCommand(request).getType() == CommandType.SERVER_ONLY) {
                throw new ServerOnlyCommandException();
            }
            answerMsg = (AnswerMsg) commandManager.runCommand(request);

            if (answerMsg.getStatus() == Response.Status.EXIT) {
                close();
            }
        } catch (CommandException e) {
            answerMsg.error(e.getMessage());
            Log.logger.error(e.getMessage());
        }
        responseQueue.offer(new AbstractMap.SimpleEntry<>(address, answerMsg));
    }

    private class RequestHandler implements Runnable {
        private final Request request;
        private final InetSocketAddress address;

        public RequestHandler(Map.Entry<InetSocketAddress, Request> requestEntry) {
            request = requestEntry.getValue();
            address = requestEntry.getKey();
        }

        public void run() {
            handleRequest(address, request);
        }
    }

    private class Sender implements Runnable {
        private final Response response;
        private final InetSocketAddress address;

        public Sender(Map.Entry<InetSocketAddress, Response> responseEntry) {
            response = responseEntry.getValue();
            address = responseEntry.getKey();
        }

        public void run() {
            try {
                send(address, response);
            } catch (ConnectionException e) {
                Log.logger.error(e.getMessage());
            }
        }
    }
}