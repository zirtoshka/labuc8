package server.database;

import common.auth.User;
import common.data.*;
import common.exceptions.*;
import common.utils.DateConverter;
import server.auth.UserManager;
import server.collection.LabWorkCollectionManager;
import server.collection.LabWorkDequeManager;
import server.log.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static common.io.ConsoleOutputter.print;

public class LabWorkDatabaseManager extends LabWorkCollectionManager {
    //language=SQL
    private final static String INSERT_LABWORK_QUERY = "INSERT INTO LABWORKS (name, coordinates_x, coordinates_y," +
            "creation_date, minimal_point, personal_qualities_minimum, average_point, difficulty, discipline_name, " +
            "discipline_lecture_hours, user_login, id) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,DEFAULT) RETURNING id; ";
    //language=SQL
    private final static String CREATE_LABWORK_QUERY = "CREATE TABLE IF NOT EXISTS LABWORKS (" +
            "id SERIAL PRIMARY KEY CHECK ( id > 0 )," +
            "name VARCHAR(1237) NOT NULL CHECK (name <> '')," +
            "coordinates_x DOUBLE PRECISION," +
            "coordinates_y INTEGER NOT NULL CHECK (coordinates_y > -545 )," +
            "creation_date TEXT NOT NULL," +
            "minimal_point INTEGER NOT NULL CHECK(minimal_point > 0)," +
            "personal_qualities_minimum INTEGER NOT NULL CHECK(personal_qualities_minimum > 0)," +
            "average_point DOUBLE PRECISION CHECK(average_point > 0)," +
            "difficulty TEXT NOT NULL," +
            "discipline_name VARCHAR(1237) NOT NULL CHECK (discipline_name <> '')," +
            "discipline_lecture_hours INTEGER NOT NULL," +
            "user_login TEXT NOT NULL REFERENCES USERS(login));";
    //language=SQL
    private final static String SELECT_ID_LABWORK_QUERY = "SELECT nextval('id')";
    //language=SQL
    private final static String DELETE_LABWORK_QUERY = "DELETE FROM LABWORKS WHERE id = ?;";
    //language=SQL
    private final static String UPDATE_LABWORK_QUERY = "UPDATE LABWORKS SET " +
            "name=?," +
            "coordinates_x=?," +
            "coordinates_y=?," +
            "creation_date=?," +
            "minimal_point=?," +
            "personal_qualities_minimum=?," +
            "average_point=?," +
            "difficulty=?," +
            "discipline_name=?," +
            "discipline_lecture_hours=?," +
            "user_login=?" +
            "WHERE id=?";
    //language=SQL
    private final static String SELECT_MAX_NAME_LABWORK_QUERY = "SELECT MAX(name) FROM LABWORKS";
    //language=SQL
    private final static String DELETE_USER_LABWORK_QUERY = "DELETE FROM LABWORKS WHERE user_login=? RETURNING id";
    //language=SQL
    private final static String SELECT_ALL_LABWORK_QUERY = "SELECT * FROM LABWORKS";

    private final DatabaseHandler databaseHandler;
    private final UserManager userManager;

    public LabWorkDatabaseManager(DatabaseHandler c, UserManager userManager) throws DatabaseException {
        super();
        databaseHandler = c;
        this.userManager = userManager;
        create();
    }

    private void create() throws DatabaseException {
        try (PreparedStatement createStatement = databaseHandler.getPreparedStatement(CREATE_LABWORK_QUERY)) {
            createStatement.execute();
        } catch (SQLException e) {
            print(e.getMessage());
            throw new DatabaseException("cannot create labWork database");
        }
    }

    public Integer generateNextId() {
        try (PreparedStatement statement = databaseHandler.getPreparedStatement(SELECT_ID_LABWORK_QUERY)) {
            ResultSet r = statement.executeQuery();
            r.next();
            return r.getInt(1);
        } catch (SQLException e) {
            return 1;
        }
    }

    private void setLabWork(PreparedStatement statement, LabWork labWork) throws SQLException {
        statement.setString(1, labWork.getName());
        statement.setDouble(2, labWork.getCoordinates().getX());
        statement.setInt(3, labWork.getCoordinates().getY());
        statement.setString(4, DateConverter.dateToString(labWork.getCreationDate()));
        statement.setInt(5, labWork.getMinimalPoint());
        statement.setInt(6, labWork.getPersonalQualitiesMinimum());
        statement.setDouble(7, labWork.getAveragePoint());
        statement.setString(8, labWork.getDifficulty().toString());
        statement.setString(9, labWork.getDiscipline().getName());
        statement.setInt(10, labWork.getDiscipline().getLectureHours());
        statement.setString(11, labWork.getUserLogin());
    }

    private LabWork getLabWork(ResultSet resultSet) throws SQLException, InvalidDataException {
        Coordinates coordinates = new Coordinates(resultSet.getFloat("coordinates_x"), resultSet.getInt("coordinates_y"));
        Integer id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        Date creationDate = DateConverter.parseDate(resultSet.getString("creation_date"));
        Integer minimalPoint = resultSet.getInt("minimal_point");
        int personalQualitiesMinimum = resultSet.getInt("personal_qualities_minimum");
        Double averagePoint = resultSet.getDouble("average_point");
        String difficultyStr = resultSet.getString("difficulty");
        Difficulty difficulty = null;
        if (difficultyStr != null) {
            try {
                difficulty = Difficulty.valueOf(difficultyStr);
            } catch (IllegalArgumentException e) {
                throw new InvalidEnumException();
            }
        }
        Discipline discipline = new Discipline(resultSet.getString("discipline_name"), resultSet.getInt("discipline_lecture_hours"));
        LabWork labWork = new LabWork(name, coordinates, minimalPoint, personalQualitiesMinimum, averagePoint, difficulty, discipline);
        labWork.setCreationDate(creationDate);
        labWork.setId(id);
        labWork.setUserLogin(resultSet.getString("user_login"));
        if (!userManager.isPresent(labWork.getUserLogin())) throw new DatabaseException("no user found");

        return labWork;
    }

    @Override
    public void add(LabWork labWork) {
        databaseHandler.setCommitMode();
        databaseHandler.setSavepoint();
        try (PreparedStatement statement = databaseHandler.getPreparedStatement(INSERT_LABWORK_QUERY, true)) {
            setLabWork(statement, labWork);
            if (statement.executeUpdate() == 0) throw new DatabaseException();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (!resultSet.next()) throw new DatabaseException();
            labWork.setId(resultSet.getInt(resultSet.findColumn("id")));

            databaseHandler.commit();
        } catch (SQLException | DatabaseException e) {
            databaseHandler.rollback();
            throw new DatabaseException("cannot add to database");
        } finally {
            databaseHandler.setNormalMode();
        }
        super.addWithoutIdGeneration(labWork);
    }

    @Override
    public boolean removeById(Integer id) {
        String query = DELETE_LABWORK_QUERY;
        try (PreparedStatement statement = databaseHandler.getPreparedStatement(query)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseException("cannot remove from database");
        }
        return super.removeById(id);
    }


    @Override
    public boolean updateById(Integer id, LabWork labWork) {
        databaseHandler.setCommitMode();
        databaseHandler.setSavepoint();
        String sql = UPDATE_LABWORK_QUERY;
        try (PreparedStatement statement = databaseHandler.getPreparedStatement(sql)) {
            setLabWork(statement, labWork);
            statement.setInt(12, id);
            statement.execute();
            databaseHandler.commit();
        } catch (SQLException e) {
            databaseHandler.rollback();
            throw new DatabaseException("cannot update labWork #" + labWork.getId() + " in database");
        } finally {
            databaseHandler.setNormalMode();
        }
        return super.updateById(id, labWork);
    }

    @Override
    public boolean addIfMax(LabWork labWork) {
        String getMaxQuery = SELECT_MAX_NAME_LABWORK_QUERY;

        if (getCollection().isEmpty()) {
            add(labWork);
            return true;
        }
        databaseHandler.setCommitMode();
        databaseHandler.setSavepoint();
        try (Statement getStatement = databaseHandler.getStatement();
             PreparedStatement insertStatement = databaseHandler.getPreparedStatement(INSERT_LABWORK_QUERY)) {

            ResultSet resultSet = getStatement.executeQuery(getMaxQuery);
            if (!resultSet.next()) throw new DatabaseException("unable to add");

            String maxName = resultSet.getString(1);
            if (labWork.getName().compareTo(maxName) < 0)
                throw new DatabaseException("unable to add, max name is " + maxName + " current name is " + labWork.getName());

            setLabWork(insertStatement, labWork);

            labWork.setId(resultSet.getInt("id"));
            databaseHandler.commit();
        } catch (SQLException e) {
            databaseHandler.rollback();
            throw new DatabaseException("cannot add due to internal error");
        } finally {
            databaseHandler.setNormalMode();
        }
        super.addWithoutIdGeneration(labWork);
        return true;
    }


    public void clear(User user) {
        databaseHandler.setCommitMode();
        databaseHandler.setSavepoint();
        Set<Integer> ids = new HashSet<>();
        try (PreparedStatement statement = databaseHandler.getPreparedStatement(DELETE_USER_LABWORK_QUERY)) {
            statement.setString(1, user.getLogin());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                ids.add(id);
            }
        } catch (SQLException | CollectionException e) {
            databaseHandler.rollback();
            deserializeCollection("");
            throw new DatabaseException("cannot clear database");
        } finally {
            databaseHandler.setNormalMode();
        }
        removeAll(ids);
    }

    @Override
    public boolean deserializeCollection(String ignored) {
        boolean isHappened = true;
        if (!getCollection().isEmpty()) super.clear();
        String query = SELECT_ALL_LABWORK_QUERY;
        try (PreparedStatement selectAllStatement = databaseHandler.getPreparedStatement(query)) {
            ResultSet resultSet = selectAllStatement.executeQuery();
            int damagedElements = 0;
            while (resultSet.next()) {
                try {
                    LabWork labWork = getLabWork(resultSet);
                    if (!labWork.validate()) throw new InvalidDataException("element is damaged");
                    super.addWithoutIdGeneration(labWork);
                } catch (InvalidDataException | SQLException e) {
                    damagedElements += 1;
                }
            }
            if (super.getCollection().isEmpty()) throw new DatabaseException("nothing to load");
            if (damagedElements == 0) Log.logger.info("collection successfully loaded");
            else Log.logger.warn(damagedElements + " elements are damaged");
        } catch (SQLException e) {
            isHappened = false;
            throw new DatabaseException("cannot load");
        } finally {
            return isHappened;
        }
    }
}