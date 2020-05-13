package gui.controllers;

import gui.HelperMethods;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import restaurant.Restaurant;
import restaurant.user.Client;
import restaurant.user.Employee;
import restaurant.user.User;

import java.io.IOException;

public class LoginController {
    private final Restaurant restaurant;
    @FXML
    private TextField textUsername;
    @FXML
    private PasswordField textPassword;

    public LoginController(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void loginClicked() throws IOException {
        for (User user : restaurant.getUsers()) {
            // If user with correct credentials is found, show welcoming message.
            if (user.getUsername().equals(textUsername.getText()) && user.getPassword().equals(textPassword.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Welcome, " + user.getName() + "!").showAndWait();
                // Then, close the login window.
                ((Stage) textUsername.getScene().getWindow()).close();
                // And open the dashboard.
                if (user instanceof Employee) {
                    HelperMethods.createStage(getClass().getResource("../views/employeeDashboard.fxml"),
                            user.getClass().getSimpleName() + " Dashboard | " + user.getName()
                            , c -> new EmployeeDashboardController(restaurant, (Employee) user)).showAndWait();

                } else if (user instanceof Client) {
                    HelperMethods.createStage(getClass().getResource("../views/customerDashboard.fxml"),
                            "Customer Dashboard | " + user.getName(),
                            c -> new CustomerDashboardController(restaurant, (Client) user)).showAndWait();

                } else {
                    throw new IllegalStateException("Unknown user type. This indicates an application bug."); // Should NEVER happen.
                }
                return;
            }
        }
        new Alert(Alert.AlertType.ERROR, "Incorrect username or password.").showAndWait();
        textPassword.requestFocus();
    }

    public void keyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) loginClicked();
    }

}
