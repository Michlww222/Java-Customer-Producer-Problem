package Monitors;

import Interfaces.Monitor;
import Interfaces.Observer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//HAS WAITERS 4 CONDITION
public class Monitor2 implements Monitor {

    private int buffer = 0;
    private int bufferMAX;
    private Observer observer;
    private boolean comments;

    private ReentrantLock lock = new ReentrantLock(true);
    Condition firstProducer = lock.newCondition();
    Condition firstConsumer = lock.newCondition();
    Condition setProducer = lock.newCondition();
    Condition setConsumer = lock.newCondition();


    public Monitor2(int maxBuffer, Observer observer, boolean comments){
        this.bufferMAX = maxBuffer;
        this.observer = observer;
        this.comments = comments;
    }

    @Override
    public void produce(int id,int val){
        lock.lock();
        if(comments){
            System.out.println("P:id " + id + " STARTED buffer " + buffer + " value " + val);
        }
        while(lock.hasWaiters(firstProducer)) {
            try {
                if(comments){
                    System.out.println("P:id "+ id + " STUCK IN FIRST QUEUE buffer " + buffer + " value " + val);
                    System.out.println("P:FIRST QUEUE WAITERS = " + lock.getWaitQueueLength(setProducer));
                }
                setProducer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(buffer + val > bufferMAX){
            try {
                if(comments){
                    System.out.println("P:id "+ id + " STUCK IN SECOND QUEUE buffer " + buffer + " value " + val);
                    System.out.println("P:SECOND QUEUE WAITERS = " + lock.getWaitQueueLength(firstProducer));
                }
                firstProducer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(comments){
            System.out.println("----------PRODC STATUS--------------");
            System.out.println("P:id "+ id + " ENDED " + buffer + " value " + val);
            System.out.println("P:FIRST QUEUE WAITERS = " + lock.getWaitQueueLength(setProducer));
            System.out.println("P:SECOND QUEUE WAITERS = " + lock.getWaitQueueLength(firstProducer));
            System.out.println("-----------------------------------");
        }
        buffer += val;
        observer.produced(id,val);
        setProducer.signal();
        firstConsumer.signal();
        lock.unlock();

    }
    @Override
    public void consume(int id,int val){
        lock.lock();
        if(comments){
            System.out.println("C:id " + id + " STARTED buffer " + buffer + " value " + val);
        }
        while(lock.hasWaiters(firstConsumer)) {
            try {
                if(comments){
                    System.out.println("C:id "+ id + " STUCK IN FIRST QUEUE buffer " + buffer + " value " + val);
                    System.out.println("C:FIRST QUEUE WAITERS = " + lock.getWaitQueueLength(setConsumer));
                }
                setConsumer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(buffer - val < 0){
            try {
                if(comments){
                    System.out.println("C:id "+ id + " STUCK IN SECOND QUEUE buffer " + buffer + " value " + val);
                    System.out.println("C:SECOND QUEUE WAITERS = " + lock.getWaitQueueLength(firstConsumer));
                }
                firstConsumer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(comments){
            System.out.println("----------CONS STATUS--------------");
            System.out.println("C:id "+ id + " ENDED buffer " + buffer + " value " + val);
            System.out.println("C:FIRST QUEUE WAITERS = " + lock.getWaitQueueLength(setConsumer));
            System.out.println("C:SECOND QUEUE WAITERS = " + lock.getWaitQueueLength(firstConsumer));
            System.out.println("------------------------------------");
        }
        buffer -= val;
        observer.consumed(id,val);
        setConsumer.signal();
        firstProducer.signal();
        lock.unlock();

    }
}
