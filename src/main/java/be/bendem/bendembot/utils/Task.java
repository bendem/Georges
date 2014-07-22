package be.bendem.bendembot.utils;

import org.apache.commons.lang3.Validate;

/**
 * @author bendem
 */
public abstract class Task implements Runnable {

    private Thread thread;
    private volatile boolean running = false;

    public final void start() {
        Validate.isTrue(!running, "The task is alreay running");
        thread = new Thread(this);
        thread.start();
    }

    public final void run() {
        running = true;
        work();
        running = false;
    }

    protected abstract void work();

    public final boolean isRunning() {
        return running;
    }

}
