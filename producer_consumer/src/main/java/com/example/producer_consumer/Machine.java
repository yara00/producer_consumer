package com.example.producer_consumer;

import com.example.producer_consumer.websocket.WebSocketService;
import org.apache.tomcat.util.collections.SynchronizedQueue;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Machine implements Runnable, AvailableObserver {
    public Machine(String id,ArrayList<Q> queueBefore, Q queueAfter, long time) {
        this.queuesBefore = queueBefore;
        this.queueAfter = queueAfter;
        this.time = time;
        this.id = id;
    }

    Color color;
    boolean isAvailable = false;
    String id;
    long time;
    ArrayList<Q> queuesBefore;
    String defaultColor = "#ffffff";
    Q queueAfter;

    public void produce(Product product){
        this.queueAfter.addProduct(product);
        String message = this.queueAfter.getId() +","+ this.queueAfter.productsNumber() +
                "," + this.id + "," + this.defaultColor + " Produce";
        System.out.println(message);
        WebSocketService.notifyFrontEnd(message);

    }

    public Product consume(Q queueBefore) throws InterruptedException {
        Product product = queueBefore.getProduct();
      //  System.out.println(Thread.currentThread().getName() + " Consuming " + product.getColor());
        String message = queueBefore.getId() +","+ queueBefore.productsNumber() +
                "," + this.id + "," + product.getColor() +" Consume";
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
                           // System.out.println(Thread.currentThread().getName() + " Producing " + product.getColor());
                            this.queueAfter.productsAvailable();
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (counter == this.queuesBefore.size()) {
                        try {
                           // System.out.println(Thread.currentThread().getName() + " Nayem");
                            isAvailable = true;
                            this.queuesBefore.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }

    @Override
    public void notifyMachine() {
       // System.out.println("notify please ");
        if (isAvailable) {
            synchronized (this.queuesBefore) {
                this.queuesBefore.notify();
            }
        }
    }
}

