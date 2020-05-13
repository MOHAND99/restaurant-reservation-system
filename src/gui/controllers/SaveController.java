package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SaveController {
    @FXML
    private Button cancelButton;
    private boolean isAccepted;

    private void closeStage() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    public void cancel() {
        isAccepted = false;
        closeStage();
    }

    public void save() {
        isAccepted = true;
        closeStage();
    }

    public boolean getIsAccepted() {
        return isAccepted;
    }

}
