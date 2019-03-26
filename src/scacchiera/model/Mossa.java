/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model;

/**
 *
 * @author tonetto.davide
 */
public class Mossa {

    private Posizione posIni;
    private Posizione posFin;
    private Simbolo simbolo;

    public Mossa(Posizione posIni, Posizione posFin) {
        this.posIni = posIni;
        this.posFin = posFin;
    }

    public void setSimbolo(Simbolo simbolo) {
        this.simbolo = simbolo;
    }

    public Simbolo getSimbolo() {
        return simbolo;
    }
    
    public Mossa(Mossa m){
        this.posIni = new Posizione(m.posIni);
        this.posFin = new Posizione(m.posFin);
    }

    public Posizione getPosIni() {
        return posIni;
    }

    public Posizione getPosFin() {
        return posFin;
    }
    
    public boolean equals(Mossa m){
        return posIni.equals(m.posIni) && posFin.equals(m.posFin);
    }
}
