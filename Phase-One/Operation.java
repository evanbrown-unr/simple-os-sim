import java.io.FileNotFoundException;

/**
 * \brief Enumeration to abstract type of operation.
 */
enum OperationType
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
 * Operation is for representing anything that can be
 * performed by the CPU. For this phase there are six:
 * system, application, process, input, output, memory.
 */
public class Operation
{
    public OperationType type;
    public String name;
    public int numCycles;

    /**
     * \brief Operation constructor.
     * \param type Type of operation.
     * \param name Name of operation.
     * \param numCycles Number of processor cycles to perform.
     */
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
    public String typeToString()
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
                return null;
        }
    }

    /**
     * \brief Overriden to compare Operation objects.
     * \param other Operation that reference is being compared to.
     */
    public boolean equals(Operation other)
    {
        if (type == other.type &&
            name.equals(other.name) &&
            numCycles == other.numCycles)
                return true;

        else
            return false;
    }
}