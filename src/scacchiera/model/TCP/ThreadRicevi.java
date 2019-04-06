/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model.TCP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author tonetto.davide
 */
public class ThreadRicevi extends Thread {

    private int port;

    public ThreadRicevi(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9800);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        while (true) {
            try {
                Socket socket = serverSocket.accept();

                InputStream inputStream = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(isr);

                String message = br.readLine();

                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);

                if (message.equals("richiesta")) {
                    if (Settings.player == null) {
                        bw.write("richiesta accettata\n");
                        bw.flush();
                        Settings.player = socket;
                        Settings.playerReader = br;
                        Settings.playerWriter = bw;
                        new ThreadRicezione().start();
                    } else {
                        bw.write("richiesta rifiutata\n");
                        bw.flush();
                        bw.close();
                        br.close();
                        socket.close();
                    }
                } else if (message.equals("richiesta spettatore")) {
                    bw.write("richiesta accettata\n");
                    bw.flush();
                    Settings.spettatori.add(socket);
                    Settings.bufferedReaders.add(br);
                    Settings.bufferedWriters.add(bw);
                } else {
                    bw.write("errore\n");
                    bw.flush();
                    bw.close();
                    br.close();
                    socket.close();
                }

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
