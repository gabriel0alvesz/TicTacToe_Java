package ThreadsClasses;

import javax.swing.*;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ThOutput extends Thread{

    // Enviando menssagem para o middleware.
    @Override
    public void run() {
        try {
            while(true){
                Socket c1 = new Socket("127.0.0.1", 2000);

                ObjectOutputStream obj_c1_M = new ObjectOutputStream(c1.getOutputStream());

                Scanner ler = new Scanner(System.in);
                System.out.print("\n --> Escreva uma menssagem para C2: ");
                String msg_M = ler.next();

                obj_c1_M.writeUTF(msg_M);

                obj_c1_M.close();
                c1.close();
            }
        }catch (Exception ex){
          System.out.println("Erro na ThOutput C1 - " + ex.getMessage());
        }
    }

    public static void main(String[] args){

        ThOutput env_c2 = new ThOutput();
        env_c2.start();
    }
}
