package Panels;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class PainelCentral {
    private JPanel CentralPainel;
    private JLabel lblTitle;
    private JButton btnIniciarJogadores;
    private JLabel lblJogadores;
    private JButton btnIniciarExpectador;
    private JLabel lblExpectador;
    private JLabel lblJogador1;
    private JLabel lblNomeJogador1;
    private JLabel lblJogaador2;
    private JLabel lblNomeJogador2;
    private JButton btnSortearSimbolos;
    private JButton btnIniciarJogo;

    private String NomeJogador1, NomeJogador2;
    private String SimboloJ1, SimboloJ2;
    private final int[][] Tabuleiro = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
    };

    private Map<String, String> inicialX = new HashMap<>();
    private Map<String, String> inicialO = new HashMap<>();

    private int contador = 0;

    public PainelCentral() {

        btnIniciarJogo.setEnabled(false);
        btnIniciarExpectador.setEnabled(false);
        btnSortearSimbolos.setEnabled(false);
        btnIniciarJogadores.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                btnIniciarJogadores.setText("Iniciado!");
                btnIniciarJogadores.setBackground(Color.GREEN);
                btnIniciarJogadores.revalidate();
                btnIniciarJogadores.repaint();

                btnIniciarJogadores.setEnabled(false);

                new Thread(() -> {
                    try{
                        ServerSocket rec_NameLogin = new ServerSocket(2222);
                        int i = 0;
                        while(lblNomeJogador1.getText().equals("") || lblNomeJogador2.getText().equals("")){
                            System.out.println("Entrou: " + i);

                            Socket name = rec_NameLogin.accept();

                            ObjectInputStream obj_recNameLogin = new ObjectInputStream(name.getInputStream());
                            String nome_jogador = obj_recNameLogin.readUTF();

                            if(lblNomeJogador1.getText().equals("")){
                                lblNomeJogador1.setText(nome_jogador);
                                NomeJogador1 = lblNomeJogador1.getText();
                            }else {
                                lblNomeJogador2.setText(nome_jogador);
                                NomeJogador2 = lblNomeJogador2.getText();
                                continue;
                            }

                            obj_recNameLogin.close();
                            name.close();
                            i++;
                        }
                        rec_NameLogin.close();
                        System.out.println("Thread Encerrada!");

                    }catch (Exception ex){
                        System.out.println("Erro ao receber login de Competidor: " + ex.getMessage());
                    }
                }).start();
                System.out.println("Saiu da Thread!");

                btnSortearSimbolos.setEnabled(true);
            }
        });

        btnSortearSimbolos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                btnSortearSimbolos.setText("Sorteados!");
                SortearSimbolos();
                System.out.println("Os Simbolos Foram Sorteados");
                btnSortearSimbolos.setEnabled(false);
                btnIniciarJogo.setEnabled(true);
                System.out.println(SimboloJ1 + " / " + SimboloJ2);
                // Apos Isso deve-se abrir os players.
            }
        });

        btnIniciarJogo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                btnIniciarJogo.setText("Começou!");
                btnIniciarJogo.revalidate();
                btnIniciarJogo.repaint();
                btnIniciarJogo.setEnabled(false);

                if(!NomeJogador1.isEmpty()  && !NomeJogador2.isEmpty() && !SimboloJ1.isEmpty() && !SimboloJ2.isEmpty()){

//  ------------------> Envia para cada jogador seus respectivos dados Iniciais
                    // Thread envia jogo inicial para os clientes
                        try{
                            Socket env1 = new Socket("127.0.0.1", 4444);// Envia para C1;
                            ObjectOutputStream obj_env1 = new ObjectOutputStream(env1.getOutputStream());

                            Socket env2 = new Socket("127.0.0.1", 5555); //Envia para c2
                            ObjectOutputStream obj_env2 = new ObjectOutputStream(env2.getOutputStream());

                            boolean control = InicializaJogadores();

                            if(control){

                                obj_env1.flush();
                                obj_env1.writeObject(inicialX);

                                obj_env2.flush();
                                obj_env2.writeObject(inicialO);
                            }else{
                                obj_env1.flush();
                                obj_env1.writeObject(inicialO);

                                obj_env2.flush();
                                obj_env2.writeObject(inicialX);
                            }

                            obj_env1.close(); obj_env2.close();
                            env1.close(); env2.close();

                        }catch (Exception ex){
                            System.out.println("Erro ao enviar dados Iniciais: " + ex.getMessage());
                        }


// ------------------> Recebe de C1 e envia para C2
                    new Thread(() -> {
                        try{
                            ServerSocket SSc1_c2 = new ServerSocket(2000); // recebe de C1
                            while(true){
                                Socket c1_c2 = SSc1_c2.accept();

                                ObjectInputStream obj_rec = new ObjectInputStream(c1_c2.getInputStream());
                                Map<String, String> msg_c1_mw = (Map<String,String>) obj_rec.readObject();

                                if(!msg_c1_mw.isEmpty()){
                                    // LOGICA PARA VERIFICAR A JOGADA
                                    String msg_retorno = VerificaJogada(msg_c1_mw);

                                    if(contador == 8){
                                        JOptionPane.showMessageDialog(null,"Deu Velha!, Fim de Jogo");

                                        break;
                                    }
                                    if(Objects.equals((msg_retorno), "Sua Vez") || Objects.equals((msg_retorno), "Você Ganhou")){
                                        System.out.println("\nC1 -> C2");
                                        printMatrix(Tabuleiro);
                                        Socket env = new Socket("127.0.0.1", 3001); // abre para enviar para C2

                                        if(Objects.equals((msg_retorno), "Você Ganhou")){ // QUem ganhou foi o jogador 1
                                            msg_c1_mw.put("aviso", "Você Perdeu");

                                            JOptionPane.showMessageDialog(null, "O Vencedor foi o Jogador 1" );
                                        }

                                        ObjectOutputStream obj_env = new ObjectOutputStream(env.getOutputStream());
                                        obj_env.flush();
                                        obj_env.writeObject(msg_c1_mw);

                                        // Fechando Objetos e Sockets
                                        obj_env.close();
                                        env.close();
                                    }else{
                                        System.out.println("Erro ao Enviar pacote de C1 -> MW -> C2");
                                    }

                                }else {
                                    System.out.println("Pacote recebido de C1 é null");
                                }
                                obj_rec.close();
                                c1_c2.close();
                            }
                        }catch (Exception ex){
                            System.out.println("ERRO no Middleware (C1->C2) - " + ex.getMessage());
                        }
                    }).start();


// --------------------> Recebe de C2 e envia para c1
                    new Thread(() -> {
                        try{
                            ServerSocket SSc2_c1 = new ServerSocket(3000); // abre para enviar para C1
                            while(true){

                                Socket c2_c1 = SSc2_c1.accept();

                                ObjectInputStream obj_rec = new ObjectInputStream(c2_c1.getInputStream());
                                Map<String, String> msg_c2_mw = (Map<String,String>) obj_rec.readObject();

                                if(!msg_c2_mw.isEmpty()){
                                    // LOGICA PARA VERIFICAR A JOGADA
                                    String msg_retorno = VerificaJogada(msg_c2_mw);

                                    if(contador == 8){
                                        JOptionPane.showMessageDialog(null,"Deu Velha!, Fim de Jogo");
                                    }

                                    if(Objects.equals((msg_retorno), "Sua Vez") || Objects.equals((msg_retorno), "Você Ganhou")){
                                        System.out.println("\nC2 -> C1");
                                        printMatrix(Tabuleiro);
                                        Socket env = new Socket("127.0.0.1", 2001);

                                        if(Objects.equals((msg_retorno), "Você Ganhou")){
                                            msg_c2_mw.put("aviso", "Você Perdeu");
                                            JOptionPane.showMessageDialog(null, "O Vencedor foi o Jogador 2" );
                                        }

                                        ObjectOutputStream obj_env = new ObjectOutputStream(env.getOutputStream());
                                        obj_env.flush();
                                        obj_env.writeObject(msg_c2_mw);

                                        // Fechando Objetos e Sockets
                                        obj_env.close();
                                        env.close();
                                    }else{
                                        System.out.println("Erro ao Enviar pacote de C2 -> MW -> C1");
                                    }

                                }else {
                                    System.out.println("Pacote recebido de C2 é null");
                                }

                                obj_rec.close();
                                c2_c1.close();
                            }
                        }catch (Exception ex){
                            System.out.println("ERRO no Middleware (C2->C1) - " + ex.getMessage());
                        }

                    }).start();
                }else{
                    System.out.println("Erro ao Iniciar jogo");
                }
            }
        });
    }


    public static void main(String[] args){
        JFrame frame = new JFrame("Painel Central");
        frame.setContentPane(new PainelCentral().CentralPainel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

// -------------> Funções
    private void SortearSimbolos(){
        Random random = new Random();
        int numero = random.nextInt(2); // Gera 0 ou 1

        if(numero == 1){
            SimboloJ1 = "1";
            SimboloJ2 = "-1";
            lblNomeJogador1.setText(NomeJogador1 + "  (X)");
            lblNomeJogador2.setText(NomeJogador2 + "  (O)");
        }else if(numero == 0){
            SimboloJ2 = "1";
            SimboloJ1 = "-1";
            lblNomeJogador2.setText(NomeJogador2 + "  (X)");
            lblNomeJogador1.setText(NomeJogador1 + "  (O)");
        }
    }

    private boolean InicializaJogadores(){
        if(Objects.equals(SimboloJ1, "1")){
            System.out.println("Entrou no Jogador 1 = X");
            inicialX.put("nome", NomeJogador1);
            inicialX.put("linha", "-1");
            inicialX.put("coluna", "-1");
            inicialX.put("simbolo", SimboloJ1);
            inicialX.put("aviso", "Você Começa");

            inicialO.put("nome", NomeJogador2);
            inicialO.put("linha", "-1");
            inicialO.put("coluna", "-1");
            inicialO.put("simbolo", SimboloJ2);
            inicialO.put("aviso", "Espere sua Vez");

            return true;
        }else if(Objects.equals(SimboloJ2, "1")){
            System.out.println("Entrou no Jogador 2 = X");
            inicialX.put("nome", NomeJogador2);
            inicialX.put("linha", "-1");
            inicialX.put("coluna", "-1");
            inicialX.put("simbolo", SimboloJ2);
            inicialX.put("aviso", "Você Começa");

            inicialO.put("nome", NomeJogador1);
            inicialO.put("linha", "-1");
            inicialO.put("coluna", "-1");
            inicialO.put("simbolo", SimboloJ1);
            inicialO.put("aviso", "Espere sua Vez");
            return false;
        }

        System.out.println("Recebeu False!");
        return false;
    }

    public String VerificaJogada(Map<String, String> pacote) {
        int c = Integer.parseInt(pacote.get("coluna")) - 1;
        int l = Integer.parseInt(pacote.get("linha")) - 1;
        int s = Integer.parseInt(pacote.get("simbolo"));
        int somaLinha = 0;
        int somaColuna = 0;
        int somaDiagonalP = 0;
        int somaDiagonalS = 0;

        if (Tabuleiro[l][c] == 0) {
            Tabuleiro[l][c] = s;
            contador++;
        } else {
            return "ERRO";
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                somaLinha += Tabuleiro[i][j];
                somaColuna += Tabuleiro[j][i];
            }
            somaDiagonalP += Tabuleiro[i][i];
            somaDiagonalS += Tabuleiro[i][2 - i];
            if (somaLinha == 3
                    || somaLinha == -3
                    || somaColuna == 3
                    || somaColuna == -3
                    || somaDiagonalP == 3
                    || somaDiagonalP == -3
                    || somaDiagonalS == 3
                    || somaDiagonalS == -3) {
                return "Você Ganhou";
            }

            somaLinha = 0;
            somaColuna = 0;
        }
        return "Sua Vez";
    }

    private void printMatrix(int[][] matrix) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
