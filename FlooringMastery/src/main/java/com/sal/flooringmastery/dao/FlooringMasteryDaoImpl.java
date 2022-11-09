package com.sal.flooringmastery.dao;

import com.sal.flooringmastery.model.Order;
import com.sal.flooringmastery.model.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class FlooringMasteryDaoImpl implements FlooringMasteryDao{

    // declare variables
    @Autowired
    Map<Integer,Order> orderMap;
    FileDao fio = new FileDaoImpl();


    public FlooringMasteryDaoImpl(){

    }

    // get an order from the file
    @Override
    public Order getOrder(int orderNumber, String orderFile) throws FlooringMasteryException {
        orderMap = fio.readFile(orderFile);
        return orderMap.get(orderNumber);
    }

    // get all orders from the file
    @Override
    public List<Order> listAllOrders(String orderFile) throws FlooringMasteryException {
        orderMap = fio.readFile(orderFile);
        return new ArrayList<>(orderMap.values());
    }

    //get all products from the file for recalculation
    @Override
    public List<Products> listAllProducts() throws FlooringMasteryException {
        Map<String,Products> productsMap = fio.readFileProducts();
        return new ArrayList<>(productsMap.values());
    }

    // add an order to the file
    @Override
    public Order addOrder(Order order, String orderFile) throws FlooringMasteryException {
        fio.createNewFile(orderFile);
        order.setOrderNumber(listAllOrders(orderFile).size()+1);
        fio.writeNewFile(order,orderFile);
        return order;
    }

    // remove an order from the file
    @Override
    public Order removeOrder(Order order, String orderFile) throws FlooringMasteryException {
        FileDao fio = new FileDaoImpl(orderFile);
        orderMap = fio.readFile(orderFile);
        Order res = orderMap.remove(order.getOrderNumber());
        fio.writeFile(new ArrayList<Order>(orderMap.values()));
        return res;
    }

    // edit the order's customer name
    @Override
    public Order editCustomerName(int orderNumber, String customerName, String orderFile) throws FlooringMasteryException {
        FileDao fio = new FileDaoImpl(orderFile);
        orderMap = fio.readFile(orderFile);
        Order curr =  orderMap.get(orderNumber);
        curr.setCustomerName(customerName);
        try{
            fio.writeFile(new ArrayList<Order>(orderMap.values()));
        }catch(Exception e){
            throw new FlooringMasteryException("ERROR");
        }

        return curr;
    }

    // edit the order's state
    @Override
    public Order editState(Order order, String state, String orderFile) throws FlooringMasteryException {
        FileDao fio = new FileDaoImpl(orderFile);
        orderMap = fio.readFile(orderFile);
        Order curr =  orderMap.put(order.getOrderNumber(),order);

        try{
            fio.writeFile(new ArrayList<Order>(orderMap.values()));
        }catch(Exception e){
            throw new FlooringMasteryException("ERROR");
        }
        return curr;
    }

    // edit the order's product type
    @Override
    public Order editProductType(Order order, String productType, String orderFile) throws FlooringMasteryException {
        FileDao fio = new FileDaoImpl(orderFile);
        orderMap = fio.readFile(orderFile);
        Order curr =  orderMap.put(order.getOrderNumber(),order);

        try{
            fio.writeFile(new ArrayList<Order>(orderMap.values()));
        }catch(Exception e){
            throw new FlooringMasteryException("ERROR");
        }
        return curr;
    }

    //// edit the order's area
    @Override
    public Order editArea(Order order, BigDecimal area, String orderFile) throws FlooringMasteryException {
        FileDao fio = new FileDaoImpl(orderFile);
        orderMap = fio.readFile(orderFile);
        Order curr =  orderMap.put(order.getOrderNumber(),order);
        curr.setArea(area);

        try{
            fio.writeFile(new ArrayList<Order>(orderMap.values()));
        }catch(Exception e){
            throw new FlooringMasteryException("ERROR");
        }
        return curr;
    }


}
