package com.example.producer_consumer.snapshot;

import com.example.producer_consumer.producerconsumer.Machine;
import java.util.List;

public class Momento {
    private List<Machine> state;

    public Momento(List<Machine> state) {
        this.state = state;
    }

    public List<Machine> getState() {
        return state;
    }
}
