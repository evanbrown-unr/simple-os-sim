/**
 * Process control block module for OS simulation.
 * Handles how processes and operations are handled
 * by the CPU.
 */

import java.util.LinkedList;
import java.lang.Thread;
import java.lang.Runnable;
import java.lang.InterruptedException;

class Process
{
    /**
     * \brief Enumeration to abstract operation types.
     */
    public static enum OperationType
    {
        SYSTEM(0),
        APP(1),
        PROCESS(2),
        INPUT(3),
        OUTPUT(4),
        MEMORY(5);

        public final int value;

        OperationType(int value)
        {
            this.value = value;
        }
    }

    /**
     * \brief Class to abstract a primitive operation;
     */
    public static class Operation
    {
        public OperationType type; ///< type of operation to perform
        public String name; ///< name of operation
        public int numCycles; ///< amount of cycles to perform

        Operation()
        {
            name = new String();
        }

        Operation(OperationType type, String name, int numCycles)
        {
            this.type = type;
            this.name = new String(name);
            this.numCycles = numCycles;
        }

        /**
         * \brief Returns object's type as a string.
         * \return Type in string format.
         */
        String typeToString()
        {
            switch (type)
            {
                case SYSTEM:
                    return "system";

                case APP:
                    return "app";

                case PROCESS:
                    return "process";

                case INPUT:
                    return "input";

                case OUTPUT:
                    return "output";

                case MEMORY:
                    return "memory";

                default:
                    return "no type";
            }
        }
    }

    /* Class instance variables */
    private String processName = new String(); ///< Name of PCB
    private LinkedList<Operation> operationsQueue; ///< PCB's operation queue

    /**
     * \brief ProcessControlBloack constructor
     * \details If the reference to operationsQueue
     *          is null, then it is allocated in the
     *          constructor.
     * \param processName Name of new process.
     * \param operationsQueue LinkedList of all operations
     */
    Process(String processName)
    {
        this.processName = processName;
        operationsQueue = new LinkedList<Operation>();
    }

    /**
     * \brief Runs the current PCB's operation queue.
     * \details If there is an IO operation in the queue,
     *  it executes its job in a different thread. Otherwise,
     *  it executes on main thread.
     */
    public void run() throws InterruptedException
    {
        while (!operationsQueue.isEmpty())
        {
            final Operation currOperation = operationsQueue.poll();

            if (currOperation.type == OperationType.INPUT ||
                currOperation.type == OperationType.OUTPUT)
            {
                Thread ioThread = new Thread(new Runnable()
                {
                    public void run()
                    {
                        executeOperation(currOperation);
                    }
                });

                ioThread.start();
                ioThread.join();
            }

            else
                executeOperation(currOperation);
        }
    }

    /**
     * \breif Getter for the process name.
     */
    public String getName()
    {
        return processName;
    }

    /**
     * \brief Getter for the process cycle time.
     */
    public int getCycleTime(String opName)
    {
        switch (opName)
        {
            case "run":
                return Configuration.getIntOption(Configuration.Option.PROCESS_TIME);

            case "hard drive":
                return Configuration.getIntOption(Configuration.Option.HARD_DRIVE_TIME);

            case "keyboard":
                return Configuration.getIntOption(Configuration.Option.KEYBOARD_TIME);

            case "monitor":
                return Configuration.getIntOption(Configuration.Option.MONITOR_TIME);

            case "projector":
                return Configuration.getIntOption(Configuration.Option.PROJECTOR_TIME);

            case "scanner":
                return Configuration.getIntOption(Configuration.Option.SCANNER_TIME);

            case "allocate": case "block":
                return Configuration.getIntOption(Configuration.Option.MEMORY_TIME);

            case "begin": case "finish":
                return 0;

            default:
                return -1;
        }
    }

    /**
     * /brief Adds operation to the end of LinkedList.
     * /param op New operation.
     */
    public void addOperation(Operation op)
    {
        operationsQueue.add(op);
    }

    /**
     * \brief Processes operation in front of LinkedList.
     * \details Loops for required amount of cycles and
     *          then waits for the required amount of time
     *          for cycle(s). The logger stores the currOperation elapsed
     *          time in a local variable so the time in both logs
     *          is consistent.
     */
    public final void executeOperation(Operation op)
    {
        BasicTimer tempTimer = new BasicTimer();
        int waitTime = op.numCycles * getCycleTime(op.name);

        Logger.log(processName + ": start " + op.name + " " + op.typeToString());

        tempTimer.start();
        while (tempTimer.getElapsedTime() < waitTime);

        Logger.log(processName + ": end " + op.name + " " + op.typeToString());
    }
}