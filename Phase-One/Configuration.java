
/**
 * Configuration module of the OS simulation.
 * It stores the current simulation settings, as well performing
 * the various operations on it. I've written the read method to
 * handle config files that are only in the shown format.
 * In the future, I hope to improve flexibility and performance
 * by using regular expressions.
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Configuration
{
    /**
     * \brief Enumeration to represent the different config options.
     */
    public static enum Option
    {
        VERSION(0),
        MDF_PATH(1),
        PROCESS_TIME(2),
        MONITOR_TIME(3),
        HARD_DRIVE_TIME(4),
        PRINTER_TIME(5),
        KEYBOARD_TIME(6),
        SCANNER_TIME(7),
        PROJECTOR_TIME(8),
        MEMORY_TIME(9),
        LOG_TYPE(10),
        LOG_FILE_PATH(11);

        public final int value;

        Option(int value)
        {
            this.value = value;
        }
    }

    /**
     * \brief Enumeration to represent log type outputs.
     */
    public static enum LogType

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

    public static int processorTime,
                      monitorTime,
                      hardDriveTime,
                      keyboardTime,
                      scannerTime,
                      projectorTime,
                      memoryTime;
    public static LogType logType;
    public static String version,
                         mdfPath,
                         logFilePath;

    /**
     * \brief Initializes the configuration settings and reads the config file.
     * \details Since there is no constructor for this class, this method
     *          must be called before using any of its functionalities.
     * \param configFilePath The file path for the configuration file.
     */
    public static void init(String configFilePath) throws FileNotFoundException, IOException
    {
        version = new String();
        mdfPath = new String();
        logFilePath = new String();
        readConfigFile(configFilePath);
    }

    /**
     * \brief Helper method to return string options.
     * \details Theres no way to return both objects, so
     *          I created two functions for retrieving option
     *          data: one for integers, one for strings.
     * \param opt: An instance of the enum Option.
     * \return String value that corresponds to the case
     *         labels name.
     */
    public static String getStringOption(Option opt)
    {
        switch (opt)
        {
            case VERSION:
                return version;

            case MDF_PATH:
                return mdfPath;

            case LOG_FILE_PATH:
                return logFilePath;

            default:
                return "Option is not a valid string";
        }
    }

    /**
     * \brief Helper method to return integer options.
     * \details Theres no way to return both objects, so
     *          I created two functions for retrieving option
     *          data: one for integers, one for strings.
     * \param opt: An instance of the enum Option.
     * \return Integer value that corresponds to the case
     *         labels name.
     */
    public static int getIntOption(Option opt)
    {
        switch (opt)
        {
            case PROCESS_TIME:
                return processorTime;

            case MONITOR_TIME:
                return monitorTime;

            case HARD_DRIVE_TIME:
                return hardDriveTime;

            case KEYBOARD_TIME:
                return keyboardTime;

            case SCANNER_TIME:
                return scannerTime;

            case PROJECTOR_TIME:
                return projectorTime;

            case MEMORY_TIME:
                return memoryTime;

            case LOG_TYPE:
                return logType.value;

            default:
                return -1;
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

        // Time values must be integers
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
                Logger.logError("Failed to configure log type");
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
        return configScanner.nextLine().split(":")[1].trim();
    }
}
