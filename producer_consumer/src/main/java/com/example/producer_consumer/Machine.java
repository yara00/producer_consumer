package com.example.producer_consumer;

import org.apache.tomcat.util.collections.SynchronizedQueue;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Machine implements Runnable, AvailableObserver {
    public Machine(ArrayList<Q> queueBefore, Q queueAfter, long time, int id) {
        this.queuesBefore = queueBefore;
        this.queueAfter = queueAfter;
        this.time = time;
        this.id = id;
    }
    Color color;
    boolean isAvailable = false;
    int id;
    long time;
    ArrayList<Q> queuesBefore;
    Color defualtColor = new Color(255,255,255);
    Q queueAfter;
    boolean register = false;
    public void produce(Product product) {
        this.queueAfter.addProduct(product);
    }
    public Product consume(Q queueBefore) throws InterruptedException {
        Product product = queueBefore.getProduct();
        System.out.println( Thread.currentThread().getName() +" Consuming " + product.name);
        this.color = product.color;
        Thread.sleep(time);
        return product;
    }
    boolean available = false;
    Object lock =  new Object();



    @Override
    public void run() {
            while (true) {
                synchronized (queuesBefore) {
                    Product product = new Product();
                    int counter = 0;
                    for (int i = 0; i < this.queuesBefore.size(); i++) {
                        if (this.queuesBefore.get(i).isEmpty()) {
                            counter++;
                            this.queuesBefore.get(i).register(this);
                        } else {
                            try {
                                isAvailable = false;
                                product = consume(this.queuesBefore.get(i));
                                System.out.println( Thread.currentThread().getName() +" machine color is " +
                                        this.color + " and product color is "+ product.color);
                                produce(product);
                                isAvailable = true;
                                System.out.println( Thread.currentThread().getName() +" Producing " + product.name);
                                this.queueAfter.productsAvailable();
                                break;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (counter == this.queuesBefore.size()){
                            try {
                                System.out.println(Thread.currentThread().getName() +" Nayem");
                                isAvailable = true;
                                this.color = defualtColor;
                                System.out.println("ana waiting w lony "+this.color);
                                this.queuesBefore.wait();
                                System.out.println(Thread.currentThread().getName() +" Back to life");
                                } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        }

    }
                /*if (queueBefore.isEmpty()) {
                    queueBefore.register(this);
                    try {
                        this.queueBefore.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Product product = queueBefore.getProduct();
                System.out.println(Thread.currentThread().getName() + "consuming " + product.name);
                try {
                    Thread.sleep(time);
                    queueAfter.addProduct(product);
                    this.queueBefore.notifyAll();
                    System.out.println(Thread.currentThread().getName() + "producing " + product.name);
                    queueAfter.productsAvailable();
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }*/

    @Override
    public void notify(ObservableQueue observableQueue) {
        System.out.println("notify please "  );
        if(isAvailable){
        synchronized (this.queuesBefore){this.queuesBefore.notify();}
    }}
}

