package com.example.producer_consumer;

import org.springframework.boot.SpringApplication;

import java.util.ArrayList;

public class Simulate {

    public static void start(){
            Q q1 = new Q();
            Q q2 = new Q();
            Q q3 = new Q();
            Q q4 = new Q();
            Product product1 = new Product();
            product1.name = "yaryora";
            Product product2 = new Product();
            product2.name = "marioma";
            Product product3 = new Product();
            product3.name = "toto";
            Product product4 = new Product();
            product4.name = "lolo";
            Q q5 = new Q();
            q1.addProduct(product1);
            q1.addProduct(product2);
            q1.addProduct(product3);
            q1.addProduct(product4);
            ArrayList<Q> qb1 = new ArrayList<>();
            qb1.add(q1);
            ArrayList<Q> qb2 = new ArrayList<>();
            qb2.add(q2);
            qb2.add(q3);
            ArrayList<Q> qb3 = new ArrayList<>();
            qb3.add(q1);


            Machine m1 = new Machine(qb1, q2, 10000,1);
            Machine m2 = new Machine(qb2, q4, 10000,2);
            Machine m3 = new Machine(qb3, q3, 10000 ,3);
            Thread t1 = new Thread(m1);
            t1.setName("Machine1");
            Thread t2 = new Thread(m2);
            Thread t3 = new Thread(m3);
            t2.setName("Machine2");
            t3.setName("Machine3");
            t1.start();
            t2.start();
            t3.start();
    }
}
