/**
 * Module for handling operations in the OS simulation.
 */

import java.util.LinkedList;
import java.util.Queue;

class Process
{
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

    public class Operation
    {
        public OperationType type;
        public String operationName;
        public int numCycles;

        Operation()
        {
            operationName = new String();
        }
    }

    private String processName;
    private Queue<Operation> operationsQueue;

    Process(String processName, LinkedList<Operation> operationsQueue)
    {
        this.processName = processName;

        if (operationsQueue != null)
            this.operationsQueue = operationsQueue;
        else
            operationsQueue = new LinkedList<Operation>();
    }

    public Status run()
    {
        return Status.SUCCESS;
    }

    public String getName()
    {
        return processName;
    }

    public int getCycleTime(String operationName)
    {
        switch (operationName)
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

    public void enqueueOperation(Operation op)
    {
        operationsQueue.add(op);
    }

    public void executeOperation(Operation op)
    {}
}