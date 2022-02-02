package Interfaces;

public interface Monitor {

    void produce(int id,int val);
    void consume(int id,int val);
    static int getRandomNumber(int maxBuffer) {
        return (int) ((Math.random() * (maxBuffer/2)) + 1);
    }
}
