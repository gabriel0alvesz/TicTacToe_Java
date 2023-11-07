package Classes;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;

public class MiddlewareServer {

    private Socket Player1;
    private ObjectInputStream inputP1;
    private ObjectOutputStream outputP1;

    private Socket Player2;
    private ObjectInputStream inputP2;
    private ObjectOutputStream outputP2;

    public MiddlewareServer(){}
    public MiddlewareServer(int portX, int portO) throws IOException, ClassNotFoundException {

        ServerSocket servidorP1 = new ServerSocket(portX);
        ServerSocket servidorP2 = new ServerSocket(portO);

        Player1 = servidorP1.accept();
        JOptionPane.showMessageDialog(null, "Player 1, conectou!");
        outputP1 = new ObjectOutputStream(Player1.getOutputStream());
        inputP1 = new ObjectInputStream(Player1.getInputStream());

        // Jogador 2
        Player2 = servidorP2.accept();
        JOptionPane.showMessageDialog(null, "Player 2, conectou!");
        outputP2 = new ObjectOutputStream(Player2.getOutputStream());
        inputP2 = new ObjectInputStream(Player2.getInputStream());
//        JOptionPane.showMessageDialog(null,"Servidor TCP em Execução");
    }

    public void FinalizaMiddleware() throws IOException {
        outputP1.close();
        outputP2.close();
        inputP1.close();
        inputP2.close();
        Player1.close();
        Player2.close();
    }

    public Map<String, String>  MensagemParaJ1(Map<String, String> pacote) throws IOException, ClassNotFoundException {
        outputP1.flush();
        outputP1.writeObject(pacote);
        return (Map<String, String>) inputP1.readObject();
    }

    public Map<String, String> MensagemParaJ2(Map<String, String> pacote) throws IOException, ClassNotFoundException {
        outputP2.flush();
        outputP2.writeObject(pacote);
        return (Map<String, String>) inputP2.readObject();
    }

    public Map<String, String> MensagemPara(Map<String, String> pacote, String jogadorAtual) throws IOException, ClassNotFoundException {
        Map<String, String> e = null;
        System.out.println("Entra no mensagem para!");

        if (Objects.equals(jogadorAtual, "X")) {
            System.out.println("Entrou no Object atual jogador X -->");
            if(Objects.equals(pacote.get("simbolo1"), "1")){
                System.out.println("Enviando para jogador 1");
                e = MensagemParaJ1(pacote);
            }else if (Objects.equals(pacote.get("simbolo2"), "1")){
                System.out.println("Enviando para jogador 2");
                e = MensagemParaJ2(pacote);
            }
        } else if (Objects.equals(jogadorAtual, "O")) {
            System.out.println("Entrou no Object atual jogador O --> ");
            if(Objects.equals(pacote.get("simbolo1"), "-1")){
                System.out.println("Enviando para jogador 1");
                e = MensagemParaJ1(pacote);
            } else if (Objects.equals(pacote.get("simbolo2"), "-1")) {
                System.out.println("Enviando para jogador 2");
                e = MensagemParaJ2(pacote);
            }
        }

        System.out.println(pacote.get("nome1") + " -> " + pacote.get("simbolo1"));
        System.out.println(pacote.get("nome2") + " -> " + pacote.get("simbolo2"));

        return e;
    }

    private String QualSimbolo(Map<String, String> p){
        if(Objects.equals(p.get("simbolo"), "1")){
            return "X";
        }else if(Objects.equals(p.get("simbolo"), "-1")){
            return "O";
        }
        return "-";
    }


}