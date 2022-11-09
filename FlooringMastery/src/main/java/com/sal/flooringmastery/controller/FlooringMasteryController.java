package com.sal.flooringmastery.controller;

import com.sal.flooringmastery.dao.FlooringMasteryException;
import com.sal.flooringmastery.model.Order;
import com.sal.flooringmastery.model.Products;
import com.sal.flooringmastery.service.FlooringMasteryOrderNotFoundException;
import com.sal.flooringmastery.service.FlooringMasteryService;
import com.sal.flooringmastery.service.FlooringMasteryStateNotFoundException;
import com.sal.flooringmastery.ui.FlooringMasteryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class FlooringMasteryController {

    // declare variables
    private FlooringMasteryView view;

    private FlooringMasteryService service;

    public FlooringMasteryController(){}

    @Autowired
    public FlooringMasteryController(FlooringMasteryView view,FlooringMasteryService service){
        this.view = view;
        this.service=service;
    }

    // display the main menu and do the related operations
    public void run(){
        String fileName;
        boolean keepGoing = true;
        try {
            while (keepGoing) {
                int selection = view.printMenuAndGetSelection();
                switch (selection){
                    case 1:
                        try {
                            fileName = view.getFileName();
                            displayOrders(fileName);
                        }catch(FlooringMasteryException e){
                            view.displayErrorMessage(e.getMessage());
                        }
                        break;
                    case 2:
                        try {
                            addOrder();
                        }catch(FlooringMasteryStateNotFoundException | FlooringMasteryOrderNotFoundException e){
                            view.displayErrorMessage(e.getMessage());
                        }
                        break;
                    case 3:
                        try {
                            editOrder();
                        } catch (FlooringMasteryOrderNotFoundException e) {
                            view.displayErrorMessage(e.getMessage());
                        }
                        break;
                    case 4:
                        try{
                            removeOrder();
                        }catch(FlooringMasteryOrderNotFoundException e){
                            view.displayErrorMessage(e.getMessage());
                        }
                        break;
                    case 6:
                        quit();
                        keepGoing=false;
                        break;

                }

            }
        }catch(FlooringMasteryException e){
            view.displayErrorMessage(e.getMessage());
        }
    }

    // display all orders in the file
    public void displayOrders(String fileName) throws FlooringMasteryException{
        List<Order> orders = service.listAllOrders(fileName);
        view.printAllOrders(orders);
    }

    // recalculate related properties and then add an order to the file if the user is sure about it
    public void addOrder() throws FlooringMasteryException, FlooringMasteryStateNotFoundException, FlooringMasteryOrderNotFoundException {
        String fileName = view.getFileNameToAdd();
        List<Products> products = service.listAllProducts();
        view.printAllProducts(products);
        Order newOrder = new Order();

        newOrder.setProductType(view.getProductTypeSelection());
        view.getNewOrderInfo(newOrder);
        service.addOrderCalculations(newOrder);
        view.displayOrder(newOrder);
        String result = view.getContinueSelection();

        if(result.equals("Yes")) {
            service.addOrder(newOrder,fileName);
        }
    }

    // remove an order from the file if the user is sure about it
    public void removeOrder() throws FlooringMasteryException, FlooringMasteryOrderNotFoundException {
        String fileName = view.getFileName();
        int orderNum = view.getOrderNumber();
        Order order = service.listAllOrders(fileName).get(orderNum-1);
        view.displayRemoveBanner();
        view.displayOrder(order);
        String result = view.getRemoveSelection();

        if(result.equals("Yes")) {
            service.removeOrder(order,fileName);
        }
    }

    // display edit menu for the user and do the related operations
    public void editOrder() throws FlooringMasteryException, FlooringMasteryOrderNotFoundException {

        String fileName = view.getFileName();
        int orderNum = view.getOrderNumber();
        Order currentOrder = service.listAllOrders(fileName).get(orderNum-1);
        if(currentOrder == null){
            throw new FlooringMasteryException("ORDER DOES NOT EXIST!!");
        }
        else{
            view.editMenuBanner();
            view.displayOrder(currentOrder);
            int editSelection = 0;
            boolean keepGoing = true;
            while(keepGoing){
                editSelection = view.printEditMenuAndGetSelection();

                switch (editSelection){
                    case 1:
                        try {
                            editCustomerName();
                        } catch (FlooringMasteryOrderNotFoundException e) {
                            view.displayErrorMessage(e.getMessage());
                        }
                        break;
                    case 2:
                        try {
                            editState();
                        } catch (FlooringMasteryStateNotFoundException e) {
                            view.displayErrorMessage(e.getMessage());
                        }
                        break;
                    case 3:
                        try {
                            editProductType();
                        } catch (FlooringMasteryStateNotFoundException e) {
                            view.displayErrorMessage(e.getMessage());
                        }
                        break;
                    case 4:
                        try {
                            editArea();
                        } catch (FlooringMasteryStateNotFoundException e) {
                            view.displayErrorMessage(e.getMessage());
                        }
                        break;
                    case 5:
                        keepGoing=false;
                        break;
                }

                if(keepGoing == false)
                    break;
            }
        }
    }

    // edit order's customer name
    // first show the old version of the order, then show the new version of it
    // if the user is sure about this editing, edit the order.
    public void editCustomerName() throws FlooringMasteryException, FlooringMasteryOrderNotFoundException {
        String fileName = view.getFileName();
        int orderNum = view.getOrderNumber();
        Order order = service.getOrder(orderNum,fileName);

        String newCustomerName = view.getCustomerName();
        view.displayOrder(order);

        if(!newCustomerName.equals("")){
            order.setCustomerName(newCustomerName);
            view.displayOrder(order);
            String result = view.getContinueSelection();
            if(result.equals("Yes")) {
                service.editCustomerName(orderNum, newCustomerName,fileName);
            }
        }

        view.displayEditResult(order);
    }

    // edit order's state
    // first show the old version of the order, then show the new version of it
    // if the user is sure about this editing, edit the order.
    // and while editing, recalculate the related properties about state
    public void editState() throws FlooringMasteryException, FlooringMasteryOrderNotFoundException, FlooringMasteryStateNotFoundException {
        String fileName = view.getFileName();
        int orderNum = view.getOrderNumber();
        Order order = service.getOrder(orderNum,fileName);
        String newState = view.getState();

        view.displayOrder(order);
        if(!newState.equals("")){
            order.setState(newState);
            order=service.addOrderCalculations(order);
            view.displayOrder(order);
            String result = view.getContinueSelection();
            if(result.equals("Yes")) {
                service.editState(order, newState, fileName);
            }
        }

        view.displayEditResult(order);
    }
    // edit order's product type
    // first show the old version of the order, then show the new version of it
    // if the user is sure about this editing, edit the order.
    // and while editing, recalculate the related properties about product type
    public void editProductType() throws FlooringMasteryException, FlooringMasteryOrderNotFoundException, FlooringMasteryStateNotFoundException {
        String fileName = view.getFileName();
        int orderNum = view.getOrderNumber();
        Order order = service.getOrder(orderNum,fileName);

        List<Products> products = service.listAllProducts();
        view.printAllProducts(products);
        String newProductType = view.getProductTypeSelection();

        view.displayOrder(order);

        if(!newProductType.equals("")){
            order.setProductType(newProductType);
            order = service.addOrderCalculations(order);
            view.displayOrder(order);
            String result = view.getContinueSelection();
            if(result.equals("Yes")) {
                service.editProductType(order, newProductType, fileName);
            }
        }

        view.displayEditResult(order);
    }

    // edit order's area
    // first show the old version of the order, then show the new version of it
    // if the user is sure about this editing, edit the order.
    // and while editing, recalculate the related properties about area
    public void editArea() throws FlooringMasteryException, FlooringMasteryOrderNotFoundException, FlooringMasteryStateNotFoundException {
        String fileName = view.getFileName();
        int orderNum = view.getOrderNumber();
        Order order = service.getOrder(orderNum,fileName);
        BigDecimal newArea = view.getArea();

        view.displayOrder(order);

        if(!newArea.equals("")){
            order.setArea(newArea);
            order = service.addOrderCalculations(order);
            view.displayOrder(order);
            String result = view.getContinueSelection();
            if(result.equals("Yes")) {
                service.editArea(order, newArea, fileName);
            }
        }
        view.displayEditResult(order);
    }

    // display quit message to the user
    public void quit() {
        view.displayQuitMessage();
    }
}
