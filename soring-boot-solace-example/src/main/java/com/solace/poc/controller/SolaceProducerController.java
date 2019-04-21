package com.solace.poc.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solace.poc.listener.SolaceMessageProducer;

@RestController
@EnableAutoConfiguration
@RequestMapping("/transaction")
public class SolaceProducerController {
	@Autowired
	SolaceMessageProducer producer;
	
	  @PostMapping("/send")
	  public void send(@RequestBody Order order) {
	    System.out.println("Sending a transaction.");
	    // Post message to the message queue named "OrderTransactionQueue"
	    try {
			producer.produceMessage(order);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }

}
