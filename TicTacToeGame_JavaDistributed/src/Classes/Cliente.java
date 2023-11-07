package Classes;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class Cliente {
    private final int porta;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket client;
    public Cliente(int porta) throws IOException {
        this.porta = porta;
        IniciaCliente();
    }

    private void IniciaCliente() throws IOException {
        client = new Socket("127.0.0.1", porta);
        input = new ObjectInputStream(client.getInputStream());
        output = new ObjectOutputStream(client.getOutputStream());
    }

    private void FinalizaCliente() throws IOException {
        input.close();
        output.close();
    }

    public Map<String, String> RecebeMapPacote() throws IOException, ClassNotFoundException {
        return (Map<String, String>) input.readObject();
    }

    public void EnviaMapPacote(Map<String, String> pacote) throws IOException {
        output.writeObject(pacote);
    }

    public void FechaCliente() throws IOException {
        input.close();
        output.close();
        client.close();
    }

}
