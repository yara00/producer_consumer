package com.example.producer_consumer.producerconsumer;

import com.example.producer_consumer.observer.AvailableObserver;
import com.example.producer_consumer.websocket.WebSocketService;
import java.util.ArrayList;

public class Machine implements Runnable, AvailableObserver{
    private boolean isAvailable = false;
    private String id;
    private long time;
    private ArrayList<Q> queuesBefore;
    private String defaultColor = "#ffffff";
    private Q queueAfter;

    public Machine(String id, ArrayList<Q> queueBefore, Q queueAfter, long time) {
        this.queuesBefore = queueBefore;
        this.queueAfter = queueAfter;
        this.time = time;
        this.id = id;
    }

    private void produce(Product product){
        this.queueAfter.addProduct(product);
        String message = this.queueAfter.getId() +","+ this.queueAfter.productsNumber() +
                "," + this.id + "," + this.defaultColor;
        System.out.println(message);
        WebSocketService.notifyFrontEnd(message);
    }

    private Product consume(Q queueBefore) throws InterruptedException {
        Product product = queueBefore.getProduct();
        String message = queueBefore.getId() +","+ queueBefore.productsNumber() +
                "," + this.id + "," + product.getColor();
        System.out.println(message);
        WebSocketService.notifyFrontEnd(message);
        Thread.sleep(time);
        return product;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (queuesBefore) {
                Product product ;
                int counter = 0;
                for (int i = 0; i < this.queuesBefore.size(); i++) {
                    if (this.queuesBefore.get(i).isEmpty()) {
                        counter++;
                        this.queuesBefore.get(i).register(this);
                    } else {
                        try {
                            isAvailable = false;
                            product = consume(this.queuesBefore.get(i));
                            produce(product);
                            isAvailable = true;
                            this.queueAfter.productsAvailable();
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (counter == this.queuesBefore.size()) {
                        try {
                            isAvailable = true;
                            this.queuesBefore.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            e.printStackTrace();
                            return;
                        }
                    }
                }
            }
        }

    }

    @Override
    public void notifyMachine() {
        if (isAvailable) {
            synchronized (this.queuesBefore) {
                this.queuesBefore.notify();
            }
        }
    }
}

