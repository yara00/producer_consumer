package com.example.producer_consumer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class Simulate {
/*
    public static void start() {
        Q q1 = new Q("q1");
        Q q2 = new Q("q2");
        Q q3 = new Q("q3");
        Q q4 = new Q("q4");
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Product product4 = new Product();
        Q q5 = new Q("q4");
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


        Machine m1 = new Machine("m1", qb1, q2, 2000);
        Machine m2 = new Machine("m2", qb2, q4, 1000);
        Machine m3 = new Machine("m3", qb3, q3, 1000);
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
    
 */

    ArrayList<Machine> machines = new ArrayList<Machine>();

    public  HashMap<String, Q>  createQueues(int productsNumber, String queues) {
        String [] queuesId = queues.split(",");
        HashMap<String, Q> queuesMap = new HashMap<>();
        // append products to q0
        for(int i = 0; i < queuesId.length; i++){
            Q queue = new Q(queuesId[i]);
            if(queuesId[i].equals("q0")){
                for(int i = 0; i < productsNumber; i++){
                    Product product = new Product();
                    queue.addProduct(product);
                }
            }
            queuesMap.put(queuesId[i], queue);
        }
        return queuesMap;
    }
    public void createMachines(HashMap<String, Q> queues,JSONArray graph){
        for(Object object : graph){
            Random random = new Random();
            int low = 1000;
            int high = 5000;
            int processTime = random.nextInt(high-low) + low;
            JSONObject machineObject = (JSONObject) object;
            JSONArray before = (JSONArray) machineObject.get("before");
            String after = (String) machineObject.get("after");
            String id = (String) machineObject.get("id");
            ArrayList<Q> queuesBefore = new ArrayList();
            for(Object queueObject : before){
                String id = (String) queueObject;
                queuesBefore.add(queues.get(id));
            }
            Q queueAfter = queues.get(after);
            Machine machine = new Machine(id, queuesBefore, queueAfter,processTime);
            machines.add(machine);
        }
    }

    public void simulate(){
        for(Machine machine : machines){
            Thread thread = new Thread(machine);
            thread.start();
        }
    }

}
