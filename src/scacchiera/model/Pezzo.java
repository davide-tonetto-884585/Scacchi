/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model;

import java.util.ArrayList;

/**
 *
 * @author davide
 */
public class Pezzo {

    private Colore colore;
    private Simbolo simbolo;
    private Posizione posizione;
    private ArrayList<Mossa> mosse;

    public Pezzo(Simbolo simbolo, Colore colore){
        this.simbolo = simbolo;
        this.colore = colore;
    }
    
    public Pezzo(Colore colore, Simbolo simbolo, Posizione posizione) {
        this.colore = colore;
        this.simbolo = simbolo;
        this.posizione = posizione;
        mosse = new ArrayList<>();
    }

    public Pezzo(Pezzo p) {
        this.posizione = new Posizione(p.posizione);
        this.colore = p.colore;
        this.simbolo = p.simbolo;
        if (this.mosse == null) {
            this.mosse = new ArrayList<>();
        } else {
            this.mosse = copiaArray(mosse);
        }
    }

    public Colore getColore() {
        return colore;
    }

    public void setColore(Colore colore) {
        this.colore = colore;
    }

    public Simbolo getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(Simbolo simbolo) {
        this.simbolo = simbolo;
    }

    public Posizione getPosizione() {
        return posizione;
    }

    public void setPosizione(Posizione posizione) {
        this.posizione = posizione;
    }

    public ArrayList<Mossa> getMosse() {
        return mosse;
    }

    public static ArrayList<Mossa> copiaArray(ArrayList<Mossa> array) {
        ArrayList<Mossa> fine = new ArrayList<>();
        for (Mossa obj : array) {
            fine.add(new Mossa(obj));
        }
        return fine;
    }
}
