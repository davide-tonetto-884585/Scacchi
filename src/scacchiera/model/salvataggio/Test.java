/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model.salvataggio;

import scacchiera.model.Partita;
import scacchiera.model.Posizione;

/**
 *
 * @author tonetto.davide
 */
public class Test {

    public static void main(String[] args) {
        Partita p = new Partita();
        p.muovi(p.trovaPezzo(new Posizione(Posizione.Riga.R2, Posizione.Colonna.B)).getPosizione(), new Posizione(Posizione.Riga.R4, Posizione.Colonna.B));
        p.muovi(p.trovaPezzo(new Posizione(Posizione.Riga.R7, Posizione.Colonna.A)).getPosizione(), new Posizione(Posizione.Riga.R5, Posizione.Colonna.A));
        p.muovi(p.trovaPezzo(new Posizione(Posizione.Riga.R2, Posizione.Colonna.C)).getPosizione(), new Posizione(Posizione.Riga.R3, Posizione.Colonna.C));
        p.muovi(p.trovaPezzo(new Posizione(Posizione.Riga.R7, Posizione.Colonna.C)).getPosizione(), new Posizione(Posizione.Riga.R5, Posizione.Colonna.C));
        p.muovi(p.trovaPezzo(new Posizione(Posizione.Riga.R3, Posizione.Colonna.C)).getPosizione(), new Posizione(Posizione.Riga.R4, Posizione.Colonna.C));
        p.muovi(p.trovaPezzo(new Posizione(Posizione.Riga.R5, Posizione.Colonna.C)).getPosizione(), new Posizione(Posizione.Riga.R4, Posizione.Colonna.B));
        System.out.println(Coding.encoding(p.getMossePartita()));
    }
}
