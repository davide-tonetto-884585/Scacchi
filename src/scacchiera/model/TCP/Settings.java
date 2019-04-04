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

/**
 *
 * @author tonetto.davide
 */
public class Settings {
    public static Socket connesso;
    public static ArrayList<Socket> spettatori;
    public static BufferedWriter connessoWriter;
    public static BufferedReader connessoReader;
    public static ArrayList<BufferedWriter> bufferedWriters;
    public static ArrayList<BufferedReader> bufferedReaders;
    
}
