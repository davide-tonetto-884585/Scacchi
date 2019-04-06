/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model.TCP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;
import scacchiera.model.Partita;

/**
 *
 * @author tonetto.davide
 */
public class Settings {
    public static Socket player;
    public static ArrayList<Socket> spettatori;
    public static BufferedWriter playerWriter;
    public static BufferedReader playerReader;
    public static ArrayList<BufferedWriter> bufferedWriters;
    public static ArrayList<BufferedReader> bufferedReaders;
    public static Partita partita;
}
