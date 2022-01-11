package com.example.producer_consumer;

import java.awt.*;
import java.util.Random;

public class Product {
    String color = "";
    public Product() {
        Random random = new Random();
        int nextInt = random.nextInt(0xffffff + 1);
        color = String.format("#%06x", nextInt);
    }

    public String getColor() {
        return color;
    }




}
