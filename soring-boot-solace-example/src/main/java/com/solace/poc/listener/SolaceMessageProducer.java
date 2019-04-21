package com.solace.poc.listener;

import javax.jms.Message;
import javax.jms.MessageProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solace.poc.config.SolaceGlobalConfig;
@Service
public class SolaceMessageProducer {
	@Autowired
	SolaceGlobalConfig solConfig;
	
	public void produceMessage(Object messageObj) throws Exception{
		MessageProducer producer = solConfig.getProducer();
		producer.send((Message)messageObj);
	}

}
