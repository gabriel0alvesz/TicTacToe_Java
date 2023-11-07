package Panels;

import Classes.JogadoresStatic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PainelCentral {
    private JPanel CentralPainel;
    private JLabel lblTitle;
    private JButton btnIniciarJogadores;
    private JLabel lblJogadores;
    private JButton btnIniciarExpectador;
    private JLabel lblExpectador;
    private JLabel lblJogador1;
    private JLabel lblNomeJogador1;
    private JLabel lblJogaador2;
    private JLabel lblNomeJogador2;
    private JButton btnSortearSimbolos;

    public PainelCentral() {
        btnIniciarJogadores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                btnIniciarJogadores.setText("Iniciado!");
                btnIniciarJogadores.setBackground(Color.GREEN);
                btnIniciarJogadores.revalidate();
                btnIniciarJogadores.repaint();

                btnIniciarJogadores.setEnabled(false);


                new Thread(() -> {
                    try{
                        ServerSocket rec_NameLogin = new ServerSocket(2222);
                        int i = 0;
                        while(lblNomeJogador1.getText().equals("") || lblNomeJogador2.getText().equals("")){
                            System.out.println("Entrou: " + i);

                            Socket name = rec_NameLogin.accept();

                            ObjectInputStream obj_recNameLogin = new ObjectInputStream(name.getInputStream());
                            String nome_jogador = obj_recNameLogin.readUTF();

                            if(lblNomeJogador1.getText().equals("")){
                                lblNomeJogador1.setText(nome_jogador);
                                JogadoresStatic.NomeJogador1 = lblNomeJogador1.getText();
                            }else {
                                lblNomeJogador2.setText(nome_jogador);
                                JogadoresStatic.NomeJogador2 = lblNomeJogador2.getText();
                                continue;
                            }

                            obj_recNameLogin.close();
                            name.close();
                            i++;
                        }
                        rec_NameLogin.close();
                        System.out.println("Thread Encerrada!");

                    }catch (Exception ex){
                        System.out.println("Erro ao receber login de Competidor: " + ex.getMessage());
                    }
                }).start();
            }
        });

        btnSortearSimbolos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSortearSimbolos.setText("Sorteados!");
                SortearSimbolos();
                System.out.println("Os Simbolos Foram Sorteados");
                btnSortearSimbolos.setEnabled(false);
                // Apos Isso deve-se abrir os players.
            }
        });
    }

    private void SortearSimbolos(){
        Random random = new Random();
        int numero = random.nextInt(2); // Gera 0 ou 1

        if(numero == 1){
            JogadoresStatic.simboloJ1 = 1;
            lblNomeJogador1.setText(lblNomeJogador1.getText() + "  (X)");
            JogadoresStatic.simboloJ2 = -1;
            lblNomeJogador2.setText(lblNomeJogador2.getText() + "  (O)");
        }else if(numero == 0){
            JogadoresStatic.simboloJ2 = 1;
            lblNomeJogador2.setText(lblNomeJogador2.getText() + "  (X)");
            JogadoresStatic.simboloJ1 = -1;
            lblNomeJogador1.setText(lblNomeJogador1.getText() + "  (O)");
        }

        Map<String, String> DadosIniciais = new HashMap<>();

        DadosIniciais.put("nome1", JogadoresStatic.NomeJogador1);
        DadosIniciais.put("simbolo1", String.valueOf(JogadoresStatic.simboloJ1));

        DadosIniciais.put("nome2", JogadoresStatic.NomeJogador2);
        DadosIniciais.put("simbolo2", String.valueOf(JogadoresStatic.simboloJ2));

        try{
            Socket painel_game = new Socket("127.0.0.1",2323);
            ObjectOutputStream output = new ObjectOutputStream(painel_game.getOutputStream());
            output.writeObject(DadosIniciais);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }


    public static void main(String[] args){
        JFrame frame = new JFrame("Painel Central");
        frame.setContentPane(new PainelCentral().CentralPainel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
