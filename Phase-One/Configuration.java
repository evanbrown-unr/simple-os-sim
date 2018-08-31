
/**
 * This is the configuration module of the OS simulation.
 * It stores the current simulation settings, as well performing
 * the various operations on it.
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

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
        LOG(7),
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
    public static String version,
                         mdfPath,
                         logFilePath;
    public static LogType logType;

    Configuration()
    {
        version = new String();
        mdfPath = new String();
        logFilePath = new String();
    }

    Configuration(String configFilePath)
    {}

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

            case LOG:
                return logType.value;

            default:
                return -1;
        }
    }

    public static void readConfigFile(String configFilePath)
    {}

    public static String getNextOption(FileInputStream ifstream, String option)
    {
        return new String();
    }
}