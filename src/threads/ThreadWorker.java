package threads;
import java.util.concurrent.CyclicBarrier;

public class ThreadWorker extends Thread{

  private final Tasks header;
  
  private final CyclicBarrier locker;
  
  private boolean disposed;
  
  public ThreadWorker(Tasks header){
    this.header = header;
    disposed = false;
    locker = new CyclicBarrier(2);
    start();
  }
  @Override
  public void run(){
    lock();
    while(!disposed){
      doQueue();
      lock();
    }
  }
  private void doQueue(){
    Runnable task;
    while((task = header.getTask()) != null){
      task.run();
    }
    header.onFinish(this);
  }
  private void lock(){
    if(!disposed)
    try{
      locker.await();
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  public void begin(){
     try{
      locker.await();
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  public void dispose(){
    end();
    begin();
  }
  public void end(){
    disposed = true;
    
  }
}
