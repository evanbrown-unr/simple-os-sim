
/**
 * Basic timer interface for the simulation.
 * Once the timer is started, you can either stop it
 * and then return the elapsed time, or return the elapsed
 * time while the timer still runs. This class can be extended
 * for further functionality.
 */

class BasicTimer
{
    private long startTime;
    private long stopTime;
    private boolean isRunning;

    /**
     * \brief Timer constructor.
     */
    BasicTimer()
    {
        startTime = 0;
        stopTime = 0;
        isRunning = false;
    }

    /**
     * \brief Starts the timer
     */
    public void start()
    {
        startTime = System.currentTimeMillis();
        isRunning = true;
    }

    /**
     * \brief Stops the timer.
     */
    public void stop()
    {
        if (isRunning)
        {
            stopTime = System.currentTimeMillis();
            isRunning = false;
        }
    }

    /**
     * \brief Gives the amount of time elapsed since started (msec).
     * \detaills If the timer hasn't been stopped then it performs calculation
     *          based off of that moment.
     * \return The elapsed time since start was called in msec.
     */
    public int getElapsedTime()
    {
        if (isRunning)
            return (int)(System.currentTimeMillis() - startTime);
        else
            return (int)(stopTime - startTime);
    }
}