package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import restaurant.Restaurant;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public final class HelperMethods {

    private HelperMethods() {
    }

    public static Stage createStage(URL fxmlResource, String title, Callback<Class<?>, Object> controllerFactory) throws IOException {
        FXMLLoader loader = new FXMLLoader(fxmlResource);
        if (controllerFactory != null) loader.setControllerFactory(controllerFactory);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        return stage;
    }

    public static void saveRestaurantToXml(String xmlFileName, Restaurant restaurant) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Restaurant.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(restaurant, new File(xmlFileName));
    }
}
