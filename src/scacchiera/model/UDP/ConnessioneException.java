/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model.UDP;

/**
 *
 * @author davide
 */
public class ConnessioneException extends Exception {

    /**
     * Creates a new instance of <code>ConnessioneException</code> without
     * detail message.
     */
    public ConnessioneException() {
    }

    /**
     * Constructs an instance of <code>ConnessioneException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ConnessioneException(String msg) {
        super(msg);
    }
}
