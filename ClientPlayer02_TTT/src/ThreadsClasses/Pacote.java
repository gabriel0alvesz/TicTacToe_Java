package ThreadsClasses;

import java.io.Serializable;

public class Pacote implements Serializable {

    public EnumMSG alerta;
    public int pos_linha, pos_coluna, simbolo;
    public String nome_competidor;

    public Pacote(){
        this.alerta = null;
        this.pos_linha = 0;
        this.pos_coluna = 0;
        this.nome_competidor = null;
    }


    // Construtor de Inicio
    public Pacote(String name, EnumMSG msg){
        this.nome_competidor = name;
        this.alerta = msg;
    }

    public Pacote(int[] pos, int simbolo){
        this.pos_linha = pos[0];
        this.pos_coluna = pos[1];
        this.simbolo = simbolo;
    }

    public Pacote(Pacote old_pack){
        this.alerta = old_pack.alerta;
        this.nome_competidor = old_pack.nome_competidor;
        this.simbolo = old_pack.simbolo;
    }

}
