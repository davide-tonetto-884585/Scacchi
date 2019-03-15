/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model;

/**
 *
 * @author davide
 */
public enum Simbolo {
    RE('K'), REGINA('Q'), ALFIERE('B'), CAVALLO('N'), TORRE('R'), PEDONE(' ');

    private final char coding;

    private Simbolo(char coding) {
        this.coding = coding;
    }

    public char getCodingSimbol() {
        return coding;
    }
}
