/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model;

import java.util.ArrayList;

import static scacchiera.model.Colore.*;

import scacchiera.model.Posizione.*;

import static scacchiera.model.Posizione.Colonna.*;
import static scacchiera.model.Posizione.Riga.*;
import static scacchiera.model.Simbolo.*;

/**
 * @author davide
 */
public class Partita {

    //private Map<Posizione, Pezzo> pezziPos; //TODO possibile futura implementazione
    private Colore turnoCorrente;
    private final ArrayList<Pezzo> pezziBianchi;
    private final ArrayList<Pezzo> pezziNeri;
    private final ArrayList<Mossa> mossePartita;
    private Mossa ultimaMossa;
    private int regola50Mosse;
    private ArrayList<String> ripetizioneMosse;

    public Partita() {
        turnoCorrente = BIANCO;
        pezziBianchi = new ArrayList<>();
        pezziNeri = new ArrayList<>();
        mossePartita = new ArrayList<>();
        ripetizioneMosse = new ArrayList<>();
        posizioniIniziali();
        codificaScacchiera();
    }

    public Partita(ArrayList<Pezzo> bianchi, ArrayList<Pezzo> neri, Mossa ultimaMossa, ArrayList<Mossa> mossePartita) {
        this.pezziBianchi = copiaArray(bianchi);
        this.pezziNeri = copiaArray(neri);
        this.ultimaMossa = ultimaMossa;
        this.mossePartita = Pezzo.copiaArray(mossePartita);
    }

    public ArrayList<Pezzo> getPezziBianchi() {
        return pezziBianchi;
    }

    public ArrayList<Pezzo> getPezziNeri() {
        return pezziNeri;
    }

    public Colore getTurnoCorrente() {
        return turnoCorrente;
    }

    public void setTurnoCorrente(Colore turnoCorrente) {
        this.turnoCorrente = turnoCorrente;
    }

    public ArrayList<Mossa> getMossePartita() {
        return mossePartita;
    }

    public Mossa getUltimaMossa() {
        return ultimaMossa;
    }

    public void posizioniIniziali() {
        Pezzo pezz = null;
        Posizione pos = null;
        //pedoni
        for (Colonna c : Colonna.values()) {
            pezziBianchi.add(new Pezzo(BIANCO, PEDONE, new Posizione(R2, c)));
            pezziNeri.add(new Pezzo(NERO, PEDONE, new Posizione(R7, c)));
        }
        //torri
        pos = new Posizione(R1, A);
        pezz = new Pezzo(BIANCO, TORRE, pos);
        pezziBianchi.add(pezz);
        pos = new Posizione(R1, H);
        pezz = new Pezzo(BIANCO, TORRE, pos);
        pezziBianchi.add(pezz);
        pos = new Posizione(R8, A);
        pezz = new Pezzo(NERO, TORRE, pos);
        pezziNeri.add(pezz);
        pos = new Posizione(R8, H);
        pezz = new Pezzo(NERO, TORRE, pos);
        pezziNeri.add(pezz);
        //cavalli
        pos = new Posizione(R1, B);
        pezz = new Pezzo(BIANCO, CAVALLO, pos);
        pezziBianchi.add(pezz);
        pos = new Posizione(R1, G);
        pezz = new Pezzo(BIANCO, CAVALLO, pos);
        pezziBianchi.add(pezz);
        pos = new Posizione(R8, B);
        pezz = new Pezzo(NERO, CAVALLO, pos);
        pezziNeri.add(pezz);
        pos = new Posizione(R8, G);
        pezz = new Pezzo(NERO, CAVALLO, pos);
        pezziNeri.add(pezz);
        //alfieri
        pos = new Posizione(R1, C);
        pezz = new Pezzo(Colore.BIANCO, Simbolo.ALFIERE, pos);
        pezziBianchi.add(pezz);
        pos = new Posizione(R1, F);
        pezz = new Pezzo(BIANCO, ALFIERE, pos);
        pezziBianchi.add(pezz);
        pos = new Posizione(R8, C);
        pezz = new Pezzo(NERO, ALFIERE, pos);
        pezziNeri.add(pezz);
        pos = new Posizione(R8, F);
        pezz = new Pezzo(NERO, ALFIERE, pos);
        pezziNeri.add(pezz);
        //re
        pos = new Posizione(R1, E);
        pezz = new Pezzo(BIANCO, RE, pos);
        pezziBianchi.add(pezz);
        pos = new Posizione(R8, E);
        pezz = new Pezzo(NERO, RE, pos);
        pezziNeri.add(pezz);
        //regina
        pos = new Posizione(R1, D);
        pezz = new Pezzo(BIANCO, REGINA, pos);
        pezziBianchi.add(pezz);
        pos = new Posizione(R8, D);
        pezz = new Pezzo(NERO, REGINA, pos);
        pezziNeri.add(pezz);
    }

    public boolean controlloPosizione(Pezzo p, Posizione posizioneFin) {
        if (posizioneFin.equals(p.getPosizione())) {
            return false;
        }

        if (p.getColore() == BIANCO) {
            for (Pezzo pezzo : pezziBianchi) {
                if (pezzo.getPosizione().equals(posizioneFin)) {
                    return false;
                }
            }
        }

        if (p.getColore() == NERO) {
            for (Pezzo pezzo : pezziNeri) {
                if (pezzo.getPosizione().equals(posizioneFin)) {
                    return false;
                }
            }
        }

        switch (p.getSimbolo()) {
            case PEDONE:
                if (p.getPosizione().getColonna() != posizioneFin.getColonna()) {
                    if (ultimaMossa != null && trovaPezzo(ultimaMossa.getPosFin()) != null && enPassant(p, posizioneFin)) {
                        return true;
                    }

                    if ((p.getPosizione().getColonna().ordinal() + 1 == posizioneFin.getColonna().ordinal() || p.getPosizione().getColonna().ordinal() - 1 == posizioneFin.getColonna().ordinal()) && p.getPosizione().getRiga().ordinal() + p.getColore().getDirection() == posizioneFin.getRiga().ordinal()) {
                        return isOccupato(posizioneFin, p.getColore());
                    } else {
                        return false;
                    }
                } else {
                    if (isOccupato(posizioneFin)) {
                        return false;
                    }

                    if (p.getPosizione().getRiga().ordinal() + p.getColore().getDirection() == posizioneFin.getRiga().ordinal()) {
                        return true;
                    }

                    if (isOccupato(new Posizione(Riga.values()[p.getPosizione().getRiga().ordinal() + p.getColore().getDirection()], p.getPosizione().getColonna()))) {
                        return false;
                    }

                    if ((p.getColore() == BIANCO && p.getPosizione().getRiga() == R2 && posizioneFin.getRiga() == R4) ||
                            (p.getColore() == NERO && p.getPosizione().getRiga() == R7 && posizioneFin.getRiga() == R5)) {
                        return true;
                    }
                }
                break;
            case TORRE:
                return torre(p, posizioneFin);
            case ALFIERE:
                return alfiere(p, posizioneFin);
            case REGINA:
                return torre(p, posizioneFin) || alfiere(p, posizioneFin);
            case CAVALLO:
                if (posizioneFin.getColonna().ordinal() == p.getPosizione().getColonna().ordinal() || posizioneFin.getRiga().ordinal() == p.getPosizione().getRiga().ordinal()) {
                    return false;
                }

                if (Math.abs(posizioneFin.getColonna().ordinal() - p.getPosizione().getColonna().ordinal()) + Math.abs(posizioneFin.getRiga().ordinal() - p.getPosizione().getRiga().ordinal()) == 3) {
                    if (isOccupato(posizioneFin)) {
                        return isOccupato(posizioneFin, p.getColore());
                    }

                    return true;
                }

                return false;
            case RE:
                if (Math.abs(posizioneFin.getColonna().ordinal() - p.getPosizione().getColonna().ordinal()) + Math.abs(posizioneFin.getRiga().ordinal() - p.getPosizione().getRiga().ordinal()) == 1 || (Math.abs(posizioneFin.getColonna().ordinal() - p.getPosizione().getColonna().ordinal()) + Math.abs(posizioneFin.getRiga().ordinal() - p.getPosizione().getRiga().ordinal()) == 2 && !(posizioneFin.getColonna().ordinal() == p.getPosizione().getColonna().ordinal() || posizioneFin.getRiga().ordinal() == p.getPosizione().getRiga().ordinal()))) {
                    if (isOccupato(posizioneFin)) {
                        return isOccupato(posizioneFin, p.getColore());
                    }

                    return true;
                }

                return false;
        }

        return false;
    }

    public boolean enPassant(Pezzo p, Posizione posizioneFin) {
        if (ultimaMossa == null) {
            return false;
        }

        return ((((ultimaMossa.getPosIni().getRiga().ordinal() == ultimaMossa.getPosFin().getRiga().ordinal() + (2 * p.getColore().getDirection())) &&
                trovaPezzo(ultimaMossa.getPosFin()).getSimbolo() == PEDONE) &&
                (ultimaMossa.getPosFin().getColonna().ordinal() == p.getPosizione().getColonna().ordinal() + 1 ||
                        ultimaMossa.getPosFin().getColonna().ordinal() == p.getPosizione().getColonna().ordinal() - 1)) &&
                (posizioneFin.getColonna().ordinal() == ultimaMossa.getPosFin().getColonna().ordinal() &&
                        posizioneFin.getRiga().ordinal() == ultimaMossa.getPosFin().getRiga().ordinal() + p.getColore().getDirection())) &&
                p.getPosizione().getRiga().ordinal() == ultimaMossa.getPosFin().getRiga().ordinal();
    }

    public boolean arrocco(Pezzo p, Posizione posizioneFin) {
        if (p.getSimbolo() == RE && ((posizioneFin.equals(new Posizione(R1, G)) && p.getColore() == BIANCO) || (posizioneFin.equals(new Posizione(R8, G)) && p.getColore() == NERO))) {
            Partita partita = new Partita(pezziBianchi, pezziNeri, ultimaMossa, mossePartita);
            if ((trovaPezzo(new Posizione(p.getPosizione().getRiga(), H)) != null && trovaPezzo(new Posizione(p.getPosizione().getRiga(), H)).getSimbolo() == TORRE && trovaPezzo(new Posizione(p.getPosizione().getRiga(), H)).getMosse().isEmpty()) && p.getMosse().isEmpty() && !isScacco(partita, p.getColore(), new Posizione(p.getPosizione().getRiga(), F)) && !isOccupato(new Posizione(p.getPosizione().getRiga(), F)) && !isScacco(partita, p.getColore(), new Posizione(p.getPosizione().getRiga(), E)) && !isOccupato(posizioneFin)) {
                return true;
            }
        }
        return false;
    }

    public boolean arroccoLungo(Pezzo p, Posizione posizioneFin) {
        if (p.getSimbolo() == RE && ((posizioneFin.equals(new Posizione(R1, C)) && p.getColore() == BIANCO) || (posizioneFin.equals(new Posizione(R8, C)) && p.getColore() == NERO))) {
            Partita partita = new Partita(pezziBianchi, pezziNeri, ultimaMossa, mossePartita);
            if ((trovaPezzo(new Posizione(p.getPosizione().getRiga(), A)) != null && trovaPezzo(new Posizione(p.getPosizione().getRiga(), A)).getSimbolo() == TORRE && trovaPezzo(new Posizione(p.getPosizione().getRiga(), A)).getMosse().isEmpty()) && p.getMosse().isEmpty() && !isScacco(partita, p.getColore(), new Posizione(p.getPosizione().getRiga(), D)) && !isOccupato(new Posizione(p.getPosizione().getRiga(), D)) && !isScacco(partita, p.getColore(), new Posizione(p.getPosizione().getRiga(), E)) && !isOccupato(new Posizione(p.getPosizione().getRiga(), B)) && !isOccupato(posizioneFin) && !isScacco(partita, p.getColore(), new Posizione(p.getPosizione().getRiga(), C))) {
                return true;
            }
        }
        return false;
    }

    public Pezzo trovaPezzo(Posizione p) {
        for (Pezzo pezzo : pezziBianchi) {
            if (p.equals(pezzo.getPosizione())) {
                return pezzo;
            }
        }

        for (Pezzo pezzo : pezziNeri) {
            if (p.equals(pezzo.getPosizione())) {
                return pezzo;
            }
        }

        return null;
    }

    public boolean isOccupato(Posizione posizione, Colore colore) {
        if (colore == BIANCO) {
            for (Pezzo p : pezziNeri) {
                if (p.getPosizione().equals(posizione)) {
                    return true;
                }
            }
        } else {
            for (Pezzo p : pezziBianchi) {
                if (p.getPosizione().equals(posizione)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isOccupato(Posizione posizione) {
        for (Pezzo p : pezziBianchi) {
            if (p.getPosizione().equals(posizione)) {
                return true;
            }
        }

        for (Pezzo p : pezziNeri) {
            if (p.getPosizione().equals(posizione)) {
                return true;
            }
        }

        return false;
    }

    public boolean muovi(Posizione pos1, Posizione pos2) {
        if (trovaPezzo(pos2) != null || trovaPezzo(pos1).getSimbolo() == PEDONE) {
            regola50Mosse = 0;
        } else {
            regola50Mosse++;
        }

        Pezzo pez = trovaPezzo(pos1);
        if (pez.getColore() != turnoCorrente) {
            return false;
        }

        for (Posizione p : mossePossibiliConSacco(pez)) {
            if (p.equals(pos2)) {
                mossePartita.add(new Mossa(pos1, pos2));
                sposta(pez, pos2);
                codificaScacchiera();
                ultimaMossa = new Mossa(pos1, pos2);
                trovaPezzo(pos2).getMosse().add(new Mossa(pos1, pos2));

                if (turnoCorrente == BIANCO) {
                    turnoCorrente = NERO;
                } else {
                    turnoCorrente = BIANCO;
                }

                return true;
            }
        }

        return false;
    }

    private boolean torre(Pezzo p, Posizione posizioneFin) {
        if (p.getPosizione().getRiga() != posizioneFin.getRiga() && p.getPosizione().getColonna() != posizioneFin.getColonna()) {
            return false;
        } else if (p.getPosizione().getRiga().ordinal() == posizioneFin.getRiga().ordinal()) {
            if (p.getPosizione().getColonna().ordinal() < posizioneFin.getColonna().ordinal()) {
                for (int i = p.getPosizione().getColonna().ordinal() + 1; i < posizioneFin.getColonna().ordinal(); i++) {
                    if (isOccupato(new Posizione(p.getPosizione().getRiga(), Colonna.values()[i]))) {
                        return false;
                    }
                }

            } else {
                for (int i = p.getPosizione().getColonna().ordinal() - 1; i > posizioneFin.getColonna().ordinal(); i--) {
                    if (isOccupato(new Posizione(p.getPosizione().getRiga(), Colonna.values()[i]))) {
                        return false;
                    }
                }

            }

            if (isOccupato(posizioneFin, p.getColore())) {
                return true;
            }

            return !isOccupato(posizioneFin);
        } else {
            if (p.getPosizione().getRiga().ordinal() < posizioneFin.getRiga().ordinal()) {
                for (int i = p.getPosizione().getRiga().ordinal() + 1; i < posizioneFin.getRiga().ordinal(); i++) {
                    if (isOccupato(new Posizione(Riga.values()[i], p.getPosizione().getColonna()))) {
                        return false;
                    }
                }

            } else {
                for (int i = p.getPosizione().getRiga().ordinal() - 1; i > posizioneFin.getRiga().ordinal(); i--) {
                    if (isOccupato(new Posizione(Riga.values()[i], p.getPosizione().getColonna()))) {
                        return false;
                    }
                }

            }

            if (isOccupato(posizioneFin, p.getColore())) {
                return true;
            }

            return !isOccupato(posizioneFin);
        }
    }

    private boolean alfiere(Pezzo p, Posizione posizioneFin) {
        if (Math.abs(p.getPosizione().getRiga().ordinal() - posizioneFin.getRiga().ordinal()) != Math.abs(p.getPosizione().getColonna().ordinal() - posizioneFin.getColonna().ordinal())) {
            return false;
        }

        if (posizioneFin.getRiga().ordinal() > p.getPosizione().getRiga().ordinal() && posizioneFin.getColonna().ordinal() > p.getPosizione().getColonna().ordinal()) {
            int k = p.getPosizione().getRiga().ordinal() + 1;
            for (int j = p.getPosizione().getColonna().ordinal() + 1; j < posizioneFin.getColonna().ordinal(); j++) {
                if (isOccupato(new Posizione(Riga.values()[k], Colonna.values()[j]))) {
                    return false;
                }

                k++;
            }

            if (isOccupato(posizioneFin, p.getColore())) {
                return true;
            }

            return true;
        } else if (posizioneFin.getRiga().ordinal() > p.getPosizione().getRiga().ordinal() && posizioneFin.getColonna().ordinal() < p.getPosizione().getColonna().ordinal()) {
            int k = p.getPosizione().getRiga().ordinal() + 1;
            for (int j = p.getPosizione().getColonna().ordinal() - 1; j > posizioneFin.getColonna().ordinal(); j--) {
                if (isOccupato(new Posizione(Riga.values()[k], Colonna.values()[j]))) {
                    return false;
                }

                k++;
            }

            if (isOccupato(posizioneFin, p.getColore())) {
                return true;
            }

            return true;
        } else if (posizioneFin.getRiga().ordinal() < p.getPosizione().getRiga().ordinal() && posizioneFin.getColonna().ordinal() > p.getPosizione().getColonna().ordinal()) {
            int k = p.getPosizione().getRiga().ordinal() - 1;
            for (int j = p.getPosizione().getColonna().ordinal() + 1; j < posizioneFin.getColonna().ordinal(); j++) {
                if (isOccupato(new Posizione(Riga.values()[k], Colonna.values()[j]))) {
                    return false;
                }

                k--;
            }

            if (isOccupato(posizioneFin, p.getColore())) {
                return true;
            }

            return true;
        } else {
            int k = p.getPosizione().getRiga().ordinal() - 1;
            for (int j = p.getPosizione().getColonna().ordinal() - 1; j > posizioneFin.getColonna().ordinal(); j--) {
                if (isOccupato(new Posizione(Riga.values()[k], Colonna.values()[j]))) {
                    return false;
                }

                k--;
            }

            if (isOccupato(posizioneFin, p.getColore())) {
                return true;
            }

            return true;
        }
    }

    public ArrayList<Posizione> mossePossibili(Pezzo p) {
        ArrayList<Posizione> a = new ArrayList<>();
        Posizione pos;

        for (int i = 0; i < Riga.values().length; i++) {
            for (int j = 0; j < Colonna.values().length; j++) {
                pos = new Posizione(Riga.values()[i], Colonna.values()[j]);
                if (controlloPosizione(p, pos)) {
                    a.add(pos);
                } else if (arrocco(p, pos)) {
                    a.add(pos);
                } else if (arroccoLungo(p, pos)) {
                    a.add(pos);
                }
            }
        }

        return a;
    }

    public ArrayList<Posizione> mossePossibiliConSacco(Pezzo p) {
        ArrayList<Posizione> a = new ArrayList<>();
        Partita partita = new Partita(pezziBianchi, pezziNeri, ultimaMossa, mossePartita);
        ArrayList<Posizione> mosse = partita.mossePossibili(p);

        for (Posizione posizione : mosse) {
            partita.sposta(partita.trovaPezzo(p.getPosizione()), posizione);
            if (pedineScacco(partita, p.getColore()).isEmpty()) {
                a.add(posizione);
            }

            partita = new Partita(pezziBianchi, pezziNeri, ultimaMossa, mossePartita);
        }

        return a;
    }

    public Posizione trovaRe(Colore c) {
        if (c == BIANCO) {
            for (Pezzo p : pezziBianchi) {
                if (p.getSimbolo() == RE) {
                    return p.getPosizione();
                }
            }
        } else {
            for (Pezzo p : pezziNeri) {
                if (p.getSimbolo() == RE) {
                    return p.getPosizione();
                }
            }
        }

        return null;
    }

    public Posizione trovaRe(ArrayList<Pezzo> array) {
        for (Pezzo p : array) {
            if (p.getSimbolo() == RE) {
                return p.getPosizione();
            }
        }

        return null;
    }

    public void rimuoviPezzo(Posizione p, Colore c) {
        if (c == BIANCO) {
            for (Pezzo pezzo : pezziBianchi) {
                if (pezzo.getPosizione().equals(p)) {
                    pezziBianchi.remove(pezzo);
                    break;
                }
            }
        } else {
            for (Pezzo pezzo : pezziNeri) {
                if (pezzo.getPosizione().equals(p)) {
                    pezziNeri.remove(pezzo);
                    break;
                }
            }
        }
    }

    public void sposta(Pezzo p, Posizione pos) {
        ArrayList<Pezzo> cPezzi1, cPezzi2;

        if (p.getColore() == BIANCO) {
            cPezzi1 = pezziBianchi;
            cPezzi2 = pezziNeri;
        } else {
            cPezzi1 = pezziNeri;
            cPezzi2 = pezziBianchi;
        }

        if (enPassant(p, pos)) {
            for (Pezzo pezzo : cPezzi2) {
                if (pezzo.getPosizione().equals(new Posizione(Riga.values()[pos.getRiga().ordinal() - p.getColore().getDirection()], pos.getColonna()))) {
                    cPezzi2.remove(pezzo);
                    break;
                }
            }
        }

        if (mossePartita.size() > 1 && mossePartita.get(mossePartita.size() - 1) != null) {
            if ((mossePartita.get(mossePartita.size() - 1).equals(new Mossa(new Posizione(R1, E), new Posizione(R1, G))) && p.getSimbolo() == RE) || (mossePartita.get(mossePartita.size() - 1).equals(new Mossa(new Posizione(R8, E), new Posizione(R8, G))) && p.getSimbolo() == RE)) {
                trovaPezzo(new Posizione(pos.getRiga(), H)).setPosizione(new Posizione(pos.getRiga(), F));
            }
            if ((mossePartita.get(mossePartita.size() - 1).equals(new Mossa(new Posizione(R1, E), new Posizione(R1, C))) && p.getSimbolo() == RE) || (mossePartita.get(mossePartita.size() - 1).equals(new Mossa(new Posizione(R8, E), new Posizione(R8, C))) && p.getSimbolo() == RE)) {
                trovaPezzo(new Posizione(pos.getRiga(), A)).setPosizione(new Posizione(pos.getRiga(), D));
            }
        }

        if (trovaPezzo(pos) != null) {
            for (Pezzo pezzo : cPezzi2) {
                if (pezzo.getPosizione().equals(pos)) {
                    cPezzi2.remove(pezzo);
                    break;
                }
            }
        }

        for (Pezzo pezzo : cPezzi1) {
            if (pezzo.getPosizione().equals(p.getPosizione())) {
                pezzo.setPosizione(pos);
                break;
            }
        }
    }

    public boolean isScacco(Colore c) {
        return !pedineScacco(this, c).isEmpty();
    }

    public boolean isScaccoMatto(Colore c) {
        Partita partita = new Partita(pezziBianchi, pezziNeri, ultimaMossa, mossePartita);
        if (pedineScacco(partita, c).isEmpty()) {
            return false;
        }

        for (Pezzo pezzo : (c == BIANCO ? partita.pezziBianchi : partita.pezziNeri)) {
            ArrayList<Posizione> mosse = partita.mossePossibili(pezzo);
            for (Posizione posizione : mosse) {
                partita.sposta(pezzo, posizione);

                if (pedineScacco(partita, c).isEmpty()) {
                    return false;
                }

                partita = new Partita(pezziBianchi, pezziNeri, ultimaMossa, mossePartita);
            }

            partita = new Partita(pezziBianchi, pezziNeri, ultimaMossa, mossePartita);
        }

        return true;
    }

    public static ArrayList<Pezzo> copiaArray(ArrayList<Pezzo> array) { //TODO spostare su utility
        ArrayList<Pezzo> fine = new ArrayList<>();

        for (Pezzo obj : array) {
            fine.add(new Pezzo(obj));
        }

        return fine;
    }

    public static ArrayList<Pezzo> pedineScacco(Partita partita, Colore colore) {
        ArrayList<Pezzo> nemici;
        Posizione posRe = partita.trovaRe(colore);

        if (colore == BIANCO) {
            nemici = partita.getPezziNeri();
        } else {
            nemici = partita.getPezziBianchi();
        }

        ArrayList<Pezzo> fine = new ArrayList<>();
        for (Pezzo p : nemici) {
            if (partita.controlloPosizione(p, posRe)) {
                fine.add(p);
            }
        }

        return fine;
    }

    public void promuovi(Simbolo promozione, Colore c) {
        if (c == BIANCO) {
            for (Pezzo p : pezziBianchi) {
                if (p.getPosizione().getRiga() == R8 && p.getSimbolo() == PEDONE) {
                    p.setSimbolo(promozione);
                    mossePartita.get(mossePartita.size() - 1).setSimbolo(promozione);
                    break;
                }
            }
        } else {
            for (Pezzo p : pezziNeri) {
                if (p.getPosizione().getRiga() == R1 && p.getSimbolo() == PEDONE) {
                    p.setSimbolo(promozione);
                    mossePartita.get(mossePartita.size() - 1).setSimbolo(promozione);
                    break;
                }
            }
        }
    }

    public boolean isPromozione(Colore c) {
        if (c == BIANCO) {
            for (Pezzo p : pezziBianchi) {
                if (p.getPosizione().getRiga() == R8 && p.getSimbolo() == PEDONE) {
                    return true;
                }
            }
        } else {
            for (Pezzo p : pezziNeri) {
                if (p.getPosizione().getRiga() == R1 && p.getSimbolo() == PEDONE) {
                    return true;
                }
            }
        }

        return false;
    }

    public Colore isPromozione() {
        for (Pezzo p : pezziBianchi) {
            if (p.getPosizione().getRiga() == R8 && p.getSimbolo() == PEDONE) {
                return BIANCO;
            }
        }

        for (Pezzo p : pezziNeri) {
            if (p.getPosizione().getRiga() == R1 && p.getSimbolo() == PEDONE) {
                return NERO;
            }
        }

        return null;
    }

    public boolean isScacco(Partita p, Colore c, Posizione pos) {
        if (c == BIANCO) {
            p.sposta(p.trovaPezzo(p.trovaRe(p.getPezziBianchi())), pos);
        } else {
            p.sposta(p.trovaPezzo(p.trovaRe(p.getPezziNeri())), pos);
        }

        return !pedineScacco(p, c).isEmpty();
    }

    public boolean isStallo(Colore c) {
        if (isScacco(c)) {
            return false;
        }

        if (c == BIANCO) {
            for (Pezzo p : pezziBianchi) {
                if (!mossePossibiliConSacco(p).isEmpty()) {
                    return false;
                }
            }
        } else {
            for (Pezzo p : pezziNeri) {
                if (!mossePossibiliConSacco(p).isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * indica se la partita è finita
     *
     * @return
     */
    public boolean isFinita() {
        if (isStallo(NERO) || isStallo(BIANCO)) {
            return true;
        }

        if (isScaccoMatto(NERO) || isScaccoMatto(BIANCO)) {
            return true;
        }

        if (regola50Mosse == 100) {
            return true;
        }

        if (isRipetizioneScacchiera()) {
            return true;
        }

        return false;
    }

    /**
     * indica come è finita la partita null se non è finita
     *
     * @return
     */
    public String comeEFinita() {
        if (isStallo(NERO) || isStallo(BIANCO)) {
            return "stallo";
        }

        if (isScaccoMatto(NERO) || isScaccoMatto(BIANCO)) {
            return "scacco matto";
        }

        if (regola50Mosse == 100) {
            return "regola delle 50 mosse";
        }

        if (isRipetizioneScacchiera()) {
            return "ripetizione di mosse";
        }

        return null;
    }

    public Colore vincitore() {
        if (isStallo(BIANCO) || isStallo(NERO)) {
            return null;
        }

        if (isScaccoMatto(BIANCO)) {
            return NERO;
        }

        if (isScaccoMatto(NERO)) {
            return BIANCO;
        }

        if (regola50Mosse == 100) {
            return null;
        }

        if (isRipetizioneScacchiera()) {
            return null;
        }

        return null;
    }

    private void codificaScacchiera() {
        StringBuilder sb = new StringBuilder();
        for (Riga r : Riga.values()) {
            for (Colonna c : Colonna.values()) {
                Pezzo p = trovaPezzo(new Posizione(r, c));

                if (p == null) {
                    sb.append('x');
                } else {
                    sb.append(p.getColore() == BIANCO ? 'b' : 'n');
                    sb.append(p.getSimbolo().getCodingSimbol());
                }
            }
        }

        ripetizioneMosse.add(sb.toString());
    }

    private boolean isRipetizioneScacchiera() {
        String ultimaConfigurazione = ripetizioneMosse.get(ripetizioneMosse.size() - 1);

        int cont = 0;
        for (String s : ripetizioneMosse) {
            if (s.equals(ultimaConfigurazione)) {
                cont++;
            }
        }

        return cont == 3;
    }
}
