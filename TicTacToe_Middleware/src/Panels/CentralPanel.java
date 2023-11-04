package Panels;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CentralPanel extends JFrame {
    private JTextField inputP1_mw;
    private JTextField inputP2_mw;
    private JLabel lblTitle_mw;
    private JLabel lblP1_mw;
    private JLabel lblP2_mw;
    private JButton btnIniciarP1;
    private JLabel lblExpectador_mw;
    public JPanel centralPanel;
    private JLabel lblNameP1_mw = null;
    private JLabel lblNameP2_mw = null;
    private JButton btnIniciarP2;
    private JButton btnIniciarExpectador;
}
