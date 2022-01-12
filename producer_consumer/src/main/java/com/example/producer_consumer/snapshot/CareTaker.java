package com.example.producer_consumer.snapshot;

public class CareTaker {

    private Momento momentos;

    public void addMomento(Momento momento) {
        momentos = momento;
    }

    public Momento getMomento() {
        return momentos;
    }


}
