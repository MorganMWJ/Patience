package uk.ac.aber.dcs.cs12320.cards;

public enum Suit {
	Clubs('c'),
	Spades('s'),
	Hearts('h'),
	Diamonds('d');
	
	private char suit;
	
	private Suit(char suit){
		this.suit = suit;
	}
	
	public char getSuit(){
		return suit;
	}
	
	public static Suit readSuit(char s){
		for(Suit c: values()){
			if(c.getSuit() == s){
				return c;
			}
		}
		return null;
	}
	
	public String toString(){
		return String.valueOf(suit);
	}

}
