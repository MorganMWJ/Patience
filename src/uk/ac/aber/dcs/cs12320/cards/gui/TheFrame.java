package uk.ac.aber.dcs.cs12320.cards.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import uk.ac.aber.dcs.cs12320.cards.Game;
/**
 * Represents a window on which to draw the cards
 * @author Lynda Thomas (and Chris Loftus)
 * @version 1.1 (5th March 2015)
 *
 */
public class TheFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MenuBar gameMenu;
	private ThePanel canvas;
	
	/**
	 * The constructor creates a Frame ready to display the cards
	 */
	public TheFrame(Game game) {

		// Calls the constructor in the JFrame superclass passing up the name to 
		// display in the title
		super("Patience Game");
		
		// When you click on the close window button the window will be closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// This has North, East, South, West and Center positions for components
		setLayout(new BorderLayout());

		// This is what we will draw on (see the inner class below)
		canvas = new ThePanel();
		
		
		setSize(800, 350);
		
		
		this.add(canvas, BorderLayout.CENTER);

		//This adds a menubar to the window
		gameMenu = new MenuBar(game);
		this.setJMenuBar(gameMenu);
		
		//window opens in the center of the screen
		this.setLocationRelativeTo(null);
		
		setVisible(true); // Display the window
		
		//This adds a image icon to the window
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("cards/as.gif"));
	}

	/**
	 * Displays all cards
	 * 
	 * @param cards
	 *            an arraylist of strings of the form 3h.gif for 3 of hearts
	 */
	public void cardDisplay(ArrayList<String> cards) {
		canvas.cardDisplay(cards);
	}

	/**
	 * Call before cardDisplay at end of game (takes away the unused pile)
	 */
	public void allDone() {
		canvas.allDone();
	}
	/**
	 * Call before cardDisplay at start of new game
	 */
	public void notDone() {
		canvas.notDone();
		
	}
	//////////////
	//methods for use from Game class
	public int readNumber(String message,int upperBound,String errorString)throws Exception{
		do{
			Object result = JOptionPane.showInputDialog(this, message);
			if(result == null){//if input cancelled or closed throws exception to cancel the amalgamation
				throw new Exception(); 
			}
			try{
				int input = Integer.parseInt((String) result);
				if(input < 1 || input > upperBound){
					throw new Exception();
				}
				return input;
			}
			catch(NumberFormatException e){
				displayErrorMessage("Please enter a valid Integer!","Invalid Input");
			}
			catch(Exception e){
				displayErrorMessage("Please enter a valid " + errorString + "!","Invalid Input");
			}
		}
		while(true);
	}
	
	public String readString(String message){
		Object result = JOptionPane.showInputDialog(this, message);
		return (String) result;
	}
	
	public void displayMessage(String message, String title){ 
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void displayErrorMessage(String message, String title){
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}
	
	public void displayLongMessage(String message, String title){ 
		JPanel textPanel = new JPanel();//use JPanel to center text
		JLabel text = new JLabel(message,SwingConstants.CENTER);
		textPanel.add(text);
		JOptionPane.showMessageDialog(this, textPanel, title, JOptionPane.PLAIN_MESSAGE);//add the JPanel inplace of a String message
	}

	public boolean checkSure(String message) {	
		int response = JOptionPane.showConfirmDialog(this, message, "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	
		if (response == JOptionPane.NO_OPTION) {
	    	return false;
	    } 
	    else if (response == JOptionPane.YES_OPTION) {
	        return true;
	    } 
	    else{
	    	return false;
	    }		
	}
	
	// /////////////////////////////////////////////////

	/*
	 * This is an example of an inner class (like Russian dolls)
	 * It private so can only be seen by the outer class. It's part
	 * of the implementation of TheFrame. Because it extends JPanel we
	 * can draw on it
	 */
	private class ThePanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		ArrayList<String> cards = new ArrayList<String>();
		private Image image;
		private Image indexImage;
		private boolean done;

		private ThePanel() {
			setBackground(Color.BLACK);
			done = false;
		}

		public void notDone() {
			done = false;
			
		}

		private void cardDisplay(ArrayList<String> c) {
			cards = c;
			repaint();
		}

		private void allDone() {
			done = true;
		}

		/**
		 * This is called automatically by Java when it want to draw this panel.
		 * So we have to put our drawing command in here. 
		 * @param g Is the graphics object on which we draw.
		 */
		@Override
		public void paintComponent(Graphics g) {
			// Always do this. It's giving the JPanel superclass a change to
			// paint its parts before we paint ours. E.g. we don't draw the
			// edge of the window, one of the super-classes does that.
			super.paintComponent(g);
			int x = 20;
			int y = 50;
			//
			int x2 = 35;
			int y2 = 20;
			//co-ords of index images
			/*
			 * I have added index images because it is difficult for the user to count 
			 * every time he/she wants to amalgamate by pile index.
			 */
			int index = 0;
			// Loop through all the cards get each image in turn
			for (String c : cards) {
				index++;
				String indexFile = "indexes/" + index + ".gif";
				indexImage = Toolkit.getDefaultToolkit().getImage(indexFile);
				g.drawImage(indexImage, x2, y2, 35, 20, this);
				String file = "cards/" + c;
				image = Toolkit.getDefaultToolkit().getImage(file);
				g.drawImage(image, x, y, 70, 100, this);
				x += 72;  // The x position is moved on in order to position the next card
				x2 += 72;          // This could be improved by having a horizontal scroll bar
			}
			if (!done) {
				// Draws the face-down top card of our pack of cards
				String file = "cards/b.gif";
				image = Toolkit.getDefaultToolkit().getImage(file);
				g.drawImage(image, 100, 160, 70, 100, this);
			}
		}
	} // ThePanel inner class
} 