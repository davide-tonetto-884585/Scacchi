/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model.TCP;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import scacchiera.model.Posizione;
import scacchiera.model.Posizione.Colonna;
import scacchiera.model.Posizione.Riga;
import scacchiera.model.Simbolo;

/**
 *
 * @author tonetto.davide
 */
public class ThreadRicezione extends Thread implements Closeable {

    private boolean isFinito = false;

    @Override
    public void run() {
        while (true) {
            try {
                String line = Settings.playerReader.readLine();
                if (line.length()>4 && line.substring(0, 5).equals("mossa")) {
                    String temp = line.substring(6, line.length());

                    Posizione pos1 = new Posizione(Riga.values()[Integer.parseInt(String.valueOf(temp.charAt(1))) - 1], Colonna.getFromChar(temp.charAt(0)));
                    Posizione pos2 = new Posizione(Riga.values()[Integer.parseInt(String.valueOf(temp.charAt(3))) - 1], Colonna.getFromChar(temp.charAt(2)));

                    Settings.partita.muovi(pos1, pos2);
                    if (temp.charAt(4) != '0') {
                        Settings.partita.promuovi(Simbolo.values()[Integer.parseInt(String.valueOf(temp.charAt(4)))], Settings.partita.trovaPezzo(pos2).getColore());
                    }

                    Platform.runLater(() -> {
                        Settings.controller.aggiornaScacchiera();
                    });
                } else if (line.equals("richiesta patta")) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.NONE);
                        ButtonType Accetta = new ButtonType("Accetta");
                        ButtonType Rifiuta = new ButtonType("Rifiuta");
                        alert.getButtonTypes().addAll(Accetta, Rifiuta);
                        alert.setTitle("Settings");
                        alert.setHeaderText("Richiesta patta dall'avversario.");
                        alert.setContentText("Seleziona una risposta, se accetti la partità si concluderà e potrai richiedere il restart per iniziarne un'altra.");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent()) {
                            if (result.get() == Accetta) {
                                try {
                                    Settings.playerWriter.write("conferma patta\n");
                                    Settings.playerWriter.flush();
                                    Platform.runLater(() -> {
                                        Settings.controller.pattaOResaAccettata();
                                    });
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                try {
                                    Settings.playerWriter.write("rifiuto patta\n");
                                    Settings.playerWriter.flush();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });
                } else if (line.equals("rifiuto patta")) {
                    Platform.runLater(() -> {
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setTitle("Patta rifiutata");
                        a.setHeaderText("L'avversario non ha acettato la patta.");
                        a.setContentText("La partita continua!");
                        a.show();
                    });
                } else if (line.equals("conferma patta")) {
                    Platform.runLater(() -> {
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setTitle("Patta accettata");
                        a.setHeaderText("L'avversario ha acettato la patta.");
                        a.setContentText("La partita è conclusa! Puoi richiedere il restart per iniziarne un'altra.");
                        a.show();
                        Settings.controller.pattaOResaAccettata();
                    });
                } else if (line.equals("richiesta restart")) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.NONE);
                        ButtonType Accetta = new ButtonType("Accetta");
                        ButtonType Rifiuta = new ButtonType("Rifiuta");
                        alert.getButtonTypes().addAll(Accetta, Rifiuta);
                        alert.setTitle("Settings");
                        alert.setHeaderText("Richiesta di restart dall'avversario.");
                        alert.setContentText("Seleziona una risposta, se accetti la partità verrà reiniziata.");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent()) {
                            if (result.get() == Accetta) {
                                try {
                                    Settings.playerWriter.write("conferma restart\n");
                                    Settings.playerWriter.flush();
                                    Settings.controller.restartAccettato();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                try {
                                    Settings.playerWriter.write("rifiuto restart\n");
                                    Settings.playerWriter.flush();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });
                } else if (line.equals("conferma restart")) {
                    Platform.runLater(() -> {
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setTitle("restart accettato");
                        a.setHeaderText("L'avversario ha acettato il restart.");
                        a.setContentText("Avviata una nuova partita.");
                        a.show();
                        Settings.controller.restartAccettato();
                    });
                } else if (line.equals("rifiuto restart")) {
                    Platform.runLater(() -> {
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setTitle("Restart rifiutato");
                        a.setHeaderText("L'avversario ha rifiutato il restart.");
                        a.setContentText("La partita continua.");
                        a.show();
                    });
                } else if (line.equals("resa")) {
                    Platform.runLater(() -> {
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setTitle("Vittoria!");
                        a.setHeaderText("L'avversario si è arreso.");
                        a.setContentText("Complimenti! Puoi richiedere il restart per iniziarne un'altra.");
                        a.show();
                        Settings.controller.pattaOResaAccettata();
                    });
                } else {
                    System.out.println("errore, message: " + line);
                }
            } catch (IOException | NullPointerException ex) {
                if (isFinito) {
                    return;
                } else {
                    Platform.runLater(() -> {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Vittoria!");
                        a.setHeaderText("Hai vinto!");
                        a.setContentText("avversario disconnesso.");
                        a.show();
                    });
                    try {
                        Settings.trr.setIsFinito(true);
                        Socket temp = new Socket("localhost", 9800);
                        temp.close();
                        return;
                    } catch (IOException ex1) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        isFinito = true;
    }

}
