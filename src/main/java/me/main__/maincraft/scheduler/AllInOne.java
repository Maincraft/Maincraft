package me.main__.maincraft.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

public class AllInOne<T> implements BukkitWorker, BukkitTask, Future<T>, Runnable {

    private final int id;
    private final Plugin owner;
    private final Callable<T> task;
    private final long delay;
    private final long period;
    private final boolean sync;
    private long leftTicks;

    private Thread thread = null;
    private boolean done = false;
    private boolean running = false;
    private boolean cancelled = false;
    private Exception exception = null;

    private T result;

    public AllInOne(int taskId, Plugin plugin, Callable<T> task, long delay, long period, boolean isSync) {
        this.id = taskId;
        this.owner = plugin;
        this.task = task;
        this.delay = delay;
        this.period = period;
        this.sync = isSync;

        this.leftTicks = delay;
    }

    public boolean isNow() {
        return --leftTicks <= 0;
    }

    @Override
    public int getTaskId() {
        return id;
    }

    public Callable<T> getTask() {
        return task;
    }

    public long getDelay() {
        return delay;
    }

    public long getPeriod() {
        return period;
    }

    public void resetPeriod() {
        this.leftTicks = period;
    }

    @Override
    public Plugin getOwner() {
        return owner;
    }

    @Override
    public boolean isSync() {
        return sync;
    }

    @Override
    public void run() {
        synchronized (this) {
            if (cancelled)
                return;
            running = true;
        }

        synchronized (thread) {
            thread = Thread.currentThread();
        }

        try {
            T theResult = task.call();
            synchronized (result) {
                result = theResult;
            }
        } catch (Exception e) {
            this.exception = e;
        }

        synchronized (this) {
            running = false;
            done = true;
            this.notify();
        }
    }

    @Override
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
            Bukkit.getScheduler().cancelTask(getTaskId());
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
