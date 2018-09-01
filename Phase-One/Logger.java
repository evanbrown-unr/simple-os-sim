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
    public static BasicTimer timer;
    public static boolean printDebug,
                          toConsole,
                          toFile;
    public static String filePath;
    private static StringBuffer buffer;

    private Logger()
    {
        filePath = new String();
        buffer = new StringBuffer();
    }

    public static void init(boolean newPrintDebug, boolean newToFile,
                            boolean newToConsole, String newFilePath)
    {
        printDebug = newPrintDebug;
        toFile = newToFile;
        toConsole = newToConsole;
        filePath = newFilePath;
    }

    public static void log(String msg)
    {
        if (toConsole)
            System.out.println(timer.getElapsedTime() + " - " + msg);

        // need a new line
        if (toFile)
            buffer.append(timer.getElapsedTime() + " - " + msg + "\n");


    }

    public static void writeToFile() throws FileNotFoundException, IOException
    {
        FileOutputStream outputFile = new FileOutputStream(filePath);
        byte[] bytes = buffer.toString().getBytes();
        outputFile.write(bytes);
        outputFile.close();
    }

    public static void logError(String errMsg)
    {
        System.err.println("Error: " + errMsg + "\nExiting with return code 1");
        System.exit(1);
    }
}