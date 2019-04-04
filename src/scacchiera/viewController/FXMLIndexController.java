/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.viewController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 *
 * @author tonetto.davide
 */
public class FXMLIndexController implements Initializable {

    @FXML
    private ToggleGroup radio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void start(ActionEvent event) {
        if (radio.getSelectedToggle() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore.");
            alert.setContentText("Seleziona un'opzione.");
            alert.showAndWait();
        } else if (((RadioButton) radio.getSelectedToggle()).getText().equals("Multigiocatore in locale")) {
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("FXMLDocument.fxml"));
                Scene scene = stage.getScene();
                node.getScene().getWindow().setHeight(594 + 39);
                node.getScene().getWindow().setWidth(971 + 16);
                scene.setRoot((Parent) fxmlLoader.load());
            } catch (IOException e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }
        } else {
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("FXMLConnection.fxml"));
                Scene scene = stage.getScene();
                node.getScene().getWindow().setHeight(400 + 39);
                node.getScene().getWindow().setWidth(600 + 16);
                scene.setRoot((Parent) fxmlLoader.load());
            } catch (IOException e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }
        }
    }
}
