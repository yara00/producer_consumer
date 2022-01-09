package com.example.producer_consumer;

import com.example.producer_consumer.snapshot.CareTaker;
import com.example.producer_consumer.snapshot.Originator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SpringBootApplication
public class ProducerConsumerApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(ProducerConsumerApplication.class, args);
		List<Machine> machines = new ArrayList<>();
		List<Product> products = new ArrayList<>();
		List<List> state = new ArrayList<>();
		Q q1 = new Q();
		Q q2 = new Q();
		Q q3 = new Q();
		Q q4 = new Q();
		Product product1 = new Product();

		product1.name = "yaryora";
		products.add(product1);
		Product product2 = new Product();
		product2.name = "marioma";
		products.add(product2);
		Product product3 = new Product();
		product3.name = "toto";
		products.add(product3);
		Product product4 = new Product();
		product4.name = "lolo";
		products.add(product4);
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


		Machine m1 = new Machine(qb1, q2, 2000,1);
		machines.add(m1);
		Machine m2 = new Machine(qb2, q4, 1000,2);
		machines.add(m1);
		Machine m3 = new Machine(qb3, q3, 10 ,3);
		machines.add(m1);
		state.add(machines);
		state.add(products);
		Thread t1 = new Thread(m1);
		t1.setName("Machine1");
		Thread t2 = new Thread(m2);
		Thread t3 = new Thread(m3);
		t2.setName("Machine2");
		t3.setName("Machine3");
		CareTaker careTaker = new CareTaker();
		Originator originator = new Originator();
		originator.setState(state);
		careTaker.addMomento(originator.save());
		List<Product> productList = new ArrayList<>();
		productList = originator.restore(careTaker.getMomento());
		for(int i=0; i< productList.size(); i++) {
			System.out.println(productList.get(i).getName()+ " ana hena " + productList.get(i).getColor());
		}
		t1.start();
		t2.start();
		t3.start();
	}

}
