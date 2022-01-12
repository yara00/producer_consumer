package com.example.producer_consumer.websocket;

import com.example.producer_consumer.Machine;
import com.example.producer_consumer.Simulation;
import com.example.producer_consumer.snapshot.CareTaker;
import com.example.producer_consumer.snapshot.Originator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/simulation")
public class WebSocketController {
    Simulation simulation = new Simulation();

    @PostMapping ("/start")
    public void startSimulation(@RequestParam("productsNumber") int productsNumber,
                                @RequestParam("queuesList") String queuesList,
                                @RequestBody JSONObject graph) throws ParseException {
        JSONParser parser = new JSONParser();

        JSONObject graphObject = (JSONObject) parser.parse(graph.toString());
        JSONArray graphArray = (JSONArray) graphObject.get("graph");
        System.out.println("jojo" + graphObject);

        simulation.simulate(productsNumber, queuesList, graphArray);

    }
    @PostMapping("/replay")
    public void replaySimulation() {
        simulation.replay();

    }

}
