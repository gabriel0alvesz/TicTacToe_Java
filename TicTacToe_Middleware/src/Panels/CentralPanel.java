package Panels;
import Classes.EnumMSG;
import Classes.Pacote;
import Classes.Tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

    public CentralPanel() {

        Pacote c1pack = new Pacote();
        btnIniciarP1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(btnIniciarP1.isSelected()){

                    btnIniciarP1.setBackground(Color.GREEN);
                    btnIniciarP1.setText("Iniciado!");
                }
                try{
                    ServerSocket rec_NameLoginC1 = new ServerSocket(7777);
                    Socket nameC1 = rec_NameLoginC1.accept();
                    System.out.println("Login 1 sendo realizado...");

                    ObjectInputStream obj_recNameLoginC1 = new ObjectInputStream(nameC1.getInputStream());
                    lblNameP1_mw.setText(obj_recNameLoginC1.readUTF());

                    c1pack.nome_competidor = obj_recNameLoginC1.readUTF();

                    obj_recNameLoginC1.close();
                    nameC1.close();
                    rec_NameLoginC1.close();

                }catch (Exception ex){
                    System.out.println("Erro ao receber login de Competidor 1: " + ex.getMessage());
                }
            }
        });

        Pacote c2pack = new Pacote();
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
                    lblNameP2_mw.setText(obj_recNameLoginC2.readUTF());

                    c2pack.nome_competidor = obj_recNameLoginC2.readUTF();

                    obj_recNameLoginC2.close();
                    nameC2.close();
                    rec_NameLoginC2.close();

                }catch (Exception ex){
                    System.out.println("Erro ao receber login de Competidor 2: " + ex.getMessage());
                }
            }
        });

        // Verifica quem Começa.
        Tools.shuffle(c1pack, c2pack);

        if(c1pack.simbolo == 1){
            lblNameP1_mw.setText(lblNameP1_mw.getText() + "  (X)");
            lblNameP2_mw.setText(lblNameP2_mw.getText() + "  (O)");
        }else{
            lblNameP1_mw.setText(lblNameP1_mw.getText() + "  (O)");
            lblNameP2_mw.setText(lblNameP2_mw.getText() + "  (X)");
        }

        new Thread(() -> {

            try {
                //Cria Sockets
                Socket Sc1 = new Socket("127.0.0.1",5000); // envia para o competidor 1
                Socket Sc2 = new Socket("127.0.0.1",6000); // envia para o competidor 2

                // Cria objeto para envio
                ObjectOutputStream obj_c1 = new ObjectOutputStream(Sc1.getOutputStream());
                ObjectOutputStream obj_c2 = new ObjectOutputStream(Sc2.getOutputStream());

                obj_c1.flush();
                obj_c2.flush();
                obj_c1.writeObject(c1pack);
                obj_c2.writeObject(c2pack);

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

    public static void main(String[] args) {

        CentralPanel cp = new CentralPanel();
        cp.setContentPane(cp.centralPanel);
        cp.setTitle("Entrada de Participantes");
        cp.setSize(400, 200);
        cp.setVisible(true);
    }
}
