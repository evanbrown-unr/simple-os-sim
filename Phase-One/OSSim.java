/**
 * Main class for the OS simulation.
 */

class OSSim
{
    private static final int REQUIRED_ARGS = 2;

    public static void main(String[] args)
    {
        if (args.length != REQUIRED_ARGS)
        {
            System.err.println("Must supply configuration and metadata files");
            System.err.println("Exiting with return code 1");
            System.exit(1);
        }

        OperatingSystem os = new OperatingSystem(); // create OS object
    }
}