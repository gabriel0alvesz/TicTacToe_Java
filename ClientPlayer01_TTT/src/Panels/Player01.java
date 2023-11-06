package Panels;

import ThreadsClasses.Pacote;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Player01 extends JFrame{
    private JLabel lblPlayer01;
    private JLabel lblAvisoP1;
    private JPanel player01Panel;

    public Player01(){

        new Thread(() -> {
            try {
                // Recebendo mensagem do Middleware.
                ServerSocket SSc1 = new ServerSocket(4000);
                Socket c1 = SSc1.accept();
                System.out.println("Central conectada!");

                ObjectInputStream obj_M_c1 = new ObjectInputStream(c1.getInputStream());
                Map<String, String> data = (Map<String,String>) obj_M_c1.readObject();
//                Pacote msg =  new Pacote((Pacote) obj_M_c1.readObject());

                lblPlayer01.setText(data.get("Nome"));

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
        Player01 cp = new Player01();
        cp.setContentPane(cp.player01Panel);
        cp.setTitle("Player 1");
        cp.setSize(300, 300);
        cp.setVisible(true);
    }
}
