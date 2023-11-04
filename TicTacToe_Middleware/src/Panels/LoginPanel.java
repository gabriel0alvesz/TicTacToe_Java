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

    int cont_comp = 0;
    public LoginPanel() {
        btnEnviarLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(inputNameLogin.getText() != null){
                    try{
                        while (cont_comp < 2){
                            cont_comp++;
                            String msg = inputNameLogin.getText();
                            Socket cliente = new Socket("127.0.0.1", 2222);
                            ObjectOutputStream writer = new ObjectOutputStream(cliente.getOutputStream());
                            writer.flush();
                            writer.writeUTF(msg);

                            writer.close();
                            cliente.close();
                        }
                    }catch (Exception er){
                        JOptionPane.showMessageDialog(null, "Erro no envio do nome no Login: " + er.getMessage());
                    }
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
