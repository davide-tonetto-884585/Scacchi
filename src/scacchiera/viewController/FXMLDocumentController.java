/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.viewController;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import scacchiera.model.*;
import scacchiera.model.Posizione.*;

/**
 *
 * @author tonetto.davide
 */
public class FXMLDocumentController implements Initializable {

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

    @FXML
    private TextArea azioni;

    private Partita p;
    private Posizione pos1, pos2;
    private Colore versoScacchiera;
    private static final double DIM_SC = 1080;

    @FXML
    void guida(MouseEvent event) {
//        turno.setText(p.getTurnoCorrente().toString());
//        double sx = 0, sy = 0, sw = 200, sh = 200, dx = 0, dy = 0, dw = 64, dh = 64;
//        GraphicsContext graphics = canvas2.getGraphicsContext2D();
//        Image img = new Image("/scacchiera/viewController/res/imgs/puntatore.png");
//        Image img2 = new Image("/scacchiera/viewController/res/imgs/puntatoreRosso.png");
//        graphics.clearRect(0, 0, 600, 600);
//        if (event.getSceneX() - 46 < 0 || event.getSceneY() - 46 < 0 || event.getSceneX() - 46 > 504 || event.getSceneY() - 46 > 504) {
//            cella.setText("Cursore fuori dalla scacchiera.");
//        } else {
//            Posizione pos = null;
//            if (versoScacchiera == Colore.BIANCO) {
//                pos = new Posizione(Riga.values()[7 - ((int) ((event.getSceneY() - 49) / (504 / 8)))], Colonna.values()[(int) ((event.getSceneX() - 47) / (504 / 8))]);
//            } else {
//                pos = new Posizione(Riga.values()[(int) ((event.getSceneY() - 47) / (504 / 8))], Colonna.values()[7 - ((int) ((event.getSceneX() - 47) / (504 / 8)))]);
//            }
//            cella.setText(pos.getColonna().toString() + ", " + pos.getRiga().toString());
//            if (p.trovaPezzo(pos) != null && p.trovaPezzo(pos).getColore() == p.getTurnoCorrente()) {
//                if (p.trovaPezzo(pos) != null) {
//                    ArrayList<Posizione> posizioni = p.mossePossibiliConSacco(p.trovaPezzo(pos));
//                    for (Posizione posizione : posizioni) {
//                        if (versoScacchiera == Colore.BIANCO) {
//                            dy = (7 - posizione.getRiga().ordinal()) * (504 / 8) + 49;
//                            dx = posizione.getColonna().ordinal() * (504 / 8) + 46;
//                        } else {
//                            dy = posizione.getRiga().ordinal() * (504 / 8) + 47;
//                            dx = (7 - posizione.getColonna().ordinal()) * (504 / 8) + 47;
//                        }
//                        if (p.isOccupato(posizione)) {
//                            graphics.drawImage(img2, sx, sy, sw, sh, dx, dy, dw, dh);
//                        } else {
//                            graphics.drawImage(img, sx, sy, sw, sh, dx, dy, dw, dh);
//                        }
//                    }
//                }
//            }
//        }
    }

    @FXML
    void handleButtonAction(MouseEvent event) {
        GraphicsContext graphics2 = canvas3.getGraphicsContext2D();
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
                if (p.trovaPezzo(pos1) != null && p.trovaPezzo(pos1).getColore() != p.getTurnoCorrente()) {
                    stato.setText("Non è il tuo turno.");
                    graphics2.clearRect(0, 0, 600, 600);
                    pos1 = null;
                } else if (p.trovaPezzo(pos1) == null) {
                    pos1 = null;
                    graphics2.clearRect(0, 0, 600, 600);
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
                if (p.trovaPezzo(pos2) != null && p.trovaPezzo(pos2).getColore() == p.getTurnoCorrente()) {
                    mostraGuida(pos2);
                    pos1.clone(pos2);
                    stato.setText("Selezionato " + p.trovaPezzo(pos1).getSimbolo() + " in " + pos1.getColonna() + ", " + pos1.getRiga().toString());
                    pos2 = null;
                } else {
                    stato.setText("Selezionata cella " + pos2.getColonna() + ", " + pos2.getRiga().toString());
                    if (!p.muovi(pos1, pos2)) {
                        stato.setText("Mossa non valida");
                        graphics2.clearRect(0, 0, 600, 600);
                        pos1 = null;
                        pos2 = null;
                    } else {
                        aggiornaScacchiera();
                        graphics2.clearRect(0, 0, 600, 600);
                        pos1 = null;
                        pos2 = null;
//                        System.out.println("Scacco bianchi " + p.isScacco(Colore.BIANCO));
//                        System.out.println("Scacco neri " + p.isScacco(Colore.NERO));
                        if (p.isScaccoMatto(Colore.BIANCO)) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Scacco Matto!");
                            alert.setContentText("il nero ha vinto!");
                            alert.showAndWait();
                        } else if (p.isScaccoMatto(Colore.NERO)) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Scacco Matto!");
                            alert.setContentText("il bianco ha vinto!");
                            alert.showAndWait();
                        }
                        Alert alert = new Alert(Alert.AlertType.NONE);
                        ButtonType REGINA = new ButtonType("Regina");
                        ButtonType ALFIERE = new ButtonType("Alfiere");
                        ButtonType TORRE = new ButtonType("Torre");
                        ButtonType CAVALLO = new ButtonType("Cavallo");
                        alert.getButtonTypes().addAll(REGINA, ALFIERE, CAVALLO, TORRE);
                        alert.setTitle("Promozione");
                        alert.setHeaderText("Promozione pedone!");
                        alert.setContentText("Seleziona un pezzo:");
                        if (p.isPromozione(Colore.BIANCO)) {
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent()) {
                                if (result.get() == REGINA) {
                                    p.promuovi(Simbolo.REGINA, Colore.BIANCO);
                                } else if (result.get() == CAVALLO) {
                                    p.promuovi(Simbolo.CAVALLO, Colore.BIANCO);
                                } else if (result.get() == ALFIERE) {
                                    p.promuovi(Simbolo.ALFIERE, Colore.BIANCO);
                                } else {
                                    p.promuovi(Simbolo.TORRE, Colore.BIANCO);
                                }
                                aggiornaScacchiera();
                            }
                        } else if (p.isPromozione(Colore.NERO)) {
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent()) {
                                if (result.get() == REGINA) {
                                    p.promuovi(Simbolo.REGINA, Colore.BIANCO);
                                } else if (result.get() == CAVALLO) {
                                    p.promuovi(Simbolo.CAVALLO, Colore.BIANCO);
                                } else if (result.get() == ALFIERE) {
                                    p.promuovi(Simbolo.ALFIERE, Colore.BIANCO);
                                } else {
                                    p.promuovi(Simbolo.TORRE, Colore.BIANCO);
                                }
                                aggiornaScacchiera();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pos1 = null;
        pos2 = null;
        p = new Partita();
        versoScacchiera = Colore.BIANCO;
        aggiornaScacchiera();
        stato.setText("Inizio partita.");
        turno.setText(p.getTurnoCorrente().toString());
    }

    public void aggiornaScacchiera() {
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        Image img = new Image("/scacchiera/viewController/res/imgs/figure2.png");
        double dx, dy, width = img.getWidth() / 6, height = img.getHeight() / 2, sx, sy, dw = 62, dh = 62;
        graphics.clearRect(0, 0, 600, 600);
        if (pos1 != null && pos2 != null) {
            azioni.appendText(p.getTurnoCorrente().toString() + ": spostato " + p.trovaPezzo(pos2).getSimbolo().toString() + " da " + pos1.getColonna().toString() + ", " + pos1.getRiga().toString() + " a " + pos2.getColonna().toString() + ", " + pos2.getRiga().toString() + ";\n");
//            p.muovi(pos1, pos2);
        }
        ArrayList<Pezzo> pedine = new ArrayList<>();
        pedine.addAll(p.getPezziBianchi());
        pedine.addAll(p.getPezziNeri());
        for (Pezzo pezzo : pedine) {
            sx = pezzo.getSimbolo().ordinal() * width;
            sy = pezzo.getColore().ordinal() * height;
            if (versoScacchiera == Colore.BIANCO) {
                dy = (7 - pezzo.getPosizione().getRiga().ordinal()) * (504 / 8) + 49;
                dx = pezzo.getPosizione().getColonna().ordinal() * (504 / 8) + 46;
                graphics.drawImage(img, sx, sy, width, height, dx, dy, dw, dh);
            } else {
                dy = pezzo.getPosizione().getRiga().ordinal() * (504 / 8) + 47;
                dx = (7 - pezzo.getPosizione().getColonna().ordinal()) * (504 / 8) + 47;
                graphics.drawImage(img, sx, sy, width, height, dx, dy, dw, dh);
            }
        }
    }

    private void mostraGuida(Posizione pos) {
        double sx = 0, sy = 0, sw = 200, sh = 200, dx = 0, dy = 0, dw = 64, dh = 64;
        GraphicsContext graphics = canvas3.getGraphicsContext2D();
        Image img = new Image("/scacchiera/viewController/res/imgs/puntatore.png");
        Image img2 = new Image("/scacchiera/viewController/res/imgs/puntatoreRosso.png");
        Image img3 = new Image("/scacchiera/viewController/res/imgs/puntatoreVerde.png");
        if (pos2 != null) {
            graphics.clearRect(dx, dy, 600, 600);
        }
        if (versoScacchiera == Colore.BIANCO) {
            dy = (7 - pos.getRiga().ordinal()) * (504 / 8) + 49;
            dx = pos.getColonna().ordinal() * (504 / 8) + 46;
        } else {
            dy = pos.getRiga().ordinal() * (504 / 8) + 47;
            dx = (7 - pos.getColonna().ordinal()) * (504 / 8) + 47;
        }
        graphics.drawImage(img3, sx, sy, sw, sh, dx, dy, dw, dh);
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
                graphics.drawImage(img2, sx, sy, sw, sh, dx, dy, dw, dh);
            } else {
                graphics.drawImage(img, sx, sy, sw, sh, dx, dy, dw, dh);
            }
        }
    }

    @FXML
    void restart(ActionEvent event) {
        p = new Partita();
        pos1 = null;
        pos2 = null;
        GraphicsContext graphics = canvas3.getGraphicsContext2D();
        graphics.clearRect(0, 0, 600, 600);
        aggiornaScacchiera();
        azioni.clear();
        stato.setText("Inizio partita.");
        turno.setText(p.getTurnoCorrente().toString());
    }

    @FXML
    void ruotaScacchiera(ActionEvent event) {
        GraphicsContext graphics = canvas3.getGraphicsContext2D();
        graphics.clearRect(0, 0, 600, 600);
        scacchiera.setRotate((scacchiera.getRotate() + 180) % 360);
        if (versoScacchiera == Colore.BIANCO) {
            versoScacchiera = Colore.NERO;
        } else {
            versoScacchiera = Colore.BIANCO;
        }
        aggiornaScacchiera();
    }
}
