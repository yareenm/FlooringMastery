package com.sal.flooringmastery.dao;

import com.sal.flooringmastery.model.*;

import java.util.List;
import java.util.Map;

public interface FileDao {
    Order unmarshallOrder(String line);
    String marshallOrder(Order order) throws FlooringMasteryException;
    void writeFile(List<Order> list) throws FlooringMasteryException;
    Map<Integer,Order> readFile(String file) throws FlooringMasteryException;
    Products unmarshallProduct(String line);
    Tax unmarshallTax(String line);
    Map<String,Products> readFileProducts() throws FlooringMasteryException;
    Map<String,Tax> readFileTax() throws FlooringMasteryException;
    void writeNewFile(Order order, String fileName) throws FlooringMasteryException;

    void createNewFile(String fileName) throws FlooringMasteryException;
}
