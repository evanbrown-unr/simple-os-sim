/**
 * Main class for the OS simulation.
 */

import java.io.FileNotFoundException;
import java.io.IOException;

class OSSim
{
    private static final int REQUIRED_ARGS = 1;

    public static void main(String[] args) throws FileNotFoundException, IOException,
                                                  InterruptedException
    {
        if (args.length != REQUIRED_ARGS)
        {
            System.err.println("Must supply configuration file");
            System.err.println("Exiting with return code 1");
            System.exit(1);
        }
        // create OS object
        OperatingSystem os = new OperatingSystem();
        Configuration.init(args[0]);
        Logger.init();
        Configuration.outputConfig();
        os.readMetaData();
        os.run();
    }
}