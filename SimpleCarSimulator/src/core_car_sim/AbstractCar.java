package core_car_sim;

import java.util.ArrayDeque;

import javax.swing.ImageIcon;

public abstract class AbstractCar
{
	private Point startingPosition;
	private Point endPosition; 
	private int speed;
	private boolean crashed;
	protected ImageIcon carIcon = null;
	
	protected abstract void visibleWorldUpdate(WorldSim visibleWorld, Point location);
	protected abstract ArrayDeque<Direction> getSimulationRoute();
	protected abstract boolean isFinished(Point point);
	
	public AbstractCar(Point startPos, Point endPos,  int startingSpeed, String fileImage)
	{
		startingPosition = startPos;
		endPosition = endPos;
		speed = startingSpeed;
		carIcon = new ImageIcon(fileImage);
	}
	
	public int getSpeed()
	{
		return speed;
	}

	public void setSpeed(int speed)
	{
		this.speed = speed;
	}

	public Point getStartingPosition() {
		return startingPosition;
	}
	
	public Point getEndPosition() {
		return endPosition;
	}
	
	public void setEndPosition(Point endPosition) {
		this.endPosition = endPosition;
	}

	public void setStartingPosition(Point startingPosition) {
		this.startingPosition = startingPosition;
	}
	
	public ImageIcon getCarIcon()
	{
		return carIcon;
	}
	
	public boolean isCrashed()
	{
		return crashed;
	}
	
	public void setCrashed(boolean crashed)
	{
		this.crashed = crashed;
	}

}
