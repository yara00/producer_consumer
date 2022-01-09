package com.example.producer_consumer.snapshot;

import java.util.ArrayList;

public class CareTaker {
    /** a5liha list of momentos wla momento wa7da cus keda keda b3ml save le one state**/
    private ArrayList<Momento> momentos = new ArrayList<>();
    public void addMomento(Momento m) {
        momentos.add(m);
    }
    // law msh list htb2a get momento 3la tol mn gher index
    public Momento getMomento() {
        return momentos.get(0);
    }

    public void clear() {
        momentos.clear();
    }
}
