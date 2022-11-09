package com.sal.flooringmastery.ui;

import com.sal.flooringmastery.model.Order;
import com.sal.flooringmastery.model.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FlooringMasteryView {

    private UserIO io;

    public FlooringMasteryView(){io=new UserIOImpl();}

    @Autowired
    public FlooringMasteryView(UserIO io){this.io = io;}

    // main menu banner
    public void mainMenuBanner(){
        io.print("<<Flooring Program>>\n");
        io.print("======Main Menu========\n");
    }

    // print the main menu and get selection from the user
    public int printMenuAndGetSelection(){
        mainMenuBanner();
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Quit");

        return io.readInt("Please select an operation.",1,6);

    }

    // edit menu banner
    public void editMenuBanner(){
        io.print("======Edit Menu========\n");
    }

    // print the edit menu and get the selection from the user
    public int printEditMenuAndGetSelection(){
        editMenuBanner();
        io.print("1. Edit customer name");
        io.print("2. Edit state");
        io.print("3. Edit product type");
        io.print("4. Edit area");
        io.print("5. Return to the main menu");

        return io.readInt("Please select from the above choices.", 1,5);
    }

    //This method displays result of editing an order. There are 2 options successfully or not.
    public void displayEditResult(Order order){
        if(order != null){
            io.print("Order successfully editted.");
        }else{
            io.print("No such Order.");
        }
        io.readString("Please hit enter to continue.");
    }

    public Order getNewOrderInfo(Order currentOrder){
        String customerName = io.readString("Enter customer name:");
        String state = io.readString("Enter state:");
        BigDecimal area = io.readBigDecimal("Enter the area:");


        currentOrder.setCustomerName(customerName);
        currentOrder.setState(state);
        currentOrder.setArea(area);

        return currentOrder;
    }



    public String getContinueSelection(){
        return io.readString("Do you want to place the order? (Yes/No)");
    }

    public String getProductTypeSelection(){
        return io.readString("Please select from the above choices.");
    }

    // create a new file name by concating strings
    public String getFileNameToAdd(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MMddyyyy");
        LocalDate today = LocalDate.now();
        String todayDate = today.format(df);
        String date = "Orders_";
        LocalDate order;

        String input = io.readString("Enter the order date:");
        order = LocalDate.parse(input,df);

        if(order.isBefore(today)){
            io.print("Order date should be in future.");

        }
        else {
            date+=(input)+".txt";
        }

        return date;
    }

    public String getFileName(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MMddyyyy");
        LocalDate today = LocalDate.now();
        String todayDate = today.format(df);

        LocalDate order;
        String input = io.readString("Enter the order date:");
        order = LocalDate.parse(input,df);

        String date = "Orders_";
        date+=input + ".txt";

        return date;

    }



    // This method displays information about a particular order.
    public void displayOrder(Order order) {
        displayOrderBanner();
        if (order != null) {
            io.print(order.toString());
            io.print("");
        } else {
            io.print("No such order.");
        }
        io.readString("Please hit enter to continue.");
    }



    public void displayOrderBanner () {
        io.print("=== Display Order ===\n");
    }

    public void displayOrdersBanner () {
        io.print("=== Display All Orders ===\n");
    }

    // displays a list of products for the user selection
    public ArrayList<Products> printAllProducts(List<Products> productsList){
        int j=1;
        ArrayList<Products> list=new ArrayList<>();
        list.add(null);
        for(Products i:productsList)
        {
            io.print(i.toString());
            list.add(i);
            j++;
        }
        return list;
    }
    // display all the order from a specified text file
    public ArrayList<Order> printAllOrders(List<Order> orderList){
        displayOrdersBanner();
        int j=1;
        ArrayList<Order> list=new ArrayList<>();
        list.add(null); // ?????
        for(Order i:orderList)
        {
            io.print(i.toString());
            list.add(i);
            j++;
        }
        return list;
    }


    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!\n");
    }

    public void displayExitBanner() {
        io.print("Good Bye!!!\n");
    }

    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===\n");
        io.print(errorMsg);
    }

    public BigDecimal getArea(){return io.readBigDecimal("Enter the new area:");}
    public String getState(){ return io.readString("Enter the state:");}

    public int getOrderNumber(){ return io.readInt("Enter order number:");}

    public void displayRemoveBanner(){io.print("=== DELETE AN ORDER ===\n");}

    public String getRemoveSelection(){
        return io.readString("Do you want to remove the order? (Yes/No)");
    }

    public String getCustomerName(){return io.readString("Enter new customer name:");}


    public void displayQuitMessage()
    {
        io.print("Goodbye" + "\n");
    }
}
