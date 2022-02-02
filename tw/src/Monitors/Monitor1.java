package Monitors;

import Interfaces.Monitor;
import Interfaces.Observer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//LOCK ON 2 CONDITIONS
public class Monitor1 implements Monitor {

    private int buffer = 0;
    private int bufferMAX;
    private Observer observer;
    private boolean comments;

    private Lock lock = new ReentrantLock(true);

    Condition condition1 = lock.newCondition();
    Condition condition2 = lock.newCondition();

    public Monitor1(int maxBuffer, Observer observer, boolean comments){
        this.bufferMAX = maxBuffer;
        this.observer = observer;
        this.comments = comments;
    }


    public void produce(int id,int val){
        lock.lock();
        while(buffer + val > bufferMAX){
            try {
                condition1.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(comments){
            System.out.println("id " + id + " Produced " + val + " to " + buffer);
        }
        buffer += val;
        observer.produced(id,val);
        condition2.signal();
        lock.unlock();

    }

    public void consume(int id,int val){
        lock.lock();
        while(buffer - val < 0){
            try {
                condition2.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(comments){
            System.out.println("id " + id + " Consumed " + val + " from " + buffer);
        }
        buffer -= val;
        observer.consumed(id,val);
        condition1.signal();
        lock.unlock();

    }
}

