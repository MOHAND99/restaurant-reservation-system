package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import restaurant.ClientPayments;
import restaurant.DayIncome;
import restaurant.Restaurant;
import restaurant.dish.Dish;
import restaurant.order.Order;
import restaurant.order.OrderItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StatisticsController {
    private final Restaurant restaurant;

    @FXML
    private Label bestSellerDishLabel;

    @FXML
    private Label bestSellerDishTodayLabel;

    @FXML
    private Label highestIncomeLabel;

    @FXML
    private Label bestCustomerLabel;

    public StatisticsController(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void initialize() {
        bestSellerDishLabel.setText(getBestSellerDishAt(null));
        bestSellerDishTodayLabel.setText(getBestSellerDishAt(LocalDate.now()));

        DayIncome dayIncome = restaurant.getMaxDayIncome();
        if (dayIncome == null) {
            highestIncomeLabel.setText("N/A");
        } else {
            highestIncomeLabel.setText(dayIncome.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)) + " (" + dayIncome.getDayEarn() + "LE)");
        }


        ClientPayments payments = restaurant.getBestCustomer();
        if (payments == null) {
            bestCustomerLabel.setText("N/A");
        } else {
            bestCustomerLabel.setText(payments.getClient().getName() + " has paid " + payments.getPayment() + "LE");
        }
    }

    // Pass a date, or null to get BestSellerDish ever
    private String getBestSellerDishAt(LocalDate date) {
        HashMap<Dish, Integer> dishes = new HashMap<>();
        Collection<Order> orders = (date == null) ? restaurant.getOrders() : restaurant.getOneDayOrders(date);
        // Calculate number of dishes sold in a HashMap.
        for (Order order : orders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                // putIfAbsent docs: If the specified key is not already associated with a value (or is mapped to null) associates it with the given value and returns null, else returns the current value.
                Integer currentValue;
                if ((currentValue = dishes.putIfAbsent(orderItem.getDish(), orderItem.getQuantity())) != null) {
                    dishes.put(orderItem.getDish(), currentValue + orderItem.getQuantity());
                }
            }
        }

        // Find the best-seller(s) dish(es).
        int maxUnitsSold = -1;
        StringBuilder bestSellers = new StringBuilder();
        for (Map.Entry<Dish, Integer> entry : dishes.entrySet()) {
            if (entry.getValue() > maxUnitsSold) {
                maxUnitsSold = entry.getValue();
                bestSellers = new StringBuilder();
                bestSellers.append(entry.getKey().getName())
                        .append(" x").append(entry.getValue()).append('\n');
            } else if (entry.getValue() == maxUnitsSold) {
                bestSellers.append(entry.getKey().getName())
                        .append(" x").append(entry.getValue()).append('\n');
            }
        }
        return bestSellers.toString();

    }

}
