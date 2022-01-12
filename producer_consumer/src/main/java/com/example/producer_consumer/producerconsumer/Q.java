package com.example.producer_consumer.producerconsumer;

import com.example.producer_consumer.observer.AvailableObserver;
import com.example.producer_consumer.observer.ObservableQueue;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;


public class Q implements ObservableQueue {
    private Queue<Product> queue = new LinkedList<Product>();
    private Set<AvailableObserver> observers= new HashSet<AvailableObserver>();
    private String id = "";

    public Q(String id) {
        this.id = id;
    }

    @Override
    public void register(AvailableObserver observer) {
        observers.add(observer);
    }
    public boolean isEmpty(){
        return queue.isEmpty();
    }
    public String getId() {
        return id;
    }
    @Override
    public  synchronized Product getProduct() {
        Product product = this.queue.remove();
        return  product;
    }
    public synchronized void  addProduct(Product product){
        this.queue.add(product);
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
    public void clear() {
        this.queue.clear();
    }

}
