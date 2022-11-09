package com.sal.flooringmastery.service;

import com.sal.flooringmastery.dao.*;
import com.sal.flooringmastery.model.Order;
import com.sal.flooringmastery.model.Products;
import com.sal.flooringmastery.model.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FlooringMasteryServiceImpl implements FlooringMasteryService{

    String fileName;
    private FlooringMasteryDao dao;
    private AuditDao adao;

    public FlooringMasteryServiceImpl() throws FlooringMasteryException{}

    @Autowired
    public FlooringMasteryServiceImpl(FlooringMasteryDao dao, AuditDao adao){
        this.dao = dao;
        this.adao = adao;
    }

    // get the order by its order number (key)
    @Override
    public Order getOrder(int orderNumber,String fileName) throws FlooringMasteryException {
        if(dao.getOrder(orderNumber,fileName) == null){
            throw new FlooringMasteryException("ORDER NOT FOUND!!");
        }
        return dao.getOrder(orderNumber,fileName);
    }

    // displays all orders
    // first it converts to a stream, then it converts to a collection.
    @Override
    public List<Order> listAllOrders(String fileName) throws FlooringMasteryException {
        adao.writeAuditEntry("ALL ORDERS LISTED.");
        return dao.listAllOrders(fileName)
                .stream()
                .collect(Collectors.toList());
    }

    // displays all products for user selection
    // first it converts to a stream, then it converts to a collection.
    @Override
    public List<Products> listAllProducts() throws FlooringMasteryException {
        adao.writeAuditEntry("ALL PRODUCTS LISTED.");
        return dao.listAllProducts()
                .stream()
                .collect(Collectors.toList());
    }

    // recalculates the order properties due to editing a property on order
    @Override
    public Order addOrderCalculations(Order order) throws FlooringMasteryException, FlooringMasteryStateNotFoundException, FlooringMasteryOrderNotFoundException {
        FileDao fdaoTax = new FileDaoImpl("Taxes.txt");
        FileDao fdaoProduct = new FileDaoImpl("Products.txt");

        if(order.getCustomerName() == null){
            throw new FlooringMasteryException("CUSTOMER NAME NOT BE NULL.");
        }

        if (!fdaoTax.readFileTax().containsKey(order.getState())) {
            throw new FlooringMasteryStateNotFoundException("STATE DOES NOT EXISTS IN THE TAXES FILE.");
        }
        if(order.getArea().compareTo(BigDecimal.ZERO) > 100 ){
            throw new FlooringMasteryException("AREA CAN NOT BE ZERO, MIN AREA SIZE IS 100 FT");
        }
        Map<String, Products> product = fdaoProduct.readFileProducts();
        Map<String, Tax> tax = fdaoTax.readFileTax();

        order.setTaxRate(tax.get(order.getState()).getTaxRate());
        order.setCostPsf(product.get(order.getProductType()).getCostPSF());
        order.setLaborPsf(product.get(order.getProductType()).getLaborCostPSF());


        order.setMaterialCost(order.getArea().multiply(product.get(order.getProductType()).getCostPSF()));
        order.setLaborCost(order.getArea().multiply(product.get(order.getProductType()).getLaborCostPSF()));
        order.setTax((order.getMaterialCost().add(order.getLaborCost()))
                .multiply(tax.get(order.getState()).getTaxRate().divide(BigDecimal.valueOf(100.0))));
        order.setTotal(order.getMaterialCost().add(order.getLaborCost()).add(order.getTax()));

        return order;
    }


    // adds order to the file
    @Override
    public Order addOrder(Order order, String fileName) throws FlooringMasteryException{
        dao.addOrder(order,fileName);
        adao.writeAuditEntry("ORDER "+order.getOrderNumber()+" ADDED.");
        return order;
    }

    // removes order from a file
    // checks if the order exist or not
    @Override
    public Order removeOrder(Order order, String fileName) throws FlooringMasteryException ,FlooringMasteryOrderNotFoundException {
        if(dao.getOrder(order.getOrderNumber(),fileName) == null){
            throw new FlooringMasteryOrderNotFoundException("ORDER DOES NOT EXISTS.");
        }
        adao.writeAuditEntry("ORDER "+order.getOrderNumber()+" REMOVED.");
        return dao.removeOrder(order,fileName);
    }

    // edits order's customer name
    @Override
    public Order editCustomerName(int orderNumber,String name, String fileName) throws FlooringMasteryException {

        adao.writeAuditEntry("ORDER "+orderNumber+ "'S CUSTOMER NAME EDITED.");
        return dao.editCustomerName(orderNumber,name,fileName);
    }

    // edits order's state
    @Override
    public Order editState(Order order,String state, String fileName) throws FlooringMasteryException {
        adao.writeAuditEntry("ORDER "+order.getOrderNumber()+ "'S STATE EDITED.");
        return dao.editState(order,state,fileName);
    }

    // edits order's product type
    @Override
    public Order editProductType(Order order, String productType, String fileName) throws FlooringMasteryException {
        adao.writeAuditEntry("ORDER "+ order.getOrderNumber()+ "'S PRODUCT TYPE EDITED.");
        return dao.editProductType(order,productType,fileName);
    }

    // edits order's area
    @Override
    public Order editArea(Order order, BigDecimal area, String fileName) throws FlooringMasteryException {
        adao.writeAuditEntry("ORDER "+ order.getOrderNumber()+ "'S AREA EDITED.");
        return dao.editArea(order,area,fileName);
    }

}
