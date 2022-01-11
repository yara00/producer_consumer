package com.example.producer_consumer;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Q implements ObservableQueue {
    private Queue<Product> queue = new LinkedList<Product>();
    private Set<AvailableObserver> observers= new HashSet<AvailableObserver>();
    public Lock lock = new ReentrantLock();
    String id = "";
    public Q(String id) {
        this.id = id;
    }

    @Override
    public void register(AvailableObserver observer) {
        //System.out.println(Thread.currentThread().getName() + "reges");
        observers.add(observer);
    }
    public boolean isEmpty(){
        //System.out.println("Size "+ this.queue.size());
        return queue.isEmpty();
    }
    public String getId() {
        return id;
    }
    @Override
    public  synchronized Product getProduct() {
        //lock.lock();
        Product product = this.queue.remove();
        //System.out.println("Size "+ this.queue.size());
        //lock.unlock();
        return  product;
    }
    public synchronized void  addProduct(Product product){
        //lock.lock();
        this.queue.add(product);
        //lock.unlock();
    }

    public int productsNumber() {
        return queue.size();
    }
    public void productsAvailable(){
        if(!queue.isEmpty()){
            for(AvailableObserver observer : observers){
                observer.notifyMachine();
            }
        }
    }

}
