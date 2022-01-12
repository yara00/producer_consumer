package com.example.producer_consumer.snapshot;

import com.example.producer_consumer.producerconsumer.Machine;
import com.example.producer_consumer.Simulation;
import java.util.List;

public class Originator {
    private List<Machine> state;

    public void setState(List<Machine> state) {
        System.out.println("Originator setting state to " + state);
        this.state = state;
    }

    public Momento save() {
        System.out.println("Originator: Saving to momento");
        return new Momento(state);
    }

    public void restore(Momento m) {
        state = m.getState();
        System.out.println("Originator: Restoring state from momento to "+ state);
        List<Machine> machines = state;
        while(!Simulation.threads.isEmpty()) {
            Simulation.threads.remove().interrupt();
        }
        for(Machine machine : machines){
            Thread thread = new Thread(machine);
            thread.start();
            Simulation.threads.add(thread);
        }
    }
}


