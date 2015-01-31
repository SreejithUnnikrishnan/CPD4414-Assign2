/*
 * Copyright 2015 Len Payne <len.payne@lambtoncollege.ca>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cpd4414.assign2;

import cpd4414.assign2.OrderQueue;
import cpd4414.assign2.Purchase;
import cpd4414.assign2.Order;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Len Payne <len.payne@lambtoncollege.ca>
 */
public class OrderQueueTest {
    
    public OrderQueueTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testWhenCustomerExistsAndPurchasesExistThenTimeReceivedIsNow() {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        order.addPurchase(new Purchase(4, 50));
        order.addPurchase(new Purchase(6, 40));
        try {
            orderQueue.add(order);
        } catch (Exception ex) {
            System.out.println("Exception: " +ex.getMessage());;
        }
        
        long expResult = new Date().getTime();
        long result = order.getTimeReceived().getTime();
        assertTrue(Math.abs(result - expResult) < 1000);
    }
    
    @Test
    public void testWhenNeitherCustomerExistsNorCustomerNameExistThenThrowException() {
        boolean check = false;
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("", "");
        order.addPurchase(new Purchase(4, 10));
        order.addPurchase(new Purchase(6, 20));
        try{
            orderQueue.add(order);
        }
        catch(NoCustomerDetailsException ex){
            check = true;
        }
        catch(Exception ex){
            check = false;
        }
        
        assertTrue(check);
    }
    
    @Test
    public void testWhenNewOrderArrivesWhenNoListOfPurchasesThenThrowException() {
        boolean check = false;
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        try{
            orderQueue.add(order);
        }
        catch(NoPurchaseFoundException ex){
            check = true;
        }
        catch(Exception ex){
            check = false;
        }
        
        assertTrue(check);
    }
    
    @Test
    public void testWhenNoOrdersThenReturnNull() {
        OrderQueue orderQueue = new OrderQueue();
        Order order = orderQueue.next();
        assertNull(order);
        
    }
    
    @Test
    public void testWhenOrdersAvailableThenReturnOrderWithEarliestTimeReceived(){
        try {
            OrderQueue orderQueue = new OrderQueue();
            
            Order order1 = new Order("CUST00001", "ABC Construction");
            order1.addPurchase(new Purchase(4, 50));
            orderQueue.add(order1);
            Order order2 = new Order("CUST00002", "JJ Construction");
            order2.addPurchase(new Purchase(6, 20));
            orderQueue.add(order2);
            
            Order order3 = orderQueue.next();
            assertEquals(order3, order1);
        } catch (Exception ex) {
            System.out.println("Exception: " +ex.getMessage());
        }
    }
    
    @Test
    public void testWhenOrderHasTimeReceivedAndAllOfPurchasesAreInStock(){
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        order.addPurchase(new Purchase(2, 50));
        order.addPurchase(new Purchase(6, 20));
        try {
            orderQueue.add(order);
            orderQueue.process();
        } 
        
        catch (Exception ex) {
            System.out.println("Some Other exception: " +ex.getMessage());
        }  
        long expResult = new Date().getTime();
        long result = order.getTimeProcessed().getTime();
        assertTrue(Math.abs(result - expResult) < 1000);
    }
    
    @Test
    public void testWhenOrderDoesNotHaveTimeReceivedThenThrowException(){
        boolean check = false;
        OrderQueue orderQueue = new OrderQueue();
        Order order1 = new Order("CUST00001", "ABC Construction");
        order1.addPurchase(new Purchase(4, 40));
        try {
            orderQueue.add(order1);
            //order1.setTimeReceived(null);
        } catch (Exception ex) {
            System.out.println("Exception in adding order: " +ex.getMessage());
        }
        
        try {
            orderQueue.process();
        } 
        catch (NoTimeReceivedException ex){
            check = true;
            System.out.println("NO Time exception: " +ex.getMessage());
        }
        catch (Exception ex) {
            System.out.println("Some Other exception: " +ex.getMessage());
        }
        assertTrue(check);
    }
    
    @Test
    public void testWhenOrderHasTimeProcessedTimeReceivedAndAllOfPurchasesInStockThenSetTimeFulfilled(){
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00002", "JJ Construction");
        order.addPurchase(new Purchase(2, 50));
        order.addPurchase(new Purchase(6, 20));
        try {
            orderQueue.add(order);
            orderQueue.process();
            orderQueue.fulfill();

        } 
        
        catch (Exception ex) {
            System.out.println("Some Other exception: " +ex.getMessage());
        }  
        long expResult = new Date().getTime();
        long result = order.getTimeFulfilled().getTime();
        assertTrue(Math.abs(result - expResult) < 1000);
    }
    
    @Test
    public void testWhenOrderDoesNotHaveTimeProcessedThenThrowException(){
        boolean check = false;
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        order.addPurchase(new Purchase(4, 10));
        order.addPurchase(new Purchase(6, 30));
        try {
            orderQueue.add(order);
            orderQueue.process();
            orderQueue.fulfill();
        } 
        catch(NoTimeProcessedException ex){
            check = true;
        }
        catch(Exception ex){
            System.out.println("Some Other Exception: " +ex.getMessage());
        }
        
        assertTrue(check);
    }
    
    @Test
    public void testWhenOrderDoesNotHaveTimeReceivedForFulFillThenThrowException(){
        boolean check = false;
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        order.addPurchase(new Purchase(2, 10));
        
        try {
            orderQueue.add(order);
            orderQueue.process();
            orderQueue.fulfill();
        } 
        catch(NoTimeReceivedException ex){
            check = true;
        }
        catch(Exception ex){
            System.out.println("Some Other Exception: " +ex.getMessage());
        }
        
        assertTrue(check);
    }
    
    @Test
    public void testWhenThereAreNoOrdersThenReturnEmptyString(){
        
        OrderQueue orderQueue = new OrderQueue();
        String result = orderQueue.report();
        assertTrue(result.isEmpty());
        
    }
    
    @Test
    public void testWhenThereAreOrdersThenReturnJSONObject(){
            String expected = "{ \"orders\" : [\n" +
                              " { \"customerId\" : \"CUST00001\",\n" +
                                " \"customerName\" : \"ABC Construction\",\n" +
                                " \"timeReceived\" : " +new Date().getTime()+ " ,\n" +
                                " \"timeProcessed\" : " +new Date().getTime()+ " ,\n" +
                                " \"timeFulfilled\" : " +new Date().getTime()+ " ,\n" +
                                " \"purchases\" : [\n" +
                                " { \"productId\" : \"PROD0004\", \"quantity\" : 10 }\n" +
                                " ] \n" +
                                " },\n" +
                                " { \"customerId\" : \"CUST00002\",\n" +
                                " \"customerName\" : \"JJ Construction\",\n" +
                                " \"timeReceived\" : " +new Date().getTime()+ " ,\n" +
                                " \"timeProcessed\" : \"\",\n" +
                                " \"timeFulfilled\" : \"\",\n" +
                                " \"purchases\" : [\n" +
                                " { \"productId\" : \"PROD0003\", \"quantity\" : 20 },\n" +
                                " { \"productId\" : \"PROD0002\", \"quantity\" : 10 }\n" +
                                " ]\n" +
                                " }\n" +
                                "] }";
            String result = "";
            OrderQueue orderQueue = new OrderQueue();
            Order order1 = new Order("CUST00001", "ABC Construction");
            order1.addPurchase(new Purchase(1, 10));
            Order order2 = new Order("CUST00002", "JJ Construction");
            order2.addPurchase(new Purchase(3, 20));
            order2.addPurchase(new Purchase(4, 10));
            try {
                orderQueue.add(order1);
                orderQueue.process();
                orderQueue.fulfill();
                orderQueue.add(order2);
            } 
            catch(Exception ex){
                System.out.println("Exception: " +ex.getMessage());
            }
            result = orderQueue.report();
            assertEquals(expected, result);
        
    }
    
}
