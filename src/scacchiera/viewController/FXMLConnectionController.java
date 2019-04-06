/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.viewController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import scacchiera.model.TCP.ThreadRicevi;
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
        new ThreadRicevi(9800).start();
    }

    @FXML
    void richiediConnessione(ActionEvent event) throws Exception {
        richiediConnessione(ip.getText(), 9800);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void richiediConnessione(String ip, int port) {
        new ThreadSend(ip, port).start();
    }

    private class ThreadSend extends Thread {

        private String ip;
        private int port;

        public ThreadSend(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), 1000);

                InputStream inputStream = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(isr);

                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);

                bw.write("richiesta\n");
                bw.flush();

                String message = br.readLine();

                if (message.equals("richiesta accettata")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Connessione effettuata!");
                            alert.setContentText("Ti sei collegato a " + ip);
                            alert.showAndWait();
                        }
                    });
                } else if (message.equals("richiesta rifiutata")) {
                    //TODO messaggi errore
                }
            } catch (IOException ex) {
                System.out.println("errore avvio socket");
            }
        }
    }
}
