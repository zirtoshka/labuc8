package common.io;
/**
 * Operates with console output
 */
public class OutputManager {
    /**
     * Static method to print message
     * @param msg
     */
    static public void print(Object msg)
    {
        System.out.println(msg);
    }
    /**
     * Static method to print error notification
     * @param msg Message
     */
    static public void printErr(String msg)
    {
        System.out.println("Err: " + msg);
    }


}
