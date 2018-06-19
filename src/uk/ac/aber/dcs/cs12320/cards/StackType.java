package uk.ac.aber.dcs.cs12320.cards;
import java.util.ArrayList;

public class StackType{
	
	ArrayList<Card> cards;
	//cards is the only one I want to affect from the subclasses
	//setters and getters used instead of protected access modifier
	
	public StackType(ArrayList<Card> cards){//### used for the creation of decks?
		this.cards = cards;
	}
	
	public StackType(Card firstOnANewPile){//used for creation of new piles
		this.cards = new ArrayList<Card>();
		this.cards.add(firstOnANewPile);
	}
	
	public ArrayList<Card> getCards(){
		return cards;
	}
	
	public void setCards(ArrayList<Card> newCards){
		cards = newCards;
	}
	
	public Card getTopMostCard(){
		return cards.get(cards.size()-1);
	}
	
	public int getNumberOfCards(){
		return cards.size();
	}
	
}
