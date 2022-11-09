package com.sal.flooringmastery.test;

import com.sal.flooringmastery.dao.FlooringMasteryException;
import com.sal.flooringmastery.model.Order;
import com.sal.flooringmastery.service.FlooringMasteryService;
import com.sal.flooringmastery.service.FlooringMasteryServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlooringMasteryServiceImplTest {

    public static FlooringMasteryService service;

    public FlooringMasteryServiceImplTest() throws FlooringMasteryException{
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = ctx.getBean("serviceLayer",FlooringMasteryService.class);
    }
    @BeforeAll
    public static void setUpClass() throws FlooringMasteryException{
        service = new FlooringMasteryServiceImpl();
    }

    @Test
    public void testAddListAllOrders() throws Exception{
        // declare test inputs
        String fileName = "Orders_11132023.txt";
        int orderNumber = 1;
        Order order = new Order(1);

        order.setCustomerName("Aelita Stones");
        order.setState("TX");
        order.setProductType("Wood");
        order.setArea(BigDecimal.valueOf(Double.valueOf(125)));

        order = service.addOrderCalculations(order);
        service.addOrder(order, fileName);

        List<Order> orders = service.listAllOrders(fileName);

        assertEquals(orders.get(orderNumber-1),order,"Checking the order existing.");
        assertFalse(orders.get(order.getOrderNumber()-1).getCustomerName()==null,"The customer name must not be null");

    }

    @Test
    public void testRemoveOrder() throws Exception{
        // declare test inputs
        String fileName = "Orders_11042023.txt";
        Order order = new Order(1);

        order.setCustomerName("Aelita Stones");
        order.setState("TX");
        order.setTaxRate(BigDecimal.valueOf(4.45));
        order.setProductType("Wood");
        order.setArea(BigDecimal.valueOf(Double.valueOf(125)));

        // calculate properties and add item through service layer
        order = service.addOrderCalculations(order);
        service.addOrder(order, fileName);


        Order order2 = new Order(2);

        order2.setCustomerName("Yumi Ishiyama");
        order2.setState("WA");
        order2.setTaxRate(BigDecimal.valueOf(9.25));
        order2.setProductType("Carpet");
        order2.setArea(BigDecimal.valueOf(Double.valueOf(125)));

        // calculate properties and add item through service layer
        order2 = service.addOrderCalculations(order2);
        service.addOrder(order2, fileName);

        // remove the second order
        Order removedOrder = service.removeOrder(order2,fileName);

        // check that the correct object was removed
        assertEquals(removedOrder,order2,"The removed order should be order number 2");



        List<Order> orders = service.listAllOrders(fileName);

        // first check the general content of the list
        assertNotNull(orders,"All orders list should not be null");
        assertEquals(1,orders.size(),"All orders should be size 1");

        // check the specific contents of the list
        assertFalse(orders.contains(order2),"All orders should not include order 2");
        assertTrue(orders.contains(order),"All orders should include order 1");


    }

    @Test
    public void testAddEditArea() throws Exception{
        String fileName = "Orders_11052023.txt";
        Order order = new Order(1);

        order.setCustomerName("Aelita Stones");
        order.setState("TX");
        order.setTaxRate(BigDecimal.valueOf(4.45));
        order.setProductType("Wood");
        order.setArea(BigDecimal.valueOf(Double.valueOf(125)));


        // calculate properties and add order through service layer
        order = service.addOrderCalculations(order);
        service.addOrder(order, fileName);

        // set the new area value, and store the old area value for compare
        BigDecimal newArea = new BigDecimal(150);
        BigDecimal oldArea = new BigDecimal(String.valueOf(order.getArea()));

        // calculate properties and edit order through service layer
        order = service.addOrderCalculations(order);
        service.editArea(order,newArea,fileName);

        // check the area data
        assertFalse(oldArea == order.getArea(),"The new area for order can not be equal to the old area.");
        assertNotNull(order.getArea(),"Order area should not be null");


    }
}