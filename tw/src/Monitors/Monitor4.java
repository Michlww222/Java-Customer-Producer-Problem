package Monitors;

import Interfaces.Monitor;
import Interfaces.Observer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
//LOCKS IN LOCKS
public class Monitor4 implements Monitor {

    private int buffer = 0;
    private final int bufferMAX;
    private Observer observer;
    private boolean comments;

    private ReentrantLock lockProducer = new ReentrantLock(true);
    private ReentrantLock lockConsumer = new ReentrantLock(true);
    private ReentrantLock lockCommon = new ReentrantLock(true);



    Condition freePlace = lockCommon.newCondition();

    public Monitor4(int maxBuffer,Observer observer,boolean comments){
        this.bufferMAX = maxBuffer;
        this.observer = observer;
        this.comments = comments;
    }

    @Override
    public void produce(int id,int val){
        lockProducer.lock();
        lockCommon.lock();
        while(val + buffer > bufferMAX){
            try {
                freePlace.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(comments){
            System.out.println("id " + id + " Produced " + val + " to " + buffer);
        }
        buffer += val;
        observer.produced(id,val);
        freePlace.signal();
        lockCommon.unlock();
        lockProducer.unlock();

    }
    @Override
    public void consume(int id,int val){
        lockConsumer.lock();
        lockCommon.lock();
        while(buffer - val < 0){
            try {
                freePlace.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(comments){
            System.out.println("id " + id + " Consumed " + val + " from " + buffer);
        }
        buffer -= val;
        observer.consumed(id,val);
        freePlace.signal();
        lockCommon.unlock();
        lockConsumer.unlock();

    }

}
