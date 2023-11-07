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

    public MiddlewareServer() throws IOException, ClassNotFoundException {
        iniciaServidores();
    }

    private void iniciaServidores() throws IOException {

        ServerSocket servidorP1 = new ServerSocket(7777);
        Player1 = servidorP1.accept();
//        JOptionPane.showMessageDialog(null, "Player 1, conectou!");
        outputP1 = new ObjectOutputStream(Player1.getOutputStream());
        inputP1 = new ObjectInputStream(Player1.getInputStream());

        // Jogador 2
        ServerSocket servidorP2 = new ServerSocket(8888);
        Player2 = servidorP2.accept();
//        JOptionPane.showMessageDialog(null, "Player 2, conectou!");
        outputP2 = new ObjectOutputStream(Player2.getOutputStream());
        inputP2 = new ObjectInputStream(Player2.getInputStream());
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

        if (Objects.equals(jogadorAtual, "X")) {
            e = MensagemParaJ1(pacote);
        } else if (Objects.equals(jogadorAtual, "O")) {
            e = MensagemParaJ2(pacote);
        }
        return e;
    }



}