package Panels;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Player01 extends JFrame{
    private JLabel lblPlayer01;
    private JLabel lblAvisoP1;
    private JPanel player01Panel;

    public Player01(){

        // recebe os dados inicias
        new Thread(() -> {
            try {
                // Recebendo mensagem do Middleware.
                ServerSocket SSc1 = new ServerSocket(4000);
                Socket c1 = SSc1.accept();
                System.out.println("Central conectada!");

                ObjectInputStream obj_M_c1 = new ObjectInputStream(c1.getInputStream());
                Map<String, String> data = (Map<String,String>) obj_M_c1.readObject();

                lblPlayer01.setText(lblPlayer01.getText() + data.get("Nome"));
                lblAvisoP1.setText(data.get("Aviso"));

                obj_M_c1.close();
                c1.close();
                SSc1.close();

            }catch (Exception ex){
                System.out.println("Erro no player 1 - " + ex.getMessage());
            }
        }).start();
    }

    public static void main(String[] args){
        Player01 p1 = new Player01();
        p1.setContentPane(p1.player01Panel);
        p1.setTitle("Player 1");
        p1.setSize(300, 300);
        p1.setVisible(true);
        p1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
