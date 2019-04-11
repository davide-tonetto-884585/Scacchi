/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.viewController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import scacchiera.model.*;
import scacchiera.model.Posizione.*;
import scacchiera.model.TCP.Settings;

/**
 *
 * @author tonetto.davide
 */
public class FXMLChessOnlineController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView scacchiera;

    @FXML
    private Canvas canvas;

    @FXML
    private Canvas canvas2;

    @FXML
    private Canvas canvas3;

    @FXML
    private Label turno;

    @FXML
    private Label stato;

    @FXML
    private Label cella;

    private Colore player;
    private Partita p;
    private Posizione pos1, pos2;
    private Colore versoScacchiera;
    private static final double DIM_SC = 1080;
    private GraphicsContext graphics3;
    private GraphicsContext graphics2;
    private GraphicsContext graphics1;

    @FXML
    void guida(MouseEvent event) {
        double sx = 0, sy = 0, sw = 200, sh = 200, dx = 0, dy = 0, dw = 64, dh = 64;
        Image img = new Image("/scacchiera/viewController/res/imgs/puntatore.png");
        Image img2 = new Image("/scacchiera/viewController/res/imgs/puntatoreRosso.png");
        graphics2.clearRect(0, 0, 600, 600);
        if (event.getSceneX() - 46 < 0 || event.getSceneY() - 46 < 0 || event.getSceneX() - 46 > 504 || event.getSceneY() - 46 > 504) {
            cella.setText("Cursore fuori dalla scacchiera.");
        } else {
            Posizione pos = null;
            if (versoScacchiera == Colore.BIANCO) {
                pos = new Posizione(Riga.values()[7 - ((int) ((event.getSceneY() - 49) / (504 / 8)))], Colonna.values()[(int) ((event.getSceneX() - 47) / (504 / 8))]);
            } else {
                pos = new Posizione(Riga.values()[(int) ((event.getSceneY() - 47) / (504 / 8))], Colonna.values()[7 - ((int) ((event.getSceneX() - 47) / (504 / 8)))]);
            }
            cella.setText(pos.getColonna().toString() + ", " + pos.getRiga().toString());
            if (p.trovaPezzo(pos) != null && p.trovaPezzo(pos).getColore() == p.getTurnoCorrente() && p.trovaPezzo(pos).getColore() == player) {
                if (p.trovaPezzo(pos) != null) {
                    ArrayList<Posizione> posizioni = p.mossePossibiliConSacco(p.trovaPezzo(pos));
                    for (Posizione posizione : posizioni) {
                        if (versoScacchiera == Colore.BIANCO) {
                            dy = (7 - posizione.getRiga().ordinal()) * (504 / 8) + 49;
                            dx = posizione.getColonna().ordinal() * (504 / 8) + 46;
                        } else {
                            dy = posizione.getRiga().ordinal() * (504 / 8) + 47;
                            dx = (7 - posizione.getColonna().ordinal()) * (504 / 8) + 47;
                        }
                        if (p.isOccupato(posizione)) {
                            graphics2.drawImage(img2, sx, sy, sw, sh, dx, dy, dw, dh);
                        } else {
                            graphics2.drawImage(img, sx, sy, sw, sh, dx, dy, dw, dh);
                        }
                    }
                }
            }
        }
    }

    @FXML
    void handleButtonAction(MouseEvent event) {
        stato.setText("Selezione mossa.");
        if (event.getSceneX() - 46 < 0 || event.getSceneY() - 46 < 0 || event.getSceneX() - 46 > 504 || event.getSceneY() - 46 > 504) {
            stato.setText("Hai premuto fuori dalla scacchiera.");
        } else {
            if (pos1 == null) {
                if (versoScacchiera == Colore.BIANCO) {
                    pos1 = new Posizione(Riga.values()[7 - ((int) ((event.getSceneY() - 49) / (504 / 8)))], Colonna.values()[(int) ((event.getSceneX() - 47) / (504 / 8))]);
                } else {
                    pos1 = new Posizione(Riga.values()[(int) ((event.getSceneY() - 47) / (504 / 8))], Colonna.values()[7 - ((int) ((event.getSceneX() - 47) / (504 / 8)))]);
                }
                if (p.trovaPezzo(pos1) != null && p.trovaPezzo(pos1).getColore() != player || p.getTurnoCorrente() != player) {
                    stato.setText("Non è il tuo turno.");
                    graphics3.clearRect(0, 0, 600, 600);
                    pos1 = null;
                } else if (p.trovaPezzo(pos1) == null) {
                    pos1 = null;
                    graphics3.clearRect(0, 0, 600, 600);
                    stato.setText("Devi selezionare un pezzo al primo tocco.");
                } else {
                    mostraGuida(pos1);
                    stato.setText("Selezionato " + p.trovaPezzo(pos1).getSimbolo() + " in " + pos1.getColonna() + ", " + pos1.getRiga().toString());
                }
            } else {
                if (versoScacchiera == Colore.BIANCO) {
                    pos2 = new Posizione(Riga.values()[7 - ((int) ((event.getSceneY() - 49) / (504 / 8)))], Colonna.values()[(int) ((event.getSceneX() - 47) / (504 / 8))]);
                } else {
                    pos2 = new Posizione(Riga.values()[(int) ((event.getSceneY() - 47) / (504 / 8))], Colonna.values()[7 - ((int) ((event.getSceneX() - 47) / (504 / 8)))]);
                }
                if (p.trovaPezzo(pos2) != null && p.trovaPezzo(pos2).getColore() == player) {
                    mostraGuida(pos2);
                    pos1.clone(pos2);
                    stato.setText("Selezionato " + p.trovaPezzo(pos1).getSimbolo() + " in " + pos1.getColonna() + ", " + pos1.getRiga().toString());
                    pos2 = null;
                } else {
                    stato.setText("Selezionata cella " + pos2.getColonna() + ", " + pos2.getRiga().toString());
                    if (!p.muovi(pos1, pos2)) {
                        stato.setText("Mossa non valida");
                        graphics3.clearRect(0, 0, 600, 600);
                        pos1 = null;
                        pos2 = null;
                    } else {
                        Mossa m = new Mossa(pos1, pos2);
                        aggiornaScacchiera();
                        graphics3.clearRect(0, 0, 600, 600);
                        pos1 = null;
                        pos2 = null;

                        //controllo fine partita
                        if (p.isFinita()) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Partita conclusa!");
                            if (p.vincitore() != null) {
                                alert.setContentText("il " + p.vincitore() + " ha vinto per " + p.comeEFinita());
                            } else {
                                alert.setContentText("La partita è patta per " + p.comeEFinita());
                            }
                            alert.showAndWait();
                        }

                        //controllo promozione
                        Colore promozione = p.isPromozione();
                        if (promozione != null) {
                            Alert alert = new Alert(Alert.AlertType.NONE);
                            ButtonType REGINA = new ButtonType("Regina");
                            ButtonType ALFIERE = new ButtonType("Alfiere");
                            ButtonType TORRE = new ButtonType("Torre");
                            ButtonType CAVALLO = new ButtonType("Cavallo");
                            alert.getButtonTypes().addAll(REGINA, ALFIERE, CAVALLO, TORRE);
                            alert.setTitle("Promozione");
                            alert.setHeaderText("Promozione pedone!");
                            alert.setContentText("Seleziona un pezzo:");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent()) {
                                if (result.get() == REGINA) {
                                    p.promuovi(Simbolo.REGINA, promozione);
                                    m.setSimbolo(Simbolo.REGINA);
                                } else if (result.get() == CAVALLO) {
                                    p.promuovi(Simbolo.CAVALLO, promozione);
                                    m.setSimbolo(Simbolo.CAVALLO);
                                } else if (result.get() == ALFIERE) {
                                    p.promuovi(Simbolo.ALFIERE, promozione);
                                    m.setSimbolo(Simbolo.ALFIERE);
                                } else {
                                    p.promuovi(Simbolo.TORRE, promozione);
                                    m.setSimbolo(Simbolo.TORRE);
                                }
                                aggiornaScacchiera();
                            }
                        }
                        sendMossa(m);
                    }
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        graphics1 = canvas.getGraphicsContext2D();
        graphics2 = canvas2.getGraphicsContext2D();
        graphics3 = canvas3.getGraphicsContext2D();
        pos1 = null;
        pos2 = null;
        p = new Partita();
        player = Settings.colore;
        versoScacchiera = Settings.colore;
        aggiornaScacchiera();
        stato.setText("Inizio partita.");
        turno.setText(p.getTurnoCorrente().toString());
        Settings.partita = p;
        Settings.controller = this;

        if (player == Colore.NERO) {
            giraScacchiera();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Partita avviata!");
        if (player == Colore.BIANCO) {
            alert.setContentText("Giochi con i bianchi. Buona fortuna!");
        } else {
            alert.setContentText("Giochi con i neri. Buona fortuna!");
        }
        alert.show();
    }

    public void aggiornaScacchiera() {
        if (turno != null) {
            turno.setText(p.getTurnoCorrente().toString());
        }
        Image img = new Image("/scacchiera/viewController/res/imgs/figure2.png");
        double dx, dy, width = img.getWidth() / 6, height = img.getHeight() / 2, sx, sy, dw = 62, dh = 62;
        graphics1.clearRect(0, 0, 600, 600);
        ArrayList<Pezzo> pedine = new ArrayList<>();
        pedine.addAll(p.getPezziBianchi());
        pedine.addAll(p.getPezziNeri());
        for (Pezzo pezzo : pedine) {
            sx = pezzo.getSimbolo().ordinal() * width;
            sy = pezzo.getColore().ordinal() * height;
            if (versoScacchiera == Colore.BIANCO) {
                dy = (7 - pezzo.getPosizione().getRiga().ordinal()) * (504 / 8) + 49;
                dx = pezzo.getPosizione().getColonna().ordinal() * (504 / 8) + 46;
                graphics1.drawImage(img, sx, sy, width, height, dx, dy, dw, dh);
            } else {
                dy = pezzo.getPosizione().getRiga().ordinal() * (504 / 8) + 47;
                dx = (7 - pezzo.getPosizione().getColonna().ordinal()) * (504 / 8) + 47;
                graphics1.drawImage(img, sx, sy, width, height, dx, dy, dw, dh);
            }
        }
    }

    private void mostraGuida(Posizione pos) {
        double sx = 0, sy = 0, sw = 200, sh = 200, dx = 0, dy = 0, dw = 64, dh = 64;
        Image img = new Image("/scacchiera/viewController/res/imgs/puntatore.png");
        Image img2 = new Image("/scacchiera/viewController/res/imgs/puntatoreRosso.png");
        Image img3 = new Image("/scacchiera/viewController/res/imgs/puntatoreVerde.png");
        if (pos2 != null) {
            graphics3.clearRect(dx, dy, 600, 600);
        }
        if (versoScacchiera == Colore.BIANCO) {
            dy = (7 - pos.getRiga().ordinal()) * (504 / 8) + 49;
            dx = pos.getColonna().ordinal() * (504 / 8) + 46;
        } else {
            dy = pos.getRiga().ordinal() * (504 / 8) + 47;
            dx = (7 - pos.getColonna().ordinal()) * (504 / 8) + 47;
        }
        graphics3.drawImage(img3, sx, sy, sw, sh, dx, dy, dw, dh);
        ArrayList<Posizione> posizioni = p.mossePossibiliConSacco(p.trovaPezzo(pos));
        for (Posizione posizione : posizioni) {
            if (versoScacchiera == Colore.BIANCO) {
                dy = (7 - posizione.getRiga().ordinal()) * (504 / 8) + 49;
                dx = posizione.getColonna().ordinal() * (504 / 8) + 46;
            } else {
                dy = posizione.getRiga().ordinal() * (504 / 8) + 47;
                dx = (7 - posizione.getColonna().ordinal()) * (504 / 8) + 47;
            }
            if (p.isOccupato(posizione)) {
                graphics3.drawImage(img2, sx, sy, sw, sh, dx, dy, dw, dh);
            } else {
                graphics3.drawImage(img, sx, sy, sw, sh, dx, dy, dw, dh);
            }
        }
    }

    @FXML
    void richiediPatta(ActionEvent event) {
        try {
            Settings.playerWriter.write("richiesta patta\n");
            Settings.playerWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void resa(ActionEvent event) {
        try {
            Settings.playerWriter.write("resa\n");
            Settings.playerWriter.flush();
            pattaOResaAccettata();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void menuPrincipale(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Attenzione!");
        a.setHeaderText("Vuoi abbandonare?");
        a.setContentText("Se torni al menù principale abbandonerai la partita e ti disconnetterai dal server.");
        Optional<ButtonType> result = a.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            Scene scene = stage.getScene();
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/scacchiera/viewController/FXMLIndex.fxml"));
            Parent root;
            try {
                root = (Parent) fXMLLoader.load();
                anchorPane.getScene().getWindow().setHeight(400 + 39);
                anchorPane.getScene().getWindow().setWidth(600 + 16);
                scene.setRoot(root);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            try {
                Settings.trr.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @FXML
    void restart(ActionEvent event) {
        try {
            Settings.playerWriter.write("richiesta restart\n");
            Settings.playerWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void pattaOResaAccettata() {
        p.setTurnoCorrente(null);
    }

    public void restartAccettato() {
        p = new Partita();
        Settings.partita = p;
        player = Settings.colore;
        pos1 = null;
        pos2 = null;
        graphics3.clearRect(0, 0, 600, 600);
        aggiornaScacchiera();
        stato.setText("Inizio partita.");
        turno.setText(p.getTurnoCorrente().toString());
    }

    @FXML
    void ruotaScacchiera(ActionEvent event) {
        graphics3.clearRect(0, 0, 600, 600);
        scacchiera.setRotate((scacchiera.getRotate() + 180) % 360);
        if (versoScacchiera == Colore.BIANCO) {
            versoScacchiera = Colore.NERO;
        } else {
            versoScacchiera = Colore.BIANCO;
        }
        aggiornaScacchiera();
    }

    private void sendMossa(Mossa mossa) {
        try {
            Settings.playerWriter.write("mossa " + mossa.toString() + "\n");
            Settings.playerWriter.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void giraScacchiera() {
        graphics3.clearRect(0, 0, 600, 600);
        scacchiera.setRotate((scacchiera.getRotate() + 180) % 360);
        aggiornaScacchiera();
    }
}
