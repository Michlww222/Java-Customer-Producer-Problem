package engine;

import Interfaces.Monitor;
import Interfaces.Observer;
import Threads.Consumer;
import Threads.Producer;

import java.util.ArrayList;

public class Simulation {
    public static void start(int producerNumber, int consumerNumber, Monitor monitor, Observer observer,int seconds,int maxBuffer,boolean starvation,int extraWork) throws InterruptedException {

        ArrayList<Consumer> consumerList = new ArrayList<Consumer>();
        ArrayList<Producer> producerList = new ArrayList<Producer>();

        if(starvation){
            producerList.add(new Producer(0,monitor,maxBuffer,1,extraWork));
            producerList.add(new Producer(1,monitor,maxBuffer,1,extraWork));
            consumerList.add(new Consumer(0,monitor,maxBuffer,1,extraWork));
            consumerList.add(new Consumer(1,monitor,maxBuffer,2,extraWork));
            consumerList.add(new Consumer(2,monitor,maxBuffer,2,extraWork));
            consumerList.add(new Consumer(3,monitor,maxBuffer,5,extraWork));
            consumerList.add(new Consumer(4,monitor,maxBuffer,5,extraWork));
        }
        else{
            for (int i = 0; i < producerNumber; i += 1) {
                producerList.add(new Producer(i,monitor,maxBuffer,0,extraWork));
            }
            for (int i = 0; i < consumerNumber; i += 1) {
                consumerList.add(new Consumer(i,monitor,maxBuffer,0,extraWork));
            }
        }

        for(Producer a:producerList){
            a.start();
        }

        for(Consumer a:consumerList){
            a.start();
        }

        Thread.sleep(seconds*1000);

        if(seconds != 0){
            for(Consumer a:consumerList){
                a.exit();
            }
            for(Producer a:producerList){
                a.exit();
            }
            observer.getCustomerStatistics();
            observer.getProducerStatistics();
        }
        for(Consumer a:consumerList){
            a.join();
        }
        for(Producer a:producerList){
            a.join();
        }

        System.out.println("-------------------------------------");
        System.out.println("SIMULATION ENDED");
    }
}
