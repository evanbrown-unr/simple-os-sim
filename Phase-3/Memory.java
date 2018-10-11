/**
 * Simple memory allocator used in the context of the
 * simulator. It generates an address counting upword in
 * increments of 0x80.
 */

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