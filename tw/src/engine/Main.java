package engine;

import Interfaces.Monitor;
import Interfaces.Observer;
import Monitors.*;
import Observers.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int maxBuffer = 20;
        int producerNumber = 10;
        int consumerNumber = 10;
        int seconds = 10;
        int extraWork = 0;
        boolean starvation = false;
        boolean comments = false;
        Observer observer = new Observer1(maxBuffer,producerNumber,consumerNumber);
        Monitor monitor = new Monitor4(maxBuffer,observer,comments);
        Simulation.start(producerNumber,consumerNumber,monitor,observer,seconds,maxBuffer,starvation,extraWork);
    }
}
