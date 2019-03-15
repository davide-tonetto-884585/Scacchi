/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scacchiera.model.salvataggio;

import java.util.ArrayList;
import scacchiera.model.Colore;
import scacchiera.model.Mossa;
import scacchiera.model.Partita;
import scacchiera.model.Pezzo;
import scacchiera.model.Simbolo;

/**
 *
 * @author tonetto.davide
 */
public class Coding {

    private static String encoding(ArrayList<Mossa> mosse) {
        String code = "";
        Partita partita = new Partita();
        for (int i = 0; i < (mosse.size() + 1) / 2; i++) {
            code += (i + 1) + ". ";
            Mossa mossa = mosse.get(i * 2);
            Pezzo p = partita.trovaPezzo(mossa.getPosIni());
            if (p.getSimbolo() != Simbolo.PEDONE) {
                code += p.getSimbolo().getCodingSimbol();
            }
            if(partita.trovaPezzo(mossa.getPosFin()) != null){
                code += "x";
            }
            code += mossa.getPosFin().toString() + " ";
            partita.sposta(p, mossa.getPosFin());
            
            if(mosse.size() == (i*2)+1)
                continue;
            mossa = mosse.get((i*2)+1);
            p = partita.trovaPezzo(mossa.getPosIni());
            if (p.getSimbolo() != Simbolo.PEDONE) {
                code += p.getSimbolo().getCodingSimbol();
            }
            if(partita.trovaPezzo(mossa.getPosFin()) != null){
                code += "x";
            }
            code += mossa.getPosFin().toString() + " ";
            partita.sposta(p, mossa.getPosFin());
        }

        return code;
    }
    
    public Pezzo ambiguo(Partita partita, Mossa mossa){
        ArrayList<Pezzo> sameSimbolo = new ArrayList<>();
        Pezzo p = null;
        
        return p;
    }
    
    public ArrayList<Pezzo> sameSimbolo(Pezzo pezzo, Partita partita){
        ArrayList<Pezzo> sameSimbolo = new ArrayList<>();
        if(pezzo.getColore() == Colore.BIANCO){
            for(Pezzo p : partita.getPezziBianchi()){
//                if()
            }
        }else{
            
        }
        return sameSimbolo;
    }
}
