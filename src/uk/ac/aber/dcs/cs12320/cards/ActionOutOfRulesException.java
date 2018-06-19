package uk.ac.aber.dcs.cs12320.cards;

public class ActionOutOfRulesException extends Exception {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ActionOutOfRulesException(){
		// do nothing
	}
	
	public ActionOutOfRulesException(String message ){
		super(message); //give the message to superclass to deal with.
	}
	
}
	

