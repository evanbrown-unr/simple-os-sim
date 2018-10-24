/**
 * This class contains the resource management module. This controls the
 * simulation's access to hardware resources by defining a critical section
 * with the use of semaphore locks. This is definitely not an ideal algorithm, and
 * works best on simulations that run sequentially and do not perform time slicing.
 * I am planning on refactoring this module for the next phase. So far the only hardware
 * devices that are implemented are projectors and hard drives. Monitors and keyboards are
 * accounted for but no locking is done for them. This is in order to account for error handling
 * when the operation passed in doesn't have a valid resource name.
 */

import java.util.concurrent.Semaphore;
import java.util.LinkedList;

public class ResourceManager
{
    private static Semaphore[] projectorLocks = new Semaphore[Configuration.projectorQuantity];
    private static Semaphore[] hardDriveLocks = new Semaphore[Configuration.hardDriveQuantity];
    private static int projectorIndex = 0;
    private static int hardDriveIndex = 0;

    /**
     * \brief Initializs necessary fields before using any class utlities.
     * \details Allocates memory to each object in the arrays.
     */
    public static void init()
    {
        for (int i = 0; i < Configuration.projectorQuantity; i++)
            projectorLocks[i] = new Semaphore(1);

        for (int i = 0; i < Configuration.hardDriveQuantity; i++)
            hardDriveLocks[i] = new Semaphore(1);
    }

    /**
     * \brief Acquires the next available resource.
     * \details After being acquired, the resource must be released,
     *          in order to use it again.
     */
    public static void acquireResource(Operation op)
    {
        switch (op.name)
        {
            case "projector":
                try {
                    projectorLocks[projectorIndex].acquire();
                } catch (InterruptedException e) {
                    Logger.logError("Projector thread interrupted");
                }
                break;

            case "hard drive":
                try {
                    hardDriveLocks[hardDriveIndex].acquire();
                } catch (InterruptedException e) {
                    Logger.logError("Hard drive thread interrupted");
                }
                break;

            case "keyboard": break;
            case "monitor": break;

            default:
                Logger.logError("Operation not valid, unable to acquire resource");
        }
    }

    /**
     * \brief Releases an acquired resource.
     */
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

            case "keyboard": break;
            case "monitor": break;

            default:
                Logger.logError("Operation not valid, unable to release resouce");
        }
    }

    /**
     * \brief Returns the current projector in use.
     */
    public static int getCurrentProjector()
    {
        return projectorIndex;
    }

    /**
     * \brief Returns the current hardDrive in use.
     */
    public static int getCurrentHardDrive()
    {
        return hardDriveIndex;
    }
}