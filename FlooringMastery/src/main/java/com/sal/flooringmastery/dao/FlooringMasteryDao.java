package com.sal.flooringmastery.dao;

import com.sal.flooringmastery.model.Order;
import com.sal.flooringmastery.model.Products;
import com.sal.flooringmastery.service.FlooringMasteryOrderNotFoundException;

import java.math.BigDecimal;
import java.util.*;

public interface FlooringMasteryDao {

    Order getOrder(int orderNumber, String orderFile) throws FlooringMasteryException;

    List<Order> listAllOrders(String orderFile) throws FlooringMasteryException;

    List<Products> listAllProducts () throws FlooringMasteryException;

    Order addOrder(Order order, String orderFile) throws FlooringMasteryException;

    Order removeOrder(Order order, String orderFile) throws FlooringMasteryException;

    Order editCustomerName(int orderNumber, String customerName, String orderFile) throws FlooringMasteryException;

    Order editState(Order order, String state, String orderFile) throws FlooringMasteryException;

    Order editProductType(Order order, String productType, String orderFile) throws FlooringMasteryException;

    Order editArea(Order order, BigDecimal area, String orderFile) throws FlooringMasteryException;

}
