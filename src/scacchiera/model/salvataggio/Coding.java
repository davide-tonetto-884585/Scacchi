/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model.salvataggio;

import java.util.ArrayList;
import scacchiera.model.Colore;
import static scacchiera.model.Colore.*;
import scacchiera.model.Mossa;
import scacchiera.model.Partita;
import scacchiera.model.Pezzo;
import scacchiera.model.Posizione;
import scacchiera.model.Posizione.*;
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
                sb.append("0-0");
                if (mossa.getPosIni().getColonna().ordinal() == mossa.getPosFin().getColonna().ordinal() + 2) {
                    sb.append("-0");
                }
            } else {
                if (p.getSimbolo() != PEDONE) {
                    sb.append(p.getSimbolo().getCodingSimbol());
                }
                if (isAmbiguo(partita, p, mossa.getPosFin())) {
                    sb.append(ambiguo(partita, mossa));
                }
                if (partita.trovaPezzo(mossa.getPosFin()) != null) {
                    sb.append("x");
                }
                sb.append(mossa.getPosFin().toString());
                if (mossa.getSimbolo() != null) {
                    sb.append("=");
                    sb.append(mossa.getSimbolo().getCodingSimbol());
                    p.setSimbolo(mossa.getSimbolo());
                }
            }
            partita.sposta(p, mossa.getPosFin());
            if (partita.isScaccoMatto(Colore.NERO)) {
                sb.append("#");
            } else if (partita.isScacco(Colore.NERO)) {
                sb.append("+");
            }
            sb.append(" ");
            if (mosse.size() == (i * 2) + 1) {
                break;
            }
            mossa = mosse.get((i * 2) + 1);
            p = partita.trovaPezzo(mossa.getPosIni());
            if (p.getSimbolo() == RE && (mossa.getPosIni().getColonna().ordinal() == mossa.getPosFin().getColonna().ordinal() + 2 || mossa.getPosIni().getColonna().ordinal() == mossa.getPosFin().getColonna().ordinal() - 2)) {
                sb.append("0-0");
                if (mossa.getPosIni().getColonna().ordinal() == mossa.getPosFin().getColonna().ordinal() + 2) {
                    sb.append("-0");
                }
            } else {
                if (p.getSimbolo() != PEDONE) {
                    sb.append(p.getSimbolo().getCodingSimbol());
                }
                if (isAmbiguo(partita, p, mossa.getPosFin())) {
                    sb.append(ambiguo(partita, mossa));
                }
                if (partita.trovaPezzo(mossa.getPosFin()) != null) {
                    sb.append("x");
                }
                sb.append(mossa.getPosFin().toString());
                if (mossa.getSimbolo() != null) {
                    sb.append("=");
                    sb.append(mossa.getSimbolo().getCodingSimbol());
                    p.setSimbolo(mossa.getSimbolo());
                }
            }
            partita.sposta(p, mossa.getPosFin());
            if (partita.isScaccoMatto(Colore.BIANCO)) {
                sb.append("#");
            } else if (partita.isScacco(Colore.BIANCO)) {
                sb.append("+");
            }
            sb.append(" ");
        }
        if (partita.isScaccoMatto(Colore.NERO)) {
            sb.append("1-0");
        } else if (partita.isScaccoMatto(Colore.BIANCO)) {
            sb.append("0-1");
        } else {
            sb.append("1/2-1/2");
        }
        return sb.toString();
    }

    public ArrayList<Mossa> decoding(String code) {
        ArrayList<Mossa> mosse = new ArrayList<>();
        String[] array = code.split(" ");
        int giri = 0;

        for (String s : array) {
            if (!s.contains(".")) {

                //pezzo iniziale
            }
            giri++;
        }

        return mosse;
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
        ArrayList<Pezzo> sameSimbolo = sameSimbolo(partita.trovaPezzo(mossa.getPosIni()), partita, mossa.getPosFin());
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
        return (colonna ? p.getPosizione().getRiga().toString() + (riga ? p.getPosizione().getColonna().toString() : "") : (riga ? p.getPosizione().getColonna().toString() : ""));
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

    public static Mossa stringToMossa(String s, int giri) {
        Pezzo p;
        if (giri % 3 == 1) {
            p = new Pezzo(PEDONE, BIANCO);
        } else {
            p = new Pezzo(PEDONE, NERO);
        }

        switch (s.charAt(0)) {
            case 'N':
                p.setSimbolo(CAVALLO);
                break;
            case 'K':
                p.setSimbolo(RE);
                break;
            case 'Q':
                p.setSimbolo(REGINA);
                break;
            case 'B':
                p.setSimbolo(ALFIERE);
                break;
            case 'T':
                p.setSimbolo(TORRE);
                break;
        }

        //controllo lettera per lettera
        Colonna col = null;
        Riga rig = null;
        int stato = 0;
        for (char c : s.toCharArray()) {
            switch (stato) {
                case 0:
                    switch (c) {
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                        case 'g':
                        case 'h':
                            stato = 1;
                            break;
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                            stato = 2;
                    }
                    break;
                case 1: // gestione colonna
                    switch(c){
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                        case 'g':
                        case 'h':
                        case 'x':
                            
                    }
            }
        }
        return null;
    }
}
