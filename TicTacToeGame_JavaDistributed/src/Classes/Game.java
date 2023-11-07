package Classes;
import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Game {
    private final int[][] Tabuleiro = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
    };

    private MiddlewareServer servidor;
    private boolean control;
    private String jogadorAtual;
    private Map<String, String> DadosInicias;

    public Game() throws IOException, ClassNotFoundException {

        DadosInicias = new HashMap<>(VerificaJogadorX()); // recebe nomes e simbolos dos jogadores pelo painel Central
        System.out.println("Chegou os simbolos");

        System.out.println(DadosInicias.get("nome1") + " -> " + DadosInicias.get("simbolo1"));
        System.out.println(DadosInicias.get("nome2") + " -> " + DadosInicias.get("simbolo2"));

        if(Objects.equals(DadosInicias.get("simbolo1"), "1")){
            System.out.println("Jogador 1 é o X");
            control = true;

        } else if (Objects.equals(DadosInicias.get("simbolo2"), "1")) {
            System.out.println("Jogador 2 é o X");
            control = false;
        }

        servidor = new MiddlewareServer(7777, 8888);

        if(servidor != null){
            System.out.println("Servidor On");
        }

        System.out.println("Chegou no jogador atual.");
        jogadorAtual = "X";

        Map<String, String> pacote = new HashMap<>();
        pacote.put("nome1", DadosInicias.get("nome1"));
        pacote.put("nome2", DadosInicias.get("nome2"));
        pacote.put("linha", "-1");
        pacote.put("coluna", "-1");
        pacote.put("aviso", "Você Começa!");

        if(control){
            pacote.put("simbolo1", "1");
            pacote.put("simbolo2", "-1");

        }else{
            pacote.put("simbolo1", "-1");
            pacote.put("simbolo2", "1");
        }

        System.out.println(pacote.get("nome1") + " -> " + pacote.get("simbolo1"));
        System.out.println(pacote.get("nome2") + " -> " + pacote.get("simbolo2"));
        System.out.println(control);

        String msg_retorno;
        int flag_partida = JOptionPane.YES_OPTION;
        int i = 0;

        while(i < 9 || Objects.equals(flag_partida, JOptionPane.YES_OPTION)){
            System.out.println("Entrou no loop" + i);
            i++;

            pacote = new HashMap<>(servidor.MensagemPara(pacote, jogadorAtual));
            msg_retorno = VerificaJogada(pacote);

            if(Objects.equals(msg_retorno, "Sua Vez!")){
                TrocaDeJogador();
            }else if(Objects.equals(msg_retorno, "Você Ganhou!")){
                pacote.put("aviso", "Você Ganhou!");
                JOptionPane.showMessageDialog(null, "Jogo Encerrado, " + jogadorAtual + " GANHOU!");
                pacote = new HashMap<>(servidor.MensagemPara(pacote, jogadorAtual));
                flag_partida = JOptionPane.CLOSED_OPTION;
            }

            if (i == 8) {
                pacote.put("aviso", "Deu Velha!");
                pacote = new HashMap<>(servidor.MensagemPara(pacote, jogadorAtual));
                JOptionPane.showMessageDialog(null, "Deu Velha!");
                flag_partida = JOptionPane.CLOSED_OPTION;
            }

            printMatrix(Tabuleiro);
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
        int c = Integer.parseInt(pacote.get("coluna")) - 1;
        int l = Integer.parseInt(pacote.get("linha")) - 1;
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

    private void printMatrix(int[][] matrix) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public Map<String, String> VerificaJogadorX() {
        Map<String, String> temp = null;
        try{
            ServerSocket SS_rec_DI = new ServerSocket(2323);
            while(true){
                Socket S_client = SS_rec_DI.accept();
                ObjectInputStream input = new ObjectInputStream(S_client.getInputStream());
                temp = (Map<String, String>) input.readObject();

                input.close();
                S_client.close();

                if(!temp.isEmpty()){
                    break;
                }
            }

        }catch (Exception ex){
            System.out.println("Erro ao receber dados: " + ex.getMessage());
        }

        System.out.println(temp.get("simbolo1") + "/" + temp.get("simbolo2"));

        return temp;
    }

    public static void main(String[] args) {
        try {
            new Game();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

