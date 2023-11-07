package Panels;

import Classes.Cliente;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Player01 {
    private JLabel lblNomeJogador;
    private JLabel lblAviso;
    private JButton button11;
    private JButton button12;
    private JButton button13;
    private JButton button21;
    private JButton button22;
    private JButton button23;
    private JButton button31;
    private JButton button32;
    private JButton button33;
    private JPanel panelP1;

    String simboloJogador1 = "X";
    int simbolo_int = 1;

    String simbolo_str = String.valueOf(simbolo_int);


    JButton[] ListaDeBotoes = {
            button11, button12, button13,
            button21, button22, button23,
            button31, button32, button33
    };
    private Cliente Jogador1;
    public Player01(){

        button11.addActionListener(ActionEvent -> {Map<String, String> pacote = new HashMap<>();pacote.put("linha", "1"); pacote.put("coluna", "1");pacote.put("aviso", "Sua Vez!");pacote.put("simbolo", simbolo_str);FazJogada(pacote);});
        button12.addActionListener(ActionEvent -> {Map<String, String> pacote = new HashMap<>();pacote.put("linha", "1"); pacote.put("coluna", "2");pacote.put("aviso", "Sua Vez!");pacote.put("simbolo", simbolo_str);FazJogada(pacote);});
        button13.addActionListener(ActionEvent -> {Map<String, String> pacote = new HashMap<>();pacote.put("linha", "1"); pacote.put("coluna", "3");pacote.put("aviso", "Sua Vez!");pacote.put("simbolo", simbolo_str);FazJogada(pacote);});
        button21.addActionListener(ActionEvent -> {Map<String, String> pacote = new HashMap<>();pacote.put("linha", "2"); pacote.put("coluna", "1");pacote.put("aviso", "Sua Vez!");pacote.put("simbolo", simbolo_str);FazJogada(pacote);});
        button22.addActionListener(ActionEvent -> {Map<String, String> pacote = new HashMap<>();pacote.put("linha", "2"); pacote.put("coluna", "2");pacote.put("aviso", "Sua Vez!");pacote.put("simbolo", simbolo_str);FazJogada(pacote);});
        button23.addActionListener(ActionEvent -> {Map<String, String> pacote = new HashMap<>();pacote.put("linha", "2"); pacote.put("coluna", "3");pacote.put("aviso", "Sua Vez!");pacote.put("simbolo", simbolo_str);FazJogada(pacote);});
        button31.addActionListener(ActionEvent -> {Map<String, String> pacote = new HashMap<>();pacote.put("linha", "3"); pacote.put("coluna", "1");pacote.put("aviso", "Sua Vez!");pacote.put("simbolo", simbolo_str);FazJogada(pacote);});
        button32.addActionListener(ActionEvent -> {Map<String, String> pacote = new HashMap<>();pacote.put("linha", "3"); pacote.put("coluna", "2");pacote.put("aviso", "Sua Vez!");pacote.put("simbolo", simbolo_str);FazJogada(pacote);});
        button33.addActionListener(ActionEvent -> {Map<String, String> pacote = new HashMap<>();pacote.put("linha", "3"); pacote.put("coluna", "3");pacote.put("aviso", "Sua Vez!");pacote.put("simbolo", simbolo_str);FazJogada(pacote);});

        new Thread(() -> {
            try {
                Jogador1 = new Cliente(7777);
            }catch (Exception ex){
                System.out.println("Erro ao abrir cliente 1: " + ex.getMessage());
                ex.printStackTrace();
            }

            // Iniciando Jogador 1
            try{
                Map<String, String> pacoteJ1;
                DesabilitaHabilitaBotoes(false);
                while(true){
                    pacoteJ1 = Jogador1.RecebeMapPacote();
                    String aviso = pacoteJ1.get("aviso");
//                    lblAviso.setText(aviso);

                    if (Objects.equals(aviso, "Você Começa!") || Objects.equals(aviso, "Sua Vez!")) {

                        DesabilitaHabilitaBotoes(true);

                        if (Objects.equals(aviso, "Sua vez!")) {
                            MarcaJogada(pacoteJ1);
                        }
                    } else {
                        pacoteJ1.replace("aviso", "Você Começa!");
                        Jogador1.EnviaMapPacote(pacoteJ1);
                        DesabilitaHabilitaBotoes(false);
                        mostraMensagem(pacoteJ1);
                    }
                }
            }catch (Exception ex){
                System.out.println("Erro no Jogador 1: " + ex.getMessage());
            }
        });

    }

    private void DesabilitaHabilitaBotoes(boolean cod){

        for(JButton b : ListaDeBotoes){
            b.setEnabled(cod);
        }
    }

    private void MarcaJogada(Map<String, String> pacote) {
        String linha = pacote.get("linha");
        String coluna = pacote.get("coluna");
        String botao = linha + coluna;
        String simbolo = simboloJogador1;

        switch (botao) {
            case "11" -> button11.setText(simbolo);
            case "12" -> button12.setText(simbolo);
            case "13" -> button13.setText(simbolo);
            case "21" -> button21.setText(simbolo);
            case "22" -> button22.setText(simbolo);
            case "23" -> button23.setText(simbolo);
            case "31" -> button31.setText(simbolo);
            case "32" -> button32.setText(simbolo);
            case "33" -> button33.setText(simbolo);
        }
    }

    private boolean ValidaJogada(Map<String, String> pacote) {
        String linha = pacote.get("linha");
        String coluna = pacote.get("coluna");
        String botao = linha + coluna;
//        System.out.println(botao);

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
//        System.out.println(pacote.get("linha") + "," + pacote.get("coluna"));
        if (ValidaJogada(pacote)) {
            try {
                Jogador1.EnviaMapPacote(pacote);
                MarcaJogada(pacote);
                DesabilitaHabilitaBotoes(false);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error ao Enviar pacote jogada Jogador 1" + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error:  A Jogada é inválida!");
        }
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
        JFrame frame = new JFrame("Jogador 1");
        frame.setContentPane(new Player01().panelP1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
