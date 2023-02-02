package core_car_sim;

import java.util.ArrayDeque;

import javax.swing.ImageIcon;


// the father of all kind of cars
// Abstract RoTRA
// 
public abstract class AbstractCar{
	//general attributes
	public Point startingPosition;
	public Point endPosition;
	public int speed;
	protected ImageIcon carIcon = null;
	
	//detect whether the car has crashed or not
	public boolean crashed;
	
	//the current Position of the car
	public Point currentPosition;
	//current moving direction of the car
	public Direction cmd = Direction.north;
	//Previous moving direction of the car
	public Direction pmd = Direction.north;
	
	protected abstract void visibleWorldUpdate(WorldSim visibleWorld, Point location);
	protected abstract ArrayDeque<Direction> getSimulationRoute();
	protected abstract boolean isFinished(Point point);
	
	public AbstractCar(Point startPos, Point endPos, int startingSpeed, String fileImage){
		startingPosition = startPos;
		endPosition = endPos;
		currentPosition = startPos;
		speed = startingSpeed;
		carIcon = new ImageIcon(fileImage);
	}
	
	// getter and setter of current moving direction and previous moving direction of the car
	public void setCMD(Direction cmd){
		this.cmd = cmd;
	}
	
	public Direction getCMD(){
		return this.cmd;
	}
	
	public void setPMD(Direction pmd){
		this.pmd = pmd;
	}
	
	public Direction getPMD(){
		return this.pmd;
	}
	
	//getter and setter of speed
	public int getSpeed(){
		return speed;
	}
	
	public void setSpeed(int speed){
		this.speed = speed;
	}
	
	// getters and setters of positions.. 
	public Point getStartingPosition(){
		return startingPosition;
	}
	
	public Point getEndPosition() {
		return endPosition;
	}
	
	public Point getCurrentPosition(){
		return this.currentPosition;
	}
	
	public void setEndPosition(Point endPosition){
		this.endPosition = endPosition;
	}

	public void setStartingPosition(Point startingPosition){
		this.startingPosition = startingPosition;
	}
	
	public void setCurrentPosition(Point currentPosition){
		this.currentPosition = currentPosition;
	}
	
	
	// getter and setter of crash
	public boolean isCrashed(){
		return crashed;
	}
	
	public void setCrashed(boolean crashed){
		this.crashed = crashed;
	}
	
	public ImageIcon getCarIcon(){
		return carIcon;
	}
}
