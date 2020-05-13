package gui;

import gui.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import restaurant.Restaurant;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/login.fxml"));
        Restaurant restaurant = loadRestaurantFromXmlFile("restaurant.xml");
        loader.setControllerFactory(c -> new LoginController(restaurant));
        Parent root = loader.load();
        primaryStage.setTitle("Login");
        primaryStage.setResizable(false);
        //Scene scene = new Scene(new JFXDecorator(primaryStage, root));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Restaurant loadRestaurantFromXmlFile(String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Restaurant.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Restaurant) unmarshaller.unmarshal(new File(filePath));
    }
}
