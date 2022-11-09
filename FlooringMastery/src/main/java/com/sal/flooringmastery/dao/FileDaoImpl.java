package com.sal.flooringmastery.dao;

import com.sal.flooringmastery.model.Order;
import com.sal.flooringmastery.model.Products;
import com.sal.flooringmastery.model.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class FileDaoImpl  implements FileDao{

    // declare variables
    @Autowired
    private Map<String,Products> products = new HashMap<>();
    private Map<String,Tax> taxes = new HashMap<>();
    private static String ORDER_FILE;
    private static final String DELIMITER = ",";

    public FileDaoImpl(String fileName){
        ORDER_FILE = fileName;
    }

    public FileDaoImpl() {

    }

    // This function is used for read order from a file
    // It splits each line according to delimiter.
    // It returns an item as a String
    @Override
    public Order unmarshallOrder(String line) {
        String[] orderTokens = line.split(DELIMITER);
        int orderNumber;
        Order orderFromFile = new Order();

        if(orderTokens.length>1){
            orderNumber = Integer.parseInt(orderTokens[0]);
            orderFromFile = new Order(orderNumber);
            orderFromFile.setCustomerName(orderTokens[1]);
            orderFromFile.setState(orderTokens[2]);
            orderFromFile.setTaxRate(BigDecimal.valueOf(Double.valueOf(orderTokens[3])));
            orderFromFile.setProductType(orderTokens[4]);
            orderFromFile.setArea(BigDecimal.valueOf(Double.valueOf(orderTokens[5])));
            orderFromFile.setCostPsf(BigDecimal.valueOf(Double.valueOf(orderTokens[6])));
            orderFromFile.setLaborPsf(BigDecimal.valueOf(Double.valueOf(orderTokens[7])));
            orderFromFile.setMaterialCost(BigDecimal.valueOf(Double.valueOf(orderTokens[8])));
            orderFromFile.setLaborCost(BigDecimal.valueOf(Double.valueOf(orderTokens[9])));
            orderFromFile.setTax(BigDecimal.valueOf(Double.valueOf(orderTokens[10])));
            orderFromFile.setTotal(BigDecimal.valueOf(Double.valueOf(orderTokens[11])));
        }
        return orderFromFile;
    }

    // This function is used for write order to a file
    // It returns a String which contains order and its properties
    @Override
    public String marshallOrder(Order order) throws FlooringMasteryException {

        return order.getOrderNumber() + DELIMITER + order.getCustomerName() + DELIMITER
                + order.getState() + DELIMITER + order.getTaxRate() + DELIMITER
                + order.getProductType() + DELIMITER + order.getArea() + DELIMITER
                + order.getCostPsf() + DELIMITER + order.getLaborPsf() + DELIMITER
                + order.getMaterialCost() + DELIMITER + order.getLaborCost() + DELIMITER
                + order.getTax() + DELIMITER + order.getTotal();
    }

    // This function is used for write to a file. It uses marshall method here.
    // It writes line by line, not the all file at once. At every line it calls marshall method.
    @Override
    public void writeFile(List<Order> list) throws FlooringMasteryException {
        PrintWriter out;

        try{
            out = new PrintWriter(new FileWriter(ORDER_FILE));
        } catch (IOException e) {
            throw new FlooringMasteryException("Could not save the order data", e);
        }
        String orderAsText;

        for(Order currentOrder : list){
            orderAsText = marshallOrder(currentOrder);
            out.println(orderAsText);
            out.flush();
        }

        out.close();

    }

    // This function is used for read from a file. It uses unmarshall method here.
    // It reads line by line, not the all file at once. At every line it calls unmarshall method.
    @Override
    public Map<Integer, Order> readFile(String file) throws FlooringMasteryException {
        Scanner scanner;
        Map<Integer,Order> ordersFromFile = new HashMap<>();
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(file)));
        }catch(FileNotFoundException e){
            throw new FlooringMasteryException("File not found",e);
        }

        String currentLine;
        Order currentOrder;

        while(scanner.hasNextLine()){

            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            ordersFromFile.put(currentOrder.getOrderNumber(),currentOrder);

        }

        scanner.close();
        return ordersFromFile;
    }

    @Override
    public Products unmarshallProduct(String line) {
        String[] productTokens = line.split(DELIMITER);
        String productType;
        Products productFromFile = new Products();

        if(productTokens.length>1){
            productType = productTokens[0];
            productFromFile = new Products(productType);
            productFromFile.setCostPSF(BigDecimal.valueOf(Double.valueOf(productTokens[1])));
            productFromFile.setLaborCostPSF(BigDecimal.valueOf(Double.valueOf(productTokens[2])));
        }

        return productFromFile;
    }

    @Override
    public Tax unmarshallTax(String line) {
        String[] taxTokens = line.split(DELIMITER);
        String stateAbbreviation;
        Tax taxFromFile = new Tax();

        if(taxTokens.length>1){
            stateAbbreviation = taxTokens[0];
            taxFromFile = new Tax(stateAbbreviation);
            taxFromFile.setStateName(taxTokens[1]);
            taxFromFile.setTaxRate(BigDecimal.valueOf(Double.valueOf(taxTokens[2])));
        }

        return taxFromFile;
    }

    @Override
    public Map<String, Products> readFileProducts() throws FlooringMasteryException {
        Scanner scanner;

        try{
            scanner = new Scanner(new BufferedReader(new FileReader("Products.txt")));
        }catch(FileNotFoundException e){
            throw new FlooringMasteryException("File not found",e);
        }

        String currentLine;
        Products currentProduct;
        scanner.nextLine();
        while(scanner.hasNextLine()){

            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            products.put(currentProduct.getProductType(),currentProduct);

        }

        scanner.close();
        return products;
    }

    @Override
    public Map<String, Tax> readFileTax() throws FlooringMasteryException {
        Scanner scanner;

        try{
            scanner = new Scanner(new BufferedReader(new FileReader("Taxes.txt")));
        }catch(FileNotFoundException e){
            throw new FlooringMasteryException("File not found",e);
        }

        String currentLine;
        Tax currentTax;
        scanner.nextLine();
        while(scanner.hasNextLine()){

            currentLine = scanner.nextLine();
            currentTax = unmarshallTax(currentLine);
            taxes.put(currentTax.getStateAbbreviation(),currentTax);

        }

        scanner.close();
        return taxes;
    }

    @Override
    public void writeNewFile(Order order, String fileName) throws FlooringMasteryException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(fileName, true));
        } catch (IOException e) {
            throw new FlooringMasteryException("Could not write the file.", e);
        }

        String orderAsText;

        orderAsText = marshallOrder(order);
        out.println(orderAsText);
        out.flush();

        out.close();

    }

    @Override
    public void createNewFile(String fileName) throws FlooringMasteryException{
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(fileName, true));
        } catch (IOException e) {
            throw new FlooringMasteryException("Could not write the file.", e);
        }
    }
}
