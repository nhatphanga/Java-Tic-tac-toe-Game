/*
 * Created by Ethan Blaizis,and Nick Phan
 * 10/24/17. This is our work.
 */

// package
package tic.tac.toe;

// imports
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// class
public class Game extends JFrame {
	// class variable
	private static final long serialVersionUID = 1L;
	
	// swing variables
	private Container container;
	private JButton play, exit;
	private JComboBox<String> diffCombo;
	private JPanel head, board, foot;
	private JLabel banner, xWinLabel, oWinLabel, drawLabel, diffLabel;
	private ImageIcon xImage = new ImageIcon("res/x.png");
	private ImageIcon oImage = new ImageIcon("res/o.png");
	static JButton[][] squares;
	
	// functional variables
	private int xWins = 0;
	private int oWins = 0;
	private int draws = 0;
	private static int diff = 5;
	public static int totalMoves = 0;
	public char first = 'x';
	public char turn = 'x';
	private boolean winnerFound = false;
	private String[] diffArray = new String[] {"Easy", "Medium", "Hard", "Impossible", "No AI"};
	public static char[][] moves = {{'c', 'c', 'c', 'c', 'c'}, {'c', 'e', 'e', 'e', 'c'}, {'c', 'e', 'e', 'e', 'c'}, {'c', 'e', 'e', 'e', 'c'}, {'c', 'c', 'c', 'c', 'c'},};
			
	public Game() {
		super("Tic Tac Toe");
		// container
		container = getContentPane();
		container.setLayout(new BorderLayout());
		
		GraphicsEnvironment obj4 = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		Font openSans = null;
		Font openSansHead = null;
		Font openSansFoot = null;
		try {
			openSans = Font.createFont(Font.TRUETYPE_FONT, new File("res/OpenSans-Light.ttf"));
			openSansHead = openSans.deriveFont(46f);
			openSansFoot = openSans.deriveFont(14f);
			obj4.registerFont(openSans);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		// head
		head = new JPanel();
		head.setBackground(new java.awt.Color(190, 190, 190));
		head.setLayout(new FlowLayout());
		banner = new JLabel("Tic Tac Toe");
		banner.setFont(openSansHead);
		head.add(banner);
		
		// board
		board = new JPanel();
		board.setLayout(new GridLayout(5, 5));
		squares = new JButton[5][5];
		ButtonHandler obj1 = new ButtonHandler();
		MoveHandler obj2 = new MoveHandler();
		ItemHandler obj3 = new ItemHandler();
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				squares[i][j] = new JButton();
				
				if (i == 0 || i == 4 || j == 0 || j == 4) {
					squares[i][j].setOpaque(false);
					squares[i][j].setBorderPainted(false);
				}
				
				squares[i][j].setBackground( Color.GREEN );
				squares[i][j].setContentAreaFilled(false);
				squares[i][j].setEnabled(false);
				squares[i][j].addActionListener(obj2);
				board.add(squares[i][j]);
			}
		}
		
		// foot
		foot = new JPanel();
		foot.setLayout(new FlowLayout());
		foot.setBackground(new java.awt.Color(190, 190, 190));
		foot.setPreferredSize(new Dimension(320, 70));
		xWinLabel = new JLabel("X Wins: " + xWins, SwingConstants.CENTER);
		xWinLabel.setFont(openSansFoot);
		xWinLabel.setPreferredSize(new Dimension(90, 25));
		oWinLabel = new JLabel("O Wins: " + oWins, SwingConstants.CENTER);
		oWinLabel.setFont(openSansFoot);
		oWinLabel.setPreferredSize(new Dimension(90, 25));
		drawLabel = new JLabel("Draws: " + draws, SwingConstants.CENTER);
		drawLabel.setFont(openSansFoot);
		drawLabel.setPreferredSize(new Dimension(90, 25));
		diffLabel = new JLabel("Difficulty:");
		diffLabel.setFont(openSansFoot);
		diffCombo = new JComboBox<String>(diffArray);
		diffCombo.setFont(openSansFoot);
		diffCombo.setPreferredSize(new Dimension(80, 25));
		diffCombo.setBackground(new java.awt.Color(240, 240, 240));
		diffCombo.addItemListener(obj3);
		play = new JButton("Play");
		play.setFont(openSansFoot);
		play.setPreferredSize(new Dimension(80, 25));
		play.setBackground(new java.awt.Color(240, 240, 240));
		play.addActionListener(obj1);
		exit = new JButton("Exit");
		exit.setFont(openSansFoot);
		exit.setPreferredSize(new Dimension(60, 25));
		exit.setBackground(new java.awt.Color(240, 240, 240));
		exit.addActionListener(obj1);
		foot.add(xWinLabel);
		foot.add(oWinLabel);
		foot.add(drawLabel);
		foot.add(diffLabel);
		foot.add(diffCombo);
		foot.add(play);
		foot.add(exit);
		
		// container again
		container.add(head, BorderLayout.NORTH);
		container.add(board, BorderLayout.CENTER);
		container.add(foot, BorderLayout.SOUTH);
		setSize(320, 480);
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		Game obj4 = new Game();
		obj4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void play() {
		play.setEnabled(false);
		diffCombo.setEnabled(false);
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				squares[i][j].setEnabled(true);
			}
		}
		
		turn = first;
		
		if (first == 'o') {
			botPlay();
		}
	}
	
	public void botPlay() {
		int[] botMove = AI.findIJ(diff);
		move(botMove[0], botMove[1]);
	}
	
	public void move(int i, int j) {
		switch (turn) {
		case 'x':
			if (moves[i][j] == 'c' || moves[i][j] == 'e') {
				moves[i][j] = 'x';
				totalMoves++;
				squares[i][j].setIcon(xImage);
				checkWinner();
				turn = 'o';
				break;
			} else {
				break;
			}
		case 'o':
			if (moves[i][j] == 'c' || moves[i][j] == 'e') {
				moves[i][j] = 'o';
				totalMoves++;
				squares[i][j].setIcon(oImage);
				checkWinner();
				turn = 'x';
				break;
			} else {
				break;
			}
		}
	}
	
	public void checkWinner() {
		winnerFound = false;
		for(int i = 0;i<5;i++) {
			for (int j = 0;j<5;j++) {
				if (i<=2) {// horizontal check
					if (checkAdjacent(i,j,i+1,j)) {
	    				if (checkAdjacent(i+1,j,i+2,j)) {
	    					showWinner(i,j,i+1,j,i+2,j);
	    					winnerFound = true;
	    					gameOver(turn);
	    					break;
	    				}
	    			}
	    		}
	    		
	    		if (j<=2) {// vertical check
	    			if (checkAdjacent(i,j,i,j+1)) {
	    				if (checkAdjacent(i,j+1,i,j+2)) {
	    					showWinner(i, j, i, j+1, i, j+2);
	    					winnerFound = true;
	    					gameOver(turn);
	    					break;
	    				}
	    			}
	    		}
	    		
	    		if (i<=2 && j<=2) {// diagonal check top down
	    			if (checkAdjacent(i,j,i+1,j+1)) {
	    				if (checkAdjacent(i+1,j+1,i+2,j+2)) {
	    					showWinner(i, j, i+1, j+1, i+2, j+2);
	    					winnerFound = true;
	    					gameOver(turn);
	    					break;
	    				}
	    			}
	    		}
	    		
	    		if (i<=2 && j>=2) {// checking diagonal bottom up
	    			if (checkAdjacent(i,j,i+1,j-1)) {
	    				if (checkAdjacent(i+1,j-1,i+2,j-2)) {
	    					showWinner(i, j, i+1, j-1, i+2, j-2);
	    					winnerFound = true;
	    					gameOver(turn);
	    					break;
	    				}
	    			}
	    		}
	    	}
	    }
	    
	    if (totalMoves >= 9 && winnerFound == false) {
			winnerFound = true;
			gameOver('d');
		}
	}
	
	public boolean checkAdjacent(int i1, int j1, int i2, int j2)  // general check for the winning condition
	{
	    if ( moves[i1][j1] == moves[i2][j2] && moves[i1][j1] != 'c' && moves[i1][j1] != 'e' ) {
	      return true;
	    } else {
	      return false;
	    }
	}
	
	private static void showWinner(int i1, int j1, int i2, int j2, int i3, int j3) {
		squares[i1][j1].setContentAreaFilled(true);
		squares[i2][j2].setContentAreaFilled(true);
		squares[i3][j3].setContentAreaFilled(true);
	}
	
	public void gameOver(char par1) {
		switch (par1) {
			case 'x':
				xWins++;
				first = 'x';
				xWinLabel.setText("X Wins: " + xWins);
				play.setText("Replay");
				play.setEnabled(true);
				disableBoard();
				break;
			case 'o':
				oWins++;
				first = 'o';
				oWinLabel.setText("O Wins: " + oWins);
				play.setText("Replay");
				play.setEnabled(true);
				disableBoard();
				break;
			case 'd':
				draws++;
				drawLabel.setText("Draws: " + draws);
				play.setText("Replay");
				play.setEnabled(true);
				disableBoard();
				break;
		}
	}
	
	public static void disableBoard() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				squares[i][j].setEnabled(false);
				
				if (i == 0 || i == 4 || j == 0 || j == 4) {
					moves[i][j] = 'c';
				} else {
					moves[i][j] = 'e';
				}
			}
		}
	}
	
	public void reset() {
		play.setText("Play");
		diffCombo.setEnabled(true);
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				squares[i][j].setIcon(null);
				squares[i][j].setContentAreaFilled(false);
				
				if (i == 0 || i == 4 || j == 0 || j == 4) {
					moves[i][j] = 'c';
				} else {
					moves[i][j] = 'e';
				}
			}
		}
		
		setMoves(0);
		winnerFound = false;
	}
	
	/*
	 * getter and setter methods
	 */
	public static int getMoves() {
		// returns the total moves
		return totalMoves;
	}
	
	public static void setMoves(int par1) {
		// sets the total moves using parameter 1
		totalMoves = par1;
	}
	
	public static int getDiff() {
		// returns the difficulty
		return diff;
	}
	
	public static void setDiff(int par1) {
		// sets the difficulty using parameter 1
		diff = par1;
	}
	
	/*
	 * utility methods
	 */
	public static void print(String par1, boolean par2) {
		// print method, because I'm lazy
		if (par2 == true) {
			System.out.print(par1);
		} else if (par2 == false) {
			System.out.println(par1);
		}
	}
	
	public static void exit() {
		// exit method to kill the program
		System.exit(0);
	}
	
	/*
	 * handler classes
	 */
	private class ButtonHandler implements ActionListener {
		// listener for the function buttons
		public void actionPerformed(ActionEvent par1) {
			if (par1.getSource() == play && winnerFound == false) {
				play();
			} else if (par1.getSource() == play && winnerFound == true) {
				reset();
			} else if (par1.getSource() == exit) {
				exit();
			}
			
		}
	}
	
	private class MoveHandler implements ActionListener {
		// listener for the game board buttons
		public void actionPerformed(ActionEvent par1) {
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					if (par1.getSource() == squares[i][j]) {
						move(i, j);
						
						if (diff != -1 && winnerFound == false && turn == 'o') {
							botPlay();
						}
					}
				}
			}
		}
	}
	
	private class ItemHandler implements ItemListener {
		// listener for the difficulty combo box
		public void itemStateChanged(ItemEvent par1) {
			int var1 = diffCombo.getSelectedIndex();
			
			switch(var1) {
				case 0:
					setDiff(5);
					break;
				case 1:
					setDiff(7);
					break;
				case 2:
					setDiff(9);
					break;
				case 3:
					setDiff(38779);
					break;
				case 4:
					setDiff(-1);
					break;
			}
		}
	}
}
