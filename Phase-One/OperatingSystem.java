/**
 * Operating system module that wraps the functionality of the
 * other OS modules. This drives the simulation and reads the metadata.
 */

import java.util.LinkedList;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.InterruptedException;

class OperatingSystem
{
    /* Class instance variables */
    private LinkedList<ProcessControlBlock> readyQueue;
    private boolean isMetaDataLoaded;
    private final Operation startOperation = new Operation(OperationType.SYSTEM, "begin", 0),
                            endOperation = new Operation(OperationType.SYSTEM, "finish", 0);

    /**
     * \brief Class constructor.
     * \details The meta data is read upon creation.
     *          The status flag is stored in an instance variable.
     */
    OperatingSystem() throws FileNotFoundException, IOException
    {
        readyQueue = new LinkedList<ProcessControlBlock>();
        isMetaDataLoaded = readMetaData();
    }

    /**
     * \brief Runs through queue of PCB.
     * \details For this phase we are doing a simple
     *          FCFS sheduling algorithm. This is the most
     *          crucial part of the simulation.
     */
    public void simulate() throws IOException, InterruptedException
    {
        if (!isMetaDataLoaded)
            Logger.logError("Meta data file has not been processed");

        Logger.startLogTimer();
        Logger.log("Beginning OS simulation");

        while (!readyQueue.isEmpty())
        {
            ProcessControlBlock currPCB = readyQueue.poll();

            Logger.log("OS: beginning " + currPCB.getName());

            currPCB.run();

            Logger.log("OS: finishing " + currPCB.getName());
        }

        Logger.log("Finishing OS simulation");
        Logger.writeToFile();
    }

    /**
     * \brief Loads meta data into OS environment;
     * \details Using regular expressions, I was able to parse
     *          the file into tokens, each one containing the data
     *          needed to define an operation.
     */
    public boolean readMetaData() throws FileNotFoundException, IOException
    {
        String filePath = Configuration.getStringOption(Option.MDF_PATH);

        if (filePath.isEmpty())
            Logger.logError("No meta data path found");

        FileInputStream metaDataFile = new FileInputStream(filePath);
        Scanner metaDataScanner = new Scanner(metaDataFile).useDelimiter(";|:|\\.");
        ProcessControlBlock currPCB = null;
        boolean foundSystemStart = false;
        int appCount = 0;

        if (!metaDataScanner.next().contains("Start Program"))
            Logger.logError("Meta data file does not contain start prompt");

        while (true)
        {
            Operation currOperation = getTokens(metaDataScanner);

            // Found system finish operation
            if (currOperation.equals(endOperation))
                break;

            // Found system begin operation
            else if (currOperation.equals(startOperation))
                foundSystemStart = true;

            // System begin operation does not exist
            else if (!foundSystemStart)
                Logger.logError("Missing OS system start opertation");

            // Handling application processes
            else if (currOperation.type == OperationType.APP)
            {
                if (currOperation.name.contains("begin"))
                {
                    currPCB = new ProcessControlBlock("Process " + Integer.toString(appCount));
                    readyQueue.add(currPCB);
                    ++appCount;
                }
            }

            // Add operation to current application
            else
            {
                if (currPCB == null)
                    Logger.logError("No application created for current operations");

                currPCB.addOperation(currOperation);
            }
        }

        metaDataFile.close();
        metaDataScanner.close();

        return true;
    }

    /**
     * \brief Reads a chunk of the meta data file that represents an operation.
     * \details Regex are used to extract one job meta datum.
     *          The string is then split three ways with another regex
     *          in order to store the respective tokens.
     * \param metaDataScanner Scanner that's tied to the meta data file.
     * \param op The operation object that will store the extracted tokens.
     * \return The operation with fields set to extracted metadata.
               If the end of the file has been reached, then returns null.
     */
    private Operation getTokens(Scanner metaDataScanner) throws FileNotFoundException, IOException
    {
        String jobString = metaDataScanner.next();
        if (jobString.contains("End Program")) return null;
        String[] tokens = jobString.split("\\{|\\}");
        for (String s : tokens) s.trim();
        return new Operation(tokenToType(tokens[0]), tokens[1],
                             Integer.parseInt(tokens[2]));
    }

    /**
     * \brief Converts valid token string to operation type.
     * \token Valid token string.
     * \return The type of operation represented by token.
     */
    private OperationType tokenToType(String token) throws FileNotFoundException, IOException
    {
        if (token.contains("S"))
            return OperationType.SYSTEM;
        if (token.contains("A"))
            return OperationType.APP;
        if (token.contains("P"))
            return OperationType.PROCESS;
        if (token.contains("I"))
            return OperationType.INPUT;
        if (token.contains("O"))
            return OperationType.OUTPUT;
        if (token.contains("M"))
            return OperationType.MEMORY;

        Logger.logError("Token does not represent valid type");
        return null;
    }
}