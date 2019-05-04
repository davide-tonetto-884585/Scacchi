/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import scacchiera.model.TCP.Settings;

/**
 *
 * @author tonetto.davide
 */
public class Scacchiera extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/scacchiera/viewController/FXMLIndex.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setOnCloseRequest((WindowEvent event) -> {
            try {
                if (Settings.trr != null && Settings.trr.isAlive()) {
                    Settings.trr.close();
                }
            } catch (IOException ex) {
                Platform.runLater(() -> {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Errore!");
                    a.setHeaderText("Errore nella chiusura della partita.");
                    a.setContentText("Ritenta");
                    a.show();
                });
                event.consume();
            }
        });
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
