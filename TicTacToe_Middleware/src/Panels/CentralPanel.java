package Panels;

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
    private JButton btnComecarGame;

    Map<String, String> data1 = new HashMap<>();
    Map<String, String> data2 = new HashMap<>();

    public CentralPanel() {
        btnIniciarP1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnIniciarP1.setBackground(Color.GREEN);
                btnIniciarP1.setText("Iniciado!");

                try{
                    ServerSocket rec_NameLoginC1 = new ServerSocket(7777);
                    Socket nameC1 = rec_NameLoginC1.accept();
                    System.out.println("Login 1 sendo realizado...");

                    ObjectInputStream obj_recNameLoginC1 = new ObjectInputStream(nameC1.getInputStream());

                    data1.put("Nome", obj_recNameLoginC1.readUTF());
                    data1.put("Aviso", "Você Começa!");
                    data1.put("Simbolo", "X");

                    lblNameP1_mw.setText(data1.get("Nome") + "  (X)");

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
                btnIniciarP2.setBackground(Color.GREEN);
                btnIniciarP2.setText("Iniciado!");
                try{

                    ServerSocket rec_NameLoginC2 = new ServerSocket(7777);
                    Socket nameC2 = rec_NameLoginC2.accept();
                    System.out.println("Login 2 sendo realizado...");

                    ObjectInputStream obj_recNameLoginC2 = new ObjectInputStream(nameC2.getInputStream());

                    data2.put("Nome", obj_recNameLoginC2.readUTF());
                    data2.put("Aviso", "Espere sua Vez");
                    data2.put("Simbolo", "O");

                    lblNameP2_mw.setText(data2.get("Nome") + "  (O)");

                    obj_recNameLoginC2.close();
                    nameC2.close();
                    rec_NameLoginC2.close();

                }catch (Exception ex){
                    System.out.println("Erro ao receber login de Competidor 2: " + ex.getMessage());
                }
            }
        });

        btnComecarGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnComecarGame.setBackground(Color.BLACK);
                new Thread(() -> {
                    try {
                        //Cria Sockets
                        Socket Sc1 = new Socket("127.0.0.1",4000); // envia para o competidor 1
                        Socket Sc2 = new Socket("127.0.0.1",6000); // envia para o competidor 2

                        // Cria objeto para envio
                        ObjectOutputStream obj_c1 = new ObjectOutputStream(Sc1.getOutputStream());
                        ObjectOutputStream obj_c2 = new ObjectOutputStream(Sc2.getOutputStream());

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
