package Monitors;

import Interfaces.Monitor;
import Interfaces.Observer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
//4 CONDITION NO HASWAITERS
public class Monitor3 implements Monitor {

    private int buffer = 0;
    private int bufferMAX;
    private Observer observer;
    private boolean comments;

    private boolean freeFirstConsumer = true;
    private boolean freeFirstProducer = true;
    private ReentrantLock lock = new ReentrantLock(true);
    Condition firstProducer = lock.newCondition();
    Condition firstConsumer = lock.newCondition();
    Condition setProducer = lock.newCondition();
    Condition setConsumer = lock.newCondition();


    public Monitor3(int maxBuffer, Observer observer, boolean comments){
        this.bufferMAX = maxBuffer;
        this.observer = observer;
        this.comments = comments;
    }

    @Override
    public void produce(int id,int val){
        lock.lock();
        while(!freeFirstProducer) {
            try {
                setProducer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(buffer + val > bufferMAX){
            try {
                freeFirstProducer = false;
                firstProducer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(comments){
            System.out.println("id " + id + " Produced " + val + " to " + buffer);
        }
        buffer += val;
        observer.produced(id,val);
        freeFirstProducer = true;
        setProducer.signal();
        firstConsumer.signal();
        lock.unlock();

    }

    @Override
    public void consume(int id,int val){
        lock.lock();
        while(!freeFirstConsumer) {
            try {
                setConsumer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(buffer - val < 0){
            try {
                freeFirstConsumer = false;
                firstConsumer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(comments){
            System.out.println("id " + id + " Consumed " + val + " from " + buffer);
        }
        buffer -= val;
        observer.consumed(id,val);
        freeFirstConsumer = true;
        setConsumer.signal();
        firstProducer.signal();
        lock.unlock();

    }
}
