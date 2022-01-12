package com.example.producer_consumer;

import com.example.producer_consumer.snapshot.CareTaker;
import com.example.producer_consumer.snapshot.Originator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class Simulation {
    CareTaker careTaker = new CareTaker();
    Originator originator = new Originator();

    public static Queue<Thread> threads = new LinkedList<>();
    private  ArrayList<Machine> machines = new ArrayList<Machine>();
    private  ArrayList<Machine> machinesCopy = new ArrayList<>();

    private HashMap<String, Q> queuesMapCopy = new HashMap<>();

    public  ArrayList<Machine> getMachines() {
        return machines;
    }

    public  HashMap<String, Q>  createQueues(int productsNumber, String queues) {
        String [] queuesId = queues.split(",");
        HashMap<String, Q> queuesMap = new HashMap<>();
        // append products to q0
        for(int i = 0; i < queuesId.length; i++){
            Q queue = new Q(queuesId[i]);
            Q queueCopy = new Q(queuesId[i]);
            if(queuesId[i].equals("q0")){
                for(int j = 0; j < productsNumber; j++){
                    Product product = new Product();
                    queue.addProduct(product);
                    queueCopy.addProduct(product);
                }
            }
            queuesMap.put(queuesId[i], queue);
            queuesMapCopy.put(queuesId[i], queueCopy);
        }
        return queuesMap;
    }
    public void createMachines(HashMap<String, Q> queues, JSONArray graph){
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
            ArrayList<Q> queuesbeforeCopy = new ArrayList<>();
            for(Object queueObject : before){
                String idQ = (String) queueObject;
                queuesBefore.add(queues.get(idQ));
                queuesbeforeCopy.add(queuesMapCopy.get(idQ));
            }
            Q queueAfter = queues.get(after);
            Q queueAfterCopy = queuesMapCopy.get(after);
            Machine machine = new Machine(id, queuesBefore, queueAfter,processTime);
            Machine machineCopy = new Machine(id, queuesbeforeCopy, queueAfterCopy,processTime);
            machines.add(machine);
            machinesCopy.add(machineCopy);
        }
        System.out.println("machines" + machines);
    }

    public void simulate(int productsNumber, String queues, JSONArray graph){
        while(!threads.isEmpty()) {
            threads.remove().interrupt();
        }
        HashMap<String, Q> queuesMap = createQueues(productsNumber, queues);
        createMachines(queuesMap, graph);
        //save
        //Machine[] temp = Arrays.stream(new ArrayList[]{machines}).map(Point::new).toArray(Point[]::new);

        originator.setState(machinesCopy);
        careTaker.addMomento(originator.save());
        for(Machine machine : machines){
            Thread thread = new Thread(machine);
            threads.add(thread);
            thread.start();
        }
    }

    public void replay() {
        originator.restore(careTaker.getMomento());
    }
}
