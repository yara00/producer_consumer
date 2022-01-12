package com.example.producer_consumer.snapshot;

import java.util.ArrayList;

public class CareTaker {

    private Momento momentos;

    public void addMomento(Momento momento) {
        momentos = momento;
    }

    public Momento getMomento() {
        return momentos;
    }


}
