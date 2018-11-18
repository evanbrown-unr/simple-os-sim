/**
 * \brief Enumeration to abstract type of operation.
 */
enum OperationType
{
    SYSTEM,
    APP,
    PROCESS,
    INPUT,
    OUTPUT,
    MEMORY;
}


/**
 * Operation is for representing anything that can be
 * performed by the CPU. For this phase there are six:
 * system, application, process, input, output, memory.
 */
class Operation
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
    public String typeToToken()
    {
        switch (type)
        {
            case SYSTEM:
                return "system";
            case APP:
                return "application";
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
     * \return True if equal, otherwise false.
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