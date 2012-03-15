package tk.maincraft.network;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;



public class NetworkClientActionAllInOne<T> implements Future<T>, Runnable {

    private final PacketClient client;
    private final PacketClientAction<T> task;

    private Thread thread = null;
    private boolean done = false;
    private boolean running = false;
    private boolean cancelled = false;
    private Exception exception = null;

    private T result;

    public NetworkClientActionAllInOne(PacketClient client, PacketClientAction<T> task) {
        this.client = client;
        this.task = task;
    }

    @Override
    public void run() {
        synchronized (this) {
            if (cancelled)
                return;
            running = true;
        }

        thread = Thread.currentThread();

        try {
            T theResult = task.call(client);
            synchronized (result) {
                result = theResult;
            }
            // these are only executed in the clienthandlingthreads,
            // so they're allowed to throw Exceptions
        } catch (Exception e) {
            this.exception = e;
        }

        synchronized (this) {
            running = false;
            done = true;
            this.notify();
        }
    }

    public Thread getThread() {
        synchronized (thread) {
            return thread;
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        synchronized (this) {
            if (cancelled) {
                return false;
            }
            cancelled = true;
            if (running) {
                getThread().interrupt();
            }
            else if (done)
                return false;

            return true;
        }
    }

    @Override
    public boolean isCancelled() {
        synchronized (this) {
            return cancelled;
        }
    }

    @Override
    public boolean isDone() {
        synchronized (this) {
            return done;
        }
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        try {
            return get(0, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
            TimeoutException {
        synchronized (this) {
            if (done)
                return getResult();

            this.wait(TimeUnit.MILLISECONDS.convert(timeout, unit));

            if (done)
                return getResult();
            else
                throw new TimeoutException();
        }
    }

    private T getResult() throws CancellationException, ExecutionException {
        if (cancelled) {
            throw new CancellationException();
        }
        else if (exception != null) {
            throw new ExecutionException(exception);
        }
        return result;
    }

}
