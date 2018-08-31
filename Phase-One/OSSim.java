/**
 * Main class for the OS simulation.
 */

class OSSim
{
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.err.println("Must supply configuration and metadata files");
            System.err.println("Exiting with return code 1");
            System.exit(1);
        }

        OperatingSystem os = new OperatingSystem(); // create OS object
    }
}