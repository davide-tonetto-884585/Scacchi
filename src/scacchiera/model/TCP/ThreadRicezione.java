/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model.TCP;

import java.io.IOException;
import scacchiera.model.Partita;
import scacchiera.model.Posizione;
import scacchiera.model.Posizione.Colonna;
import scacchiera.model.Posizione.Riga;
import scacchiera.model.Simbolo;

/**
 *
 * @author tonetto.davide
 */
public class ThreadRicezione extends Thread {

    @Override
    public void run() {
        while (true) {
            try {
                String line = Settings.playerReader.readLine();
                if(line.substring(0, 4).equals("mossa")){
                    String temp = line.substring(6, line.length());
                    Posizione pos1 = new Posizione(Riga.values()[Integer.parseInt(temp, 1)], Colonna.getFromChar(temp.charAt(0)));
                    Posizione pos2 = new Posizione(Riga.values()[Integer.parseInt(temp, 3)], Colonna.getFromChar(temp.charAt(2)));
                
                    Settings.partita.muovi(pos1, pos2);
                    if(temp.charAt(4) != '0'){
                        Settings.partita.promuovi(Simbolo.values()[Integer.parseInt(temp, 4)], Settings.partita.trovaPezzo(pos2).getColore());
                    }
                }else{
                    System.out.println("errore");
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
