package core_car_sim;

import java.util.ArrayDeque;

import javax.swing.ImageIcon;

public class Pedestrian {
	//generate attributes
	private Point startPos;
	private Point endPos;
	private Point referencePosition;
	//it will also be 1
	private int speed = 1;
	private Direction md; 
	protected ImageIcon pedestrianIcon;
	private boolean isFinished = false;
	private final ArrayDeque<Direction> directions = new ArrayDeque<>();
	
	public Pedestrian(Point startPos, Point endPos, Point referencePos, Direction d, String fileImage) {
		this.startPos = startPos;
		this.endPos = endPos;
		this.referencePosition = referencePos;
		this.pedestrianIcon = new ImageIcon(fileImage);
		this.md = d;
	}
	
	//getter and setter of positions
	public void setStartPosition(Point p) {
		this.startPos = p;
	}
	public Point getStartPosition() {
		return this.startPos;
	}
	public void setEndPosition(Point p) {
		this.endPos = p;
	}
	public Point getEndPosition() {
		return this.endPos;
	}
	public void setMovingDirection(Direction d) {
		this.md = d;
	}
	public Direction getMovingDirection() {
		return this.md;
	}
	public ImageIcon getPedestrianIcon() {
		return this.pedestrianIcon;
	}
	
	public ArrayDeque<Direction> getSimulationRoute(){
		
		directions.push(this.md);
		return directions;
	}
	
	public boolean isFinished(Point p) {
		return p.equals(endPos);
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public Point getReferencePosition() {
		return referencePosition;
	}

	public void setReferencePosition(Point referencePosition) {
		this.referencePosition = referencePosition;
	}
	
}
