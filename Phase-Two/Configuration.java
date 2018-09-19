/**
 * Configuration module of the OS simulation.
 * It stores the current simulation settings, as well performing
 * the various operation times on it on it.
 *
 * This class cannot utilize any of the Logger functions as this
 * is the first class that is initialized in the course of execution.
 * Instead of handling errors with the logError() method, we simply write
 * to stderr. I plan on fixing that soon, but for now it handles all errors
 * so it is good enough.
 */

import java.util.Scanner;
import java.io.FileInputStream;
<<<<<<< HEAD
import java.io.FileOutputStream;
=======
>>>>>>> 1a1d0ce492862c6731fcf61ac2679a2f4a8e86af
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * \brief Enumeration to represent the different config options.
 */
enum Option
{
    VERSION(0),
    MDF_PATH(1),
    PROCESS_TIME(2),
    MONITOR_TIME(3),
    HARD_DRIVE_TIME(4),
    KEYBOARD_TIME(5),
    SCANNER_TIME(6),
    PROJECTOR_TIME(7),
    MEMORY_TIME(8),
    LOG_TYPE(9),
    LOG_FILE_PATH(10);

    public final int value;

    Option(int value)
    {
        this.value = value;
    }
}

/**
 * \brief Enumeration to represent log type outputs.
 */
enum LogType
{
    MONITOR(0),
    FILE(1),
    BOTH(2);

    public final int value;

    LogType(int value)
    {
        this.value = value;
    }
}



public class Configuration
{
    public static int processorTime,
                      monitorTime,
                      hardDriveTime,
                      keyboardTime,
                      scannerTime,
                      projectorTime,
                      memoryTime;
    public static String version,
                         mdfPath,
                         logFilePath;
    public static LogType logType;

    /**
     * \brief Initializes the configuration settings and reads the config file.
     * \details Since there is no constructor for this class, this method
     *          must be called before using any of its functionalities.
     * \param configFilePath The file path for the configuration file.
     */
    public static void init(String configFilePath)
    {
        version = new String();
        mdfPath = new String();
        logFilePath = new String();

        try {
            readConfigFile(configFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Configuration file not found\n" +
                               "Exiting with return code 1");
            System.exit(1);
        } catch (IOException e) {
<<<<<<< HEAD
            System.err.println("IO failed on file " + configFilePath +
=======
            System.err.println("ERROR: IO failed on file " + configFilePath +
>>>>>>> 1a1d0ce492862c6731fcf61ac2679a2f4a8e86af
                               "\nExiting with return code 1");
            System.exit(1);
        }
    }

    /**
     * \brief Reads the configuration file and sets configuration
     *        settings.
     * \details Called in class's initialization method.
     * \param A string containing the configuration file path.
     */
    private static void readConfigFile(String configFilePath) throws FileNotFoundException, IOException
    {
        FileInputStream configFile = new FileInputStream(configFilePath);
        Scanner configScan = new Scanner(configFile);

        configScan.nextLine(); // Ignore first line

        version = extractOption(configScan);
        mdfPath = extractOption(configScan);

        monitorTime = Integer.parseInt(extractOption(configScan));
        processorTime = Integer.parseInt(extractOption(configScan));
        scannerTime = Integer.parseInt(extractOption(configScan));
        hardDriveTime = Integer.parseInt(extractOption(configScan));
        keyboardTime = Integer.parseInt(extractOption(configScan));
        memoryTime = Integer.parseInt(extractOption(configScan));
        projectorTime = Integer.parseInt(extractOption(configScan));

        String logTypeString = extractOption(configScan);
        logFilePath = extractOption(configScan);

        switch (logTypeString.toLowerCase())
        {
            case "log to both":
                logType = LogType.BOTH;
                break;
            case "log to monitor":
                logType = LogType.MONITOR;
                break;
            case "log to file":
                logType = LogType.FILE;
                break;
            default:
                System.err.println("ERROR: Failed to configure log type\n" +
                                   "Exiting with return code 1");
                System.exit(1);
        }

        configFile.close();
        configScan.close();
    }


    /**
     * \brief Helper function to parse configuration line
     *        and return the relevant data;
     * \details A lot is going on in that one line of code.
     *          First the line is read fron the scanner, then
     *          it is split at the colon. Any whitespace surrounding
     *          the data string is trimmed.
     * \param configScan The Scanner that is attached to the config file.
     * \returns A string containing the valid option data.
     */
    private static String extractOption(Scanner configScanner)
    {
        String option = null;

        try {
            option = configScanner.nextLine().split(":")[1].trim();
        } catch (Exception e) {
            System.err.println("ERROR: Missing required configuration data\n" +
                               "Exiting with return code 1");
            System.exit(1);
        }

        return option;
    }
}
