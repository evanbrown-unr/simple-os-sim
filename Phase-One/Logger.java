/**
 * Logger module for the OS simulation.
 * This is a singleton class meant for global access
 * across all of the simulation's modules.
 */

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.StringBuffer;

public class Logger
{
    public static boolean toConsole,
                          toFile;
    private static BasicTimer timer;
    public static String filePath;
    private static StringBuffer buffer;

    /**
     * \brief Initializes the class.
     * \details Since this class is never instantiate, there is no constructor.
     *          Therefore, this method must be called before the logger is used.
     *          This allcates memory to the required objects and sets the configuration.
     */
    public static void init(boolean newToFile, boolean newToConsole, String newFilePath)
    {
        timer = new BasicTimer();
        buffer = new StringBuffer();
        filePath = new String(newFilePath);
        toFile = newToFile;
        toConsole = newToConsole;

    }

    /**
     * \brief Function to wrap the timer's functionality.
     */
    public static void startLogTimer()
    {
        timer.start();
    }

    /**
     * \brief Logs the current elapsed time and a message.
     * \details It gets sent to either the console, the file,
     *          both, or neither.
     * \param msg String that gets sent through.
     */
    public static void log(String msg)
    {
        if (toConsole)
            System.out.println(timer.getElapsedTime() + " (msec) - " + msg);

        // need a new line
        if (toFile)
            buffer.append(timer.getElapsedTime() + " (msec) - " + msg + "\n");
    }

    /**
     * \brief Used to flush the string buffer to the specified logFile
     * \details The buffer must be converted to bytes before writing
     *          data to the file.
     */
    public static void writeToFile() throws FileNotFoundException, IOException
    {
        FileOutputStream outputFile = new FileOutputStream(filePath);
        byte[] bytes = buffer.toString().getBytes();
        outputFile.write(bytes);
        outputFile.close();
    }

    /**
     * \brief Used to log any error that might occur throughout the simulation.
     * \details Flushes to file and exits the program.
     */
    public static void logError(String errMsg) throws FileNotFoundException, IOException
    {
        log("Error: " + errMsg + "\nExiting with return code 1");
        writeToFile();
        System.exit(1);
    }
}