/**
 * Operating system module that wraps the functionality of the
 * other OS modules. This drives the simulation and reads the metadata.
 */

import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

class OperatingSystem
{
    private PriorityQueue<ProcessControlBlock> runningQueue;
    private boolean isMetaDataLoaded,
                    foundSystemBegin,
                    foundSystemFinish;
    private final Operation beginOperation = new Operation(OperationType.SYSTEM, "begin", 0),
                            finishOperation = new Operation(OperationType.SYSTEM, "finish", 0);

    /**
     * \brief Class constructor.
     * \details The meta data is read upon creation.
     *          The status flag is stored in an instance variable.
     */
    OperatingSystem(String configFilePath)
    {
        Configuration.init(configFilePath);
        Logger.init();
        ResourceManager.init();

        foundSystemBegin  = false;
        foundSystemFinish = false;
        runningQueue = new PriorityQueue<ProcessControlBlock>(100);
        readMetaData();
    }

    /**
     * \brief Loads meta data into OS environment;
     * \details Using regular expressions, I was able to parse
     *          the file into tokens, each one containing the data
     *          needed to define an operation.
     */
    public void readMetaData()
    {
        FileInputStream metaDataFile = null;
        String filePath = Configuration.mdfPath;

        if (filePath.isEmpty())
            Logger.logError("No meta data path found");

        try {
            metaDataFile = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            Logger.logError("Meta data file not found");
        }
        Scanner metaDataScanner = new Scanner(metaDataFile).useDelimiter("; |;\n|:\n|\\.");
        ProcessControlBlock currPCB = null;
        int appCount = 0;

        if (!metaDataScanner.next().contains("Start"))
            Logger.logError("Meta data file does not contain start prompt");

        while (metaDataScanner.hasNext())
        {
            Operation currOperation = extractOperation(metaDataScanner);

            // Found system begin operation
            if (currOperation.equals(beginOperation))
                foundSystemBegin = true;

            // Found system finish operation
            else if (currOperation.equals(finishOperation))
            {
                foundSystemFinish = true;
                break;
            }

            // System begin operation does not exist
            else if (!foundSystemBegin)
                Logger.logError("Missing OS system start operation");

            // Handling application processes
            else if (currOperation.type == OperationType.APP)
            {
                if (currOperation.name.equals("begin"))
                {
                    currPCB = new ProcessControlBlock(++appCount, 0, State.NEW);
                    runningQueue.add(currPCB);
                }
            }

            // Add operation to current application
            else if (currOperation.type == OperationType.PROCESS ||
                     currOperation.type == OperationType.INPUT ||
                     currOperation.type == OperationType.OUTPUT ||
                     currOperation.type == OperationType.MEMORY)
            {
                if (currPCB == null)
                    Logger.logError("No application created for current operations");

                currPCB.addOperation(currOperation);
            }

            else
                Logger.logError("Operation is not valid");
        }

        try {
            metaDataScanner.close();
            metaDataFile.close();
        } catch (IOException e) {
            Logger.logError("IO failed on " + Configuration.mdfPath);
        }
    }

    /**
     * \brief Runs through queue of PCB.
     * \details For this phase we are doing a simple
     *          FCFS sheduling algorithm. This is the most
     *          crucial part of the simulation.
     */
    public void simulate()
    {
        Logger.startMasterTimer();
        Logger.log("Simulator program starting");

        while (!runningQueue.isEmpty())
        {
            ProcessControlBlock currPCB = runningQueue.poll();
            Logger.log("OS: preparing process " + currPCB.getProcessID());
            currPCB.setProcessState(State.READY);
            Logger.log("OS: starting process " + currPCB.getProcessID());
            currPCB.setProcessState(State.RUNNING);
            currPCB.run();
            Logger.log("OS: removing process " + currPCB.getProcessID());
            currPCB.setProcessState(State.TERMINATED);
        }

        Logger.log("Simulator program ending");
        Logger.writeBufferToFile();
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
    private Operation extractOperation(Scanner metaDataScanner)
    {
        String[] tokens = null;

        try {
            tokens = metaDataScanner.next().split("\\{|\\}");
            for (String s : tokens) s.trim();
        } catch (Exception e) {
            Logger.logError("Failed to parse meta data operation");
        }

        return new Operation(tokenToType(tokens[0]), tokens[1],
                             Integer.parseInt(tokens[2]));
    }

    /**
     * \brief Converts valid token string to operation type.
     * \token Valid token string.
     * \return The type of operation represented by token.
     */
    private OperationType tokenToType(String token)
    {
        if (token.equals("S"))
            return OperationType.SYSTEM;
        if (token.equals("A"))
            return OperationType.APP;
        if (token.equals("P"))
            return OperationType.PROCESS;
        if (token.equals("I"))
            return OperationType.INPUT;
        if (token.equals("O"))
            return OperationType.OUTPUT;
        if (token.equals("M"))
            return OperationType.MEMORY;

        Logger.logError("Not a valid token");
        return null;
    }
}