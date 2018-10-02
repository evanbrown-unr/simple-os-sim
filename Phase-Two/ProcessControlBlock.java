/**
 * Process control block module for OS simulation.
 * Handles how processes and operations are handled
 * by the CPU.
 */

import java.util.LinkedList;
import java.util.Random;

enum State
{
    NEW,
    READY,
    RUNNING,
    WAITING,
    TERMINATED
}

class ProcessControlBlock
{
    private int processID;
    private State processState;
    private LinkedList<Operation> operationQueue;

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
    public void run() throws InterruptedException
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
                            executeOperation(currOperation);
                        }
                    }
                );

                ioThread.start();
                ioThread.join();
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

    public void setProcessState(State processState)
    {
        this.processState = processState;
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
        BasicTimer tempTimer = new BasicTimer();
        double waitTime = (double)(op.numCycles * Configuration.getCycleTime(op.name)) / 1000.0;

        if (op.name.equals("allocate"))
            Logger.log("Process " + processID + ": allocating " + op.typeToToken());
        else if (op.name.equals("run"))
            Logger.log("Process " + processID + ": start processing action");
        else
            Logger.log("Process " + processID + ": start " + op.name + " " + op.typeToToken());

        tempTimer.start();
        while (tempTimer.getElapsedTime() < waitTime);

        if (op.name.equals("allocate"))
            Logger.log("Process " + processID + ": memory allocated at " + generateAddress());
        else if (op.name.equals("run"))
            Logger.log("Process " + processID + ": end processing action");
        else
            Logger.log("Process " + processID + ": end " + op.name + " " + op.typeToToken());
    }

    /**
     * \brief Generates a random memory address.
     * \details Used for the allocate operation in PCB.
     *		This is determined by the max system memory field
     *		in the configuration file. For example, if given a
     *	 	1024 kbytes, then 1024*1000*8 bits exist. Which means
     *		the address will never be higher than that value.
     * \return A string containing a memory address in hex format.
     */
    private String generateAddress()
    {
        return "0x" + Integer.toHexString(new Random().nextInt(Configuration.totalSystemMemoryKB * 8000));
    }
}
