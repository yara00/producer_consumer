package com.example.producer_consumer.controller;

import com.example.producer_consumer.Simulation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/simulation")
public class Controller {
    private Simulation simulation = new Simulation();

    @PostMapping ("/start")
    public void startSimulation(@RequestParam("productsNumber") int productsNumber,
                                @RequestParam("queuesList") String queuesList,
                                @RequestBody JSONObject graph) throws ParseException {
        simulation = new Simulation();
        JSONParser parser = new JSONParser();
        JSONObject graphObject = (JSONObject) parser.parse(graph.toString());
        JSONArray graphArray = (JSONArray) graphObject.get("graph");
        simulation.simulate(productsNumber, queuesList, graphArray);

    }
    @PostMapping("/replay")
    public void replaySimulation() {
        simulation.replay();

    }

}
