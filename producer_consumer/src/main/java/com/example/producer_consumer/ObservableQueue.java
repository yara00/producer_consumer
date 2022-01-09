package com.example.producer_consumer;

public interface ObservableQueue {
    public void register(AvailableObserver availableObserver);
    public Product getProduct();
}
