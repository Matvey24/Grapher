package threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Tasks {
    private final ConcurrentLinkedQueue<Runnable> queue;
    private final List<ThreadWorker> workers;
    private final Stack<ThreadWorker> freeWorkers;
    private final int threadCount;

    private boolean disposeOnFinish;

    public Tasks() {
        this(1);
    }

    public Tasks(int threadCount) {
        this.threadCount = threadCount;
        queue = new ConcurrentLinkedQueue<>();
        workers = new ArrayList<>(threadCount);
        freeWorkers = new Stack<>();
    }

    public synchronized void runTask(Runnable task) {
        queue.add(task);
        if (freeWorkers.empty() && workers.size() != threadCount) {
            addWorker();
        }
        if (!freeWorkers.empty()) {
            run();
        }
    }

    private void addWorker() {
        ThreadWorker worker = new ThreadWorker(this);
        workers.add(worker);
        freeWorkers.push(worker);
    }

    private void run() {
        ThreadWorker worker = freeWorkers.pop();
        worker.begin();
    }

    public boolean isFinished() {
        return queue.isEmpty() && workers.size() == freeWorkers.size();
    }

    synchronized Runnable getTask() {
        if (!queue.isEmpty()) return queue.remove();
        return null;
    }

    synchronized void onFinish(ThreadWorker worker) {
        freeWorkers.push(worker);
        if (disposeOnFinish && isFinished()) {
            dispose(worker);
        }
    }

    private void dispose(ThreadWorker main) {
        for (ThreadWorker w : workers) {
            if (w == main) {
                main.end();
            } else {
                w.dispose();
            }
        }
    }
    public synchronized void clearTasks(){
        queue.clear();
    }
    public void disposeOnFinish() {
        disposeOnFinish = true;
        if (isFinished()) dispose(null);
    }
}
