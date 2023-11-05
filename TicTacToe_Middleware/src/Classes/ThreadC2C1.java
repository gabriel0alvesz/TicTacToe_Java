package Classes;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadC2C1 extends Thread{


    @Override
    public void run() {

        // sdasdasd
        try{
            ServerSocket SSc2_c1 = new ServerSocket(3000); // recebe C2
            while(true){
                System.out.println("\nEsperando Mensagem do C2...");
                Socket c2_c1 = SSc2_c1.accept();
                System.out.println("Cliente 2 Conectado!");

                ObjectInputStream obj_rec = new ObjectInputStream(c2_c1.getInputStream());
                String msg_mw = "\nC2 -> MW -> C1: " + obj_rec.readUTF();

                System.out.println(msg_mw);

            // Enviando a menssagem para C1
                Socket env = new Socket("127.0.0.1", 2001);

                ObjectOutputStream obj_env = new ObjectOutputStream(env.getOutputStream());
                obj_env.flush();
                obj_env.writeUTF(msg_mw);

            // Fechando Objetos e Sockets
                obj_env.close();
                env.close();

                obj_rec.close();
                c2_c1.close();
            }
        }catch (Exception ex){
            System.out.println("ERRO no Middleware (C2->C1) - " + ex.getMessage());
        }
    }

    public static void main(String[] args){

        ThreadC2C1 c2_c1 = new ThreadC2C1();
        c2_c1.start();
    }
}
