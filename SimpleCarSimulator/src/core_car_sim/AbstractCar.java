package core_car_sim;

import java.util.ArrayDeque;
import java.util.ArrayList;


import javax.swing.ImageIcon;


// the father of all kind of cars
// Abstract RoTRA
// 
public abstract class AbstractCar{

	public enum CarType{
		car_large,
		car_small,
		car_AI
	}

	private CarType carType;
	public Point startingPosition;
	public Point endPosition;
	public int speed;
	protected ImageIcon carIcon;
	public boolean crashed;
	public Point currentPosition;
	public Direction cmd = Direction.north;
	public Direction pmd = Direction.north;
	public ArrayDeque<Direction> currentMovingDirectionList = new ArrayDeque<>();
	
	protected abstract void visibleWorldUpdate(WorldSim visibleWorld, Point location);
	protected abstract ArrayDeque<Direction> getSimulationRoute();
	protected abstract boolean isFinished(Point point);
	
	public AbstractCar(Point startPos, Point endPos, int startingSpeed, String fileImage, CarType ct){
		this.startingPosition = startPos;
		this.endPosition = endPos;
		this.currentPosition = startPos;
		this.speed = startingSpeed;
		this.carIcon = new ImageIcon(fileImage);
		this.carType = ct;
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

	public ArrayDeque<Direction> getCurrentMovingDirectionList(){
		return this.currentMovingDirectionList;
	}

	public void setCurrentMovingDirectionList(ArrayDeque<Direction> currentMovingDirectionList){
		this.currentMovingDirectionList = currentMovingDirectionList;
	}

	public CarType getCarType(){
		return  this.carType;
	}

}
