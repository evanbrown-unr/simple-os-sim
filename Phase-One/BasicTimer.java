
/**
 * Basic timer interface for the simulation.
 * Once the timer is started, you can either stop it
 * and then return the elapsed time, or return the elapsed
 * time while the timer still runs. This class can be extended
 * for further functionality.
 */

class BasicTimer
{
    protected long startTime;
    protected long stopTime;
    protected boolean isRunning;

    BasicTimer()
    {
        startTime = 0;
        stopTime = 0;
        isRunning = false;
    }

    public void start()
    {
        startTime = System.currentTimeMillis();
        isRunning = true;
    }

    public void stop()
    {
        if (isRunning)
        {
            stopTime = System.currentTimeMillis();
            isRunning = false;
        }
    }

    public int getElapsedTime()
    {
        if (isRunning)
            return (int)(System.currentTimeMillis() - startTime);
        else
            return (int)(stopTime - startTime);
    }
}