/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import jdk.nashorn.internal.ir.ContinueNode;

/**
 *
 * @author tonetto.davide
 */
public class Client {

    private InetAddress ip;
    private static final int port = 50000;
    private boolean connessione = false;

    /**
     * tipologie messaggi: r - richiesta connessione c - onferma connessione c -
     * conferma finale
     *
     * @param ipString : String
     * @throws java.net.SocketException
     * @throws java.net.UnknownHostException
     * @throws scacchiera.model.UDP.ConnessioneException
     */
    public Client(String ipString) throws SocketException, UnknownHostException, IOException, ConnessioneException {
        ip = InetAddress.getByName(ipString);
        DatagramSocket socketDelClient = new DatagramSocket();
        byte[] datiInviati = "r".getBytes();
        DatagramPacket pacchettoInviato = new DatagramPacket(datiInviati, datiInviati.length, ip, port);
        socketDelClient.send(pacchettoInviato);

        byte[] datiRicevuti = new byte[1024];
        DatagramPacket pacchettoRicevuto = new DatagramPacket(datiRicevuti, datiRicevuti.length);
        for (int i = 0; i < 2; i++) {
            socketDelClient.setSoTimeout(2000);
            try {
                socketDelClient.receive(pacchettoRicevuto);
                break;
            } catch (SocketTimeoutException ex) {
                if (i == 1) {
                    throw new ConnessioneException("Connessione fallita");
                } else {
                    socketDelClient.send(pacchettoInviato);
                }
            }
        }

        String stringaRicevuta = new String(pacchettoRicevuto.getData(), 0, pacchettoRicevuto.getLength());
//        System.out.println(stringaRicevuta);
        if (stringaRicevuta.equals("c")) {
            datiInviati = "cc".getBytes();
            pacchettoInviato = new DatagramPacket(datiInviati, datiInviati.length, ip, port);
            socketDelClient.send(pacchettoInviato);
            
            //se il server non riceve conferma reinvia il pacchetto e se lo ricevo reinvio la conferma finale
            socketDelClient.setSoTimeout(2000);
            try {
                socketDelClient.receive(pacchettoRicevuto);
                socketDelClient.send(pacchettoInviato);
                connessione = true;
            } catch (SocketTimeoutException ex) {
                connessione = true;
            }
        }else{
            throw new ConnessioneException("Connessione fallita");
        }
        socketDelClient.close();
    }

    public InetAddress getIp() {
        return ip;
    }

    public boolean isConnessione() {
        return connessione;
    }
}
