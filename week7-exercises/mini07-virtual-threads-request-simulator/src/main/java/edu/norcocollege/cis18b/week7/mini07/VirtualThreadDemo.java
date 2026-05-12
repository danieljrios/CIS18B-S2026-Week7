package edu.norcocollege.cis18b.week7.mini07;

public class VirtualThreadDemo {

    public static void main(String[] args) throws Exception {
        // If Java 21 virtual threads are not available, print a clear message.
        if (!VirtualThreadSupport.isAvailable()) {
            System.out.println(VirtualThreadSupport.errorMessage());
            return;
        }

        RequestSimulator simulator = new RequestSimulator();

        System.out.println("Virtual thread support available.");
        System.out.println(simulator.simulateRequests(5, 25L));
    }
}