/**
 * Process control block module for OS simulation.
 * Handles how processes and operations are handled
 * by the CPU.
 */

import java.util.LinkedList;

class ProcessControlBlock
{
    private String processName;
    private LinkedList<Operation> operationQueue;

    /**
     * \brief ProcessControlBloack constructor
     * \details If the reference to operationQueue
     *          is null, then it is allocated in the
     *          constructor.
     * \param processName Name of new PCB
     * \param operationQueue LinkedList of all operations
     */
    ProcessControlBlock(String processName)
    {
        this.processName = processName;
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
     * \brief Getter for the process name.
     */
    public String getName()
    {
        return processName;
    }

    /**
     * \brief Getter for the operation cycle time.
     */
    public int getCycleTime(String opName)
    {
        switch (opName)
        {
            case "run":
                return Configuration.processorTime;
            case "hard drive":
                return Configuration.hardDriveTime;
            case "keyboard":
                return Configuration.keyboardTime;
            case "monitor":
                return Configuration.monitorTime;
            case "projector":
                return Configuration.projectorTime;
            case "scanner":
                return Configuration.scannerTime;
            case "allocate": case "block":
                return Configuration.memoryTime;
            case "begin": case "finish":
                return 0;
            default:
                return -1;
        }
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
     *          is consistent.
     */
    public final void executeOperation(Operation op)
    {
        int waitTime = op.numCycles * getCycleTime(op.name);

        Logger.timer.start();
        while (Logger.timer.getElapsedTime() < waitTime);

        Logger.log(op.typeToToken() + "{" + op.name + "}" +
                   op.numCycles + " - " + waitTime + " ms");
    }
}