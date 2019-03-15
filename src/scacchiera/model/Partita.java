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
 *
 * @author davide
 */
public class Partita {

//    private Map<Posizione, Pezzo> pezziPos;
    private Colore turnoCorrente;
    private ArrayList<Pezzo> pezziBianchi;
    private ArrayList<Pezzo> pezziNeri;
    private ArrayList<Mossa> mossePartita;
    private Mossa ultimaMossa;

    public Partita() {
        turnoCorrente = Colore.BIANCO;
        pezziBianchi = new ArrayList<>();
        pezziNeri = new ArrayList<>();
//        pezziPos = new HashMap<>();
        mossePartita = new ArrayList<>();
        posizioniIniziali();
    }

    public Partita(ArrayList<Pezzo> bianchi, ArrayList<Pezzo> neri) {
        this.pezziBianchi = copiaArray(bianchi);
        this.pezziNeri = copiaArray(neri);
    }

    public Partita(ArrayList<Pezzo> bianchi, ArrayList<Pezzo> neri, Mossa ultimaMossa) {
        this.pezziBianchi = copiaArray(bianchi);
        this.pezziNeri = copiaArray(neri);
        this.ultimaMossa = ultimaMossa;
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
                if (p.getColore() == BIANCO) {
                    if (p.getPosizione().getColonna() != posizioneFin.getColonna()) {
                        if (enPassant(p, posizioneFin)) {
                            return true;
                        }
                        if ((p.getPosizione().getColonna().ordinal() + 1 == posizioneFin.getColonna().ordinal() || p.getPosizione().getColonna().ordinal() - 1 == posizioneFin.getColonna().ordinal()) && p.getPosizione().getRiga().ordinal() + 1 == posizioneFin.getRiga().ordinal()) {
                            return isOccupato(posizioneFin, BIANCO);
                        } else {
                            return false;
                        }
                    } else {
                        if (isOccupato(posizioneFin)) {
                            return false;
                        }
                        if (p.getPosizione().getRiga().ordinal() + 1 == posizioneFin.getRiga().ordinal()) {
                            return true;
                        }
                        if (isOccupato(new Posizione(Riga.values()[p.getPosizione().getRiga().ordinal() + 1], p.getPosizione().getColonna()))) {
                            return false;
                        }
                        if (p.getPosizione().getRiga() == R2 && posizioneFin.getRiga() == R4) {
                            return true;
                        }
                    }
                } else {
                    if (p.getPosizione().getColonna() != posizioneFin.getColonna()) {
                        if (enPassant(p, posizioneFin)) {
                            return true;
                        }
                        if ((p.getPosizione().getColonna().ordinal() + 1 == posizioneFin.getColonna().ordinal() || p.getPosizione().getColonna().ordinal() - 1 == posizioneFin.getColonna().ordinal()) && p.getPosizione().getRiga().ordinal() - 1 == posizioneFin.getRiga().ordinal()) {
                            return isOccupato(posizioneFin, NERO);
                        } else {
                            return false;
                        }
                    } else {
                        if (isOccupato(posizioneFin)) {
                            return false;
                        }
                        if (p.getPosizione().getRiga().ordinal() - 1 == posizioneFin.getRiga().ordinal()) {
                            return true;
                        }
                        if (isOccupato(new Posizione(Riga.values()[p.getPosizione().getRiga().ordinal() - 1], p.getPosizione().getColonna()))) {
                            return false;
                        }
                        if (p.getPosizione().getRiga() == R7 && posizioneFin.getRiga() == R5) {
                            return true;
                        }
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
                if(arrocco(p, posizioneFin)){
                    return true;
                }
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
        if (p.getColore() == BIANCO) {
            if (((((ultimaMossa.getPosIni().getRiga().ordinal() == ultimaMossa.getPosFin().getRiga().ordinal() + 2) && trovaPezzo(ultimaMossa.getPosFin()).getSimbolo() == PEDONE) && (ultimaMossa.getPosFin().getColonna().ordinal() == p.getPosizione().getColonna().ordinal() + 1 || ultimaMossa.getPosFin().getColonna().ordinal() == p.getPosizione().getColonna().ordinal() - 1)) && (posizioneFin.getColonna().ordinal() == ultimaMossa.getPosFin().getColonna().ordinal() && posizioneFin.getRiga().ordinal() == ultimaMossa.getPosFin().getRiga().ordinal() + 1)) && p.getPosizione().getRiga().ordinal() == ultimaMossa.getPosFin().getRiga().ordinal()) {
                return true;
            }
        } else {
            if (((((ultimaMossa.getPosIni().getRiga().ordinal() == ultimaMossa.getPosFin().getRiga().ordinal() - 2) && trovaPezzo(ultimaMossa.getPosFin()).getSimbolo() == PEDONE) && (ultimaMossa.getPosFin().getColonna().ordinal() == p.getPosizione().getColonna().ordinal() + 1 || ultimaMossa.getPosFin().getColonna().ordinal() == p.getPosizione().getColonna().ordinal() - 1)) && (posizioneFin.getColonna().ordinal() == ultimaMossa.getPosFin().getColonna().ordinal() && posizioneFin.getRiga().ordinal() == ultimaMossa.getPosFin().getRiga().ordinal() - 1)) && p.getPosizione().getRiga().ordinal() == ultimaMossa.getPosFin().getRiga().ordinal()) {
                return true;
            }
        }
        return false;
    }

    public boolean arrocco(Pezzo p, Posizione posizioneFin) {
        if ((posizioneFin.getColonna().ordinal() == p.getPosizione().getColonna().ordinal() + 2 && posizioneFin.getRiga() == p.getPosizione().getRiga()) && !isOccupato(new Posizione(posizioneFin.getRiga(), F)) && p.getMosse().isEmpty() && trovaPezzo(new Posizione(posizioneFin.getRiga(), H)).getMosse().isEmpty()) {
            return true;
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
        Pezzo pez = trovaPezzo(pos1);
        if (pez.getColore() != turnoCorrente) {
            return false;
        }
        for (Posizione p : mossePossibiliConSacco(pez)) {
            if (p.equals(pos2)) {
                trovaPezzo(pos1).getMosse().add(new Mossa(pos1, pos2));
                sposta(pez, pos2);
                ultimaMossa = new Mossa(pos1, pos2);
                mossePartita.add(new Mossa(pos1, pos2));
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
                if (isOccupato(posizioneFin, p.getColore())) {
                    return true;
                }
                return !isOccupato(posizioneFin);
            } else {
                for (int i = p.getPosizione().getColonna().ordinal() - 1; i > posizioneFin.getColonna().ordinal(); i--) {
                    if (isOccupato(new Posizione(p.getPosizione().getRiga(), Colonna.values()[i]))) {
                        return false;
                    }
                }
                if (isOccupato(posizioneFin, p.getColore())) {
                    return true;
                }
                return !isOccupato(posizioneFin);
            }
        } else {
            if (p.getPosizione().getRiga().ordinal() < posizioneFin.getRiga().ordinal()) {
                for (int i = p.getPosizione().getRiga().ordinal() + 1; i < posizioneFin.getRiga().ordinal(); i++) {
                    if (isOccupato(new Posizione(Riga.values()[i], p.getPosizione().getColonna()))) {
                        return false;
                    }
                }
                if (isOccupato(posizioneFin, p.getColore())) {
                    return true;
                }
                return !isOccupato(posizioneFin);
            } else {
                for (int i = p.getPosizione().getRiga().ordinal() - 1; i > posizioneFin.getRiga().ordinal(); i--) {
                    if (isOccupato(new Posizione(Riga.values()[i], p.getPosizione().getColonna()))) {
                        return false;
                    }
                }
                if (isOccupato(posizioneFin, p.getColore())) {
                    return true;
                }
                return !isOccupato(posizioneFin);
            }
        }
    }

    private boolean alfiere(Pezzo p, Posizione posizioneFin) {
        int k = 0;
        if (Math.abs(p.getPosizione().getRiga().ordinal() - posizioneFin.getRiga().ordinal()) != Math.abs(p.getPosizione().getColonna().ordinal() - posizioneFin.getColonna().ordinal())) {
            return false;
        }
        if (posizioneFin.getRiga().ordinal() > p.getPosizione().getRiga().ordinal() && posizioneFin.getColonna().ordinal() > p.getPosizione().getColonna().ordinal()) {
            k = p.getPosizione().getRiga().ordinal() + 1;
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
            k = p.getPosizione().getRiga().ordinal() + 1;
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
            k = p.getPosizione().getRiga().ordinal() - 1;
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
            k = p.getPosizione().getRiga().ordinal() - 1;
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
        Posizione pos = null;
        for (int i = 0; i < Riga.values().length; i++) {
            for (int j = 0; j < Colonna.values().length; j++) {
                pos = new Posizione(Riga.values()[i], Colonna.values()[j]);
                if (controlloPosizione(p, pos)) {
                    a.add(pos);
                }
            }
        }
        return a;
    }

    public ArrayList<Posizione> mossePossibiliConSacco(Pezzo p) {
        ArrayList<Posizione> a = new ArrayList<>();
        Partita partita = new Partita(pezziBianchi, pezziNeri, ultimaMossa);
        ArrayList<Posizione> mosse = partita.mossePossibili(p);
        for (Posizione posizione : mosse) {
            partita.sposta(partita.trovaPezzo(p.getPosizione()), posizione);
            if (pedineScacco(partita, p.getColore()).isEmpty()) {
                a.add(posizione);
            }
            partita = new Partita(pezziBianchi, pezziNeri, ultimaMossa);
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
        if (enPassant(p, pos)) {
            if (p.getColore() == BIANCO) {
                for (Pezzo pezzo : pezziNeri) {
                    if (pezzo.getPosizione().equals(ultimaMossa.getPosFin())) {
                        pezziNeri.remove(pezzo);
                        break;
                    }
                }
            } else {
                for (Pezzo pezzo : pezziBianchi) {
                    if (pezzo.getPosizione().equals(ultimaMossa.getPosFin())) {
                        pezziBianchi.remove(pezzo);
                        break;
                    }
                }
            }
        }
        //TODO sposta torre in arrocco
//        if(arrocco(p, pos)){
//            sposta(trovaPezzo(new Posizione(pos.getRiga(), H)), new Posizione(pos.getRiga(), F));
//        }
        if (turnoCorrente == BIANCO) {
            for (Pezzo pezzo : pezziNeri) {
                if (pezzo.getPosizione().equals(pos)) {
                    pezziNeri.remove(pezzo);
                    break;
                }
            }
            for (Pezzo pezzo : pezziBianchi) {
                if (pezzo.getPosizione().equals(p.getPosizione())) {
                    pezzo.setPosizione(pos);
                    break;
                }
            }
        } else {
            for (Pezzo pezzo : pezziBianchi) {
                if (pezzo.getPosizione().equals(pos)) {
                    pezziBianchi.remove(pezzo);
                    break;
                }
            }
            for (Pezzo pezzo : pezziNeri) {
                if (pezzo.getPosizione().equals(p.getPosizione())) {
                    pezzo.setPosizione(pos);
                    break;
                }
            }
        }
    }

    public boolean isScacco(Colore c) {
        Partita partita = new Partita(pezziBianchi, pezziNeri, ultimaMossa);
        if (pedineScacco(partita, c).isEmpty()) {
            return false;
        }
        if (c == BIANCO) {
            for (Pezzo pezzo : partita.pezziBianchi) {
                ArrayList<Posizione> mosse = partita.mossePossibili(pezzo);
                for (Posizione posizione : mosse) {
                    partita.sposta(pezzo, posizione);
                    if (pedineScacco(partita, c).isEmpty()) {
                        return false;
                    }
                    partita = new Partita(pezziBianchi, pezziNeri, ultimaMossa);
                }
                partita = new Partita(pezziBianchi, pezziNeri, ultimaMossa);
            }
        } else {
            for (Pezzo pezzo : partita.pezziNeri) {
                ArrayList<Posizione> mosse = partita.mossePossibili(pezzo);
                for (Posizione posizione : mosse) {
                    partita.sposta(pezzo, posizione);
                    if (pedineScacco(partita, c).isEmpty()) {
                        return false;
                    }
                    partita = new Partita(pezziBianchi, pezziNeri, ultimaMossa);
                }
                partita = new Partita(pezziBianchi, pezziNeri, ultimaMossa);
            }
        }
        return true;
    }

    public static ArrayList<Pezzo> copiaArray(ArrayList<Pezzo> array) {
        ArrayList<Pezzo> fine = new ArrayList<>();
        for (Pezzo obj : array) {
            fine.add(new Pezzo(obj));
        }
        return fine;
    }

    public static ArrayList<Pezzo> pedineScacco(Partita partita, Colore colore) {
        ArrayList<Pezzo> nemici = null;
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
                }
            }
        } else {
            for (Pezzo p : pezziNeri) {
                if (p.getPosizione().getRiga() == R1 && p.getSimbolo() == PEDONE) {
                    p.setSimbolo(promozione);
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
            return false;
        } else {
            for (Pezzo p : pezziNeri) {
                if (p.getPosizione().getRiga() == R1 && p.getSimbolo() == PEDONE) {
                    return true;
                }
            }
            return false;
        }
    }
}
