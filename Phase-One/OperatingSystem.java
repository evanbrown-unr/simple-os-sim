import java.util.LinkedList;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.InterruptedException;

class OperatingSystem
{
    private LinkedList<Process> readyQueue;
    private boolean isMetaDataLoaded;
    private Process.Operation startOperation,
                              endOperation;

    OperatingSystem() throws FileNotFoundException, IOException
    {
        readyQueue = new LinkedList<Process>();
        startOperation = new Process.Operation();
        endOperation = new Process.Operation();
        isMetaDataLoaded = false;
    }

    public void run() throws IOException, InterruptedException
    {
        //if (!isMetaDataLoaded)
           // Logger.logError("Meta data file has not been processed");

        Logger.startLogTimer();
        Logger.log("Starting OS simulation");

        while (!readyQueue.isEmpty())
        {
            Process currProcess = readyQueue.poll();

            Logger.log("OS: starting " + currProcess.getName());

            currProcess.run();

            Logger.log("OS: exiting " + currProcess.getName());
        }

        Logger.log("Ending OS simulation");
        Logger.writeToFile();
    }

    public boolean readMetaData() throws FileNotFoundException, IOException
    {
        String filePath = Configuration.getStringOption(Configuration.Option.MDF_PATH);

        if (filePath.isEmpty())
            Logger.logError("No meta data path found");

        FileInputStream metaDataFile = new FileInputStream(filePath);
        Scanner metaDataScanner = new Scanner(metaDataFile).useDelimiter(";|:|\\.");
        LinkedList<Process.Operation> currOperationsQueue = new LinkedList<Process.Operation>();
        Process currProcess = null;
        boolean foundSystemStart = false;
        int processCount = 0;

        if (!metaDataScanner.next().contains("Start Program"))
            Logger.logError("Meta data file does not contain start prompt");

        while (true)
        {
            Process.Operation operationBuff = getTokens(metaDataScanner);
            if (operationBuff == null) break;

            System.out.println("\n");
            operationBuff.showFields();
            System.out.println("\n");

            if ((!foundSystemStart) &&
                (operationBuff.type == Process.OperationType.SYSTEM) &&
                (operationBuff.operationName.contains("begin")))
            {
                startOperation = operationBuff;
                foundSystemStart = true;
                continue;
            }

            if (!foundSystemStart)
                Logger.logError("Missing OS system start opertation");

            // creating/ending app
            if (operationBuff.type == Process.OperationType.APP)
            {
                if (operationBuff.operationName.contains("begin"))
                {
                    currProcess = new Process("Process " + Integer.toString(processCount),
                                              currOperationsQueue);
                    readyQueue.add(currProcess);
                }
                else if (operationBuff.operationName == "finish")
                    processCount += 1;
            }

            // adding operations to current app process
            else
            {
                if (currProcess == null || currOperationsQueue == null)
                    Logger.logError("No application created for current operations");

                currProcess.enqueueOperation(operationBuff);
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
    private Process.Operation getTokens(Scanner metaDataScanner)
    {
        String jobString = metaDataScanner.next();
        if (jobString.contains("End Program")) return null;
        String[] tokens = jobString.split("\\{|\\}");
        for (String s : tokens) s.trim();
        return new Process.Operation(tokenToType(tokens[0]), tokens[1],
                                     Integer.parseInt(tokens[2]));
        //op.type = tokenToType(tokens[0]);
        //op.operationName = tokens[1];
        //op.numCycles = Integer.parseInt(tokens[2]);
        //return op;
    }

    private Process.OperationType tokenToType(String token)
    {
        if (token.contains("S"))
            return Process.OperationType.SYSTEM;
        if (token.contains("A"))
            return Process.OperationType.APP;
        if (token.contains("P"))
            return Process.OperationType.PROCESS;
        if (token.contains("I"))
            return Process.OperationType.INPUT;
        if (token.contains("O"))
            return Process.OperationType.OUTPUT;
        if (token.contains("M"))
            return Process.OperationType.MEMORY;
        return Process.OperationType.PROCESS;
    }
}