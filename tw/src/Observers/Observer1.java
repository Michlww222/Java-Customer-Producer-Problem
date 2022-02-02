package Observers;

import Interfaces.Observer;
import java.util.ArrayList;

public class Observer1 implements Observer {

    ArrayList<Integer> consumedList = new ArrayList<Integer>();
    ArrayList<Integer> producedList = new ArrayList<Integer>();
    ArrayList<Integer> producers = new ArrayList<Integer>();
    ArrayList<Integer> consumers = new ArrayList<Integer>();

    public Observer1(int maxBuffer,int producerNumber,int consumerNumber){
        for(int i=0;i<maxBuffer/2 + 1;i+=1){
            consumedList.add(0);
            producedList.add(0);
        }
        for(int i=0;i<producerNumber;i+=1){
            producers.add(0);
        }
        for(int i=0;i<consumerNumber;i+=1){
            consumers.add(0);
        }
    }
    @Override
    public void produced(int id,int val){
        int a = producedList.get(val);
        producedList.set(val,a+1);
        int b = producers.get(id);
        producers.set(id,b+1);
    }
    @Override
    public void consumed(int id,int val){
        int a = consumedList.get(val);
        consumedList.set(val,a+1);
        int b = consumers.get(id);
        consumers.set(id,b+1);
    }

    @Override
    public void getCustomerStatistics(){
        System.out.println("CUSTOMERS STATISTICS");
        System.out.println("-------------------------------------");
        System.out.println("VALUE DONE");
        for(int i=1;i<consumedList.size();i+=1){
            System.out.println(i + "     " + consumedList.get(i));
        }
        System.out.println("-------------------------------------");
        System.out.println("ID DONE");
        for(int i=0;i<consumers.size();i+=1){
            System.out.println(i + "  " + consumers.get(i));
        }
    }
    @Override
    public void getProducerStatistics(){
        System.out.println("PRODUCERS STATISTICS");
        System.out.println("-------------------------------------");
        System.out.println("VALUE DONE");
        for(int i=1;i<producedList.size();i+=1){
            System.out.println(i + "     " + producedList.get(i));
        }
        System.out.println("-------------------------------------");
        System.out.println("ID DONE");
        for(int i=0;i<producers.size();i+=1){
            System.out.println(i + "  " + producers.get(i));
        }
    }
}
