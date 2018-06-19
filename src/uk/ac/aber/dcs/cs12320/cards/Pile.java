package uk.ac.aber.dcs.cs12320.cards;
import java.util.ArrayList;


public class Pile extends StackType{

	
	public Pile(Card nextFromDeck){
		super(nextFromDeck);
	}

	/**
	 * 
	 * @param pileToAdd
	 * @return true if piles have been successfully amalgamated
	 * @throws ActionOutOfRulesException
	 */
	public void amalgamate(Pile pileToAdd) throws ActionOutOfRulesException{
		Card topCardToMove = pileToAdd.getTopMostCard();
		if(this.getTopMostCard().matches(topCardToMove)){	
			ArrayList<Card> cardsToAdd = pileToAdd.getCards();
			for (Card c : cardsToAdd){			
				this.cards.add(c);		
			}
		}
		else{
			throw new ActionOutOfRulesException();
		}
	}
	
}
