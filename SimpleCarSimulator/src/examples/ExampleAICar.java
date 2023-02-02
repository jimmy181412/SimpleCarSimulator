package examples;

import java.util.ArrayDeque;

import core_car_sim.AbstractCar;
import core_car_sim.Direction;
import core_car_sim.Point;
import core_car_sim.WorldSim;

public class ExampleAICar extends AbstractCar
{
	private ArrayDeque<Direction> goLeft = new ArrayDeque<Direction>();

	public ExampleAICar(Point startPos, Point endPos, String imageLoc)
	{
		super(startPos,endPos, 1, imageLoc);
	}

	@Override
	protected ArrayDeque<Direction> getSimulationRoute()
	{
		goLeft.push(Direction.north);
	
		return goLeft;
	}

	@Override
	protected boolean isFinished(Point point)
	{
		boolean isFinished = false;
		// Dont care where the car is when finished
		  if(currentPosition.equals(endPosition)) {
	            isFinished = true;
	        }
	        else {
	            isFinished = false;
	        }
	        return isFinished;
	        
	}

	@Override
	protected void visibleWorldUpdate(WorldSim visibleWorld, Point location)
	{
		// TODO Auto-generated method stub
		
	}

}
