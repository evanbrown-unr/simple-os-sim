/**
 * Module for handling operations in the OS simulation.
 * Done writing, has not been debugged.
 */

import java.util.LinkedList;
import java.util.Queue;
import java.lang.Thread;
import java.lang.Runnable;
import java.lang.InterruptedException;

class ProcessControlBlock
{
    /**
     * \brief Enumeration to abstract operation types.
     */
    public enum OperationType
    {
        SYSTEM(0),
        APP(1),
        PROCESS(2),
        INPUT(3),
        OUTPUT(4);

        public final int value;

        OperationType(int value)
        {
            this.value = value;
        }
    }

    /**
     * \brief Enumeration to abstract status codes;
     */
    public enum Status
    {
        SUCCESS(0),
        FAILURE(1);

        public final int value;

        Status(int value)
        {
            this.value = value;
        }
    }

    /**
     * \brief Class to abstract a primitive operation;
     */
    public class Operation
    {
        public OperationType type; ///< type of operation to perform
        public String operationName; ///< name of process
        public int numCycles; ///< amount of cycles to perform

        Operation()
        {
            operationName = new String();
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

                default:
                    return "no type";
            }
        }
    }

    /* Class instance variables */
    private String processName = new String();
    private Queue<Operation> operationsQueue;

    /**
     * \brief ProcessControlBloack constructor
     * \details If the reference to operationsQueue
     *          is null, then it is allocated in the
     *          constructor.
     * \param processName Name of new process.
     * \param operationsQueue Queue of all operations
     */
    ProcessControlBlock(String processName, LinkedList<Operation> operationsQueue)
    {
        this.processName = processName;

        if (operationsQueue != null)
            this.operationsQueue = operationsQueue;
        else
            this.operationsQueue = new LinkedList<Operation>();
    }

    public Status run() throws InterruptedException
    {
        while (!operationsQueue.isEmpty())
        {
            // local object must be final to run in thread
            final Operation current = operationsQueue.poll();

            if (current.type == OperationType.OUTPUT ||
                current.type == OperationType.INPUT)
            {
                // create new thread for io operation
                Thread ioThread = new Thread(new Runnable()
                {
                    public void run()
                    {
                        executeOperation(current);
                    }
                });

                ioThread.start(); // start new thread
                ioThread.join(); // wait until thread is finished
            }
            // execute on main thread
            else
                executeOperation(current);
        }

        return Status.SUCCESS;
    }

    public String getName()
    {
        return processName;
    }

    public int getCycleTime(String opName)
    {
        switch (opName)
        {
            case "hard drive":
                return Configuration.getIntOption(Configuration.Option.HARD_DRIVE_TIME);

            case "keyboard":
                return Configuration.getIntOption(Configuration.Option.KEYBOARD_TIME);

            case "monitor":
                return Configuration.getIntOption(Configuration.Option.MONITOR_TIME);

            case "printer":
                return Configuration.getIntOption(Configuration.Option.PRINTER_TIME);

            case "run":
                return Configuration.getIntOption(Configuration.Option.PROCESS_TIME);

            default:
                return 0;
        }
    }

    /**
     * /brief Adds operation to the end of queue.
     * /param op New operation.
     */
    public void enqueueOperation(Operation op)
    {
        operationsQueue.add(op);
    }

    /**
     * \brief Processes operation in front of queue.
     * \details Loops for required amount of cycles and
     *          then waits for the required amount of time
     *          for cycle. The logger outputs both
     *
     */
    public final void executeOperation(Operation op)
    {
        BasicTimer tempTimer = new BasicTimer();

        Logger.log(processName + ": start " + op.operationName + " " + op.typeToString());

        tempTimer.start(); // start local timer
        // for each number of cycles wait for required time
        for (int i = 0; i < op.numCycles; ++i)
            while (tempTimer.getElapsedTime() < getCycleTime(op.operationName));

        Logger.log(processName + ": end " + op.operationName + " " + op.typeToString());
    }
}