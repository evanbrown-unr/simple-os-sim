
public class Memory
{
    private static int availableMemoryBits = Configuration.totalSystemMemoryKB * 1000 * 8;
    private static int currMemoryLocation = 0x0;

    public static String allocate()
    {
        if (currMemoryLocation >= availableMemoryBits) return "";
        String hexAddress = String.format("%08x", currMemoryLocation);
        currMemoryLocation += 0x80;
        return "0x" + hexAddress;
    }
}