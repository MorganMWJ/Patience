package uk.ac.aber.dcs.cs12320.cards;

public class Score implements Comparable<Score>{
	private String player;
	private int piles;
	
	public Score(String player, int piles){
		this.player = player;
		this.piles = piles;
	}
	
	public String getPlayerName(){
		return player;
	}
	
	public int getPileScore(){
		return piles;
	}
	
	//Used to sort scores in ascending order using Collections.sort
	public int compareTo(Score other) {
	    int compareScore = other.getPileScore();
	    return this.piles - compareScore;
   }
	
	public String toString(){
		return "Player Name: " + player + "<br>Player Score: " + piles + " (Piles)<br>";
	}
}
