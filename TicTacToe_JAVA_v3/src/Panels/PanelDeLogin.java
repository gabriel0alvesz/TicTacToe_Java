package Panels;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PanelDeLogin {
    private JTextField inNameJogador;
    private JLabel lblTitulo;
    private JButton btnEnviar;
    private JLabel lblNomeJogador;
    private JPanel loginPanel;
    public PanelDeLogin() {
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = "";
                try{
                    name = inNameJogador.getText();

                    Socket env_LoginName = new Socket("127.0.0.1", 2222);
                    ObjectOutputStream obj_envNameLogin = new ObjectOutputStream(env_LoginName.getOutputStream());
                    obj_envNameLogin.flush();
                    obj_envNameLogin.writeUTF(name);

                    inNameJogador.setText("");

                    obj_envNameLogin.close();
                    env_LoginName.close();

                    System.out.println("Nome Enviado com Sucesso!");

                }catch (Exception ex){
                    System.out.println("Erro ao enviar Nome de Competidor: " + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("Painel de Login");
        frame.setSize(300,300);
        frame.setContentPane(new PanelDeLogin().loginPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}

