/*
 *  it is building on work by Joe Collenette.
 */

package core_car_sim;

//There are no needs to edit Direction File
public enum Direction{
	north("^"),
	south("V"),
	east(">"),
	west("<");
	
	private final String dir;
	private Direction(String s){
		dir = s;
	}
	
	public String toString(){
		return dir;
	}
}