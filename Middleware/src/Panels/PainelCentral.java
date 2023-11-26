package Panels;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
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

    private Map<String, String> pacote_env_final1 = new HashMap<>();
    private Map<String, String> pacote_env_final2 = new HashMap<>();

    private Map<String,String> DadosApostadores = new HashMap<>();

    private int contador = 0;

    private boolean jogo_comecou = false;

    public PainelCentral() {

        btnIniciarJogo.setEnabled(false);
//        btnIniciarExpectador.setEnabled(false);
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

// -----------------> Inicia o Jogo, Middleware esta aqui!
        btnIniciarJogo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jogo_comecou = true;
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

                                    // Verifica se houve Velha ou se ouve ganhador
                                    if(Objects.equals(msg_retorno,"Deu Velha") || Objects.equals(msg_retorno,"Você Ganhou")){

                                        if(Objects.equals(msg_retorno,"Deu Velha")){
                                            pacote_env_final1.put("Velha", "Deu Velha");
                                        }

                                        if(Objects.equals(msg_retorno,"Você Ganhou")){
                                            pacote_env_final1.put("GanhadorPerdedor", "Sim");
                                            pacote_env_final1.put("Ganhador","C1");
                                        }

                                        PacoteFinalParaJogadores(pacote_env_final1, msg_c1_mw.get("linha"), msg_c1_mw.get("coluna"), msg_c1_mw.get("simbolo"));
                                        atualizaJogadaParaExpectadores(msg_c1_mw.get("linha"), msg_c1_mw.get("coluna"), msg_c1_mw.get("simbolo"),true);
                                        JOptionPane.showMessageDialog(null, "Middleware: Fim de Jogo!");
                                        break;
                                    }

                                    if(Objects.equals((msg_retorno), "Sua Vez")){
                                        System.out.println("\nC1 -> C2");
                                        printMatrix(Tabuleiro);
                                        Socket env = new Socket("127.0.0.1", 3001); // abre para enviar para C2

                                        ObjectOutputStream obj_env = new ObjectOutputStream(env.getOutputStream());
                                        obj_env.flush();
                                        obj_env.writeObject(msg_c1_mw);

                                        atualizaJogadaParaExpectadores(msg_c1_mw.get("linha"), msg_c1_mw.get("coluna"), msg_c1_mw.get("simbolo"),false);
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


// --------------------> Recebe de C2 e envia para C1
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

                                    // Verifica se houve Velha ou se ouve ganhador
                                    if(Objects.equals(msg_retorno,"Deu Velha") || Objects.equals(msg_retorno,"Você Ganhou")){

                                        System.out.println("Entrou no envio do pacote Final");

                                        if(Objects.equals(msg_retorno,"Deu Velha")){
                                            pacote_env_final2.put("Velha", "Deu Velha");
                                        }

                                        if(Objects.equals(msg_retorno,"Você Ganhou")){
                                            pacote_env_final2.put("GanhadorPerdedor", "Sim");
                                            pacote_env_final2.put("Ganhador","C2");
                                        }

                                        PacoteFinalParaJogadores(pacote_env_final2, msg_c2_mw.get("linha"), msg_c2_mw.get("coluna"), msg_c2_mw.get("simbolo"));
                                        atualizaJogadaParaExpectadores(msg_c2_mw.get("linha"), msg_c2_mw.get("coluna"), msg_c2_mw.get("simbolo"), true);
                                        JOptionPane.showMessageDialog(null, "Middleware: Fim de Jogo!");
                                        break;
                                    }

                                    if(Objects.equals((msg_retorno), "Sua Vez")){
                                        System.out.println("\nC2 -> C1");
                                        printMatrix(Tabuleiro);

                                        Socket env = new Socket("127.0.0.1", 2001);

                                        ObjectOutputStream obj_env = new ObjectOutputStream(env.getOutputStream());
                                        obj_env.flush();
                                        obj_env.writeObject(msg_c2_mw);

                                        atualizaJogadaParaExpectadores(msg_c2_mw.get("linha"), msg_c2_mw.get("coluna"), msg_c2_mw.get("simbolo"), false);
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

        // Expectador via UDP
        btnIniciarExpectador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                btnIniciarExpectador.setText("Iniciado!");
                btnIniciarExpectador.revalidate();
                btnIniciarExpectador.repaint();
                btnIniciarExpectador.setEnabled(false);

                // Recebe os dados dos apostadores
                new Thread(() -> {
                    while(!jogo_comecou) {
                        try {

                            InetAddress address = InetAddress.getByName("239.0.0.1");
                            InetSocketAddress group = new InetSocketAddress(address, 6666);

                            NetworkInterface nif = NetworkInterface.getByName("lo");

                            MulticastSocket multi = new MulticastSocket(group.getPort());

                            multi.joinGroup(group, nif);

                            byte[] msg = new byte[256];
                            DatagramPacket dP = new DatagramPacket(msg, msg.length);
                            multi.receive(dP);

                            String pacote_recebido = new String(dP.getData()).trim();

                            String[] tokens = pacote_recebido.split(";");
                            if(!Objects.equals(tokens[1],"#")){
                                DadosApostadores.put(tokens[0],tokens[1]); //Nome, simbolo apostado
                            }
//                            System.out.println("Apostador: " + tokens[0] + " apostou no: " + tokens[1]);

                            multi.close();
                        } catch (Exception ex) {
                            System.out.println("Erro no cliente: " + ex.getMessage());
                        }
                    }

                    System.out.println("NÃO PODE ENTRAR MAIS EXPECTADORES, o jogo começou!");
                }).start();
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

        if(contador == 8){
            return "Deu Velha";
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

    private void PacoteFinalParaJogadores(Map<String, String> DadosJogadores, String linha, String coluna, String simbolo){
        Map<String, String> pacote_env = new HashMap<>();

        pacote_env.put("linha", linha);
        pacote_env.put("coluna", coluna);
        pacote_env.put("simbolo", simbolo);

        if(Objects.equals(DadosJogadores.get("Velha"),"Deu Velha")){
            System.out.println("---> Deu Velha!");
            pacote_env.put("AvisoFinalC1", "Deu Velha");
            pacote_env.put("AvisoFinalC2", "Deu Velha");
        }

        if(Objects.equals(DadosJogadores.get("GanhadorPerdedor"),"Sim")){
            System.out.println("Entrou no GanhadorPerdedor");
            if(Objects.equals(DadosJogadores.get("Ganhador"),"C1")){
                pacote_env.put("AvisoFinalC1", "Você Ganhou");
                pacote_env.put("AvisoFinalC2", "Você Perdeu");
            }else if(Objects.equals(DadosJogadores.get("Ganhador"),"C2")){
                pacote_env.put("AvisoFinalC1", "Você Perdeu");
                pacote_env.put("AvisoFinalC2", "Você Ganhou");
            }
        }

        try{
            Socket env_C1 = new Socket("127.0.0.1",2001);
            Socket env_C2 = new Socket("127.0.0.1",3001);

            ObjectOutputStream objC1 = new ObjectOutputStream(env_C1.getOutputStream());
            ObjectOutputStream objC2 = new ObjectOutputStream(env_C2.getOutputStream());

            // Envia o pacote para ambos jogadores
            objC1.writeObject(pacote_env);
            objC2.writeObject(pacote_env);

            // Fecha tudo!
            objC1.close();
            objC2.close();
            env_C1.close();
            env_C2.close();
        }catch (Exception ex){
            System.out.println("Erro no envio do pacote final: " + ex.getMessage());
        }

    }

    private static void printHashMap(Map<String, String> map) {
        System.out.println("Printando a hash Final");
        for (String chave : map.keySet()) {
            String valor = map.get(chave);
            System.out.println("Chave: " + chave + ", Valor: " + valor);
        }
    }

    private void atualizaJogadaParaExpectadores(String linha, String coluna, String simbolo, boolean ganhou){
        String posicao = linha+coluna;
        String msg_envio = new String("posicao;"+posicao+";"+simbolo+";");

        if(ganhou){
            msg_envio = msg_envio + "Ganhadores;";
            msg_envio = VerificaGanhadoresEPremio(msg_envio,simbolo);
        }

        // Enviando para os espectadores
        try{
            byte[] msg = msg_envio.getBytes();

            InetAddress address = InetAddress.getByName("239.0.0.1");

            DatagramSocket ds = new DatagramSocket();
            DatagramPacket pkg = new DatagramPacket(msg, msg.length, address, 9999);

            ds.send(pkg);
            ds.close();

        }catch (Exception ex){
            System.out.println("Erro ao enviar dados de atulização para os expectadores: " + ex.getMessage());
        }
    }

    private String VerificaGanhadoresEPremio(String msg, String simbolo){

        StringBuilder msgBuilder = new StringBuilder(msg);

        int cont = 0;
        int cont_ganhadores = 0;

        for (String chave : DadosApostadores.keySet()) {
            String valor = DadosApostadores.get(chave);

            if(Objects.equals(valor, simbolo)){
                msgBuilder.append(chave).append(";");
                cont_ganhadores++;
            }
            cont++;
        }

        // Valor para cada ganhador x 1,25
        int soma = 10*cont;
        double valor_p_ganhador = (soma*1.25)/cont_ganhadores;
        msgBuilder.append("R$").append(valor_p_ganhador);

        return msgBuilder.toString();
    }
}

