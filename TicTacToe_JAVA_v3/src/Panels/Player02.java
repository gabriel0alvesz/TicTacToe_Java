package Panels;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Player02 {
    private JLabel lblAviso;
    private JLabel lblNomeJogador;
    private JButton button11;
    private JButton button12;
    private JButton button13;
    private JButton button21;
    private JButton button22;
    private JButton button23;
    private JButton button31;
    private JButton button32;
    private JButton button33;
    private JPanel panelP2;

    JButton[] ListaDeBotoes = {
            button11, button12, button13,
            button21, button22, button23,
            button31, button32, button33
    };

    public Player02(){
        DesabilitaHabilitaBotoes(false);

        new Thread(() -> {
            try {
                ServerSocket SSc2 = new ServerSocket(5555);
                Map<String, String> msg;
                // Recebendo mensagem do Middleware.
                while(true){

                    Socket c2 = SSc2.accept();
                    System.out.println("Recebendo dados do Player 2");
                    ObjectInputStream obj_M_c2 = new ObjectInputStream(c2.getInputStream());
                    msg = (Map<String, String>) obj_M_c2.readObject();

                    lblNomeJogador.setText(msg.get("nome"));

                    if(Objects.equals(msg.get("aviso"), "Você Começa")){
                        DesabilitaHabilitaBotoes(true);
                        lblAviso.setText(msg.get("aviso"));
                    }else if (Objects.equals(msg.get("aviso"), "Espere sua Vez")){
                        DesabilitaHabilitaBotoes(false);
                    }

                    obj_M_c2.close();
                    c2.close();
                }

            }catch (Exception ex){
                System.out.println("Erro na ThInput C1 - " + ex.getMessage());
            }
        }).start();

        // Tread de input Cliente 2
        new Thread(() -> {
            try {
                ServerSocket SSc2 = new ServerSocket(2001);
                Map<String, String> msg;
                // Recebendo mensagem do Middleware.
                while(true){

                    Socket c2 = SSc2.accept();
                    ObjectInputStream obj_M_c2 = new ObjectInputStream(c2.getInputStream());
                    msg = (Map<String, String>) obj_M_c2.readObject();

                    lblNomeJogador.setText(msg.get("nome"));

                    if(Objects.equals(msg.get("aviso"), "Você Começa")){
                        DesabilitaHabilitaBotoes(true);
                        lblAviso.setText(msg.get("aviso"));
                    }else if (Objects.equals(msg.get("aviso"), "Espere sua Vez")){
                        DesabilitaHabilitaBotoes(false);
                    }

                    obj_M_c2.close();
                    c2.close();
                }

            }catch (Exception ex){
                System.out.println("Erro na ThInput C1 - " + ex.getMessage());
            }
        }).start();

        //Thread de output do Cliente 1
//        new Thread(() -> {
//            try {
//                while(true){
//                    Socket c2 = new Socket("127.0.0.1", 2000);
//
//                    ObjectOutputStream obj_c2_M = new ObjectOutputStream(c2.getOutputStream());
//                    Map<String, String> jogada_temp; // parametro que deve ser sempre atualizado para ser enviado.
//
////                    obj_c1_M.writeObject(jogada_temp);
//                    obj_c2_M.close();
//                    c2.close();
//                }
//            }catch (Exception ex){
//                System.out.println("Erro na ThOutput C1 - " + ex.getMessage());
//            }
//        }).start();

    }

    private void DesabilitaHabilitaBotoes(boolean cod){

        for(JButton b : ListaDeBotoes){
            b.setEnabled(cod);
        }
    }

    private boolean ValidaJogada(Map<String, String> pacote) {
        String linha = pacote.get("linha");
        String coluna = pacote.get("coluna");
        String botao = linha + coluna;

        return switch (botao) {
            case "11" -> Objects.equals(button11.getText(), "-");
            case "12" -> Objects.equals(button12.getText(), "-");
            case "13" -> Objects.equals(button13.getText(), "-");
            case "21" -> Objects.equals(button21.getText(), "-");
            case "22" -> Objects.equals(button22.getText(), "-");
            case "23" -> Objects.equals(button23.getText(), "-");
            case "31" -> Objects.equals(button31.getText(), "-");
            case "32" -> Objects.equals(button32.getText(), "-");
            case "33" -> Objects.equals(button33.getText(), "-");
            default -> false;
        };
    }

    private void FazJogada(Map<String, String> pacote) {

    }
    public void mostraMensagem(Map<String,String> pacote) {
        String aviso = pacote.get("aviso");
        if (Objects.equals(aviso, "Você Ganhou!")) {
            lblAviso.setText("Você Venceu!!!");
            lblAviso.setForeground(Color.GREEN);
        } else if (Objects.equals(aviso, "Você Perdeu!")) {
            lblAviso.setText("Você Perdeu!");
            lblAviso.setForeground(Color.RED);
        } else if (Objects.equals(aviso, "Deu Velha!")){
            lblAviso.setText("Ih, Deu Velha!");
            lblAviso.setForeground(Color.MAGENTA);
        } else if (Objects.equals(aviso, "ERRO")) {
            JOptionPane.showMessageDialog(null, "Vish, Deu algum Erro.");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Jogador 2");
        frame.setContentPane(new Player02().panelP2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
