package com.solace.poc.service;

import com.solace.poc.model.Customer;

public interface SolaceMessageService {

    void processSolaceMessage(Customer customer);
}
