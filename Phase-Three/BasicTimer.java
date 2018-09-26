
/**
 * Basic timer interface for the simulation.
 * At first, i implemented it in milliseceonds, but
 * for phase two we are required to have a floating point
 * seconds value for our timestamp. So I just added a little
 * more math and
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
     * \brief Gives the amount of time elapsed since started (sec).
     * \detaills If the timer hasn't been stopped then it performs calculation
     *          based off of that moment. Have to perform diviion and explicit
     *          casting to get the data in seconds.
     * \return The elapsed time since start was called in seconds.
     */
    public double getElapsedTime()
    {
        if (isRunning)
            return (double)(System.currentTimeMillis() - startTime) / 1000.0;
        else
            return (double)(stopTime - startTime) / 1000.0;
    }
}