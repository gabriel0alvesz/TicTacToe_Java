package Panels;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

    public PainelCentral() {
        btnIniciarJogadores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
                            }else {
                                lblNomeJogador2.setText(nome_jogador);
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
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("Painel Central");
        frame.setContentPane(new PainelCentral().CentralPainel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
