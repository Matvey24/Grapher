package threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Tasks {
    private ConcurrentLinkedQueue<Runnable> queue;
    private List<ThreadWorker> workers;
    private Stack<ThreadWorker> freeWorkers;
    private int threadCount;

    private boolean disposeOnFinish;

    public Tasks() {
        this(1);
    }

    public Tasks(int threadCount) {
        this.threadCount = threadCount;
        queue = new ConcurrentLinkedQueue<Runnable>();
        workers = new ArrayList<ThreadWorker>(threadCount);
        freeWorkers = new Stack<ThreadWorker>();
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
