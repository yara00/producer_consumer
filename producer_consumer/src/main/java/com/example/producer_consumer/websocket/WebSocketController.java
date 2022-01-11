package com.example.producer_consumer.websocket;

import com.example.producer_consumer.Simulate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/start")
public class WebSocketController {

    @PostMapping ("")
    public void startSimulation(){
        //System.out.println("jojo");
        Simulate.start();

    }

}
