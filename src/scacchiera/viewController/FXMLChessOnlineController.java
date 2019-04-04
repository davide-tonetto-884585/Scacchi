/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.viewController;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author davide
 */
public class FXMLChessOnlineController implements Initializable {

    private static InetAddress ip;

    @FXML
    private Label label;
    
    @FXML
    void mostra(ActionEvent event) {
        label.setText(ip.toString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
    }
}
