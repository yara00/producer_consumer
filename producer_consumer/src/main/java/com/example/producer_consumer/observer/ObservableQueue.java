package com.example.producer_consumer.observer;

import com.example.producer_consumer.producerconsumer.Product;

public interface ObservableQueue {
    public void register(AvailableObserver availableObserver);
    public Product getProduct();
}
