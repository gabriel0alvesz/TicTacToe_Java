package Panels;



import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
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

    private String Simbolo;
    private String NomeP1;

    Map<String, String> pacote_envio;
    Map<String, String> pacote_recebimento;
    JButton[] ListaDeBotoes = {
            button11, button12, button13,
            button21, button22, button23,
            button31, button32, button33
    };

    private int contador = 0;
    public Player01(){

        DesabilitaHabilitaBotoes(false);
        pacote_envio = new HashMap<>();
        pacote_recebimento = new HashMap<>();

        button11.addActionListener(ActionEvent -> {pacote_envio.put("linha", "1"); pacote_envio.put("coluna", "1");pacote_envio.put("aviso", "Sua Vez"); pacote_envio.put("simbolo", Simbolo); button11.setText(QualSimbolo(Simbolo));});

// ----------------> Recebe os dados Iniciais.
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
                    NomeP1 = msg.get("nome");
                    Simbolo = msg.get("simbolo");
                    lblNomeJogador.setText(NomeP1);

                    if(Objects.equals(msg.get("aviso"), "Você Começa")){
                        DesabilitaHabilitaBotoes(true);
                        lblAviso.setText(msg.get("aviso"));
                    }else if (Objects.equals(msg.get("aviso"), "Espere sua Vez")){
                        DesabilitaHabilitaBotoes(false);
                    }

                    obj_M_c1.close();
                    c1.close();
                    break;
                }

            }catch (Exception ex){
                System.out.println("Erro na ThInput C1 - " + ex.getMessage());
            }
        }).start();


        // Tread de input Cliente 1
        new Thread(() -> {
            try {
                ServerSocket SSc1 = new ServerSocket(2001);
                Map<String, String> msg;
                // Recebendo mensagem do Middleware.
                while(true){

                    Socket c1 = SSc1.accept();
                    ObjectInputStream obj_M_c1 = new ObjectInputStream(c1.getInputStream());
                    pacote_recebimento = (Map<String, String>) obj_M_c1.readObject();

                    if(!pacote_recebimento.isEmpty()){

                        if(Objects.equals(pacote_recebimento.get("aviso"), "Você Começa") || Objects.equals(pacote_recebimento.get("aviso"), "Sua Vez")
                        || Objects.equals(pacote_recebimento.get("aviso"), "Você Ganhou")){

                            DesabilitaHabilitaBotoes(true);

                            if(Objects.equals(pacote_recebimento.get("aviso"), "Sua Vez")){
                                MarcaJogada(pacote_recebimento);
                                DefineStatusDoBotao(pacote_recebimento);
                                lblAviso.setText(pacote_envio.get("aviso"));
                                pacote_recebimento = null;
                            }

                            if(Objects.equals(pacote_recebimento.get("aviso"), "Você Ganhou")){
                                DesabilitaHabilitaBotoes(false);
                                lblAviso.setText(pacote_envio.get("aviso"));
                            }
                        }else{
                            System.out.println("Aviso enviando ao C1,  não correspondente");
                        }
                    }else{
                        System.out.println("Recebimento de pacote Nulo ao C1!");
                    }

                    obj_M_c1.close();
                    c1.close();
                }

            }catch (Exception ex){
                System.out.println("Erro na ThInput C1 - " + ex.getMessage());
            }
        }).start();

        // Thread de output do Cliente 1
        new Thread(() -> {
            try {
                while(true){
                    Socket c1 = new Socket("127.0.0.1", 2000);

                    ObjectOutputStream obj_c1_M = new ObjectOutputStream(c1.getOutputStream());

                    if(ValidaJogada(pacote_envio)){
                        DefineStatusDoBotao(pacote_envio);
                        obj_c1_M.writeObject(pacote_envio); // parametro que deve ser sempre atualizado para ser enviado.
                    }else {
                        System.out.println("Jogada não pode ser validada!");
                    }

                    obj_c1_M.close();
                    c1.close();

                    pacote_envio = null;
                }
            }catch (Exception ex){
                System.out.println("Erro na ThOutput C1 - " + ex.getMessage());
            }
        }).start();

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

    private String QualSimbolo(String simb){
        if(Objects.equals(simb, "1")){
            return "X";
        }else if(Objects.equals(simb, "-1")){
            return "O";
        }
        return "-";
    }

    private boolean ValidaJogada(Map<String, String> pacote) {
        String linha = pacote.get("linha");
        String coluna = pacote.get("coluna");
        String botao = linha + coluna;

        return switch (botao) {
            case "11" -> Objects.equals(button11.getText(), "-");
            case "12" -> Objects.equals(button12.getText(), "-");
            case "13" -> Objects.equals(button13.getText(), "-");
            case "21" -> Objects.equals(button21.getText(), "-");
            case "22" -> Objects.equals(button22.getText(), "-");
            case "23" -> Objects.equals(button23.getText(), "-");
            case "31" -> Objects.equals(button31.getText(), "-");
            case "32" -> Objects.equals(button32.getText(), "-");
            case "33" -> Objects.equals(button33.getText(), "-");
            default -> false;
        };
    }

    private void DefineStatusDoBotao(Map<String, String> pacote){
        String linha = pacote.get("linha");
        String coluna = pacote.get("coluna");
        String botao = linha + coluna;

        switch (botao) {
            case "11" -> button11.setEnabled(false);
            case "12" -> button12.setEnabled(false);
            case "13" -> button13.setEnabled(false);
            case "21" -> button21.setEnabled(false);
            case "22" -> button22.setEnabled(false);
            case "23" -> button23.setEnabled(false);
            case "31" -> button31.setEnabled(false);
            case "32" -> button32.setEnabled(false);
            case "33" -> button33.setEnabled(false);
        };
    }

    private void MarcaJogada(Map<String, String> pacote) {
        String linha = pacote.get("linha");
        String coluna = pacote.get("coluna");
        String botao = linha + coluna;
        String simbolo = QualSimbolo(pacote.get("simbolo"));

        switch (botao) {
            case "11" -> button11.setText(simbolo);
            case "12" -> button12.setText(simbolo);
            case "13" -> button13.setText(simbolo);
            case "21" -> button21.setText(simbolo);
            case "22" -> button22.setText(simbolo);
            case "23" -> button23.setText(simbolo);
            case "31" -> button31.setText(simbolo);
            case "32" -> button32.setText(simbolo);
            case "33" -> button33.setText(simbolo);
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
