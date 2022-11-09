package com.sal.flooringmastery.test;

import com.sal.flooringmastery.dao.*;
import com.sal.flooringmastery.model.Order;
import com.sal.flooringmastery.service.FlooringMasteryServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlooringMasteryDaoImplTest {
    public static FlooringMasteryDao testDao;

    public FlooringMasteryDaoImplTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        testDao = ctx.getBean("flooringMasteryDao",FlooringMasteryDao.class);
    }

    @BeforeAll
    public static void setUpClass() throws Exception {
        testDao = new FlooringMasteryDaoImpl();
    }

    @BeforeEach
    public void setUp() throws Exception {
        testDao = new FlooringMasteryDaoImpl();
    }

    @Test
    public void testAddListAllOrders()throws Exception{
        //create test inputs
        String fileName = "Orders_11132023.txt";
        int orderNumber = 1;
        Order order = new Order(1);

        order.setCustomerName("Aelita Stones");
        order.setState("TX");
        order.setTaxRate(BigDecimal.valueOf(4.45));
        order.setProductType("Wood");
        order.setArea(BigDecimal.valueOf(Double.valueOf(125)));
        order.setCostPsf(BigDecimal.valueOf(Double.valueOf(5.15)));
        order.setLaborPsf(BigDecimal.valueOf(Double.valueOf(4.75)));
        order.setMaterialCost(order.getArea().multiply(order.getCostPsf()).divide(BigDecimal.valueOf(10)));
        order.setLaborCost(order.getArea().multiply(order.getLaborPsf()).divide(BigDecimal.valueOf(10)));
        order.setTax((order.getMaterialCost().add(order.getLaborCost()))
                .multiply(order.getTaxRate().divide(BigDecimal.valueOf(100.0))).divide(BigDecimal.valueOf(100)));
        order.setTotal(order.getMaterialCost().add(order.getLaborCost()).add(order.getTax()).divide(BigDecimal.valueOf(100)));

        // add item to the dao
        testDao.addOrder(order,fileName);
        // list items
        List<Order> orders = testDao.listAllOrders(fileName);

        // check the data is equal
        assertEquals(orders.get(orderNumber-1),order,"Checking the order existing.");
        assertFalse(orders.get(order.getOrderNumber()-1).getCustomerName()==null,"The customer name must not be null");

    }

    @Test
    public void testRemoveOrder() throws Exception{
        String fileName = "Orders_11032023.txt";
        Order order = new Order(1);

        order.setCustomerName("Aelita Stones");
        order.setState("TX");
        order.setTaxRate(BigDecimal.valueOf(4.45));
        order.setProductType("Wood");
        order.setArea(BigDecimal.valueOf(Double.valueOf(125)));
        order.setCostPsf(BigDecimal.valueOf(Double.valueOf(5.15)));
        order.setLaborPsf(BigDecimal.valueOf(Double.valueOf(4.75)));
        order.setMaterialCost(order.getArea().multiply(order.getCostPsf()).divide(BigDecimal.valueOf(100)));
        order.setLaborCost(order.getArea().multiply(order.getLaborPsf()).divide(BigDecimal.valueOf(100)));
        order.setTax((order.getMaterialCost().add(order.getLaborCost()))
                .multiply(order.getTaxRate().divide(BigDecimal.valueOf(100.0))).divide(BigDecimal.valueOf(100)));
        order.setTotal(order.getMaterialCost().add(order.getLaborCost()).add(order.getTax()).divide(BigDecimal.valueOf(100)));

        // add item to the dao
        testDao.addOrder(order,fileName);

        Order order2 = new Order(2);

        order2.setCustomerName("Yumi Ishiyama");
        order2.setState("WA");
        order2.setTaxRate(BigDecimal.valueOf(9.25));
        order2.setProductType("Carpet");
        order2.setArea(BigDecimal.valueOf(Double.valueOf(150)));
        order2.setCostPsf(BigDecimal.valueOf(Double.valueOf(2.25)));
        order2.setLaborPsf(BigDecimal.valueOf(Double.valueOf(2.10)));
        order2.setMaterialCost(order2.getArea().multiply(order2.getCostPsf()).divide(BigDecimal.valueOf(100)));
        order2.setLaborCost(order2.getArea().multiply(order2.getLaborPsf()).divide(BigDecimal.valueOf(100)));
        order2.setTax((order2.getMaterialCost().add(order2.getLaborCost()))
                .multiply(order2.getTaxRate().divide(BigDecimal.valueOf(100.0))).divide(BigDecimal.valueOf(100)));
        order2.setTotal(order2.getMaterialCost().add(order2.getLaborCost()).add(order2.getTax()).divide(BigDecimal.valueOf(100)));

        // add item to the dao
        testDao.addOrder(order2,fileName);

        // remove the second order
        Order removedOrder = testDao.removeOrder(order2,fileName);

        // check that the correct object was removed
        assertEquals(removedOrder,order2,"The removed order should be order number 2");



        List<Order> orders = testDao.listAllOrders(fileName);

        // first check the general content of the list
        assertNotNull(orders,"All orders list should not be null");
        assertEquals(1,orders.size(),"All orders should be size 1");

        // check the specific contents of the list
        assertFalse(orders.contains(order2),"All orders should not include order 2");
        assertTrue(orders.contains(order),"All orders should include order 1");

    }


}