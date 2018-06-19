package uk.ac.aber.dcs.cs12320.cards;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.StringBuilder;

public class Deck extends StackType{
	
	private boolean deckShuffled;
	
	//we only call a constructor for this class when creating a completely new deck
	//So when we are starting a new game or restarting the current game
	public Deck(ArrayList<Card> startDeck){
		super(startDeck);
		deckShuffled = false;
	}
	
	public boolean getDeckShuffled(){
		return deckShuffled;
	}
	
	public Card takeACard() throws ActionOutOfRulesException{
		if(getNumberOfCards()==0){
			throw new ActionOutOfRulesException();
		}
		Card cardToDeal = getTopMostCard();
		
		this.cards.remove(cardToDeal);
		return cardToDeal;	
	}
	
	public void shuffleDeck() throws ActionOutOfRulesException{
		if(deckShuffled){
			throw new ActionOutOfRulesException();
		}
		Collections.shuffle(cards);
		deckShuffled = true;
	}
	
	/**
	 * Returns a string that represents all the unplaced cards left in the deck
	 */
	public String toString(){
		if(getNumberOfCards() == 0){
			return "No cards left in deck";
		}
		int counter = 1;
		StringBuilder cardString = new StringBuilder("<html>");
		for (Card c : getCards()){
			cardString.append(c.toString());
			cardString.append(' ');
			if(counter%13==0){//For formatting displays each suit on a newline.
				cardString.append("<br>");
			}
			counter++;
		}
		cardString.append("</html>");
		return cardString.toString();
	}
}
