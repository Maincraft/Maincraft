package me.main__.maincraft.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

public class MainScheduler implements BukkitScheduler {

    private final ExecutorService threadPool;
    // private final MainServer server;

    private Queue<AllInOne<?>> pendingTasks = new ConcurrentLinkedQueue<AllInOne<?>>();
    private Queue<AllInOne<?>> runningTasks = new ConcurrentLinkedQueue<AllInOne<?>>();
    private int nextId = 0;

    public MainScheduler(/* MainServer mainServer */) {
        threadPool = Executors.newCachedThreadPool();
        // server = mainServer;
    }

    /* the tick-method is executed in the main server thread! */
    public void tick() {
        for (AllInOne<?> task : pendingTasks) {
            if (task.isNow()) {
                pendingTasks.remove(task);
                runningTasks.add(task);
                if (task.isSync()) {
                    task.run();
                }
                else {
                    threadPool.submit(task);
                }
            }
        }
        for (AllInOne<?> task : runningTasks) {
            if (task.isDone()) {
                runningTasks.remove(task);
                if (task.getPeriod() != -1) {
                    task.resetPeriod();
                    pendingTasks.add(task);
                }
            }
        }
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task) {
        return scheduleSyncDelayedTask(plugin, task, 0L);
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        return scheduleSyncRepeatingTask(plugin, task, delay, -1);
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, final Runnable task, long delay, long period) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (delay < 0) {
            throw new IllegalArgumentException("Delay cannot be less than 0");
        }

        Callable<Void> work = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                task.run();
                return null;
            }
        };

        AllInOne<Void> newTask = new AllInOne<Void>(nextId++, plugin, work, delay, period, false);
        pendingTasks.add(newTask);
        return newTask.getTaskId();
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task) {
        return scheduleAsyncDelayedTask(plugin, task, 0L);
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        return scheduleAsyncRepeatingTask(plugin, task, delay, -1);
    }

    @Override
    public int scheduleAsyncRepeatingTask(Plugin plugin, final Runnable task, long delay,
            long period) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (delay < 0) {
            throw new IllegalArgumentException("Delay cannot be less than 0");
        }

        Callable<Void> work = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                task.run();
                return null;
            }
        };

        AllInOne<Void> newTask = new AllInOne<Void>(nextId++, plugin, work, delay, period, true);
        pendingTasks.add(newTask);
        return newTask.getTaskId();
    }

    @Override
    public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> task) {
        return null;
    }

    @Override
    public void cancelTask(int taskId) {
        for (BukkitTask bt : pendingTasks) {
            if (bt.getTaskId() == taskId)
                pendingTasks.remove(bt);
        }
    }

    @Override
    public void cancelTasks(Plugin plugin) {
        for (BukkitTask bt : pendingTasks) {
            if (bt.getOwner() == plugin)
                pendingTasks.remove(bt);
        }
    }

    @Override
    public void cancelAllTasks() {
        threadPool.shutdownNow();
        pendingTasks.clear();
    }

    @Override
    public boolean isCurrentlyRunning(int taskId) {
        for (BukkitTask bt : runningTasks) {
            if (bt.getTaskId() == taskId)
                return true;
        }
        return false;
    }

    @Override
    public boolean isQueued(int taskId) {
        for (BukkitTask bt : pendingTasks) {
            if (bt.getTaskId() == taskId)
                return true;
        }
        return false;
    }

    @Override
    public List<BukkitWorker> getActiveWorkers() {
        return Collections.unmodifiableList(new ArrayList<BukkitWorker>(runningTasks));
    }

    @Override
    public List<BukkitTask> getPendingTasks() {
        return Collections.unmodifiableList(new ArrayList<BukkitTask>(pendingTasks));
    }

}
