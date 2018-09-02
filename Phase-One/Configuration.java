
/**
 * This is the configuration module of the OS simulation.
 * It stores the current simulation settings, as well performing
 * the various operations on it.
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
    public static void init(String configFilePath) throws FileNotFoundException
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

    public static boolean readConfigFile(String configFilePath) throws FileNotFoundException
    {
        FileInputStream configFile = new FileInputStream(configFilePath);
    }

    public static void getNextOption(Scanner lineScan, String currentLine)
    {}
}
