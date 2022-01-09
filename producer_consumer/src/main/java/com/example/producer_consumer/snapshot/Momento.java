package com.example.producer_consumer.snapshot;

import java.util.List;

public class Momento {
    private List<List> state;

    public Momento(List<List> state) {
        this.state = state;
    }

    public List<List> getState() {
        return state;
    }
}
