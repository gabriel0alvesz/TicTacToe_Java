package ThreadsClasses;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

// Faz a leitura do que o Middleware manda.
public class ThInput extends Thread{

    @Override
    public void run() {
        try {
            ServerSocket SSc1 = new ServerSocket(2001);
            Object msg = "";
            // Recebendo mensagem do Middleware.
            while(true){

                Socket c1 = SSc1.accept();
                ObjectInputStream obj_M_c1 = new ObjectInputStream(c1.getInputStream());
                msg = obj_M_c1.readUTF();

                System.out.println("C2 -> Middleware -> C1 :: " + msg);

                obj_M_c1.close();
                c1.close();
            }

        }catch (Exception ex){
           System.out.println("Erro na ThInput C1 - " + ex.getMessage());
        }
    }

    public static void main(String[] args){

        ThInput rec_c2 = new ThInput();
        rec_c2.start();
    }
}
