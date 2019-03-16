/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model.salvataggio;

import java.util.ArrayList;
import scacchiera.model.Colore;
import scacchiera.model.Mossa;
import scacchiera.model.Partita;
import scacchiera.model.Pezzo;
import scacchiera.model.Posizione;
import static scacchiera.model.Simbolo.*;

/**
 *
 * @author tonetto.davide
 */
public class Coding {

    public static String encoding(ArrayList<Mossa> mosse) {
        StringBuilder sb = new StringBuilder();
        Partita partita = new Partita();
        for (int i = 0; i < (mosse.size() + 1) / 2; i++) {
            sb.append((i + 1));
            sb.append(". ");
            Mossa mossa = mosse.get(i * 2);
            Pezzo p = partita.trovaPezzo(mossa.getPosIni());
            if (p.getSimbolo() == RE && (mossa.getPosIni().getColonna().ordinal() == mossa.getPosFin().getColonna().ordinal() + 2 || mossa.getPosIni().getColonna().ordinal() == mossa.getPosFin().getColonna().ordinal() - 2)) {
                sb.append("O-O");
                if (mossa.getPosIni().getColonna().ordinal() == mossa.getPosFin().getColonna().ordinal() + 2) {
                    sb.append("-O");
                }
            } else {
                if (p.getSimbolo() != PEDONE) {
                    sb.append(p.getSimbolo().getCodingSimbol());
                }
                if(isAmbiguo(partita, p, mossa.getPosFin())){
                    sb.append(ambiguo(partita, mossa));
                }
                if (partita.trovaPezzo(mossa.getPosFin()) != null) {
                    sb.append("x");
                }
                sb.append(mossa.getPosFin().toString());
            }
            sb.append(" ");
            partita.sposta(p, mossa.getPosFin());
            if (mosse.size() == (i * 2) + 1) {
                break;
            }
            mossa = mosse.get((i * 2) + 1);
            p = partita.trovaPezzo(mossa.getPosIni());
            if (p.getSimbolo() == RE && (mossa.getPosIni().getColonna().ordinal() == mossa.getPosFin().getColonna().ordinal() + 2 || mossa.getPosIni().getColonna().ordinal() == mossa.getPosFin().getColonna().ordinal() - 2)) {
                sb.append("O-O");
                if (mossa.getPosIni().getColonna().ordinal() == mossa.getPosFin().getColonna().ordinal() + 2) {
                    sb.append("-O");
                }
            } else {
                if (p.getSimbolo() != PEDONE) {
                    sb.append(p.getSimbolo().getCodingSimbol());
                }
                if(isAmbiguo(partita, p, mossa.getPosFin())){
                    sb.append(ambiguo(partita, mossa));
                }
                if (partita.trovaPezzo(mossa.getPosFin()) != null) {
                    sb.append("x");
                }
                sb.append(mossa.getPosFin().toString());
            }
            sb.append(" ");
            partita.sposta(p, mossa.getPosFin());
        }
        return sb.toString();
    }

    /**
     * gestisce le ambiguità
     *
     * true - ambiguo, false - non ambiguo
     *
     * @param partita
     * @param mossa
     * @return
     */
    public static String ambiguo(Partita partita, Mossa mossa) {
        ArrayList<Pezzo> sameSimbolo = sameSimbolo(partita.trovaPezzo(mossa.getPosFin()), partita, mossa.getPosFin());
        Pezzo p = partita.trovaPezzo(mossa.getPosIni());
        boolean riga = false, colonna = false;
        for (Pezzo pezzo : sameSimbolo) {
            if (p.equals(pezzo)) {
                continue;
            }
            if (pezzo.getPosizione().getRiga() == p.getPosizione().getRiga()) {
                riga = true;
            }
            if (pezzo.getPosizione().getColonna() == p.getPosizione().getColonna()) {
                colonna = true;
            }
        }
        return (colonna ? p.getPosizione().getColonna().toString() + (riga ? p.getPosizione().getRiga().toString() : "") : (riga ? p.getPosizione().getRiga().toString() : ""));
    }

    /**
     * Ritorna l'arraylist dei pezzi ambigui
     *
     * @param pezzo
     * @param partita
     * @param posF
     * @return
     */
    public static ArrayList<Pezzo> sameSimbolo(Pezzo pezzo, Partita partita, Posizione posF) {
        ArrayList<Pezzo> sameSimbolo = new ArrayList<>();
        if (pezzo.getColore() == Colore.BIANCO) {
            for (Pezzo p : partita.getPezziBianchi()) {
                if (p.getSimbolo() == pezzo.getSimbolo() && partita.mossePossibiliConSacco(p).contains(posF)) {
                    sameSimbolo.add(p);
                }
            }
        } else {
            for (Pezzo p : partita.getPezziNeri()) {
                if (p.getSimbolo() == pezzo.getSimbolo() && partita.mossePossibiliConSacco(p).contains(posF)) {
                    sameSimbolo.add(p);
                }
            }
        }
        return sameSimbolo;
    }

    public static boolean isAmbiguo(Partita partita, Pezzo pezzo, Posizione posF) {
        return sameSimbolo(pezzo, partita, posF).size() != 1;
    }
}
