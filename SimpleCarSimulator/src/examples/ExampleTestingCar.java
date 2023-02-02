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
	boolean wallAhead = false;//
	boolean atHardShoulder;
    boolean no_right_turn = false;
    boolean no_left_turn = false;
    boolean no_down_turn = false;
	boolean no_up_turn;
	boolean approaching_vertical_zebra = false;
	boolean approaching_horizontal_zebra = false;
	boolean no_overtake= false;
	boolean no_go_north_because_other_car = false;
	boolean no_go_south_because_other_car =false;
	boolean no_go_east_because_other_car = false;
	boolean no_go_west_because_other_car = false;
	
	ArrayDeque<Direction> directions = new ArrayDeque<Direction>();

	public ExampleTestingCar(Point startPos, Point endPos,String imageLoc){
		super(startPos,endPos,0,imageLoc);
		setSpeed(1);
	}

	@Override
	public void visibleWorldUpdate(WorldSim visibleWorld, Point location){
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
									Point visibleWorldStopPoint = new Point(x , y - 1);
									atWhiteLine = visibleWorldStopPoint.equals(location);
								}		
							}
							else if(faces.get(0) == Direction.west) {
								if(cmd == Direction.east) {
									trafficLightRed = tlci.redOn;
									Point visibleWorldStopPoint = new Point(x , y + 1);
									atWhiteLine = visibleWorldStopPoint.equals(location);
								}
							}
							else if(faces.get(0) == Direction.south) {
								if(cmd == Direction.north) {
									trafficLightRed = tlci.redOn;
									Point visibleWorldStopPoint = new Point(x + 1, y);
									atWhiteLine = visibleWorldStopPoint.equals(location);
								}
							}
							else if(faces.get(0) == Direction.north) {
								if(cmd == Direction.south) {
									trafficLightRed = tlci.redOn;
									Point visibleWorldStopPoint = new Point(x - 1, y );
									atWhiteLine = visibleWorldStopPoint.equals(location);
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
						
						else if(rm == RoadMarking.rm_solid_white_line_left) {
							if((x == location.getX() && y == location.getY()) || (x == location.getX() - 1 && y == location.getY())) {
								no_left_turn = true;
							}
						}
						else if(rm == RoadMarking.rm_solid_white_line_right) {
							if((x == location.getX() && y == location.getY())|| (x == location.getX() + 1 && y == location.getY())){
								no_right_turn = true;
							}
						}
						else if(rm == RoadMarking.rm_solid_white_line_up) {
							if((x == location.getX() && y == location.getY())||(x == location.getX() && y == location.getY() - 1)) {
								no_up_turn = true;
							}
						}
						else if(rm == RoadMarking.rm_solid_white_line_down) {
							if((x == location.getX() && y == location.getY()) || x == location.getX() && y == location.getY() + 1) {
								no_down_turn = true;
							}
						}
					}	
				}
			}	
		}	
	
		
	}

	@Override
	public ArrayDeque<Direction> getSimulationRoute(){	
		
//		System.out.println("no up turn: " + no_up_turn);
//		System.out.println("no down turn: " + no_down_turn);
//		System.out.println("no right turn: " + no_right_turn);
//		System.out.println("no left turn: " + no_left_turn);
//		System.out.println("traffic light red on: " + trafficLightRed);
//		System.out.println("at white line: " + atWhiteLine);
//		System.out.println("at hardshrouder: " + atHardShoulder);
//		System.out.println("approaching_vertical_zebra: " + approaching_vertical_zebra);
//		System.out.println("approaching_horizontal_zebra: " + approaching_horizontal_zebra);	
//		System.out.println("no_overtake: " + no_overtake);
//		System.out.println("no_go_north_because_other_car: " + no_go_north_because_other_car);
//		System.out.println("no_go_south_because_other_car: " + no_go_south_because_other_car);
//		System.out.println("no_go_east_because_other_car: " + no_go_east_because_other_car);
//		System.out.println("no_go_west_because_other_car: " + no_go_west_because_other_car);
		

		if ((trafficLightRed && atWhiteLine) || finished)
		{
			setSpeed(0);
		}
		else if(atHardShoulder && no_down_turn && cmd == Direction.south) {
		
				directions.push(cmd);
		}
		
		else if(atHardShoulder && no_up_turn && cmd == Direction.north) {
					directions.push(cmd);
		}
		
		else if(atHardShoulder && no_right_turn && cmd == Direction.east) {		
				directions.push(cmd);
		}
		else if(atHardShoulder && no_left_turn && cmd == Direction.west) {
				directions.push(cmd);
		}
		
		else if(no_down_turn && cmd == Direction.south) {
			if(atHardShoulder) {
				directions.push(cmd);
			}
			else{
				directions.push(Direction.east);	
			}	
		}
		else if(no_up_turn && cmd == Direction.north) {
			if(atHardShoulder) {
				directions.push(cmd);
			}
			else {
				directions.push(Direction.west);
			}
				
		}
		else if(no_right_turn && cmd == Direction.east) {
			if(atHardShoulder) {
				directions.push(cmd);
			}
			else {
				directions.push(Direction.north);
			}
		}
		else if(no_left_turn && cmd == Direction.west) {
			if(atHardShoulder) {
				directions.push(cmd);
			}
			else {
				directions.push(Direction.south);
			}
					
		}
		else {
			directions.push(cmd);	
		}
		
		
//		System.out.println("currentMovingDirection is: " + cmd.toString());
		
		
		reMakeDecisions();
		return directions;
	}

	@Override
	protected boolean isFinished(Point point)
	{
		
		finished = point.equals(getEndPosition());
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
	   no_overtake = false;
	   no_go_north_because_other_car = false;
	   no_go_south_because_other_car = false;
	   no_go_east_because_other_car = false;
	   no_go_west_because_other_car = false;
	}
}
