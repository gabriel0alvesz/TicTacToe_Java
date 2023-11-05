package ThreadsClasses;

import javax.swing.*;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ThOutput extends Thread{
    // Enviando menssagem para o middleware -> C1.
    @Override
    public void run() {
        try {
            while(true){
                Socket c2 = new Socket("127.0.0.1", 3000);

                ObjectOutputStream obj_c2_M = new ObjectOutputStream(c2.getOutputStream());

                Scanner ler = new Scanner(System.in);
                System.out.print("\n --> Escreva uma menssagem para C1: ");
                String msg_M = ler.next();

                obj_c2_M.writeUTF(msg_M);

                obj_c2_M.close();
                c2.close();
            }
        }catch (Exception ex){
           System.out.println("Erro na ThOutput C2 - " + ex.getMessage());
        }
    }
    public static void main(String[] args){

        ThOutput env_c1 = new ThOutput();
        env_c1.start();
    }
}
