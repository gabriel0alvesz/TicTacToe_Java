package Classes;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadC1C2 extends Thread{

    // Recebe a mensagem C1:2000 e envia para C2:3001
    @Override
    public void run() {

        // Recebe a mensagem C1:2000 e envia para C2:3001
        try{
            ServerSocket SSc1_c2 = new ServerSocket(2000); // recebe de C1
            while(true){
                System.out.println("\nEsperando Mensagem do C1...");
                Socket c1_c2 = SSc1_c2.accept();
                System.out.println("Cliente 1 Conectado!");

                ObjectInputStream obj_rec = new ObjectInputStream(c1_c2.getInputStream());
                String msg_mw = "\nC1 -> MW -> C2: " + obj_rec.readUTF();

                System.out.println(msg_mw);

                // Enviando a menssagem para C2
                Socket env = new Socket("127.0.0.1", 3001);

                ObjectOutputStream obj_env = new ObjectOutputStream(env.getOutputStream());
                obj_env.flush();
                obj_env.writeUTF(msg_mw);

                // Fechando Objetos e Sockets
                obj_env.close();
                env.close();

                obj_rec.close();
                c1_c2.close();
            }
        }catch (Exception ex){
            System.out.println("ERRO no Middleware (C1->C2) - " + ex.getMessage());
        }
    }
}
