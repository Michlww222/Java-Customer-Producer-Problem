package Threads;

import Interfaces.Monitor;

public class Producer extends Thread{

    private int id;
    public Monitor monitor;
    private volatile boolean interrupt = false;
    private final int maxBuffer;
    private int val = 0;
    private final int extraWork;

    public Producer(int id, Monitor monitor,int maxBuffer,int val,int extraWork){
        this.id = id;
        this.monitor = monitor;
        this.maxBuffer = maxBuffer;
        this.val = val;
        this.extraWork = extraWork;
    }

    public void run() {
        if(val != 0){
            while(!interrupt){
                monitor.produce(id,val);
                try {
                    sleep(0, 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            while(!interrupt){
                val = getRandomNumber(maxBuffer);
                monitor.produce(id,val);
                for(int i=0;i<extraWork;i+=1){
                    try {
                        sleep(0, 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void exit(){
        interrupt = true;
    }

    static int getRandomNumber(int maxBuffer) {
        return (int) ((Math.random() * (maxBuffer/2)) + 1);
    }

}

