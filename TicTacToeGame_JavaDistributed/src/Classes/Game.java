package Classes;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Game {

    private final int[][] Tabuleiro = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
    };

    private String jogadorAtual;

    public Game() throws IOException, ClassNotFoundException {
        MiddlewareServer servidor = new MiddlewareServer();
        jogadorAtual = "X";

        Map<String, String> pacote = new HashMap<>();
        String msg_retorno;

        int flag_partida = JOptionPane.YES_OPTION;
        int i = 0;

        while(i < 9 || Objects.equals(flag_partida, JOptionPane.YES_OPTION)){
            i++;

            pacote = servidor.MensagemPara(pacote, jogadorAtual);
            msg_retorno = VerificaJogada(pacote);

            if(Objects.equals(msg_retorno, "Sua Vez!")){
                TrocaDeJogador();
            }else if(Objects.equals(msg_retorno, "Você Ganhou!")){
                pacote.replace("aviso", "Você Ganhou!");
                pacote = servidor.MensagemPara(pacote, jogadorAtual);

                JOptionPane.showMessageDialog(null, "Jogo Encerrado, " + jogadorAtual + "GANHOU!");
                flag_partida = JOptionPane.CLOSED_OPTION;
            }

            if (i == 8) {
                pacote.replace("aviso", "Deu Velha!");
                pacote = servidor.MensagemPara(pacote, jogadorAtual);
                JOptionPane.showMessageDialog(null, "Deu Velha!");
                flag_partida = JOptionPane.CLOSED_OPTION;
            }
        }

        servidor.FinalizaMiddleware();
    }

    private void TrocaDeJogador() {
        if (Objects.equals(jogadorAtual, "X")) {
            jogadorAtual = "O";
        } else {
            jogadorAtual = "X";
        }
    }

    public String VerificaJogada(Map<String, String> pacote) {
        int c = Integer.parseInt(pacote.get("coluna"));
        int l = Integer.parseInt(pacote.get("linha"));
        int s = Integer.parseInt(pacote.get("simbolo"));
        int somaLinha = 0;
        int somaColuna = 0;
        int somaDiagonalP = 0;
        int somaDiagonalS = 0;

        if (Tabuleiro[l][c] == 0) {
            Tabuleiro[l][c] = s;
        } else {
            return "ERRO";
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                somaLinha += Tabuleiro[i][j];
                somaColuna += Tabuleiro[j][i];
            }
            somaDiagonalP += Tabuleiro[i][i];
            somaDiagonalS += Tabuleiro[i][2 - i];
            if (somaLinha == 3
                    || somaLinha == -3
                    || somaColuna == 3
                    || somaColuna == -3
                    || somaDiagonalP == 3
                    || somaDiagonalP == -3
                    || somaDiagonalS == 3
                    || somaDiagonalS == -3) {
                return "Você Ganhou!";
            }

            somaLinha = 0;
            somaColuna = 0;
        }
        return "Sua Vez!";
    }

    public static void main(String[] args) {
        try {
            new Game();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}