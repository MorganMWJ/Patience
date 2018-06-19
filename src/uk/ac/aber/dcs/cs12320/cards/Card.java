package uk.ac.aber.dcs.cs12320.cards;

public class Card {
	private Value cardValue;
	private Suit cardSuit;
	
	public Card(Value cardValue, Suit cardSuit){
		this.cardValue = cardValue;
		this.cardSuit = cardSuit;
	}
	
	private Value getCardValue(){
		return cardValue;
	}
	
	private Suit getCardSuit(){
		return cardSuit;
	}
	
	public boolean matches(Card cardToMatch){
		boolean suitsMatch = cardToMatch.getCardSuit() == this.getCardSuit();
		boolean valuesMatch = cardToMatch.getCardValue() == this.getCardValue();
		if (suitsMatch || valuesMatch){
			return true;
		}
		else{
			return false;
		}
	}
	
	public String toString(){
		return cardValue.toString() + cardSuit.toString();
	}
}
