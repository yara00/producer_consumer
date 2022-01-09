package com.example.producer_consumer.snapshot;

import com.example.producer_consumer.Machine;
import com.example.producer_consumer.Product;

import java.util.List;

public class Originator {
    private List<List> state;

    public void setState(List<List> state) {
        System.out.println("Originator setting state to " + state);
        this.state = state;
    }

    public Momento save() {
        System.out.println("Originator: Saving to momento");
        return new Momento(state);
    }

    public List<Product> restore(Momento m) {
        state = m.getState();
        System.out.println("Originator: Restoring state from momento to "+ state);
        System.out.println(state.get(0).get(0));
        List<Product> products = state.get(1);
        return products;
    }
}


