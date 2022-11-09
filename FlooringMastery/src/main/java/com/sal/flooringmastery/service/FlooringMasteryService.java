package com.sal.flooringmastery.service;

import com.sal.flooringmastery.dao.FlooringMasteryException;
import com.sal.flooringmastery.model.Order;
import com.sal.flooringmastery.model.Products;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public interface FlooringMasteryService {

    Order getOrder(int orderNumber, String fileName) throws FlooringMasteryException;

    List<Order> listAllOrders(String fileName )throws FlooringMasteryException;

    List<Products> listAllProducts() throws FlooringMasteryException;

    Order addOrderCalculations(Order order) throws FlooringMasteryException, FlooringMasteryStateNotFoundException, FlooringMasteryOrderNotFoundException;

    Order addOrder(Order order, String fileName) throws FlooringMasteryException, FlooringMasteryStateNotFoundException, FlooringMasteryOrderNotFoundException;

    Order removeOrder(Order order, String fileName) throws FlooringMasteryException ,FlooringMasteryOrderNotFoundException;

    Order editCustomerName(int orderNumber, String name, String fileName) throws FlooringMasteryException;

    Order editState(Order order, String state, String fileName) throws FlooringMasteryException;

    Order editProductType(Order order, String productType, String fileName) throws FlooringMasteryException;

    Order editArea(Order order, BigDecimal area, String fileName) throws FlooringMasteryException;


}
