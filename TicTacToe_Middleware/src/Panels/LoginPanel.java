package Panels;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginPanel extends JFrame{
    private JLabel lblTitleLogin;
    private JTextField inputNameLogin;
    private JButton btnEnviarLogin;
    private JLabel lblNameLogin;
    private JPanel loginPanel;

    public LoginPanel() {
        btnEnviarLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Enviando os Nomes na porta 7777.
                try{
                    while(true){

                        String name = inputNameLogin.getText();

                        System.out.println("Nome: " + name);
                        inputNameLogin.setText("");

                        Socket env_LoginName = new Socket("127.0.0.1", 7777);
                        ObjectOutputStream obj_envNameLogin = new ObjectOutputStream(env_LoginName.getOutputStream());
                        obj_envNameLogin.flush();
                        obj_envNameLogin.writeUTF(name);

                        obj_envNameLogin.close();
                        env_LoginName.close();

                    }

                }catch (Exception ex){
                    System.out.println("Erro ao enviar Nome de Competidor: " + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {

        LoginPanel lp = new LoginPanel();
        lp.setContentPane(lp.loginPanel);
        lp.setTitle("Login de participantes");
        lp.setSize(400, 200);
        lp.setVisible(true);
    }
}
