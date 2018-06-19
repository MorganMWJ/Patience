package uk.ac.aber.dcs.cs12320.cards;

import java.util.ArrayList;
import java.util.Scanner;


public class GameBoard {
	private ArrayList<StackType> pilesOfCards;
	private boolean enableTextDisplay;
	
	public GameBoard(Scanner infile){//starts the game board by feeding construct a scanner that reads a 52 card deck to assign  
		pilesOfCards = new ArrayList<StackType>();
		this.pilesOfCards.add(this.loadDeck(infile));
		enableTextDisplay = false; //when starting a new game text is display is by default disabled
	}
	
	public String toggleTextDisplay(){
		this.enableTextDisplay = !enableTextDisplay;
		if(enableTextDisplay){
			textDisplay("***Console Text Display Enabled***");
			return "Enabled";
		}
		else{
			System.out.println("---Console Text Display Disabled---\n\n");
			return "Disabled";
		}
	}
	
	private void textDisplay(String text) {
		if(enableTextDisplay){
			System.out.println(text);
			String idealMove;
			if(this.idealMove().equals("none") || this.idealMove().equals("#IRRELEVENT")){
				idealMove = "Deal a card";
			}
			else{
				idealMove = this.idealMove();
			}
			System.out.println("No. of Piles: [" + this.obtainNumberOfPiles() + "] Deck size: [" + this.getDeck().getNumberOfCards() + 
					"] Deck shuffled: [" + this.getDeck().getDeckShuffled() + "] Recommended Move: [" + idealMove + "]");	
		}		
	}
	
	public Deck loadDeck(Scanner infile){
		//READ DECK FROM HERE 
		ArrayList<Card> cardsToLoad = new ArrayList<Card>();
		do{
			
			Value v =  Value.readValue(infile.next().charAt(0));//has to convert to char by taking first char from string
			Suit s  = Suit.readSuit(infile.next().charAt(0));
			
			infile.nextLine();
			Card newCard = new Card(v, s);
			cardsToLoad.add(newCard);
		}
		while(infile.hasNextLine());
		Deck startDeck = new Deck(cardsToLoad);
		return startDeck;
	}
	
	private Deck getDeck(){
		for (StackType stack : pilesOfCards){
			if (stack instanceof Deck){
				return (Deck)stack;
			}
		}
		//if no instance of deck
		throw new RuntimeException("ERROR: There should always be exactly one Deck");
	}
	
	public ArrayList<Pile> obtainPilesInPlay(){
		ArrayList<Pile> pilesInPlay = new ArrayList<Pile>();
		for(StackType stack : pilesOfCards){
			if(stack instanceof Pile){
				Pile p = (Pile) stack;
				pilesInPlay.add(p);
			}
		}
		return pilesInPlay;
	}
	
	public ArrayList<Card> obtainVisibleCards(){
		ArrayList<Card> visibleCards = new ArrayList<Card>();
			for(Pile p : this.obtainPilesInPlay()){
				visibleCards.add(p.getTopMostCard());
			}
		return visibleCards;
	}
	
	public void dealACard() throws ActionOutOfRulesException{
		//take a card from deck 
		Card cardStartingNewPile = getDeck().takeACard(); 
		//construct a new pile with that card
		pilesOfCards.add(new Pile(cardStartingNewPile));
		this.textDisplay("A card was just dealt.");
	}
	
	public void shuffleTheDeck() throws ActionOutOfRulesException{
		getDeck().shuffleDeck();
		this.textDisplay("The deck was just shuffled.");
	}

	
	public void move(int methodVersion) throws ActionOutOfRulesException{
		//METHOD BELOW THROWS EXCEPTION, if there are no piles in play to obtain
		ArrayList<Pile> piles = obtainPilesInPlay();
		//BELOW THROW EXCEPTION, if only one pile in play
		if(piles.size()<=1){
			throw new ActionOutOfRulesException();
		}
		String moveString ="";
		if(methodVersion==1){
			//METHOD BELOW THROWS EXCEPTION, if merging the piles is not allowed 
			piles.get(piles.size()-2).amalgamate(piles.get(piles.size()-1));
			moveString = "The end pile was amalgamated with it's adjacent pile. [Move 1]";
		}
		if(methodVersion==2){
			if(piles.size()<4){
				throw new ActionOutOfRulesException();
			}
			//METHOD BELOW THROWS EXCEPTION, if merging the piles is not allowed 
			piles.get(piles.size()-4).amalgamate(piles.get(piles.size()-1));
			moveString = "The end pile was amalgamted with it's non adjacent pile. [Move 2]";
		}
		//IF FAILED EXCEPTION WILL HAVE NOW BEEN THROWN
		
		//remove the previous end pile
		pilesOfCards.remove(pilesOfCards.get(pilesOfCards.size()-1));	
		this.textDisplay(moveString);
		
	}

	public int obtainNumberOfPiles() {
		return pilesOfCards.size()-1;
	}

	public String getPileNumStrings() {
		StringBuilder pileNumbers = new StringBuilder();
		ArrayList<Pile> allPiles = obtainPilesInPlay();
		if(allPiles.size()==0){
			return "No Piles Yet In Play";
		}
		for(Pile p : allPiles){
			pileNumbers.append(p.getNumberOfCards());
			pileNumbers.append(",");
		}
		pileNumbers.deleteCharAt(pileNumbers.length()-1);//removes last unwanted comma from the stringBuilder
		return pileNumbers.toString();
	}

	public void amalgamation(int pile1Position, int pile2Position) throws ActionOutOfRulesException{
		
		//Below if insures the piles are either next to each other or 2 cards apart, to keep rules of the game
		if(Math.abs(pile1Position-pile2Position) == 3 || Math.abs(pile1Position-pile2Position) == 1){
			Pile pileToAddTo;
			Pile pileToAdd;
			if(pile2Position>pile1Position){
				pileToAddTo = (Pile) this.obtainPilesInPlay().get(pile1Position);
				pileToAdd = (Pile) this.obtainPilesInPlay().get(pile2Position);	
			}
			else{
				pileToAddTo = (Pile) this.obtainPilesInPlay().get(pile2Position);
				pileToAdd = (Pile) this.obtainPilesInPlay().get(pile1Position);
			}
			pileToAddTo.amalgamate(pileToAdd);
			pilesOfCards.remove(pileToAdd);
			if(Math.abs(pile1Position-pile2Position) == 3){
				this.textDisplay("Amalgamation of non adjacent middle Piles.");
			}
			else{
				this.textDisplay("Amalgamation of adjacent middle Piles.");
			}
		}
		else{
			throw new ActionOutOfRulesException();
		}
	}

	public boolean isDeckEmpty() {
		if(this.getDeck().getNumberOfCards() == 0){
			return true;
		}
		else{
			return false;
		}
	}

	public String deckToString() {
		return getDeck().toString();
	}

	//method checks if a game ends with victory
	public boolean userWin() {
		if(this.isDeckEmpty() && this.obtainNumberOfPiles()==1){
			textDisplay("The Player has Won the game!");
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean userStuck(){
		if(this.idealMove().equals("none") && this.isDeckEmpty()){
			textDisplay("The Player has Run Out of Moves!");
			return true;
		}
		else if(this.userWin()){
			return true;
		}
		else{
			return false;
		}
	}

	//method is used for checking if a game has ended without victory (returns none if so)
	//it also is used for Auto-Move because if will return the possible move or the most ideal move 
	//if there is more than one possible move
	public String idealMove() {
		//////////// ONLY CARES ABOUT CARDS ON TOP OF EACH PILE
		ArrayList<Card> visibileCards = this.obtainVisibleCards();
		if(visibileCards.size()<2){
			return "#IRRELEVENT"; 	//To PREVENT returning "none" when a user wins the game
		}							
		/////////// ASSUME ALL MOVES CANT BE MADE
		boolean canMakeMove1 = false;
		boolean canMakeMove2 = false;
		boolean canAmalgamateInMiddle1 = false;
		boolean canAmalgamateInMiddle2 = false;
		/////////////////////////////// IF MOVE ONE POSSIBLE, REMEMBER IT IS
		if(visibileCards.get(visibileCards.size()-1).matches(visibileCards.get(visibileCards.size()-2))){
			canMakeMove1 = true;
		}
		///////IF AMALGAMTION OF AJACENT PILES IN THE MIDDLE IS POSSIBLE, REMEMBER SO
		int pileIndex = 0;
		int otherPileIndex = 1;
		for(int i=0;i<visibileCards.size()-2;i++){
			try{
				if(visibileCards.get(pileIndex).matches(visibileCards.get(otherPileIndex))){
					canAmalgamateInMiddle1 = true;
					break;
				}
				pileIndex++;
				otherPileIndex++;
			}
			catch(IndexOutOfBoundsException e){
				break;
			}
		}
		////////////////////////////// IF MOVE TWO POSSIBLE, REMEMBER IT IS
		try{
			if(visibileCards.get(visibileCards.size()-1).matches(visibileCards.get(visibileCards.size()-4))){
				canMakeMove2 = true;
			}
		}
		catch(IndexOutOfBoundsException e){
			canMakeMove2 = false;
		}
		//////////IF AMALGAMTION OF PILES IN THE MIDDLE THAT HAVE TWO PILES BETWEEN THEM IS POSSIBLE, REMEMBER SO
		pileIndex = 0;
		otherPileIndex = 3;
		for(int i=0;i<visibileCards.size()-4;i++){
			try{
				if(visibileCards.get(pileIndex).matches(visibileCards.get(otherPileIndex))){
					canAmalgamateInMiddle2 = true;
					break;
				}
				pileIndex++;
				otherPileIndex++;
			}
			catch(IndexOutOfBoundsException e){
				break;
			}
		}
		////////////////////////////////////////////////////////////////////////////////////////
		if(canMakeMove2) return "Move 2";
		if(canAmalgamateInMiddle2) return "Amalgamate Non Adjacent Middle Piles";
		if(canMakeMove1) return "Move 1";
		if(canAmalgamateInMiddle1) return "Amalgamate Adjacent Middle Piles";
		return "none";//if no moves available
	}

	//function called when Auto-Play has been recommended a middle amalgamation(Amalgamation by index)
	//this function gets the pile indexes for that amalgamation dependent on what type of amalgamation it is
	// adjacent amalgamation or non-adjacent amalgamation
	public int[] getAmalgamationIndexes() {
		
		int[] indexes = {0,0};//to return the pile indexes to amalgamate(combine)
		ArrayList<Card> visibileCards = this.obtainVisibleCards();
		int indexCounter1 = 0;
		int indexCounter2;
		String amalgamationType = this.idealMove();
		
		//IF idealMove SAYS TO AMALGAMATE ADJACENT PILES, FIND THE PIELS TO DO SO
		if(amalgamationType.equals("Amalgamate Adjacent Middle Piles")){
			indexCounter2 = 1;//set distance from other counter (adjacent)
			for(int i=0;i<visibileCards.size()-2;i++){
				if(visibileCards.get(indexCounter1).matches(visibileCards.get(indexCounter2))){
					indexes[0] = indexCounter1;
					indexes[1] = indexCounter2;
					return indexes;
				}
				indexCounter1++;
				indexCounter2++;
			}
		}
		
		//ELSE idealMove SAYS TO AMALGAMATE NON ADJACENT PILES, FIND THE PIELS TO DO SO
		else{
			indexCounter2 = 3;//set distance from other counter (non adjacent[by 2 piles])
			for(int i=0;i<visibileCards.size()-4;i++){
				if(visibileCards.get(indexCounter1).matches(visibileCards.get(indexCounter2))){
					indexes[0] = indexCounter1;
					indexes[1] = indexCounter2;
					return indexes;
				}
				indexCounter1++;
				indexCounter2++;
			}
		}
		return indexes;
		//will never return this {0,0} because the function is only called if idealMove must have decided there can be a middle amalgamation
		
	}

	
}