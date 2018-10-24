/**
 * Simple memory allocator used in the context of the
 * simulator. It generates an address counting upword in
 * increments of specified by total block size in the configuration
 * file.
 */

public class Memory
{
    private static int availableMemoryBits = Configuration.totalSystemMemoryKB * 1000 * 8;
    private static int blockSize = Configuration.memoryBlockSize;
    private static int currMemoryLocation = 0x0;

    public static String allocate()
    {
        if (currMemoryLocation >= (availableMemoryBits - blockSize))
            Logger.logError("Exceeded system memory");
        String hexAddress = String.format("%08x", currMemoryLocation);
        currMemoryLocation += blockSize;
        return "0x" + hexAddress;
    }
}