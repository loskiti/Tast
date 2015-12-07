package Main;

/**
 * Меню при входе
 */
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.*;

import Base.Game;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class StartGame extends JFrame {
	public StartGame() {
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
		f.add(new JScrollPane(pp));
		f.pack();
		f.setSize(640, 480);
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

		StartGame k = new StartGame();

	}

}