package ThreadsClasses;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ThInput extends Thread{

    // Recebendo mensagem do Middleware.
    @Override
    public void run() {
        try {
            ServerSocket SSc2 = new ServerSocket(3001);
            Object msg = "";
            // recebendo menssagem do Middleware
            while(true){

                Socket c2 = SSc2.accept();
                ObjectInputStream obj_M_c2 = new ObjectInputStream(c2.getInputStream());
                msg = obj_M_c2.readUTF();

                System.out.println("C1 -> Middleware -> C2 :: " + msg);

                obj_M_c2.close();
                c2.close();
            }

        }catch (Exception ex){
            System.out.println("Erro na ThInput C2 - " + ex.getMessage());
        }
    }

    public static void main(String[] args){

        ThInput rec_c1 = new ThInput();
        rec_c1.start();
    }
}
