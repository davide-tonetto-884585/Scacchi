/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model;

/**
 * @author davide
 */
public enum Colore {
    BIANCO(1), NERO(-1);

    private final int direction;

    Colore(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public Colore notThis() {
        return this == BIANCO ? NERO : BIANCO;
    }
}
