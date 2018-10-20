import java.util.concurrent.Semaphore;
import java.util.LinkedList;

public class ResourceManager
{
    private static Semaphore[] projectorLocks = new Semaphore[Configuration.projectorQuantity];
    private static Semaphore[] hardDriveLocks = new Semaphore[Configuration.hardDriveQuantity];
    private static int projectorIndex = 0;
    private static int hardDriveIndex = 0;

    public static void init()
    {
        for (int i = 0; i < Configuration.projectorQuantity; i++)
            projectorLocks[i] = new Semaphore(1);

        for (int i = 0; i < Configuration.hardDriveQuantity; i++)
            hardDriveLocks[i] = new Semaphore(1);
    }

    public static void acquireResource(Operation op)
    {
        switch (op.name)
        {
            case "projector":
                projectorLocks[projectorIndex].acquireUninterruptibly();
                break;
            case "hard drive":
                hardDriveLocks[hardDriveIndex].acquireUninterruptibly();
                break;
            default:
        }
    }

    public static void releaseResource(Operation op)
    {
        switch (op.name)
        {
            case "projector":
                projectorLocks[projectorIndex].release();
                if (projectorIndex == Configuration.projectorQuantity-1)
                    projectorIndex = 0;
                else projectorIndex++;
                break;
            case "hard drive":
                hardDriveLocks[hardDriveIndex].release();
                if (hardDriveIndex == Configuration.hardDriveQuantity-1)
                    hardDriveIndex = 0;
                else hardDriveIndex++;
                break;
            default:
        }
    }

    public static int getCurrentProjector()
    {
        return projectorIndex;
    }

    public static int getCurrentHardDrive()
    {
        return hardDriveIndex;
    }
}