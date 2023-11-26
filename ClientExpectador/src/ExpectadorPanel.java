import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Objects;

public class ExpectadorPanel {
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

    public ExpectadorPanel() {
        btnApostarX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Aposta = "1";

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

        // recebe os dados de atualização do jogo
//        new Thread(() -> {
//
//        }).start();
    }


    // Enviando via UDP
    public void EnviaAposta(String nome, String aposta){
        NomeApostador = nome;
        Aposta = aposta;
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


    public static void main(String[] args){
        JFrame frame = new JFrame("Jogador 1");
        frame.setContentPane(new ExpectadorPanel().panelExpectador);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
