package com.example.producer_consumer;

import java.awt.*;
import java.util.Random;

public class Product {
    Random rand = new Random();
    float r = rand.nextFloat();
    float g = rand.nextFloat();
    float b = rand.nextFloat();
    Color color;
    String name = "";
    public  String getName() {
        return this.name;
    }

    public Product() {
        this.color = new Color(r, g, b);

    }

    public Color getColor() {
        return color;
    }




}
