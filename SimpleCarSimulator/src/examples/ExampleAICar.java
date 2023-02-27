package examples;

import java.util.ArrayDeque;

import core_car_sim.AbstractCar;
import core_car_sim.Direction;
import core_car_sim.Point;
import core_car_sim.WorldSim;

public class ExampleAICar extends AbstractCar
{
	private ArrayDeque<Direction> goLeft = new ArrayDeque<Direction>();

	public ExampleAICar(Point startPos, Point endPos, Point referenceLoca, String imageLoc)
	{
		super(startPos,endPos, referenceLoca,1, imageLoc,CarType.car_small);
	}

	@Override
	protected ArrayDeque<Direction> getSimulationRoute()
	{
		goLeft.push(Direction.west);
	
		return goLeft;
	}

	@Override
	protected boolean isFinished(Point point)
	{
		boolean isFinished = false;
		// Dont care where the car is when finished
		  if(getCurrentPosition().equals(getEndPosition())) {
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
