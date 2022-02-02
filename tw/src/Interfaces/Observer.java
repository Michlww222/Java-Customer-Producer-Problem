package Interfaces;

public interface Observer {

    void getProducerStatistics();
    void getCustomerStatistics();

    void produced(int id, int val);
    void consumed(int id, int val);
}
