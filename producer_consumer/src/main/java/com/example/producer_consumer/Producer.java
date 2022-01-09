package com.example.producer_consumer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Producer implements  Runnable{
    Queue<Product> queue = new LinkedList<Product>();



    @Override
    public void run() {
        while(true){
            synchronized (queue){

            }
        }

    }
}
