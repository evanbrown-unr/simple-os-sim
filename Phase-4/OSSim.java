/**
 * Main class for the OS simulation.
 * Contains main driver method for the simulation.
 */

import java.io.FileNotFoundException;
import java.io.IOException;

class OSSim
{
    private static final int REQUIRED_ARGS = 1;

    public static void main(String[] args)
    {
        if (args.length != REQUIRED_ARGS)
        {
            System.err.println("Must supply configuration file as command line argument");
            System.exit(1);
        }

        OperatingSystem os = new OperatingSystem(args[0]);
        os.simulate();
    }
}