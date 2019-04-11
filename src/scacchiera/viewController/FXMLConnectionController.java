/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.viewController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import scacchiera.model.Colore;
import static scacchiera.model.Colore.*;
import scacchiera.model.TCP.Settings;
import scacchiera.model.TCP.ThreadRicezione;
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
    private TabPane tabPane;
    
    @FXML
    void avviaServer(ActionEvent event) throws Exception {
        Alert alert = new Alert(Alert.AlertType.NONE);
        ButtonType BIANCO = new ButtonType("Bianco");
        ButtonType NERO = new ButtonType("Nero");
        alert.getButtonTypes().addAll(BIANCO, NERO);
        alert.setTitle("Settings");
        alert.setHeaderText("Selezione schieramento.");
        alert.setContentText("Seleziona un colore:");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == BIANCO) {
                Settings.colore = Colore.BIANCO;
            } else {
                Settings.colore = Colore.NERO;
            }
        }
        Settings.trr = new ThreadRiceviRichiesta(9800);
        Settings.trr.start();
    }

    @FXML
    void richiediConnessione() throws Exception {
        richiediConnessione(ip.getText(), 9800);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void richiediConnessione(String ip, int port) {
        new ThreadSend(ip, port).start();
    }

    public void inizioPartita() {
        Stage stage = (Stage) tabPane.getScene().getWindow();
        Scene scene = stage.getScene();
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/scacchiera/viewController/FXMLChessOnline.fxml"));

        Parent root;
        try {
            root = (Parent) fXMLLoader.load();
            tabPane.getScene().getWindow().setHeight(594 + 39);
            tabPane.getScene().getWindow().setWidth(971 + 16);
            scene.setRoot(root);
            stage.setOnCloseRequest((WindowEvent event) -> {
                try {
                    Settings.trr.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            });
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void alert(String title, String contentText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.show();
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

                if (message.equals("richiesta accettata BIANCO")) {
                    Settings.trr = new ThreadRiceviRichiesta(port);
                    Settings.player = socket;
                    Settings.playerReader = br;
                    Settings.playerWriter = bw;
                    Settings.colore = NERO;
                    Platform.runLater(() -> {
                        alert("Connessione effettuata!", "Ti sei collegato a " + ip, Alert.AlertType.INFORMATION);

                        inizioPartita();
                    });
                    new ThreadRicezione().start();
                } else if (message.equals("richiesta accettata NERO")) {
                    Settings.trr = new ThreadRiceviRichiesta(port);
                    Settings.player = socket;
                    Settings.playerReader = br;
                    Settings.playerWriter = bw;
                    Settings.colore = BIANCO;
                    Platform.runLater(() -> {
                        alert("Connessione effettuata!", "Ti sei collegato a " + ip, Alert.AlertType.INFORMATION);

                        inizioPartita();
                    });
                    new ThreadRicezione().start();
                } else if (message.equals("richiesta rifiutata")) {
                    Platform.runLater(() -> {
                        alert("Richiesta rifiutata!", "Immetti un altro ip.", Alert.AlertType.ERROR);
                    });
                    socket.close();
                    br.close();
                    bw.close();
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            alert("Errore nella richiesta!", "Ritenta.", Alert.AlertType.ERROR);
                        }
                    });
                    socket.close();
                    br.close();
                    bw.close();
                }
            } catch (IOException ex) {
                System.out.println("errore avvio socket");
            }
        }
    }

    public class ThreadRiceviRichiesta extends Thread implements Closeable {

        private int port;
        private final ThreadRicezione tr;
        private boolean isFinito = false;

        public ThreadRiceviRichiesta(int port) {
            this.port = port;
            tr = new ThreadRicezione();
        }

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(9800);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                return;
            }
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    
                    if (isFinito) {
                        socket.close();
                        return;
                    }

                    InputStream inputStream = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(inputStream);
                    BufferedReader br = new BufferedReader(isr);

                    String message = br.readLine();

                    OutputStream os = socket.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    BufferedWriter bw = new BufferedWriter(osw);

                    if (message.equals("richiesta")) {
                        if (Settings.player == null) {
                            bw.write("richiesta accettata " + Settings.colore.toString() + "\n");
                            bw.flush();
                            Settings.player = socket;
                            Settings.playerReader = br;
                            Settings.playerWriter = bw;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    alert("Avversario trovato!", "Avvio partita.", Alert.AlertType.INFORMATION);

                                    inizioPartita();
                                }
                            });
                            tr.start();
                        } else {
                            bw.write("richiesta rifiutata\n");
                            bw.flush();
                            bw.close();
                            br.close();
                            socket.close();
                        }
                    } else if (message.equals("richiesta spettatore")) {
                        bw.write("richiesta accettata\n");
                        bw.flush();
                        Settings.spettatori.add(socket);
                        Settings.bufferedReaders.add(br);
                        Settings.bufferedWriters.add(bw);
                    } else {
                        bw.write("errore\n");
                        bw.flush();
                        bw.close();
                        br.close();
                        socket.close();
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        @Override
        public void close() throws IOException {
            isFinito = true;
            if (tr != null && tr.isAlive()) {
                tr.close();
            }
            Socket temp = new Socket("localhost", port);
            temp.close();
            if (Settings.player != null) {
                Settings.player.close();
            }
            if (Settings.spettatori != null) {
                for (Socket s : Settings.spettatori) {
                    s.close();
                }
            }
            Settings.playerWriter = null;
            Settings.bufferedWriters = null;
            Settings.player = null;
            Settings.playerReader = null;
            Settings.bufferedReaders = null;
            Settings.spettatori = null;
        }

        public void setIsFinito(boolean isFinito) {
            this.isFinito = isFinito;
        }
    }
}
