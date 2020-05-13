package gui.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import gui.HelperMethods;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import restaurant.Restaurant;
import restaurant.Table;
import restaurant.dish.Appetizer;
import restaurant.dish.Desert;
import restaurant.dish.Dish;
import restaurant.dish.MainCourse;
import restaurant.user.*;

import javax.xml.bind.JAXBException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class ControlPanelController {
    private final Restaurant restaurant;
    private static HashMap<String, Class<? extends User>> userTypes = new HashMap<>();
    private static HashMap<String, Class<? extends Dish>> dishTypes = new HashMap<>();

    static { // Static initializer.
        // This violates the Open-closed principle.
        // To solve this, we will need to use reflection.
        // I just wanted to keep it simple as reflection is out of the scope for OOP course.
        userTypes.put("Manager", Manager.class);
        userTypes.put("Waiter", Waiter.class);
        userTypes.put("Cooker", Cooker.class);
        userTypes.put("Client", Client.class);

        dishTypes.put("Main Course", MainCourse.class);
        dishTypes.put("Desert", Desert.class);
        dishTypes.put("Appetizer", Appetizer.class);

    }

    @FXML
    private JFXTextField textTableNumber;

    @FXML
    private JFXTextField textNumberOfSeats;

    @FXML
    private JFXCheckBox checkIsSmoking;

    @FXML
    private JFXTextField textDishName;

    @FXML
    private JFXTextField textDishPrice;

    @FXML
    private JFXComboBox comboDishCategory;

    @FXML
    private JFXTextField textName;

    @FXML
    private JFXTextField textUsername;

    @FXML
    private JFXPasswordField textPassword;

    @FXML
    private JFXPasswordField textPasswordReEnter;

    @FXML
    private ComboBox comboUserTypes;

    public ControlPanelController(Restaurant restaurant) {
        this.restaurant = restaurant;

    }


    public void initialize() {
        for (String userType : userTypes.keySet()) {
            comboUserTypes.getItems().add(userType);
        }
        for (String dishCategory: dishTypes.keySet()) {
            comboDishCategory.getItems().add(dishCategory);
        }

        comboUserTypes.setValue("Client");
        comboDishCategory.setValue("Main Course");
    }

    public void addNewUserClicked() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, JAXBException {
        if (textName.getText() == null || textName.getText().length() == 0) {
            textName.requestFocus();
            return;
        }

        if (textUsername.getText() == null || textUsername.getText().length() == 0) {
            textUsername.requestFocus();
            return;
        }

        if (restaurant.getUsers().stream().anyMatch(u -> u.getUsername().equals(textUsername.getText()))) {
            new Alert(Alert.AlertType.ERROR, "A user with this username already exists.").showAndWait();
            textUsername.requestFocus();
            return;
        }

        if (textPassword.getText() == null || textPassword.getText().length() == 0) {
            textPassword.requestFocus();
            return;
        }

        if (textPasswordReEnter.getText() == null || textPasswordReEnter.getText().length() == 0) {
            textPasswordReEnter.requestFocus();
            return;
        }

        if (!textPassword.getText().equals(textPasswordReEnter.getText())) {
            new Alert(Alert.AlertType.ERROR, "Passwords doesn't match.").showAndWait();
            textPasswordReEnter.requestFocus();
            return;
        }


        User newUser = userTypes.get(comboUserTypes.getValue()).getConstructor(String.class, String.class, String.class).newInstance(textUsername.getText(), textPassword.getText(), textName.getText());
        restaurant.addUser(newUser);
        HelperMethods.saveRestaurantToXml("restaurant.xml", restaurant);
        new Alert(Alert.AlertType.INFORMATION, "The user was added successfully").showAndWait();
        textUsername.setText("");
        textPassword.setText("");
        textPasswordReEnter.setText("");
        textName.setText("");
    }

    public void addNewDishClicked() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, JAXBException {
        if (textDishName.getText() == null || textDishName.getText().length() == 0) {
            textDishName.requestFocus();
            return;
        }

        if (textDishPrice.getText() == null || textDishPrice.getText().length() == 0) {
            textDishPrice.requestFocus();
            return;
        }
        double price;
        try {
            price = Double.parseDouble(textDishPrice.getText());
            if (price <= 0) {
                new Alert(Alert.AlertType.ERROR, "Dish price must be positive.");
                textDishPrice.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Enter a valid price.");
            textDishPrice.requestFocus();
            return;
        }

        Dish newDish = dishTypes.get(comboDishCategory.getValue()).getConstructor(String.class, Double.TYPE).newInstance(textDishName.getText(), price);
        restaurant.addDish(newDish);
        HelperMethods.saveRestaurantToXml("restaurant.xml", restaurant);
        new Alert(Alert.AlertType.INFORMATION, "The dish was added successfully").showAndWait();
        textDishName.setText("");
        textDishPrice.setText("");
    }

    public void addNewTableClicked() throws JAXBException {
        if (textTableNumber.getText() == null || textTableNumber.getText().length() == 0) {
            textTableNumber.requestFocus();
            return;
        }

        short tableNumber;
        try {
            tableNumber = Short.parseShort(textTableNumber.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Enter a valid table number").showAndWait();
            textTableNumber.requestFocus();
            return;
        }

        if (restaurant.getTables().stream().anyMatch(t -> t.getNum() == tableNumber)) {
            new Alert(Alert.AlertType.ERROR, "A table with this number already exists.").showAndWait();
            textTableNumber.requestFocus();
            return;
        }

        if (textNumberOfSeats.getText() == null || textNumberOfSeats.getText().length() == 0) {
            textNumberOfSeats.requestFocus();
            return;
        }


        short numberOfSeats;
        try {
            numberOfSeats = Short.parseShort(textNumberOfSeats.getText());
            if (numberOfSeats <= 0) {
                new Alert(Alert.AlertType.ERROR, "Number of seats must be positive.").showAndWait();
                textNumberOfSeats.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Enter a valid number of seats").showAndWait();
            textNumberOfSeats.requestFocus();
            return;
        }

        restaurant.addTable(new Table(numberOfSeats, tableNumber, checkIsSmoking.isSelected()));
        HelperMethods.saveRestaurantToXml("restaurant.xml", restaurant);
        new Alert(Alert.AlertType.INFORMATION, "The table was added successfully").showAndWait();
        textTableNumber.setText("");
        textNumberOfSeats.setText("");
        checkIsSmoking.setSelected(false);
    }
}
