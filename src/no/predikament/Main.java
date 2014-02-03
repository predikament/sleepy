package no.predikament;

import javax.swing.JFrame;

public class Main 
{
	public static void main(String[] args) 
	{
		Game game = new Game();
		
		JFrame frame = new JFrame(Game.TITLE);
		
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(game);
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	}
}
