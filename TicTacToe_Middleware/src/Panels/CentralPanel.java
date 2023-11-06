package Panels;
import Classes.Pacote;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class CentralPanel extends JFrame {
    private JTextField inputP1_mw;
    private JTextField inputP2_mw;
    private JLabel lblTitle_mw;
    private JLabel lblP1_mw;
    private JLabel lblP2_mw;
    private JButton btnIniciarP1;
    private JLabel lblExpectador_mw;
    public JPanel centralPanel;
    private JLabel lblNameP1_mw;
    private JLabel lblNameP2_mw;
    private JButton btnIniciarP2;
    private JButton btnIniciarExpectador;
    private JButton btnComeçarGame;


    public CentralPanel() {
        Pacote c1pack = new Pacote();
        Pacote c2pack = new Pacote();

        btnIniciarP1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    btnIniciarP1.setBackground(Color.GREEN);
                    btnIniciarP1.setText("Iniciado!");

                    ServerSocket rec_NameLoginC1 = new ServerSocket(7777);
                    Socket nameC1 = rec_NameLoginC1.accept();
                    System.out.println("Login 1 sendo realizado...");

                    ObjectInputStream obj_recNameLoginC1 = new ObjectInputStream(nameC1.getInputStream());

                    c1pack.nome_competidor = obj_recNameLoginC1.readUTF();
                    c1pack.simbolo = 1;
                    lblNameP1_mw.setText(c1pack.nome_competidor + "  (X)");

                    obj_recNameLoginC1.close();
                    nameC1.close();
                    rec_NameLoginC1.close();

                }catch (Exception ex){
                    System.out.println("Erro ao receber login de Competidor 1: " + ex.getMessage());
                }
            }
        });

        btnIniciarP2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    btnIniciarP2.setBackground(Color.GREEN);
                    btnIniciarP2.setText("Iniciado!");

                    ServerSocket rec_NameLoginC2 = new ServerSocket(7777);
                    Socket nameC2 = rec_NameLoginC2.accept();
                    System.out.println("Login 2 sendo realizado...");

                    ObjectInputStream obj_recNameLoginC2 = new ObjectInputStream(nameC2.getInputStream());

                    c2pack.nome_competidor = obj_recNameLoginC2.readUTF();
                    c2pack.simbolo = -1;
                    lblNameP2_mw.setText(c2pack.nome_competidor + "  (O)");

                    obj_recNameLoginC2.close();
                    nameC2.close();
                    rec_NameLoginC2.close();

                }catch (Exception ex){
                    System.out.println("Erro ao receber login de Competidor 2: " + ex.getMessage());
                }
            }
        });

        btnComeçarGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {

                    try {
                        //Cria Sockets
                        Socket Sc1 = new Socket("127.0.0.1",4000); // envia para o competidor 1
                        Socket Sc2 = new Socket("127.0.0.1",6000); // envia para o competidor 2

                        // Cria objeto para envio
                        ObjectOutputStream obj_c1 = new ObjectOutputStream(Sc1.getOutputStream());
                        ObjectOutputStream obj_c2 = new ObjectOutputStream(Sc2.getOutputStream());

                        Map<String, String> data1 = new HashMap<>();
                        Map<String, String> data2 = new HashMap<>();
                        data1.put("Nome", c1pack.nome_competidor);
                        data2.put("Nome", c2pack.nome_competidor);

                        data1.put("Aviso", "Voce Começa!");
                        data2.put("Aviso", "Espere a sua Vez!");

                        obj_c1.flush();
                        obj_c2.flush();
                        obj_c1.writeObject(data1);
                        obj_c2.writeObject(data2);

                        // Fecha tudo!
                        obj_c1.close();
                        obj_c2.close();
                        Sc1.close();
                        Sc2.close();

                    } catch (Exception ex) {
                        System.out.println("Erro ao enviar pacotes Iniciais: " + ex.getMessage());
                    }

                }).start();
            }
        });
    }

    public static void main(String[] args) {

        CentralPanel cp = new CentralPanel();
        cp.setContentPane(cp.centralPanel);
        cp.setTitle("Entrada de Participantes");
        cp.setSize(400, 200);
        cp.setVisible(true);
        cp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
