/**
 * Main class for the OS simulation.
 * Contains main driver method for the simulation.
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

        Configuration.init(args[0]);
        Logger.init();
        OperatingSystem os = new OperatingSystem();
        os.simulate();
    }
}