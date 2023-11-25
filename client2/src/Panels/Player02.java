package Panels;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.List;

public class Player02 {
    private JLabel lblAviso;
    private JLabel lblNomeJogador;
    private JButton button11;
    private JButton button12;
    private JButton button13;
    private JButton button21;
    private JButton button22;
    private JButton button23;
    private JButton button31;
    private JButton button32;
    private JButton button33;
    private JPanel panelP2;

    private String Simbolo;
    private String NomeP2;

    JButton[] ListaDeBotoesFisicos = {
            button11, button12, button13,
            button21, button22, button23,
            button31, button32, button33
    };
    String[] ListaDeBotoes = {
            "11", "12", "13",
            "21", "22", "23",
            "31", "32", "33"
    };

    List<String> BotoesApertdos = new ArrayList<>();

    public Player02() {

        DesabilitaHabilitaBotoes(false);

        button11.addActionListener(ActionEvent -> {FazJogada("1", "1","Sua Vez", Simbolo); button11.setEnabled(false);});
        button12.addActionListener(ActionEvent -> {FazJogada("1", "2","Sua Vez", Simbolo); button12.setEnabled(false);});
        button13.addActionListener(ActionEvent -> {FazJogada("1", "3","Sua Vez", Simbolo); button13.setEnabled(false);});
        button21.addActionListener(ActionEvent -> {FazJogada("2", "1","Sua Vez", Simbolo); button21.setEnabled(false);});
        button22.addActionListener(ActionEvent -> {FazJogada("2", "2","Sua Vez", Simbolo); button22.setEnabled(false);});
        button23.addActionListener(ActionEvent -> {FazJogada("2", "3","Sua Vez", Simbolo); button23.setEnabled(false);});
        button31.addActionListener(ActionEvent -> {FazJogada("3", "1","Sua Vez", Simbolo); button31.setEnabled(false);});
        button32.addActionListener(ActionEvent -> {FazJogada("3", "2","Sua Vez", Simbolo); button32.setEnabled(false);});
        button33.addActionListener(ActionEvent -> {FazJogada("3", "3","Sua Vez", Simbolo); button33.setEnabled(false);});

        // ----------------> Recebe os dados Iniciais.

        boolean recebeuDados = RecebeDadosIniciais();
        if (recebeuDados){
            System.out.println("recebeu os dados do player 2 com sucesso!");
        }else{
            System.out.println("SEM sucesso ao receber os dados do player 2!");
        }



        // Tread de input Cliente 2
        new Thread(() -> {
            try {
                Map<String, String> pacote_recebimento;

                // Recebendo mensagem do Middleware de C1
                while(true){

                    ServerSocket SSc2 = new ServerSocket(3001); // Server que fica aberto para receber dados de C1 -> MW -> C2(atual)
                    Socket c2 = SSc2.accept();
                    ObjectInputStream obj_M_c2 = new ObjectInputStream(c2.getInputStream());
                    pacote_recebimento = (Map<String, String>) obj_M_c2.readObject();

                    if(!pacote_recebimento.isEmpty()){

                        if( Objects.equals(pacote_recebimento.get("aviso"), "Sua Vez") || Objects.equals(pacote_recebimento.get("aviso"), "Você Perdeu")){

                            DesabilitaHabilitaBotoes(true);

                            if(Objects.equals(pacote_recebimento.get("aviso"), "Sua Vez")){
                                System.out.println("Entrou na sua Vez!");
                                MarcaJogada(pacote_recebimento);
                                DefineStatusDoBotao(pacote_recebimento);

                                lblAviso.setText(pacote_recebimento.get("aviso"));
                            }

                            if(Objects.equals(pacote_recebimento.get("aviso"), "Você Ganhou")){
                                DesabilitaHabilitaBotoes(false);
                                lblAviso.setText(pacote_recebimento.get("aviso"));
                            }
                        }else{
                            System.out.println("Aviso enviando para C2,  não correspondente");
                        }
                    }else{
                        System.out.println("Recebimento de pacote Nulo ao C2!");
                    }


                    obj_M_c2.close();
                    c2.close();
                    SSc2.close();
                }

            }catch (Exception ex){
                System.out.println("Erro na ThreadInputC2- " + ex.getMessage());
            }
        }).start();
    }

    private void DesabilitaHabilitaBotoes(boolean cod){
        if(cod){
            for(String b : ListaDeBotoes){
                // se da lista de botoes, o botão não estiver sido apertado antes, poderá ser clicado para futura jogada.
                if(!BotoesApertdos.contains(b)){
                    switch (b) {
                        case "11" -> button11.setEnabled(cod);
                        case "12" -> button12.setEnabled(cod);
                        case "13" -> button13.setEnabled(cod);
                        case "21" -> button21.setEnabled(cod);
                        case "22" -> button22.setEnabled(cod);
                        case "23" -> button23.setEnabled(cod);
                        case "31" -> button31.setEnabled(cod);
                        case "32" -> button32.setEnabled(cod);
                        case "33" -> button33.setEnabled(cod);
                    };
                }
            }
        }else{
            for(JButton b : ListaDeBotoesFisicos){
                b.setEnabled(cod);
            }
        }
    }

    private void FazJogada(String linha, String coluna, String aviso, String simbolo) {
        // Cria pacote
        Map<String, String> pacote_envio = new HashMap<>();
        pacote_envio.put("linha", linha);
        pacote_envio.put("coluna", coluna);
        pacote_envio.put("aviso", aviso);
        pacote_envio.put("simbolo", simbolo);

        try {
            boolean control = true;
            while(control){
                Socket c2 = new Socket("127.0.0.1", 3000);

                ObjectOutputStream obj_c2_M = new ObjectOutputStream(c2.getOutputStream());

                if(ValidaJogada(pacote_envio)){
//                    DefineStatusDoBotao(pacote_envio);
                    MarcaJogada(pacote_envio);
                    obj_c2_M.writeObject(pacote_envio); //Envia para o middleware
                    DesabilitaHabilitaBotoes(false);
                    lblAviso.setText("Aguarde a Sua Vez Novamente!");
                    control = false;
                }else {
                    System.out.println("Jogada não pode ser validada!");
                    control = false;
                }

                pacote_envio = null;

                obj_c2_M.close();
                c2.close();
            }

            System.out.println("Saiu do loop de envio da jogada");
        }catch (Exception ex) {
            System.out.println("Erro o envio da jogada - " + ex.getMessage());
        }
    }

    public void mostraMensagem(Map<String, String> pacote) {
        String aviso = pacote.get("aviso");
        if (Objects.equals(aviso, "Você Ganhou!")) {
            lblAviso.setText("Você Venceu!!!");
            lblAviso.setForeground(Color.GREEN);
        } else if (Objects.equals(aviso, "Você Perdeu!")) {
            lblAviso.setText("Você Perdeu!");
            lblAviso.setForeground(Color.RED);
        } else if (Objects.equals(aviso, "Deu Velha!")) {
            lblAviso.setText("Ih, Deu Velha!");
            lblAviso.setForeground(Color.MAGENTA);
        } else if (Objects.equals(aviso, "ERRO")) {
            JOptionPane.showMessageDialog(null, "Vish, Deu algum Erro.");
        }
    }

    private String QualSimbolo(String simb) {
        if (Objects.equals(simb, "1")) {
            return "X";
        } else if (Objects.equals(simb, "-1")) {
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

    private void DefineStatusDoBotao(Map<String, String> pacote) {
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
        }
        ;
    }

    private void MarcaJogada(Map<String, String> pacote) {
        String linha = pacote.get("linha");
        String coluna = pacote.get("coluna");
        String botao = linha + coluna;
        String simbolo = QualSimbolo(pacote.get("simbolo"));

        switch (botao) {
            case "11":
                button11.setText(simbolo);
                BotoesApertdos.add(botao);
                break;
            case "12":
                button12.setText(simbolo);
                BotoesApertdos.add(botao);
                break;
            case "13":
                button13.setText(simbolo);
                BotoesApertdos.add(botao);
                break;
            case "21":
                button21.setText(simbolo);
                BotoesApertdos.add(botao);
                break;
            case "22":
                button22.setText(simbolo);
                BotoesApertdos.add(botao);
                break;
            case "23":
                button23.setText(simbolo);
                BotoesApertdos.add(botao);
                break;
            case "31":
                button31.setText(simbolo);
                BotoesApertdos.add(botao);
                break;
            case "32":
                button32.setText(simbolo);
                BotoesApertdos.add(botao);
                break;
            case "33":
                button33.setText(simbolo);
                BotoesApertdos.add(botao);
                break;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Jogador 2");
        frame.setContentPane(new Player02().panelP2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private boolean RecebeDadosIniciais() {
        try {
            Map<String, String> msg;
            boolean control = true;
            // Recebendo mensagem do Middleware.
            while (control) {
                ServerSocket SSc2 = new ServerSocket(5555);
                System.out.println("Recebendo dados do Player 2");
                Socket c2 = SSc2.accept();
                System.out.println("RecebeU dados do Player 2");
                control = false;
                ObjectInputStream obj_M_c2 = new ObjectInputStream(c2.getInputStream());
                msg = (Map<String, String>) obj_M_c2.readObject();

                NomeP2 = msg.get("nome");
                Simbolo = msg.get("simbolo");
                lblNomeJogador.setText(NomeP2);

                if (Objects.equals(msg.get("aviso"), "Você Começa")) {
                    DesabilitaHabilitaBotoes(true);
                    lblAviso.setText(msg.get("aviso"));
                } else if (Objects.equals(msg.get("aviso"), "Espere sua Vez")) {
                    DesabilitaHabilitaBotoes(false);
                    lblAviso.setText(msg.get("aviso"));
                }

                obj_M_c2.close();
                c2.close();
                SSc2.close();
            }

            System.out.println("Saiu do loop de recebimento do player 2");
            return true;
        } catch (Exception ex) {
            System.out.println("Erro no recebimento de dados iniciais C2 - " + ex.getMessage());
        }

        return false;
    }
}