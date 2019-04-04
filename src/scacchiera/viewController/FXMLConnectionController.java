/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import scacchiera.model.UDP.*;

/**
 *
 * @author tonetto.davide
 */
public class FXMLConnectionController extends ComunicazioneUDP implements Initializable {

    @FXML
    private TextField ip;

    @FXML
    private Label label;

    @FXML
    private Label label2;

    @FXML
    void avviaServer(ActionEvent event) throws Exception {
        label2.setText("Attesa giocatore...");
        try {
            Server server = new Server();
            label2.setText("Connessione stabilita");
//            showCustomerDialog(server.getIp());
        } catch (ConnessioneException ex) {
            label2.setText("Connessione fallita.");
        }
    }

    @FXML
    void richiediConnessione(ActionEvent event) throws Exception {
        Client client = null;
        label.setText("Ricerca...");
        if (controlloIP(ip.getText())) {
            try {
                client = new Client(ip.getText());
                label.setText("Connessione stabilita.");  
            } catch (ConnessioneException ex) {
                label.setText("Connessione fallita.");
            }
        } else {
            label.setText("IP non valido.");
        }
    }

//    public Stage showCustomerDialog(InetAddress ip) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLChessOnline.fxml"));
//
//        Stage stage = new Stage(StageStyle.DECORATED);
//        stage.setScene(new Scene((Pane) loader.load()));
//
//        FXMLChessOnlineController controller = loader.<FXMLChessOnlineController>getController();
//        controller.setIp(ip);
//        
//        stage.show();
//        
//        return stage;
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
