/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model;

import java.util.Objects;

/**
 *
 * @author davide
 */
public class Posizione {

    private Riga riga;
    private Colonna colonna;

    public Posizione(Riga riga, Colonna colonna) {
        this.riga = riga;
        this.colonna = colonna;
    }

    public Posizione(Posizione p){
        this.riga = p.getRiga();
        this.colonna = p.getColonna();
    }

    public boolean equals(Posizione p) {
        return p.getColonna() == this.colonna && p.getRiga() == this.riga;
    }

    public Riga getRiga() {
        return riga;
    }

    public void setRiga(Riga riga) {
        this.riga = riga;
    }

    public Colonna getColonna() {
        return colonna;
    }

    public void setColonna(Colonna colonna) {
        this.colonna = colonna;
    }
    
    public void clone(Posizione p){
        this.colonna = p.getColonna();
        this.riga = p.getRiga();
    }
    
    @Override
    public String toString() {
        return colonna.toString() + riga.toString();
    }

    public enum Riga {
        R1('1'), R2('2'), R3('3'), R4('4'), R5('5'), R6('6'), R7('7'), R8('8');
        private final char valore;

        private Riga(char valore) {
            this.valore = valore;
        }

        @Override
        public String toString() {
            return Character.toString(valore);
        }
    }

    public enum Colonna {
        A('a'), B('b'), C('c'), D('d'), E('e'), F('f'), G('g'), H('h');
        
        private final char simbolo;

        private Colonna(char simbolo) {
            this.simbolo = simbolo;
        }

        public char getSimbolo(){
            return simbolo;
        }
        
        public static Colonna getFromChar(char simbolo){
            for(Colonna c : Colonna.values()){
                if(c.getSimbolo() == simbolo){
                    return c;
                }
            }
            return null;
        }
        
        @Override
        public String toString() {
            return Character.toString(simbolo);
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.riga);
        hash = 53 * hash + Objects.hashCode(this.colonna);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Posizione other = (Posizione) obj;
        if (this.riga != other.riga) {
            return false;
        }
        if (this.colonna != other.colonna) {
            return false;
        }
        return true;
    }
    
}
