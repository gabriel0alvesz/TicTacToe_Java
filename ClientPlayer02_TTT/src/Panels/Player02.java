package Panels;

import javax.swing.*;
import ThreadsClasses.Pacote;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Player02 extends JFrame {
    private JLabel lblPlayer02;
    private JLabel lblAvisoP2;
    private JPanel player02Panel;

    public Player02(){
        new Thread(() -> {
            try {
                // Recebendo mensagem do Middleware.
                ServerSocket SSc2 = new ServerSocket(6000);
                Socket c2 = SSc2.accept();
                System.out.println("Central conectada!");

                ObjectInputStream obj_M_c2 = new ObjectInputStream(c2.getInputStream());
                Map<String, String> data = (Map<String,String>) obj_M_c2.readObject();
//                Pacote msg =  new Pacote((Pacote) obj_M_c2.readObject());

                lblPlayer02.setText(data.get("Nome"));

                lblAvisoP2.setText(data.get("Aviso"));

                obj_M_c2.close();
                c2.close();
                SSc2.close();

            }catch (Exception ex){
                System.out.println("Erro no player 2 - " + ex.getMessage());
            }
        }).start();
    }

    public static void main(String[] args){
        Player02 cp = new Player02();
        cp.setContentPane(cp.player02Panel);
        cp.setTitle("Player 2");
        cp.setSize(300, 300);
        cp.setVisible(true);
    }
}
