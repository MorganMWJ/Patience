package uk.ac.aber.dcs.cs12320.cards.gui;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import uk.ac.aber.dcs.cs12320.cards.Game;

/**
 * Builds a MenuBar used by TheFrame class to access
 * the program functionality.
 */
class MenuBar extends JMenuBar{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MenuBar(Game game){
		
		//create the MenuBar
		super();
		
		//create the JMenu categories
		JMenu moves = new JMenu("Game Moves");
		moves.setMnemonic(KeyEvent.VK_M);
		//
		//
		JMenuItem dealItem = new JMenuItem("Deal A Card");
		dealItem.setToolTipText("Deals a card from the deck onto a new pile");
		dealItem.setActionCommand("deal");
		dealItem.addActionListener(game);
		moves.add(dealItem);	

		JMenuItem m1Item = new JMenuItem("Default Move 1");
		m1Item.setToolTipText("Move last pile onto previous one");
		m1Item.setActionCommand("move1");
		m1Item.addActionListener(game);
		moves.add(m1Item);

		JMenuItem m2Item = new JMenuItem("Default Move 2");
		m2Item.setToolTipText("Move last pile back over two piles");
		m2Item.setActionCommand("move2");
		m2Item.addActionListener(game);
		moves.add(m2Item);

		JMenuItem amalgamateItem = new JMenuItem("Amalgamate Piles By Specific Index");
		amalgamateItem.setToolTipText("Takes two pile indexes and Puts leftmost pile onto rightmost pile");
		amalgamateItem.setActionCommand("amalgamate");
		amalgamateItem.addActionListener(game);
		moves.add(amalgamateItem);	
		//
		//
		this.add(moves);
		/////////////////////////////////////
		JMenu utilities = new JMenu("Game Utilities");
		utilities.setMnemonic(KeyEvent.VK_U);
		//
		//
		JMenuItem autoItem = new JMenuItem("Auto Play");
		autoItem.setToolTipText("The computer will play for a given number of moves");
		autoItem.setActionCommand("auto");
		autoItem.addActionListener(game);
		utilities.add(autoItem);
		
		JMenuItem shuffleItem = new JMenuItem("Shuffle Deck");
		shuffleItem.setToolTipText("Shuffles the cards in the deck");
		shuffleItem.setActionCommand("shuffle");
		shuffleItem.addActionListener(game);
		utilities.add(shuffleItem);
		
		JMenuItem pileNumItem = new JMenuItem("Display Pile Sizes");
		pileNumItem.setToolTipText("Prints the amount of cards in each pile (from left to right)");
		pileNumItem.setActionCommand("pile");
		pileNumItem.addActionListener(game);
		utilities.add(pileNumItem);
		
		JMenuItem printItem = new JMenuItem("Display Deck Contents");
		printItem.setToolTipText("Prints the current content of the deck (from bottom to top)");
		printItem.setActionCommand("print");
		printItem.addActionListener(game);
		utilities.add(printItem);
		//
		//
		this.add(utilities);		
		//////////////////////////////////////////////////////
		JMenu options = new JMenu("Game Options");
		options.setMnemonic(KeyEvent.VK_O); 
		//
		//
		JMenuItem newGameItem = new JMenuItem("Start New Game");
		newGameItem.setToolTipText("Starts a new game of patience");
		newGameItem.setActionCommand("new");
		newGameItem.addActionListener(game);
		options.add(newGameItem);
		
		JMenuItem rulesItem = new JMenuItem("Show Game Rules");
		rulesItem.setToolTipText("Displays the rules of patience");
		rulesItem.setActionCommand("rule");
		rulesItem.addActionListener(game);
		options.add(rulesItem);
		
		JMenuItem textDisplayItem = new JMenuItem("Toggle Text Display");
		textDisplayItem.setToolTipText("Enables/Disables game state console output");
		textDisplayItem.setActionCommand("text");
		textDisplayItem.addActionListener(game);
		options.add(textDisplayItem);
		
		JMenuItem scoreItem = new JMenuItem("Show Saved Scores");
		scoreItem.setToolTipText("Displays all the saved player scores from previous games");
		scoreItem.setActionCommand("showScore");
		scoreItem.addActionListener(game);
		options.add(scoreItem);
		
		JMenuItem changeFileItem = new JMenuItem("Change .txt File");
		changeFileItem.setToolTipText("Change the text file that loads the cards into the game");
		changeFileItem.setActionCommand("change");
		changeFileItem.addActionListener(game);
		options.add(changeFileItem);
		
		JMenuItem qItem = new JMenuItem("Quit");
		qItem.setToolTipText("Exit application");
		qItem.setActionCommand("quit");
		qItem.addActionListener(game);
		options.add(qItem);
		//
		//
		this.add(options);
		///////////////////////////////////////
	}
} 
	


