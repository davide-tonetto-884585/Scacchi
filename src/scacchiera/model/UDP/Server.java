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

/**
 *
 * @author tonetto.davide
 */
public class Server {

    private InetAddress ip;
    private static final int port = 50000;
    private boolean connessione = false;

    public Server() throws SocketException, IOException, ConnessioneException {
        DatagramSocket socketDelServer = new DatagramSocket(port);
        byte[] datiRicevuti = new byte[1024];
        DatagramPacket pacchettoRicevuto = new DatagramPacket(datiRicevuti, datiRicevuti.length);

        socketDelServer.receive(pacchettoRicevuto);
        ip = pacchettoRicevuto.getAddress();
        int porta = pacchettoRicevuto.getPort();

        String stringaRicevuta = new String(pacchettoRicevuto.getData(), 0, pacchettoRicevuto.getLength());
//        System.out.println(stringaRicevuta);
        if (stringaRicevuta.equals("r")) {
            byte[] datiInviati = "c".getBytes();
            DatagramPacket pacchettoInviato = new DatagramPacket(datiInviati, datiInviati.length, ip, porta);
            socketDelServer.send(pacchettoInviato);

            for (int i = 0; i < 2; i++) {
                socketDelServer.setSoTimeout(2000);
                try {
                    pacchettoRicevuto = new DatagramPacket(datiRicevuti, datiRicevuti.length);
                    socketDelServer.receive(pacchettoRicevuto);
                    stringaRicevuta = new String(pacchettoRicevuto.getData(), 0, pacchettoRicevuto.getLength());
//                    System.out.println(stringaRicevuta);
                    if (stringaRicevuta.equals("cc")) {
                        connessione = true;
                        break;
                    } else if (stringaRicevuta.equals("c")) {
                        socketDelServer.send(pacchettoInviato);
                    } else {
                        throw new ConnessioneException("Errore ricezione pacchetto");
                    }
                } catch (SocketTimeoutException ex) {
                    if (i == 1) {
                        throw new ConnessioneException("Errore ricezione pacchetto");
                    } else {
                        socketDelServer.send(pacchettoInviato);
                    }
                }
            }
        }
    }

    public InetAddress getIp() {
        return ip;
    }

    public boolean isConnessione() {
        return connessione;
    }
}
