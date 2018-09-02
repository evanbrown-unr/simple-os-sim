/**
 * Main class for the OS simulation.
 */

import java.io.FileNotFoundException;

class OSSim
{
    private static final int REQUIRED_ARGS = 2;

    public static void main(String[] args) throws FileNotFoundException
    {
        if (args.length != REQUIRED_ARGS)
        {
            System.err.println("Must supply configuration and metadata files");
            System.err.println("Exiting with return code 1");
            System.exit(1);
        }
        // create OS object
        OperatingSystem os = new OperatingSystem();
        Configuration.readConfigFile(args[0]);
        Logger.init();
    }
}