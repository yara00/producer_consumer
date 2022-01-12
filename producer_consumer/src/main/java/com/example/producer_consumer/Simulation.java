package com.example.producer_consumer;

import com.example.producer_consumer.producerconsumer.Machine;
import com.example.producer_consumer.producerconsumer.Product;
import com.example.producer_consumer.producerconsumer.Q;
import com.example.producer_consumer.snapshot.CareTaker;
import com.example.producer_consumer.snapshot.Originator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class Simulation {
    private CareTaker careTaker = new CareTaker();
    private Originator originator = new Originator();

    public static Queue<Thread> threads = new LinkedList<>();
    private  ArrayList<Machine> machines = new ArrayList<Machine>();
    private HashMap<String, Q> queuesMap = new HashMap<>();
    private ArrayList<Product> products = new ArrayList<>();
    private String [] queueId;

    private HashMap<String, Q>  createQueues(int productsNumber, String queues) {
        String [] queuesId = queues.split(",");
        queueId = queuesId;
        HashMap<String, Q> queuesMap = new HashMap<>();
        // append products to q0
        for(int i = 0; i < queuesId.length; i++){
            Q queue = new Q(queuesId[i]);
            if(queuesId[i].equals("q0")){
                for(int j = 0; j < productsNumber; j++){
                    Product product = new Product();
                    products.add(product);
                    queue.addProduct(product);
                }
            }
            queuesMap.put(queuesId[i], queue);
        }
        return queuesMap;
    }

    private void createMachines(HashMap<String, Q> queues, JSONArray graph){
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
                String idQ = (String) queueObject;
                queuesBefore.add(queues.get(idQ));
            }
            Q queueAfter = queues.get(after);
            Machine machine = new Machine(id, queuesBefore, queueAfter,processTime);
            machines.add(machine);
        }
    }

    public void simulate(int productsNumber, String queues, JSONArray graph){
        while(!threads.isEmpty()) {
            threads.remove().interrupt();
        }
        queuesMap = createQueues(productsNumber, queues);
        createMachines(queuesMap, graph);
        //save
        originator.setState(machines);
        careTaker.addMomento(originator.save());
        for(Machine machine : machines){
            Thread thread = new Thread(machine);
            threads.add(thread);
            thread.start();
        }
    }

    public void replay() {
        for(int i = 0; i < queueId.length; i++) {
            queuesMap.get(queueId[i]).clear();
        }
        for (int i=0; i < products.size(); i++) {
            queuesMap.get("q0").addProduct(products.get(i));
        }
        originator.restore(careTaker.getMomento());
    }
}