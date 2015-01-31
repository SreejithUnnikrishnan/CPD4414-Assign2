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

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Len Payne <len.payne@lambtoncollege.ca>
 */
public class OrderQueue {

    Queue<Order> orderQueue = new ArrayDeque<>();
    List<Order> orderList = new ArrayList<>();
    List<Order> fulfillList = new ArrayList<>();

    public void add(Order order) throws Exception {
        if (order.getCustomerId().isEmpty() || order.getCustomerName().isEmpty()) {
            throw new NoCustomerDetailsException();
        } else if (order.getListOfPurchases().isEmpty()) {
            throw new NoPurchaseFoundException();
        } else {
            orderQueue.add(order);
            order.setTimeReceived(new Date());
        }
    }

    public Order next() {
        return orderQueue.peek();
    }

    public void process() throws Exception {
        Order order = orderQueue.remove();
        Inventory in = new Inventory();

        if (order.getTimeReceived() == null) {
            throw new NoTimeReceivedException();
        } else if (in.getQuantityForId(order.getListOfPurchases().get(0).getProductId()) == 0) {
            throw new NoQuantityLeftException();
        } else {
            order.setTimeProcessed(new Date());
            orderList.add(order);
        }
    }

    public void fulfill() throws Exception {
        Order order = orderList.remove(0);
        Inventory in = new Inventory();
        if (order.getTimeReceived() == null) {
            throw new NoTimeReceivedException();
        } else if (order.getTimeProcessed() == null) {
            throw new NoTimeProcessedException();
        } else if (in.getQuantityForId(order.getListOfPurchases().get(0).getProductId()) == 0) {
            throw new NoQuantityLeftException();
        } else {
            order.setTimeFulfilled(new Date());
            fulfillList.add(order);
        }

    }

    public String report() {
        String report = "";

        if (!(fulfillList.isEmpty())) {
            Order order = fulfillList.get(0);
            List<Purchase> listOfPurchases = order.getListOfPurchases();
            report = "{ \"orders\" : [\n"
                    + " { \"customerId\" : " + order.getCustomerId() + ",\n"
                    + " \"customerName\" : " + order.getCustomerName() + ",\n"
                    + " \"timeReceived\" : " + order.getTimeReceived() + " ,\n"
                    + " \"timeProcessed\" : \"\" ,\n"
                    + " \"timeFulfilled\" : \"\" ,\n"
                    + " \"purchases\" : [\n"
                    + " { \"productId\" : " + listOfPurchases.get(0).getProductId() + ", \"quantity\" : " + listOfPurchases.get(0).getQuantity() + " }\n"
                    + " ] \n"
                    + " }";

        }
        if (!(orderList.isEmpty())) {
            Order order = orderList.get(0);
            List<Purchase> listOfPurchases = order.getListOfPurchases();
            if (report.isEmpty()) {
                report = "{ \"orders\" : [\n"
                        + " { \"customerId\" : " + order.getCustomerId() + ",\n"
                        + " \"customerName\" : " + order.getCustomerName() + ",\n"
                        + " \"timeReceived\" : " + order.getTimeReceived() + " ,\n"
                        + " \"timeProcessed\" : \"\" ,\n"
                        + " \"timeFulfilled\" : \"\" ,\n"
                        + " \"purchases\" : [\n"
                        + " { \"productId\" : " + listOfPurchases.get(0).getProductId() + ", \"quantity\" : " + listOfPurchases.get(0).getQuantity() + " }\n"
                        + " ] \n"
                        + " }";
            } else {
                report = report + ",\n"
                        + " { \"customerId\" : " + order.getCustomerId() + ",\n"
                        + " \"customerName\" : " + order.getCustomerName() + ",\n"
                        + " \"timeReceived\" : " + order.getTimeReceived() + " ,\n"
                        + " \"timeProcessed\" : \"\" ,\n"
                        + " \"timeFulfilled\" : \"\" ,\n"
                        + " \"purchases\" : [\n"
                        + " { \"productId\" : " + listOfPurchases.get(0).getProductId() + ", \"quantity\" : " + listOfPurchases.get(0).getQuantity() + " }\n"
                        + " ] \n"
                        + " }";

            }

        }

        if (!(orderQueue.isEmpty())) {
            Order order = orderQueue.peek();
            List<Purchase> listOfPurchases = order.getListOfPurchases();
            if (report.isEmpty()) {
                report = "{ \"orders\" : [\n"
                        + " { \"customerId\" : " + order.getCustomerId() + ",\n"
                        + " \"customerName\" : " + order.getCustomerName() + ",\n"
                        + " \"timeReceived\" : " + order.getTimeReceived() + " ,\n"
                        + " \"timeProcessed\" : \"\" ,\n"
                        + " \"timeFulfilled\" : \"\" ,\n"
                        + " \"purchases\" : [\n"
                        + " { \"productId\" : " + listOfPurchases.get(0).getProductId() + ", \"quantity\" : " + listOfPurchases.get(0).getQuantity() + " }\n"
                        + " ] \n"
                        + " }";
            } else {
                report = report + ",\n"
                        + " { \"customerId\" : " + order.getCustomerId() + ",\n"
                        + " \"customerName\" : " + order.getCustomerName() + ",\n"
                        + " \"timeReceived\" : " + order.getTimeReceived() + " ,\n"
                        + " \"timeProcessed\" : \"\" ,\n"
                        + " \"timeFulfilled\" : \"\" ,\n"
                        + " \"purchases\" : [\n"
                        + " { \"productId\" : " + listOfPurchases.get(0).getProductId() + ", \"quantity\" : " + listOfPurchases.get(0).getQuantity() + " }\n"
                        + " ] \n"
                        + " }";

            }

        }

        return report;

    }
}
