package controller;

import threads.Tasks;

public class MyTimer implements Runnable{
    private int delay;
    private final Tasks tasks;
    private final Runnable r;
    private boolean started;
    public MyTimer(int delay, Runnable r) {
        setDelay(delay);
        tasks = new Tasks();
        this.r = r;
    }
    public void run(){
        try {
            while (started) {
                r.run();
                //noinspection BusyWait
                Thread.sleep(delay);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        tasks.clearTasks();
    }
    public synchronized boolean isRunning(){
        return started;
    }
    public void setDelay(int delay){
        this.delay = Math.max(1, delay);
    }
    public synchronized void start(){
        started = true;
        tasks.runTask(this);
    }
    public synchronized void stop(){
        started = false;
    }

}
