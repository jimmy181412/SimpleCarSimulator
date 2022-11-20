package examples;

import java.util.ArrayDeque;
import java.util.ArrayList;

import core_car_sim.AbstractCar;
import core_car_sim.AbstractCell.CellType;
import core_car_sim.AbstractInformationCell;
import core_car_sim.AbstractInformationCell.InformationCell;
import core_car_sim.TrafficLightCell.TrafficLightCellInformation;
import core_car_sim.WorldSim;
import core_car_sim.Direction;
import core_car_sim.Point;
import core_car_sim.RoadCell;
import core_car_sim.RoadCell.RoadMarking;
import core_car_sim.TrafficLightCell;

public class ExampleTestingCar extends AbstractCar
{
	boolean trafficLightRed;
	boolean atWhiteLine;
	boolean finished = false;
	
	 boolean overtakenOther = false;
	 boolean getIntoLeftLane = false;
	 boolean safegap = false;
	 boolean atRightTurn = false;
	 boolean wallAhead = false;
	 
	boolean atHardShoulder;
    boolean no_right_turn = false;
    boolean no_left_turn = false;
    boolean no_down_turn = false;
	boolean no_up_turn;
	boolean approaching_vertical_zebra = false;
	boolean approaching_horizontal_zebra = false;
	
	// car's current moving direction
	private Direction cmd = Direction.north;
	
	
	
	ArrayDeque<Direction> directions = new ArrayDeque<Direction>();
	

	public ExampleTestingCar(Point startPos, Point endPos,String imageLoc){
		super(startPos,endPos,0,imageLoc);
		setSpeed(1);
	}

	@Override
	protected void visibleWorldUpdate(WorldSim visibleWorld, Point location){
		
		
		for (int y = 0; y < visibleWorld.getHeight(); y++){
			for (int x = 0; x < visibleWorld.getWidth(); x++){
				if (visibleWorld.getCell(x, y).getCellType() == CellType.ct_information){
					if (((AbstractInformationCell)visibleWorld.getCell(x, y)).getInformationType() == InformationCell.ic_trafficLight){
						TrafficLightCell tlc = (TrafficLightCell)visibleWorld.getCell(x, y);
						TrafficLightCellInformation tlci = ((TrafficLightCell)visibleWorld.getCell(x, y)).getInformation();					
						//faces list
						ArrayList<Direction> faces = tlc.getFaces();
						if(faces.size() != 0) {
							if(faces.get(0) == Direction.east) {
								if (cmd == Direction.west) {
									trafficLightRed = tlci.redOn;
									Point visibleWorldStopPoint = new Point(x + tlci.stopAtReference.getX(), y + tlci.stopAtReference.getY());
									atWhiteLine = visibleWorldStopPoint == location;
								}		
							}
							else if(faces.get(0) == Direction.west) {
								if(cmd == Direction.east) {
									trafficLightRed = tlci.redOn;
									Point visibleWorldStopPoint = new Point(x + tlci.stopAtReference.getX(), y + tlci.stopAtReference.getY());
									atWhiteLine = visibleWorldStopPoint == location;
								}
							}
							else if(faces.get(0) == Direction.south) {
								if(cmd == Direction.north) {
									trafficLightRed = tlci.redOn;
									
									Point visibleWorldStopPoint = new Point(x + tlci.stopAtReference.getX(), y + tlci.stopAtReference.getY());
									atWhiteLine = visibleWorldStopPoint.equals(location);
								}
							}
							else if(faces.get(0) == Direction.north) {
								if(cmd == Direction.south) {
									trafficLightRed = tlci.redOn;
									Point visibleWorldStopPoint = new Point(x + tlci.stopAtReference.getX(), y + tlci.stopAtReference.getY());
									atWhiteLine = visibleWorldStopPoint == location;
								}
							}
							
						}
						
					
					}
				}
				else if(visibleWorld.getCell(x, y).getCellType() == CellType.ct_road) {		
					RoadCell rc = (RoadCell)visibleWorld.getCell(x, y);
					for(RoadMarking rm : rc.getRoadMarkings()) {
						if(rm == RoadMarking.rm_Zebra_Horizontal) {
							if(y == location.getY()){
								if(cmd == Direction.east) {
									if(x == location.getX() + 1) {
										approaching_horizontal_zebra = true;
									}
								}
								else if(cmd == Direction.west) {
									if(x == location.getX() - 1) {
										approaching_horizontal_zebra = true;
									}
								}
							}
						}
						else if(rm == RoadMarking.rm_Zebra_Vertical) {	
							//check the car's current position
							if(x == location.getX()) {
								if(cmd == Direction.north) {
									if(y == location.getY() + 1){
										approaching_vertical_zebra = true;
									}
								}
								else if(cmd == Direction.south) {
									if(y == location.getY() - 1) {
										approaching_vertical_zebra = true;
									}
								}
							}
						}
						else if(rm == RoadMarking.rm_hard_shoulder) {
							//check cars current position
							if(x == location.getX() && y == location.getY()) {
								atHardShoulder = true;
							}
						}
						
						else if(rm == RoadMarking.rm_solid_line_left) {
							if((x == location.getX() && y == location.getY()) || (x == location.getX() - 1 && y == location.getY())) {
								no_left_turn = true;
							}
						}
						else if(rm == RoadMarking.rm_solid_line_right) {
							if((x == location.getX() && y == location.getY())|| (x == location.getX() + 1 && y == location.getY())){
								no_right_turn = true;
							}
						}
						else if(rm == RoadMarking.rm_solid_line_up) {
							if((x == location.getX() && y == location.getY())||(x == location.getX() && y == location.getY() - 1)) {
								no_up_turn = true;
							}
						}
						else if(rm == RoadMarking.rm_solid_line_down) {
							if((x == location.getX() && y == location.getY()) || x == location.getX() && y == location.getY() + 1) {
								no_down_turn = true;
							}
						}
					}	
				}
			}	
		}	
		switch (cmd)
		{
		case east:
			wallAhead = visibleWorld.getCell(location.getX() + 1, location.getY()).getCellType() != CellType.ct_road;
			break;
		case north:
			wallAhead = visibleWorld.getCell(location.getX(), location.getY()-1).getCellType() != CellType.ct_road;
			break;
		case south:
			wallAhead = visibleWorld.getCell(location.getX(), location.getY()+1).getCellType() != CellType.ct_road;
			break;
		case west:
			wallAhead = visibleWorld.getCell(location.getX() - 1, location.getY()).getCellType() != CellType.ct_road;
			break;
		
		}	
	}

	@Override
	protected ArrayDeque<Direction> getSimulationRoute(){		
		if (getSpeed() == 0 || (trafficLightRed && atWhiteLine) || finished)
		{
			setSpeed(0);
			reMakeDecisions();
			
		}
		else if (atWhiteLine && !trafficLightRed){
			setSpeed(1);
			directions.push(cmd);
			reMakeDecisions();
		}
	
		else if(wallAhead) {
			setSpeed(0);
			reMakeDecisions();
		}
		else {
			directions.push(cmd);
			reMakeDecisions();
		}
		return directions;
	}

	@Override
	protected boolean isFinished(Point point)
	{
		finished = point == getEndPosition();
		return finished;
	}
	
	public void reMakeDecisions() {
	   trafficLightRed = false;
	   atWhiteLine = false;
	   wallAhead = false;
	   atHardShoulder = false;
	   no_right_turn = false;
	   no_left_turn = false;
	   no_down_turn = false;
	   no_up_turn = false;
	   approaching_vertical_zebra = false;
	   approaching_horizontal_zebra = false; 
	}
}
