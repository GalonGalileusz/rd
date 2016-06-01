/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rd;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Dendelion
 */
public final class MyFrame extends JFrame{
    
    private final String[] bcs = new String[]{"Non-periodic", "Periodic"};
    private final String[] locations = new String[]{"Own", "Ranodm", "Evenly", "With ray"};
    private final JPanel contentPane;
    private final Board board;
    private Conditions c;
    
    public MyFrame(){
        
        board = new Board();
        c = board.cond;
        
        setTitle("Recrystallization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());      
        setSize(this.getPrefferredSize());
        
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        
        setContentPane(contentPane);
        
        JPanel panel = new JPanel();
	contentPane.add(panel, BorderLayout.NORTH);
        
        JLabel amountLabel = new JLabel ("Number of grains ");
        panel.add(amountLabel);
        
        JTextField jt = new JTextField("0", 4);
        panel.add(jt);
        
        jt.addActionListener((ActionEvent e) -> {
            c.setAmount(Integer.valueOf(jt.getText()));
            System.out.println("my frame amount "+c.getAmount());
        });
  
        JLabel bcLabel = new JLabel("BC ");
        panel.add(bcLabel);
        
        JComboBox bcCombo = new JComboBox();
        bcCombo.addItemListener((ItemEvent e) -> {
            c.setBC(bcCombo.getSelectedIndex());
        });
        
        bcCombo.setModel(new DefaultComboBoxModel(this.bcs));
        panel.add(bcCombo);
                       
        JLabel locationLabel = new JLabel("Location ");
        panel.add(locationLabel);
        
        JComboBox locationCombo = new JComboBox();
        locationCombo.addItemListener((ItemEvent e) -> {
            c.setLocation(locationCombo.getSelectedIndex());
        });

        locationCombo.setModel(new DefaultComboBoxModel(this.locations));
        panel.add(locationCombo);
        
        JLabel rayLabel = new JLabel ("Ray ");
        panel.add(rayLabel);
        
        JTextField rayTF = new JTextField("0", 4);
        panel.add(rayTF);
        
        rayTF.addActionListener((ActionEvent e) -> {
            c.setRay(Integer.valueOf(rayTF.getText()));
            System.out.println("my frame amount "+c.getRay());
        });  
        
        JButton startButton = new JButton("Start");
        startButton.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e){
                c.setStatus(1);
                System.out.println("Start clicked");
            }
        });
        
        JButton stopButton = new JButton("Stop");
        stopButton.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e){
                c.setStatus(0);
                System.out.println("Stop clicked");
            }
        });
        
        JButton cleanButton = new JButton("Clean");
        cleanButton.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e){
                c.setStatus(2);
                System.out.println("Clean clicked");
            }
        });
        
        panel.add(startButton);
        panel.add(stopButton);
        panel.add(cleanButton);
            
        contentPane.add(board, BorderLayout.CENTER);
        
        new Thread(board).start();   
    } 
    
    public Dimension getPrefferredSize(){
        return new Dimension(800, 800);
    }

}

