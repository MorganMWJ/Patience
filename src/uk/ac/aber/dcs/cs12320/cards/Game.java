package uk.ac.aber.dcs.cs12320.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import uk.ac.aber.dcs.cs12320.cards.gui.TheFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Game implements ActionListener{
	
	private TheFrame frame;
	private GameBoard gameBoard;
	private String filename;
	private boolean fileCannotBeRead = false;
	private static final String DEFAULT_FILE = "cards.txt";
	private static final String SCORES_FILE = "scores.txt";
	private ArrayList<Score> savedScores;

	private Game() {
		frame = new TheFrame(this);
		filename = DEFAULT_FILE; //starts with this file but user can change txt file if wanted after starting the program
		savedScores = new ArrayList<Score>();
	}

	private void initialise() {	
		//Loads cards file
		//
		frame.notDone();//when starting a new game without exiting the program need to show deck image
		try(FileReader fr = new FileReader(filename);BufferedReader br = new BufferedReader(fr);Scanner infile = new Scanner(br)){
			/////////////////////////////////////////
			gameBoard = new GameBoard(infile);
			/////////////////////////////////////////
		}
		catch (FileNotFoundException e) {
			fileCannotBeRead = true;
			frame.displayErrorMessage("The file: " + filename + " does not exist. Default File Used!","ERROR");
		} 
		catch (IOException e) {
			fileCannotBeRead = true;
			frame.displayErrorMessage("An unexpected error occurred when trying to open the file: " + filename + e.getMessage(), "ERROR");
		}
		//Loads lowest pack size file
		//
		try(FileReader fr = new FileReader(SCORES_FILE);BufferedReader br = new BufferedReader(fr);Scanner infile = new Scanner(br)){
			String player = "";
			int score = 0;
			int counter = Integer.parseInt(infile.nextLine());
			savedScores.clear();//remove all scores the first game read when starting a new game to avoid reading them twice
			for(int i=0;i<counter;i++){
				if(infile.hasNextLine()){
					player = infile.nextLine();
					score = Integer.parseInt(infile.nextLine());
				}
				savedScores.add(new Score(player, score));
			}
		}
		catch (FileNotFoundException e) {
			frame.displayErrorMessage("The file: " + SCORES_FILE + " does not exist. Default File Used!","ERROR");
		} 
		catch (IOException e) {
			frame.displayErrorMessage("An unexpected error occurred when trying to open the file: " + SCORES_FILE + e.getMessage(), "ERROR");
		}	
	}
	
	private boolean saveScore(String name, int pileCount){
		boolean scoreGoodEnough = false;
		if(savedScores.size()<5){//if less than 5 saved on top 5 add score
			savedScores.add(new Score(name, pileCount));
			scoreGoodEnough  = true;
		}
		else{
			for(Score s : savedScores){
				if(pileCount<=s.getPileScore()){//if new score beats one of the top five overwrite that in the saved scores file
					savedScores.remove(s);
					savedScores.add(new Score(name, pileCount));
					scoreGoodEnough = true;
					if(scoreGoodEnough){
						break;
					}
				}
			}
		}
		
		Collections.sort(savedScores);
		try(FileWriter fw = new FileWriter(SCORES_FILE);BufferedWriter bw = new BufferedWriter(fw);PrintWriter outfile = new PrintWriter(bw);){
			outfile.println(savedScores.size());
			for(Score s : savedScores){
				  outfile.println(s.getPlayerName());
				  outfile.println(s.getPileScore());
			  }
			return scoreGoodEnough;
		} 
		catch (IOException e) {
			frame.displayErrorMessage("Problem when trying to write to file: " + SCORES_FILE, "ERROR");
			return false;
		}
		
	}
	
	
	private void deal(){
		try{
			gameBoard.dealACard();
			if(gameBoard.isDeckEmpty()){//if a card is dealt and now the deck is empty
				frame.allDone();//remove deck background image
			}
			updateGameVisual();
			if(gameBoard.userStuck()){//if Game Over without victory
				endGame();
			}
		}
		catch(ActionOutOfRulesException e){
			frame.displayErrorMessage("No Cards Left To Deal!","Invaild Action");
		}
	}
	
	
	private void move1(){
		try{
			gameBoard.move(1);
			updateGameVisual();
			if(gameBoard.userWin()){
				frame.displayMessage("Well Done! YOU WON the Game.","***CONGRADULATIONS***");
			}
			if(gameBoard.userStuck()){
				endGame();
			}
		}
		catch(ActionOutOfRulesException e){
			frame.displayErrorMessage("Attempted Move Is Not Currently Available!","Invaild Action");
		}
	}
	
	
	private void move2(){
		try{
			gameBoard.move(2);
			updateGameVisual();
			if(gameBoard.userStuck()){
				endGame();
			}
		}
		catch(ActionOutOfRulesException e){
			frame.displayErrorMessage("Attempted Move Is Not Currently Available!","Invaild Action");
		}
	}
	
	private void amalgamate(){
		try{
			if(gameBoard.obtainNumberOfPiles()<2){
				throw new ActionOutOfRulesException();
			}	
			//get the pile indexes
			try{
				int pIndex1 = frame.readNumber("Enter the number of the pile to combine:",gameBoard.obtainNumberOfPiles(),"Pile Index");
				int pIndex2 = frame.readNumber("Enter the number of the pile to combine with:",gameBoard.obtainNumberOfPiles(),"Pile Index");
				gameBoard.amalgamation(pIndex1-1,pIndex2-1);
				updateGameVisual();	
				if(gameBoard.userWin()){
					frame.displayMessage("Well Done! YOU WON the Game.","***CONGRADULATIONS***");
				}
				if(gameBoard.userStuck()){//if Game Over without victory
					endGame();
				}
			}
			catch(ActionOutOfRulesException e){
				//catch user entering index's at wrong distances or non-matching cards
				frame.displayErrorMessage("Pile Cards Must Match AND Piles Must Be Next To Each Other OR Two Cards Apart","Invaild Action");
			}
			catch(Exception e){
				//catch user cancel/close
				frame.displayMessage("Amalgamation Cancelled!","Confirm");
			}
		}
		catch(ActionOutOfRulesException e){
			frame.displayErrorMessage("Amalgamation Impossible, Requires At Least Two Piles!","Invaild Action");
		}
	}
	
	
	/**
	 * Below method holds the functionality of each MenuItem
	 * Matches each MenuItem by it's unique ActionCommand
	 */
	@Override
	public void actionPerformed(ActionEvent E) {
		
		if(E.getActionCommand().equals("deal")){
			this.deal();
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(E.getActionCommand().equals("move1")){
			this.move1();
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(E.getActionCommand().equals("move2")){
			this.move2();
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(E.getActionCommand().equals("amalgamate")){
			this.amalgamate();
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(E.getActionCommand().equals("print")){
			frame.displayLongMessage(gameBoard.deckToString(), "Current Contents of the Deck");
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	if(E.getActionCommand().equals("shuffle")){
		try{
			gameBoard.shuffleTheDeck();
			frame.displayMessage("Deck Shuffled!","Confirm");
		}
		catch(ActionOutOfRulesException e){
			frame.displayErrorMessage("Deck Is Already Shuffled!", "Invaild Action");
		}
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	if(E.getActionCommand().equals("pile")){
		frame.displayLongMessage("[" + gameBoard.getPileNumStrings() + "]", "Current Pile Sizes");
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	if(E.getActionCommand().equals("auto")){
		if(gameBoard.userWin() || gameBoard.userStuck()){
			frame.displayErrorMessage("Cannot Auto-Play If Game Is Over!","Invalid Action");
		}
		else{
			try {
				int moveNum = frame.readNumber("Please enter the number of moves you would like the computer to make:", 200, "Amount of Moves");
				for(int i=0;i<moveNum;i++){
					//ENTER CODE HERE TO EXECUTE MOVES EACH TIME
					String recommendedMove = gameBoard.idealMove();
					//if no moves then deal a card this turn
					if(recommendedMove.equals("none") || gameBoard.idealMove().equals("#IRRELEVENT")){
						this.deal();
						if(gameBoard.userStuck()){//if Game Over without victory
							break;//exit automation/exit for loop 
						}
					}
					//get what idealMove equals
					//if(Move 1) call amalgamate with exact indexes
					else if(recommendedMove.equals("Move 1")){
						this.move1();
						if(gameBoard.userWin() || gameBoard.userStuck()){// if game over stop trying to do moves
							break;
						}
					}
					//if(Move 2) call amalgamate with exact indexes
					else if(recommendedMove.equals("Move 2")){
						this.move2();
						if(gameBoard.userStuck()){//if Game Over without victory stop trying to do move
							break;
						}
					}
					else{//if idealMove recommends a middle amalgamation
						int indexes[] = gameBoard.getAmalgamationIndexes();
						gameBoard.amalgamation(indexes[0],indexes[1]);
						updateGameVisual();	
						if(gameBoard.userWin()){
							frame.displayMessage("Well Done! YOU WON the Game.","***CONGRADULATIONS***");
							break;
						}
						if(gameBoard.userStuck()){//if Game Over without victory
							endGame();
							break;
						}
					}
						
				}
			} 
			catch (Exception e) {
				frame.displayMessage("Auto-Play Cancelled!","Confirm");
			}
		}
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(E.getActionCommand().equals("new")){
			if(frame.checkSure("Are you sure you want to start a New Game?")){
				this.initialise();
				updateGameVisual();
			}
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(E.getActionCommand().equals("rule")){
			frame.displayLongMessage(//can't go over multiple lines.
					"<html>Aim of the Game:"
					+ "<br>  		To end up with the deck empty and only one pile in play."
					+ "<br>"
					+ "<br>The Player Can:"
					+ "<br>-Deal a card at any time as long as there is a card left in the deck to deal."
					+ "<br>-Combine (Amalgamate) piles that are in play."
					+ "<br>-Shuffle the deck. "
					+ "<br>"
					+ "<br>Moves:"
					+ "<br>     [Default Move 1]"
					+ "<br>     	Combines the end pile with the pile before it given the <br>piles top cards 'match'."
					+ "<br>"
					+ "<br>		[Default Move 2]"
					+ "<br>     	Combines the end pile with the pile three before it given<br> the piles top cards 'match'."
					+ "<br>"
					+ "<br>		[Amalgamate Piles By Index]"
					+ "<br>			Used to combine pile when one of them isnt on the end by<br> taking the two piles indexs as input."
					+ "<br>"
					+ "<br>*Notes:"
					+ "<br>		-Every new card dealt is dealt into a new pile of it's own."
					+ "<br>		-You can only combine piles if they are adjacent or have exactly two piles between them. "
					+ "<br>		-You can only combine piles if the both Piles' top cards 'match'."
					+ "<br>		-Cards 'match' if either there suit OR value is the same."
					+ "<br>	  	-Once shuffled the deck cannot be re-shuffled in the same game."
					+ "<br>"
					+ "<br>		***IF STUCK USE EITHER TEXT DISPLAY OR AUTO PLAY,<br> ONE RECOMMEDS MOVES THE OTHER DOES MOVES***</html>"
					
					, "Rules Of Patiecne:");
		}		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		if(E.getActionCommand().equals("text")){
			String displayState = gameBoard.toggleTextDisplay();
			frame.displayMessage("Console Text Display Has Been " + displayState + "!", "Confirm");
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(E.getActionCommand().equals("change")){
			String posibleNewFile = frame.readString("Enter the name of the new file:");
			if(posibleNewFile != null){
				if(frame.checkSure("Changing default load file will start a New Game,\n Continue?")){
					filename = posibleNewFile;
					this.initialise();
					if(fileCannotBeRead){//if new file fails call initialise with defualtFile 
						filename = DEFAULT_FILE;
						this.initialise();
					}
					updateGameVisual();
				}
			}
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		if(E.getActionCommand().equals("showScore")){
			StringBuilder previousScores = new StringBuilder();
			for(Score s : savedScores){
				previousScores.append(s.toString());
			}
			frame.displayLongMessage("<html>" + previousScores.toString() + "</html>", "Previous Saved Game Scores");
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(E.getActionCommand().equals("quit")){
			if(frame.checkSure("Are you sure you want to Quit?")){
				System.exit(0);
			}
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}
	
	/**
	 * A method to be called if the deck is empty and there is no moves available, the conditions for this method call are checked
	 * after every Game Move/Deal. 
	 */
	private void endGame() {
		if(frame.checkSure("Game Over, You finished with: " + gameBoard.obtainNumberOfPiles() + " Piles\nWould you like to record your score?")){
			String name = frame.readString("Enter Your name to save your score.");
			if(name != null){//if they did not press cancel or close
				if(this.saveScore(name,gameBoard.obtainNumberOfPiles())){
					frame.displayMessage("Score Saved!","Confirm");
				}
				else{
					frame.displayMessage("Score Not Saved, Not Better Than Current Top 5!","Confirm");
				}
			}
		}
	}

	//called after every visual change of the game
	private void updateGameVisual(){
		ArrayList<String> cardStrings = new ArrayList<String>();
			for(Card c : gameBoard.obtainVisibleCards()){
				cardStrings.add(c.toString() + ".gif");
			}
		frame.cardDisplay(cardStrings);	
	}
	
	
	public static void main(String args[]) {
		Game theGame = new Game();
		theGame.initialise();
	}
}
