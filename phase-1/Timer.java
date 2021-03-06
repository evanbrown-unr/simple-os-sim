
/**
 * Basic timer interface for the simulation.
 * At first, i implemented it in milliseceonds, but
 * for phase two we are required to have a floating point
 * seconds value for our timestamp.
 * After another refactor, the time is given in nanosecond
 * increments. The format is still in seconds, but this was done
 * for project specification, as they wanted microsecond precision
 * for logging timestamps. I don't necessarily agree, because the
 * underlying prescalar used for the system timer with nanoTime() is
 * not for precise.
 */

class Timer
{
    private long startTime;
    private long stopTime;
    private boolean isRunning;

    /**
     * \brief Timer constructor.
     */
    Timer()
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
        isRunning = true;
        startTime = System.currentTimeMillis();
    }

    /**
     * \brief Stops the timer.
     */
    public void stop()
    {
        if (isRunning)
        {
            isRunning = false;
            stopTime = System.currentTimeMillis();
        }
    }

    /**
     * \brief Gives the amount of time elapsed since started (sec).
     * \detaills If the timer hasn't been stopped then it performs calculation
     *          based off of that moment. Have to perform diviion and explicit
     *          casting to get the data in seconds.
     * \return The elapsed time since start was called in seconds.
     */
    public int getElapsedTime()
    {
        if (isRunning)
            return (int)(System.currentTimeMillis() - startTime);
        else
            return (int)(stopTime - startTime);
    }
}