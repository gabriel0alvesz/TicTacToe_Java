import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.util.Objects;

public class EspectadorPanel {
    private JPanel panelExpectador;
    private JLabel lbl11;
    private JLabel lbl12;
    private JLabel lbl13;
    private JLabel lbl21;
    private JLabel lbl22;
    private JLabel lbl23;
    private JLabel lbl31;
    private JLabel lbl32;
    private JLabel lbl33;
    private JLabel lblApostdor;
    private JLabel lblApostaEm;
    private JButton btnApostarX;
    private JButton btnApostarO;
    private JButton btnAssistir;
    private JLabel lblAviso;
    private JTextField inputNomeApostador;
    private JLabel lblNomeApostador;

    private String NomeApostador;
    private String Aposta;

    public EspectadorPanel() {
        btnApostarX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Aposta = "1";
                btnApostarX.setBackground(Color.GRAY);
                if(!Objects.equals(inputNomeApostador.getText(), null)){
                    NomeApostador = inputNomeApostador.getText();
                    limpaDesabilitaCampos();
                }

                if(!Objects.equals(NomeApostador, null) && !Objects.equals(Aposta, null)){
                    EnviaAposta(NomeApostador, Aposta);
                }else{
                    System.out.println("erro, alguma variavel vazia!");
                }
            }
        });

        btnApostarO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Aposta = "-1";
                btnApostarO.setBackground(Color.GRAY);
                if(!Objects.equals(inputNomeApostador.getText(), null)){
                    NomeApostador = inputNomeApostador.getText();
                    limpaDesabilitaCampos();
                }

                if(!Objects.equals(NomeApostador, null) && !Objects.equals(Aposta, null)){
                    EnviaAposta(NomeApostador, Aposta);
                }else{
                    System.out.println("erro, alguma variavel vazia!");
                }
            }
        });

        btnAssistir.addActionListener(new ActionListener() { //Apenas para assistir
            @Override
            public void actionPerformed(ActionEvent e) {
                Aposta = "#";
                btnAssistir.setBackground(Color.GRAY);

                if(!Objects.equals(inputNomeApostador.getText(), null)){
                    NomeApostador = inputNomeApostador.getText();
                    limpaDesabilitaCampos();
                }

                if(!Objects.equals(NomeApostador, null) && !Objects.equals(Aposta, null)){
                    EnviaAposta(NomeApostador, Aposta);
                }else{
                    System.out.println("erro, alguma variavel vazia!");
                }

            }
        });

        // Recebe dados de atualizacao para o apostador
        new Thread(() ->{
            while(true) {
                try {
                    InetAddress address = InetAddress.getByName("239.0.0.1");
                    InetSocketAddress group = new InetSocketAddress(address, 9999);

                    NetworkInterface nif = NetworkInterface.getByName("lo");

                    MulticastSocket multi = new MulticastSocket(group.getPort());

                    multi.joinGroup(group, nif);

                    byte[] msg = new byte[256];
                    DatagramPacket dP = new DatagramPacket(msg, msg.length);
                    multi.receive(dP);

                    String pacote_recebido = new String(dP.getData()).trim();
                    String[] tokens = pacote_recebido.split(";");

                    // Esta recebendo dados de atualizacao da partida
                    //tokens = posicao, 11, -1 ou 1
                    if(Objects.equals(tokens[0], "posicao")){
                        PreenchePainelDeJogo(tokens);
                    }

                    if(tokens.length > 3){

                        // tokens = nome1,nome2,nome3, ..., valor_ganho
                        int tam_token = tokens.length - 1; // o ultimo elemento sera o valor ganho, sempre!
                        String valor = tokens[tam_token];
                        PreenchePainelDeJogo(tokens);

                        if(SeraQueVoceGanhou(tokens)){
                            JOptionPane.showMessageDialog(null, "Você Ganhou! - " + valor);
                        }else{
                            JOptionPane.showMessageDialog(null, "Você Perdeu a aposta!");
                        }
                        break;
                    }


                    multi.close();
                } catch (Exception ex) {
                    System.out.println("Erro no cliente: " + ex.getMessage());
                }
            }
        }).start();

    }


    // Enviando via UDP a aposta (Começa aqui!)
    public void EnviaAposta(String nome, String aposta){
        NomeApostador = nome;
        Aposta = aposta;
        lblApostdor.setText(NomeApostador);

        lblAviso.setText("Começou, aguarde as jogadas");
        String pacote = nome + ";" + aposta;
        try{
            byte[] msg = pacote.getBytes();

            InetAddress address = InetAddress.getByName("239.0.0.1");

            DatagramSocket ds = new DatagramSocket();
            DatagramPacket pkg = new DatagramPacket(msg, msg.length, address, 6666);

            ds.send(pkg);
            ds.close();

        }catch (Exception ex){
            System.out.println("Erro ao enviar dados Iniciar de apostador: " + ex.getMessage());
        }
    }

    private void limpaDesabilitaCampos(){
        btnApostarX.setEnabled(false);
        btnApostarO.setEnabled(false);
        btnAssistir.setEnabled(false);
        inputNomeApostador.setText("");
        inputNomeApostador.setEnabled(false);
    }

    // Verifica se o nome do apostar esta entre os ganhadores
    private boolean SeraQueVoceGanhou(String[] vetor_nomes){
        boolean control = false;
        for(String s: vetor_nomes){
            if(s.equals(NomeApostador)){
                control = true;
                return control;
            }
        }
        return control;
    }

    private void PreenchePainelDeJogo(String[] jogadas){
        int i=1;
        switch(jogadas[i]){
            case "11":
                lbl11.setText(QualSimbolo(jogadas[i+1]));
            case "12":
                lbl12.setText(QualSimbolo(jogadas[i+1]));
            case "13":
                lbl13.setText(QualSimbolo(jogadas[i+1]));
            case "21":
                lbl21.setText(QualSimbolo(jogadas[i+1]));
            case "22":
                lbl22.setText(QualSimbolo(jogadas[i+1]));
            case "23":
                lbl23.setText(QualSimbolo(jogadas[i+1]));
            case "31":
                lbl31.setText(QualSimbolo(jogadas[i+1]));
            case "32":
                lbl32.setText(QualSimbolo(jogadas[i+1]));
            case "33":
                lbl33.setText(QualSimbolo(jogadas[i+1]));

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
    public static void main(String[] args){
        JFrame frame = new JFrame("Painel do Apostador");
        frame.setContentPane(new EspectadorPanel().panelExpectador);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        frame.setVisible(true);
    }


}
