/**
 * This class contains the resource management module. This controls the
 * simulation's access to hardware resources by defining a critical section
 * with the use of semaphore locks. This is definitely not an ideal algorithm, and
 * works best on simulations that run sequentially and do not perform time slicing.
 * I am planning on refactoring this module for the next phase.
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
     * \brief acquires the next available resource available.
     * \details After being acquired, the resource must be released,
     *          in order to use it again throughout the simulation.
     */
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

    /**
     * \brief Releases an acquired resource.
     * \details
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
            default:
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