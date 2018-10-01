/**
 * Operating system module that wraps the functionality of the
 * other OS modules. This drives the simulation and reads the metadata.
 */

import java.util.LinkedList;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

class OperatingSystem
{
    private LinkedList<ProcessControlBlock> runningQueue;
    private boolean isMetaDataLoaded,
                    foundSystemBegin,
                    foundSystemFinish;
    private final Operation startOperation = new Operation(OperationType.SYSTEM, "start", 0),
                            endOperation = new Operation(OperationType.SYSTEM, "end", 0);

    /**
     * \brief Class constructor.
     * \details The meta data is read upon creation.
     *          The status flag is stored in an instance variable.
     */
    OperatingSystem(String configFilePath) throws FileNotFoundException, IOException
    {
        Configuration.init(configFilePath);
        Logger.init();

        foundSystemBegin  = false;
        foundSystemFinish = false;
        runningQueue = new LinkedList<ProcessControlBlock>();

        try {
            readMetaData();
        } catch (FileNotFoundException e) {
            Logger.logError("ERROR: Meta data file not found");
        } catch (IOException e) {
            Logger.logError("IO failed on file " + Configuration.mdfPath);
        }
    }

    /**
     * \brief Runs through queue of PCB.
     * \details For this phase we are doing a simple
     *          FCFS sheduling algorithm. This is the most
     *          crucial part of the simulation.
     */
    public void simulate() throws IOException, InterruptedException
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
        Logger.writeToFile();
    }

    /**
     * \brief Loads meta data into OS environment;
     * \details Using regular expressions, I was able to parse
     *          the file into tokens, each one containing the data
     *          needed to define an operation.
     */
    public void readMetaData() throws FileNotFoundException, IOException
    {
        String filePath = Configuration.mdfPath;

        if (filePath.isEmpty())
            Logger.logError("No meta data path found");

        FileInputStream metaDataFile = new FileInputStream(filePath);
        Scanner metaDataScanner = new Scanner(metaDataFile).useDelimiter("; |;\n|:\n|\\.");
        ProcessControlBlock currPCB = null;
        int appCount = 0;

        if (!metaDataScanner.next().contains("Start"))
            Logger.logError("Meta data file does not contain start prompt");

        while (metaDataScanner.hasNext())
        {
            Operation currOperation = getTokens(metaDataScanner);

            // Found system begin operation
            if (currOperation.equals(startOperation))
                foundSystemBegin = true;

            // Found system finish operation
            else if (currOperation.equals(endOperation))
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
                if (currOperation.name.equals("start"))
                {
                    currPCB = new ProcessControlBlock(++appCount, State.NEW);
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

        metaDataScanner.close();
        metaDataFile.close();
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
        String[] tokens = metaDataScanner.next().split("\\{|\\}");
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