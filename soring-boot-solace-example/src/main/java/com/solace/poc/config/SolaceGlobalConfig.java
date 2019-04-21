package com.solace.poc.config;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.solace.poc.listener.JmsExceptionListener;
import com.solace.poc.listener.SolaceMessageConsumer;
import com.solacesystems.jms.SolConnectionFactory;
import com.solacesystems.jms.SolJmsUtility;

@Configuration
@Service
@PropertySource({"classpath:application.properties"})
public class SolaceGlobalConfig {

    private static final Logger logger = LoggerFactory.getLogger(SolaceGlobalConfig.class);

    @Autowired
    private Environment environment;

    @Autowired
    private JmsExceptionListener exceptionListener;

    @Bean
    public SolConnectionFactory solConnectionFactory() throws Exception {
        SolConnectionFactory connectionFactory = SolJmsUtility.createConnectionFactory();
        connectionFactory.setHost(environment.getProperty("solace.java.host"));
        connectionFactory.setVPN(environment.getProperty("solace.java.msgVpn"));
        connectionFactory.setUsername(environment.getProperty("solace.java.clientUsername"));
        connectionFactory.setPassword(environment.getProperty("solace.java.clientPassword"));
        connectionFactory.setClientID(environment.getProperty("solace.java.clientName"));
        return connectionFactory;
    }

    @Bean
    public SolaceMessageConsumer jmsMessageListener() {
        return new SolaceMessageConsumer();
    }

    @Bean(destroyMethod = "close")
    public Connection connection() {
        Connection connection = null;
        javax.jms.Session session;
        try {
            connection = solConnectionFactory().createConnection();
            session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(environment.getProperty("solace.message.consumer.queue"));
            MessageConsumer messageConsumer = session.createConsumer(queue);
            messageConsumer.setMessageListener(jmsMessageListener());
            connection.setExceptionListener(exceptionListener);
            
            // create producer
            MessageProducer producer = session.createProducer(queue);
            
            
            connection.start();
            logger.info("Connected. Awaiting message...");
        } catch (Exception e) {
            logger.info("JMS connection failed with Solace." + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }


    @Bean(destroyMethod = "close")
    public MessageProducer getProducer() {
        //Connection connection = null;
        MessageProducer producer = null;
        javax.jms.Session session;
        try {
        	Connection connection = solConnectionFactory().createConnection();
            session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(environment.getProperty("solace.message.consumer.queue"));
           // MessageConsumer messageConsumer = session.createConsumer(queue);
            //messageConsumer.setMessageListener(jmsMessageListener());
            connection.setExceptionListener(exceptionListener);
            
            // create producer
             producer = session.createProducer(queue);
            
            
            connection.start();
            logger.info("Connected. Awaiting message...");
        } catch (Exception e) {
            logger.info("JMS connection failed with Solace." + e.getMessage());
            e.printStackTrace();
        }
        return producer;
    }
    
    
    
}
