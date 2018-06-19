package uk.ac.aber.dcs.cs12320.cards;

public enum Value {
	ace('a'),
	two('2'),
	three('3'),
	four('4'),
	five('5'),
	six('6'),
	seven('7'),
	eight('8'),
	nine('9'),
	ten('t'),
	jack('j'),
	queen('q'),
	king('k');
	
	private char value;
	
	private Value(char value){
		this.value = value;
	}
	
	public char getValue() {
		return value;
	}
	
	public static Value readValue(char v){
		for(Value c: values()){
			if(c.getValue() == v){
				return c;
			}
		}
		return null;
	}
	public String toString(){
		return String.valueOf(value);
	}
	
	

}
