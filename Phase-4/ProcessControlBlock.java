/**
 * Process control block module for OS simulation.
 * Handles how processes and operations are handled
 * by the CPU.
 */

import java.util.Queue;
import java.util.LinkedList;

/**
 * \brief Enumeration to represent process states.
 */
enum State
{
    NEW,
    READY,
    RUNNING,
    WAITING,
    TERMINATED
}

class ProcessControlBlock implements Comparable<ProcessControlBlock>
{
    private int processID;
    private int numIO;
    private int numOperations;
    private State processState;
    private Queue<Operation> operationQueue;

    /**
     * \brief ProcessControlBloack constructor
     * \details If the reference to operationQueue
     *          is null, then it is allocated in the
     *          constructor.
     * \param processName Name of new PCB
     * \param operationQueue LinkedList of all operations
     */
    ProcessControlBlock(int processID, State processState)
    {
        this.processID = processID;
        this.processState = processState;
        this.operationQueue = new LinkedList<Operation>();
    }

    /**
     * \brief Runs the PCB's operation queue.
     * \details If there is an IO operation in the queue,
     *          it executes its job in a different thread.
     *          Otherwise, it executes on main thread.
     */
    public void run()
    {
        while (!operationQueue.isEmpty())
        {
            final Operation currOperation = operationQueue.poll();

            if (currOperation.type == OperationType.INPUT ||
                currOperation.type == OperationType.OUTPUT)
            {

                Thread ioThread = new Thread(new Runnable()
                    {
                        public void run()
                        {
                            ResourceManager.acquireResource(currOperation);
                            executeOperation(currOperation);
                            ResourceManager.releaseResource(currOperation);
                        }
                    }
                );

                try {
                    ioThread.start();
                    ioThread.join();
                } catch (InterruptedException e) {
                    Logger.logError("IO thread was interrupted");
                }
            }


            else
                executeOperation(currOperation);
        }
    }

    /**
     * \brief Getter for the process ID.
     */
    public int getProcessID()
    {
        return processID;
    }

    /**
     * \brief Getter for the process state.
     */
    public State getProcessState()
    {
        return processState;
    }

    /**
     * \brief Setter for process state.
     */
    public void setProcessState(State processState)
    {
        this.processState = processState;
    }

    /**
     * \brief Increments number of IO operations.
     */
    public void incrementNumIO()
    {
        numIO++;
    }

    /**
     * \brief Increments total number of operations.
     */
    public void incrementNumOperations()
    {
        numOperations++;
    }

    /**
     * \brief Adds operation to the end of LinkedList.
     * \param op New operation.
     */
    public void addOperation(Operation op)
    {
        operationQueue.add(op);
    }

    /**
     * \brief Processes operation in front of LinkedList.
     * \details Loops for required amount of cycles and
     *          then waits for the required amount of time
     *          for cycle(s). The logger stores the operation's elapsed
     *          time in a local variable so the time in both logs
     *          is consistent. It is divided by 1000 to get the value in
     *          seconds.
     */
    public final void executeOperation(Operation op)
    {
        Timer tempTimer = new Timer();
        double waitTime = (double)(op.numCycles * Configuration.getCycleTime(op.name)) / 1000.0;

        if (op.name.equals("allocate"))
            Logger.log("Process " + processID + ": allocating " + op.typeToToken());
        else if (op.name.equals("run"))
            Logger.log("Process " + processID + ": start processing action");
        else if (op.name.equals("projector"))
            Logger.log("Process " + processID + ": start " + op.name + " " +
                       op.typeToToken() + " on PROJ " + ResourceManager.getCurrentProjector());
        else if (op.name.equals("hard drive"))
            Logger.log("Process " + processID + ": start " + op.name + " " +
                       op.typeToToken() + " on HDD " + ResourceManager.getCurrentHardDrive());
        else
            Logger.log("Process " + processID + ": start " + op.name + " " + op.typeToToken());

        tempTimer.start();
        while (tempTimer.getElapsedTime() < waitTime);

        if (op.name.equals("allocate"))
            Logger.log("Process " + processID + ": memory allocated at " + Memory.allocate());
        else if (op.name.equals("run"))
            Logger.log("Process " + processID + ": end processing action");
        else
            Logger.log("Process " + processID + ": end " + op.name + " " + op.typeToToken());
    }

    /**
     * \brief Used to determine ordering for the priority queues.
     */
    public int compareTo(ProcessControlBlock other)
    {
        if (Configuration.scheduleType == ScheduleType.PS)
            return other.numIO - this.numIO;
        else if (Configuration.scheduleType == ScheduleType.SJF)
            return this.numOperations - other.numOperations;
        else return 0;
    }
}
