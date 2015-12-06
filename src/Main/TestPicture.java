package Main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Base.Game;

import javax.swing.JFrame;

import Kursovik.Game;

public class Main {
public static void main(String[] args){
	JFrame myWindow = new Window();
	myWindow.setVisible(true);
	
	
	
	//Game game = new Game();
   // game.start();
    
}
}


public class StartGame extends JFrame {

	public StartGame() {

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Игра");
		setBounds(300, 300, 640, 480);
		JPanel panel = new JPanel();
		panel.setLayout(null);
		JLabel imageLabel = new JLabel(new ImageIcon("/data/8.jpg"));
		panel.add(imageLabel);
		JButton buttonGame = new JButton("Играть");
		buttonGame.setSize(100, 30);
		buttonGame.setLocation(480, 160);
		panel.add(buttonGame);
		buttonGame.setActionCommand("g");
		buttonGame.addActionListener(actionListener);
		JButton buttonRule = new JButton("Правила");
		buttonRule.setSize(100, 30);
		buttonRule.setLocation(480, 210);
		panel.add(buttonRule);
		buttonRule.setActionCommand("r");
		buttonRule.addActionListener(actionListener);
		JButton buttonOut = new JButton("Выход");
		buttonOut.setSize(100, 30);
		buttonOut.setLocation(480, 260);
		panel.add(buttonOut);
		buttonOut.setActionCommand("o");
		buttonOut.addActionListener(actionListener);
		add(panel);
		//panel.setBackground(new Color(0, 0, 0, 0));

	}

	public void paint(Graphics g) {

		String name = "/data/8.jpg";
		g.drawImage(new ImageIcon(getClass().getResource(name)).getImage(), 0, 0, 640, 480, null);
	//super.paint(g);
	}

	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand()) {
			case "g": {
				Game game = new Game();
				game.start();

				break;
			}
			case "r":
				JOptionPane.showMessageDialog(null,
						"Задача 1 уровня: дораться до входа в подземелье\n"
								+ "Задача 2 уровня: продержаться минуту, не попадая в ловушки босса\n",
						"Правила", JOptionPane.WARNING_MESSAGE);
				break;
			case "o":
				System.exit(0);
				break;

			}

		}

	};

	public static void main(String[] args) {

		StartGame f = new StartGame();
		f.setVisible(true);

	}

}
*/




import javax.imageio.ImageIO;
import javax.swing.*;

import Base.Game;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
public class TestPicture extends JFrame{
	public TestPicture(){
		JFrame f = new JFrame();
        f.setTitle("Игра");
		f.setBounds(300, 300, 640, 480);
		f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        ImagePanel pp = new ImagePanel();
        pp.setLayout(new BorderLayout());
        URL url1 = this.getClass().getResource("/data/8.jpg");
        try {
        	
        	 pp.setImage(ImageIO.read(url1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
      //  panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        //panel.setOpaque(false);
       // panel.setLayout(null);
        
        JButton buttonGame = new JButton("Играть");
        buttonGame.setSize(100, 30);
        buttonGame.setBackground(Color.BLACK);
        buttonGame.setForeground(Color.WHITE);
		buttonGame.setAlignmentX(Component.RIGHT_ALIGNMENT); 
		buttonGame.setActionCommand("g");
		buttonGame.addActionListener(actionListener);
		panel.add(buttonGame);
        JButton buttonRule = new JButton("Правила");
        buttonRule.setBackground(Color.BLACK);
        buttonRule.setForeground(Color.WHITE);
               buttonRule.setAlignmentX(Component.RIGHT_ALIGNMENT); 
               buttonRule.setActionCommand("r"); 
               buttonRule.addActionListener(actionListener);
		panel.add(buttonRule);
        JButton buttonOut = new JButton("Выход");
        buttonOut.setBackground(Color.BLACK);
        buttonOut.setForeground(Color.WHITE);
        buttonOut.setActionCommand("o");
        buttonOut.addActionListener(actionListener);
        buttonOut.setAlignmentX(Component.RIGHT_ALIGNMENT); 
        panel.add(buttonOut);
        
        pp.add(panel, java.awt.BorderLayout.EAST);
       // pp.setPreferredSize(new Dimension(50, 50));
        f.add(new JScrollPane(pp));
        f.pack();
        f.setSize(640, 480);
       // f.setLocationRelativeTo(null);
        f.setVisible(true);
	
	}
	private ActionListener actionListener = new ActionListener() {
@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand()) {
			case "g": {
				Game game = new Game();
				game.start();

				break;
			}
			case "r":
				JOptionPane.showMessageDialog(null,
						"Задача 1 уровня: дораться до входа в подземелье\n"
								+ "Задача 2 уровня: продержаться минуту, не попадая в ловушки босса\n",
						"Правила", JOptionPane.WARNING_MESSAGE);
				break;
			case "o":
				System.exit(0);
				break;

			}

		}

	};
    public static void main(String[] args) {
        /*JFrame f = new JFrame();
        f.setTitle("My Panel");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImagePanel pp = new ImagePanel();
        pp.setLayout(new BorderLayout());
        try {
        	URL url1=getURL();
        	 pp.setImage(ImageIO.read(url1()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JPanel panel = new JPanel();
        panel.setLayout(null);
        //panel.setOpaque(false);
       // JLabel label = new JLabel();
       // label.setFont(new java.awt.Font("Tahoma", 0, 24));
       // label.setForeground(new java.awt.Color(255, 0, 0));
       // label.setText("\u042d\u0442\u043e JLabel");
       // panel.add(label);
        JButton button = new JButton();
        button.setText("0");
        panel.add(button);
        pp.add(panel);
        pp.setPreferredSize(new Dimension(10000, 10000));
       f.add(new JScrollPane(pp));
       f.pack();
       f.setSize(800, 600);
       f.setLocationRelativeTo(null);
        f.setVisible(true);*/
    	TestPicture k = new TestPicture();
		//k.setVisible(true);
    }
   
}