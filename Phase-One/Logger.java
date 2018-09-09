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
    private static BasicTimer timer;
    private static StringBuffer buffer;

    public static boolean toMonitor,
                          toFile;
    public static String filePath;

    /**
     * \brief Initializes the class.
     * \details Since this class is never instantiate, there is no constructor.
     *          Therefore, this method must be called before the logger is used.
     *          This allcates memory to the required objects and sets the configuration.
     *          This classes initialization depends upon the Configuration's initialization.
     */
    public static void init() throws FileNotFoundException, IOException
    {
        timer = new BasicTimer();
        buffer = new StringBuffer();
        filePath = new String(Configuration.logFilePath);

        if (Configuration.logType == Configuration.LogType.MONITOR)
        {
            toMonitor = true;
            toFile = false;
        }
        else if (Configuration.logType == Configuration.LogType.FILE)
        {
            toMonitor = false;
            toFile = true;
        }
        else if (Configuration.logType == Configuration.LogType.BOTH)
        {
            toMonitor = true;
            toFile = true;
        }
        else
            logError("Log type not defined");
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
     *          both, or neither. The current elapsed time is
     *          stored in a local int, so the file and monitor
     *          contain the same log data.
     * \param msg String that gets sent through.
     */
    public static void log(String msg)
    {
        int elapsedTime = timer.getElapsedTime();

        if (toMonitor)
            System.out.println(elapsedTime + " (msec) - " + msg);

        if (toFile)
            buffer.append(elapsedTime + " (msec) - " + msg + "\n");
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
        log("ERROR: " + errMsg + "\nExiting with return code 1");
        writeToFile();
        System.exit(1);
    }
}