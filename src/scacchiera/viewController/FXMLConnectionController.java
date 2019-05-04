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
import java.util.Random;
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
        ButtonType NULL = new ButtonType("Non cambia nulla");
        alert.getButtonTypes().addAll(BIANCO, NERO, NULL);
        alert.setTitle("Settings");
        alert.setHeaderText("Selezione schieramento.");
        alert.setContentText("Seleziona un colore:");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == BIANCO) {
                Settings.colore = Colore.BIANCO;
            } else if (result.get() == NERO) {
                Settings.colore = Colore.NERO;
            } else {
                Settings.colore = null;
            }
        }
        Settings.trr = new ThreadRiceviRichiesta(9800);
        Settings.trr.start();
    }

    @FXML
    void richiediConnessione() throws Exception {
        richiediConnessione(ip.getText(), 9800, "richiesta");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void richiediConnessione(String ip, int port, String message) {
        new ThreadSend(ip, port, message).start();
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void alert(String title, String contentText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.show();
    }

    @FXML
    void esc(ActionEvent event) {
        Stage stage = (Stage) tabPane.getScene().getWindow();
        Scene scene = stage.getScene();
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/scacchiera/viewController/FXMLIndex.fxml"));
        Parent root;
        try {
            root = (Parent) fXMLLoader.load();
            tabPane.getScene().getWindow().setHeight(400 + 39);
            tabPane.getScene().getWindow().setWidth(600 + 16);
            scene.setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            if (Settings.trr != null && Settings.trr.isAlive()) {
                Settings.trr.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private class ThreadSend extends Thread {

        private String ip;
        private int port;
        private String message;

        public ThreadSend(String ip, int port, String message) {
            this.ip = ip;
            this.port = port;
            this.message = message;
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

                bw.write(message);
                bw.newLine();
                bw.flush();

                String message = br.readLine();

                if (message.equals("richiesta accettata")) {
                    message = br.readLine();
                    if (message.equals("bianco") || message.equals("nero")) {
                        Settings.player = socket;
                        Settings.playerReader = br;
                        Settings.playerWriter = bw;
                        Settings.trr = new ThreadRiceviRichiesta(50000);//TODO Modifica e parametrizza
                        Settings.trr.start();
                        if (message.equals("bianco")) {
                            Settings.colore = BIANCO;
                        } else if (message.equals("nero")) {
                            Settings.colore = NERO;
                        }
                    } else if (message.equals("richiesta colore")) {
                        Settings.player = socket;
                        Settings.playerReader = br;
                        Settings.playerWriter = bw;
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.NONE);
                            ButtonType BIANCO = new ButtonType("Bianco");
                            ButtonType NERO = new ButtonType("Nero");
                            ButtonType NULL = new ButtonType("Non cambia nulla");
                            alert.getButtonTypes().addAll(BIANCO, NERO, NULL);
                            alert.setTitle("Settings");
                            alert.setHeaderText("Selezione schieramento.");
                            alert.setContentText("Seleziona un colore:");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == BIANCO) {
                                Settings.colore = Colore.BIANCO;
                            } else if (result.get() == NERO) {
                                Settings.colore = Colore.NERO;
                            } else {
                                Settings.colore = Colore.values()[Math.abs(new Random().nextInt()) % 2];
                            }
                            if (Settings.colore == Colore.BIANCO) {
                                try {
                                    Settings.playerWriter.write("nero\n");
                                    Settings.playerWriter.flush();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                try {
                                    Settings.playerWriter.write("bianco\n");
                                    Settings.playerWriter.flush();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            alert("Connessione effettuata!", "Ti sei collegato a " + ip, Alert.AlertType.INFORMATION);

                            inizioPartita();
                        });
                    }
                    new ThreadRicezione().start();
                } else if (message.equals("richiesta rifiutata")) {
                    Platform.runLater(() -> {
                        alert("Richiesta rifiutata!", "Immetti un altro ip.", Alert.AlertType.ERROR);
                    });
                    socket.close();
                    br.close();
                    bw.close();
                } else {
                    Platform.runLater(() -> {
                        alert("Errore nella richiesta!", "Ritenta.", Alert.AlertType.ERROR);
                    });
                    socket.close();
                    br.close();
                    bw.close();
                }
            } catch (IOException ex) {
                Platform.runLater(() -> {
                    alert("IP non corretto.", "Reinserisci un IP valido.", Alert.AlertType.ERROR);
                });
            }
        }
    }

    public class ThreadRiceviRichiesta extends Thread implements Closeable {

        private int port;
        private final ThreadRicezione tr;
        private boolean isFinito = false;
        private ServerSocket serverSocket;

        public ThreadRiceviRichiesta(int port) {
            this.port = port;
            tr = new ThreadRicezione();
        }

        @Override
        public void run() {
            serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException ex) {
                Platform.runLater(() -> {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Errore!");
                    a.setHeaderText("Errore apertura socket.");
                    a.setContentText("Porta occupata.");
                    a.show();
                });
                return;
            }
            while (true) {
                try {
                    Socket socket = serverSocket.accept();

                    InputStream inputStream = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(inputStream);
                    BufferedReader br = new BufferedReader(isr);

                    String message = br.readLine();

                    OutputStream os = socket.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    BufferedWriter bw = new BufferedWriter(osw);

                    if (message.equals("richiesta")) {
                        if (Settings.player == null) {
                            bw.write("richiesta accettata\n");
                            bw.flush();
                            Settings.player = socket;
                            Settings.playerReader = br;
                            Settings.playerWriter = bw;
                            if (Settings.colore == null) {
                                bw.write("richiesta colore\n");
                                bw.flush();
                                message = br.readLine();
                                if (message.equals("bianco")) {
                                    Settings.colore = BIANCO;
                                } else {
                                    Settings.colore = NERO;
                                }
                            } else {
                                bw.write(Settings.colore.notThis().toString().toLowerCase() + "\n");
                                bw.flush();
                            }
                            Platform.runLater(() -> {
                                alert("Avversario trovato!", "Avvio partita.", Alert.AlertType.INFORMATION);

                                inizioPartita();
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
                    if (isFinito) {
                        System.out.println("chiuso ta");
                        return;
                    }
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
            serverSocket.close();
            if (Settings.player != null && !Settings.player.isClosed()) {
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
            Settings.colore = null;
            Settings.trr = null;
        }

        public void setIsFinito(boolean isFinito) {
            this.isFinito = isFinito;
        }
    }
}
