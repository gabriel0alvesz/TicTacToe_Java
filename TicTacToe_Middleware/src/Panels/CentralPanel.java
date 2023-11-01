package Panels;
import Classes.Until;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CentralPanel extends JFrame {
    private JTextField inputP1_mw;
    private JTextField inputP2_mw;
    private JLabel lblTitle_mw;
    private JLabel lblP1_mw;
    private JLabel lblP2_mw;
    private JButton btnIniciarPlayers;
    private JLabel lblExpectador_mw;
    private JButton btnIniciarExpect;
    public JPanel centralPanel;

    public CentralPanel(Until obj) {
        btnIniciarPlayers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(inputP1_mw.getText() != null && inputP2_mw.getText() != null){
                    obj.p1 = inputP1_mw.getText();
                    obj.p2 = inputP2_mw.getText();

                    JOptionPane.showMessageDialog(null, obj.p1 + "/" + obj.p2);
                }
            }
        });
    }

//    public static void main(String[] args){
//
//        Until obj_until = new Until();
//        CentralPanel lp = new CentralPanel(obj_until);
//        lp.setContentPane(lp.centralPanel);
//        lp.setTitle("Login User Desktop");
//        lp.setSize(400, 200);
//        lp.setVisible(true);
//    }

}
