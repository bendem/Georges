package be.bendem.bendembot.utils;

/**
 * @author bendem
 */
public abstract class RepeatingTask implements Runnable {

    private final long time;
    private long delay = 0;
    private final Thread thread;
    private volatile boolean cancelled = false;
    private volatile boolean idling    = false;
    private volatile boolean running   = false;
    private volatile boolean finished  = false;

    /**
     * Instanciate a task which will be repeated over time.
     *
     * @param name Name of the thread handled by the class.
     * @param time Pause time between each repetition.
     */
    protected RepeatingTask(String name, long time) {
        this.time = time;
        thread = new Thread(this, name);
    }

    /**
     * Task which will be executed over time.
     */
    protected abstract void work();

    /**
     * Start a task with delay.
     *
     * @param delay Number of milliseconds to wait before starting the task.
     */
    public final void start(long delay) {
        this.delay = delay;
        start();
    }

    /**
     * Start the task.
     *
     * @throws IllegalStateException If the task is running or has run.
     */
    public final void start() {
        if(running || finished) {
            throw new IllegalStateException("Thread has already been started");
        }
        thread.start();
    }

    /**
     * Internal task handling, calling this will start the task on the main.
     * thread
     */
    @Override
    public final void run() {
        running = true;
        if(delay > 0) {
            pause(delay);
        }
        while(!cancelled) {
            work();
            pause(time);
        }
        running = false;
        finished = true;
    }

    /**
     * Pause the current thread from defined number of milliseconds.
     * <p>
     * Note that the thread is marked as idling while paused.
     *
     * @param time The time the thread will pause.
     */
    protected final void pause(long time) {
        idling = true;
        try {
            Thread.sleep(time);
        } catch(InterruptedException ignored) {
        } finally {
            idling = false;
        }
    }

    /**
     * Cancels the task
     */
    public final void cancel() {
        try {
            cancel(0);
        } catch(InterruptedException ignored) {}
    }

    /**
     * Cancels the task and wait for it to stop for a defined amount of
     * milliseconds.
     *
     * @param blockingTime The number of milliseconds to wait
     * @throws InterruptedException
     */
    public final void cancel(long blockingTime) throws InterruptedException {
        cancelled = true;
        if(idling) {
            thread.interrupt();
        }
        if(blockingTime > 0) {
            thread.join(blockingTime);
        }
    }

    /**
     * Checks if the task is currently waiting.
     *
     * @return True if the task is idling, false otherwise.
     */
    public final boolean isIdling() {
        return idling;
    }

    /**
     * Checks if the task is currently running.
     *
     * @return True if the task is running, false otherwise.
     */
    public final boolean isRunning() {
        return running;
    }

    /**
     * Checks if the task is finished.
     *
     * @return True if the task is finished, false otherwise
     */
    public boolean isFinished() {
        return finished;
    }

}
