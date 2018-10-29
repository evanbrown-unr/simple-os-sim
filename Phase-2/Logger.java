/**
 * Logger module for the OS simulation.
 * This is a singleton class meant for global access
 * across all of the simulation's modules.
 */

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Logger
{
    private static StringBuffer fileBuffer;
    private static String filePath;
    private static boolean toMonitor,
                           toFile;
    private static Timer masterTimer;


    /**
     * \brief Initializes the class.
     * \details Since this class is never instantiate, there is no constructor.
     *          Therefore, this method must be called before the logger is used.
     *          This allcates memory to the required objects and sets the configuration.
     *          This classes initialization depends upon the Configuration's initialization.
     */
    public static void init() throws FileNotFoundException, IOException
    {
        masterTimer = new Timer();
        fileBuffer = new StringBuffer();
        filePath = new String(Configuration.logFilePath);

        if (Configuration.logType == LogType.MONITOR)
        {
            toMonitor = true;
            toFile = false;
        }
        else if (Configuration.logType == LogType.FILE)
        {
            toMonitor = false;
            toFile = true;
        }
        else if (Configuration.logType == LogType.BOTH)
        {
            toMonitor = true;
            toFile = true;
        }
        else
            logError("Log type not defined");
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
        double timeStamp = masterTimer.getElapsedTime();

        if (toMonitor)
            System.out.println(String.format("%.6f", timeStamp) + " (sec) - " + msg);

        if (toFile)
            fileBuffer.append(String.format("%.6f", timeStamp) + " (sec) - " + msg + "\n");
    }

    /**
     * \brief Starts the master timer for the simulation
     */
    public static void startMasterTimer()
    {
        masterTimer.start();
    }

    /**
     * \brief Used to flush the string fileBuffer to the specified logFile
     * \details The fileBuffer must be converted to bytes before writing
     *          data to the file.
     */
    public static void writeToFile() throws FileNotFoundException, IOException
    {
        if (!toFile)
            return;

        FileOutputStream outputFile = new FileOutputStream(filePath);
        byte[] bytes = fileBuffer.toString().getBytes();
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

    /**
     * \brief Logs the configuration before the OS simulation runs
     */
    private static void outputConfiguration()
    {
        log("Configuration File Data:\n" +
            "Monitor : " + Configuration.monitorTime + " ms/cycle\n" +
            "Processor : " + Configuration.processorTime + " ms/cycle\n" +
            "Scanner : " + Configuration.scannerTime + " ms/cycle\n" +
            "Hard Drive : " + Configuration.hardDriveTime + " ms/cycle\n" +
            "Keyboard : " + Configuration.keyboardTime + " ms/cycle\n" +
            "Memory : " + Configuration.memoryTime + " ms/cycle\n" +
            "Projector : " + Configuration.projectorTime + " ms/cycle\n" +
	    "System memory : " + Configuration.totalSystemMemoryKB + " kbytes");

        if (Configuration.logType == LogType.MONITOR)
            log("Logged to : Monitor\n");
        else if (Configuration.logType == LogType.FILE)
            log("Logged to : " + Configuration.logFilePath + "\n");
        else if (Configuration.logType == LogType.BOTH)
            log("Logged to : Monitor and " + Configuration.logFilePath + "\n");
    }
}
