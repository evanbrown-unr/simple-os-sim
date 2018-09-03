
/**
 * This is the configuration module of the OS simulation.
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
    public enum Option
    {
        VERSION(0),
        MDF_PATH(1),
        PROCESS_TIME(2),
        MONITOR_TIME(3),
        HARD_DRIVE_TIME(4),
        PRINTER_TIME(5),
        KEYBOARD_TIME(6),
        LOG_TYPE(7),
        LOG_FILE_PATH(8);

        public final int value;

        Option(int value)
        {
            this.value = value;
        }
    }

    public enum LogType

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
                      printerTime,
                      keyboardTime;
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

            case PRINTER_TIME:
                return printerTime;

            case KEYBOARD_TIME:
                return keyboardTime;

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
        Scanner lineScan = new Scanner(configFile);

        lineScan.nextLine(); // ignore first line

        version = extractOption(lineScan);
        mdfPath = extractOption(lineScan);

        // time values must be integers
        processorTime = Integer.parseInt(extractOption(lineScan));
        monitorTime = Integer.parseInt(extractOption(lineScan));
        hardDriveTime = Integer.parseInt(extractOption(lineScan));
        printerTime = Integer.parseInt(extractOption(lineScan));
        keyboardTime = Integer.parseInt(extractOption(lineScan));

        // must determine log type with switch statement
        String logTypeString = extractOption(lineScan);
        logFilePath = extractOption(lineScan);

        switch (logTypeString)
        {
            case "Log to Both":
                logType = LogType.BOTH;
                break;
            case "Log to Monitor":
                logType = LogType.MONITOR;
                break;
            case "Log to File":
                logType = LogType.FILE;
                break;
            default:
                Logger.logError("Failed to read log type");
        }

        configFile.close();
        lineScan.close();
    }

    /**
     * \brief Helper function to parse configuration line
     *        and return the relevant data;
     * \details A lot is going on in that one line of code.
     *          First the line is read fron the scanner, then
     *          it is split at the colon. Since there is still a
     *          whitespace in front of the data string, it gets trimmed.
     * \param lineScan The Scanner that is attached to the config file.
     * \returns A string containing the valid option data.
     */
    private static String extractOption(Scanner lineScan)
    {
        return lineScan.nextLine().split(":")[1].substring(1);
    }
}
