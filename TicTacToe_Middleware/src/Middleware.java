import Classes.Pacote;
import Classes.ThreadC1C2;
import Classes.ThreadC2C1;
import Panels.LoginPanel;

import javax.swing.*;

public class Middleware {

    public static Pacote[] CapturaPacotes(Pacote c1, Pacote c2){
        Pacote[] list_pack = new Pacote[2];
        list_pack[0] = new Pacote(c1.nome_competidor, c1.alerta);
        list_pack[1] = new Pacote(c2.nome_competidor, c2.alerta);
        return list_pack;
    }

    public static void main(String[] args){

        ThreadC1C2 c1_c2 = new ThreadC1C2();
        c1_c2.start();

        ThreadC2C1 c2_c1 = new ThreadC2C1();
        c2_c1.start();
    }
}
