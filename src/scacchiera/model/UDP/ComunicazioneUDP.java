/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import scacchiera.model.Colore;
import scacchiera.model.Pezzo;
import scacchiera.model.Posizione;
import scacchiera.model.Posizione.*;
import scacchiera.model.Simbolo;

/**
 *
 * @author davide
 */
public abstract class ComunicazioneUDP {
    public void invioMossa(Pezzo pezzo, Posizione p, String ip) throws Exception{
        DatagramSocket socketDelClient = new DatagramSocket();
        InetAddress indirizzoIP = InetAddress.getByName(ip);
        byte[] datiInviati = convertMossaToByte(pezzo, p);
        DatagramPacket pacchettoInviato = new DatagramPacket(datiInviati, datiInviati.length, indirizzoIP, 9800);
        socketDelClient.send(pacchettoInviato);
        socketDelClient.close();
    }
    
    public ArrayList ricezioneMossa() throws Exception{
        ArrayList mossa = new ArrayList();
        DatagramSocket socketDelClient = new DatagramSocket();
        byte[] datiRicevuti = new byte[1024];
        DatagramPacket pacchettoRicevuto = new DatagramPacket(datiRicevuti, datiRicevuti.length);
        socketDelClient.receive(pacchettoRicevuto);
        mossa = convertByteToMossa(pacchettoRicevuto.getData());
        socketDelClient.close();
        return mossa;
    }

    public byte[] convertMossaToByte(Pezzo pezzo, Posizione p) {
        byte[] array = new byte[6];
        int byteInScrittura = 0;
        array[byteInScrittura++] = (byte)pezzo.getPosizione().getRiga().ordinal();
        array[byteInScrittura++] = (byte)pezzo.getPosizione().getColonna().ordinal();
        array[byteInScrittura++] = (byte)pezzo.getSimbolo().ordinal();
        array[byteInScrittura++] = (byte)pezzo.getColore().ordinal();
        array[byteInScrittura++] = (byte)p.getRiga().ordinal();
        array[byteInScrittura++] = (byte)p.getColonna().ordinal();
        return array;
    }

    public ArrayList convertByteToMossa(byte[] array) {
        ArrayList mossa = new ArrayList();
        int byteInLettura = 0;
        Simbolo simbolo;
        Colore colore;
        Posizione posizione;
        Riga riga;
        Colonna colonna;
        riga = Riga.values()[array[byteInLettura++]];
        colonna = Colonna.values()[array[byteInLettura++]];
        posizione = new Posizione(riga, colonna);
        simbolo = Simbolo.values()[array[byteInLettura++]];
        colore = Colore.values()[array[byteInLettura++]];
        Pezzo pezzo = new Pezzo(colore, simbolo, posizione);
        mossa.add(pezzo);
        riga = Riga.values()[array[byteInLettura++]];
        colonna = Colonna.values()[array[byteInLettura++]];
        posizione = new Posizione(riga, colonna);
        mossa.add(posizione);
        return mossa;
    }
    
    public boolean controlloIP(String ipString) throws ConnessioneException{
        try {
            InetAddress ip = InetAddress.getByName(ipString);
            return true;
        } catch (UnknownHostException ex) {
            return false;
        }
    }
}
