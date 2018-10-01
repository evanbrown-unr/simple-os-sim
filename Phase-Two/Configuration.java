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
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * \brief Enumeration to represent the different config options.
 */
enum Option
{
    VERSION,
    MDF_PATH,
    PROCESS_TIME,
    MONITOR_TIME,
    HARD_DRIVE_TIME,
    KEYBOARD_TIME,
    SCANNER_TIME,
    PROJECTOR_TIME,
    MEMORY_TIME,
    LOG_TYPE,
    LOG_FILE_PATH
}

/**
 * \brief Enumeration to represent log type outputs.
 */
enum LogType
{
    MONITOR,
    FILE,
    BOTH
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
            System.err.println("ERROR: IO failed on file " + configFilePath +
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

        if (!configScan.nextLine().contains("Start"))
        {
            System.err.println("ERROR: Configuration file does not contain start prompt\n" +
                               "Exiting with return code 1");
            System.exit(1);
        }

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

        configScan.close();
        configFile.close();
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

    /**
     * \brief Getter for the operation cycle time.
     * \detials Uses a switch statement to modularize the method.
     *          Otherwise, I'd need to override the method for as
     *          many different cycle times there are.
     */
    public static int getCycleTime(String opName)
    {
        switch (opName)
        {
            case "run":
                return processorTime;
            case "hard drive":
                return hardDriveTime;
            case "keyboard":
                return keyboardTime;
            case "monitor":
                return monitorTime;
            case "projector":
                return projectorTime;
            case "scanner":
                return scannerTime;
            case "allocate": case "block":
                return memoryTime;
            case "begin": case "finish":
                return 0;
            default:
                return -1;
        }
    }
}
