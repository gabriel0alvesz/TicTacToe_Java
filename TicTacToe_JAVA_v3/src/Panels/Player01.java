package Panels;



import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;

public class Player01 {
    private JLabel lblNomeJogador;
    private JLabel lblAviso;
    private JButton button11;
    private JButton button12;
    private JButton button13;
    private JButton button21;
    private JButton button22;
    private JButton button23;
    private JButton button31;
    private JButton button32;
    private JButton button33;
    private JPanel panelP1;

    JButton[] ListaDeBotoes = {
            button11, button12, button13,
            button21, button22, button23,
            button31, button32, button33
    };
    private Map<String, String> inicial = null;
    public Player01(){

        // recebe dados iniciais
        new Thread(() -> {
            try {
                ServerSocket SSc1 = new ServerSocket(4444);
                Map<String, String> msg;
                // Recebendo mensagem do Middleware.
                while(true){

                    Socket c1 = SSc1.accept();
                    System.out.println("Recebendo dados do Player 1");
                    ObjectInputStream obj_M_c1 = new ObjectInputStream(c1.getInputStream());
                    msg = (Map<String, String>) obj_M_c1.readObject();

                    lblNomeJogador.setText(msg.get("nome"));

                    if(Objects.equals(msg.get("aviso"), "Você Começa")){
                        DesabilitaHabilitaBotoes(true);
                        lblAviso.setText(msg.get("aviso"));
                    }else if (Objects.equals(msg.get("aviso"), "Espere sua Vez")){
                        DesabilitaHabilitaBotoes(false);
                    }

                    obj_M_c1.close();
                    c1.close();
                }

            }catch (Exception ex){
                System.out.println("Erro na ThInput C1 - " + ex.getMessage());
            }
        }).start();

        DesabilitaHabilitaBotoes(false);

        // Tread de input Cliente 1
        new Thread(() -> {
            try {
                ServerSocket SSc1 = new ServerSocket(2001);
                Map<String, String> msg;
                // Recebendo mensagem do Middleware.
                while(true){

                    Socket c1 = SSc1.accept();
                    ObjectInputStream obj_M_c1 = new ObjectInputStream(c1.getInputStream());
                    msg = (Map<String, String>) obj_M_c1.readObject();

                    lblNomeJogador.setText(msg.get("nome"));

                    if(Objects.equals(msg.get("aviso"), "Você Começa")){
                        DesabilitaHabilitaBotoes(true);
                        lblAviso.setText(msg.get("aviso"));
                    }else if (Objects.equals(msg.get("aviso"), "Espere sua Vez")){
                        DesabilitaHabilitaBotoes(false);
                    }

                    obj_M_c1.close();
                    c1.close();
                }

            }catch (Exception ex){
                System.out.println("Erro na ThInput C1 - " + ex.getMessage());
            }
        }).start();

        //Thread de output do Cliente 1
//        new Thread(() -> {
//            try {
//                while(true){
//                    Socket c1 = new Socket("127.0.0.1", 2000);
//
//                    ObjectOutputStream obj_c1_M = new ObjectOutputStream(c1.getOutputStream());
//                    Map<String, String> jogada_temp; // parametro que deve ser sempre atualizado para ser enviado.
//
////                    obj_c1_M.writeObject(jogada_temp);
//                    obj_c1_M.close();
//                    c1.close();
//                }
//            }catch (Exception ex){
//                System.out.println("Erro na ThOutput C1 - " + ex.getMessage());
//            }
//        }).start();

    }

    private void DesabilitaHabilitaBotoes(boolean cod){

        for(JButton b : ListaDeBotoes){
            b.setEnabled(cod);
        }
    }

    private void FazJogada(Map<String, String> pacote) {

    }
    public void mostraMensagem(Map<String,String> pacote) {
        String aviso = pacote.get("aviso");
        if (Objects.equals(aviso, "Você Ganhou!")) {
            lblAviso.setText("Você Venceu!!!");
            lblAviso.setForeground(Color.GREEN);
        } else if (Objects.equals(aviso, "Você Perdeu!")) {
            lblAviso.setText("Você Perdeu!");
            lblAviso.setForeground(Color.RED);
        } else if (Objects.equals(aviso, "Deu Velha!")){
            lblAviso.setText("Ih, Deu Velha!");
            lblAviso.setForeground(Color.MAGENTA);
        } else if (Objects.equals(aviso, "ERRO")) {
            JOptionPane.showMessageDialog(null, "Vish, Deu algum Erro.");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Jogador 1");
        frame.setContentPane(new Player01().panelP1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
