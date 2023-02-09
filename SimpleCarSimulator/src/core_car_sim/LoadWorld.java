package core_car_sim;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import core_car_sim.PavementCell.PavementMarking;
import core_car_sim.RoadCell.RoadMarking;

public class LoadWorld{
	public static Direction charToDirection(char dir){
		switch (dir){
			case '>':
				return Direction.east;
			case '<':
				return Direction.west;
			case '^':
				return Direction.north;
			case 'V':
				return Direction.south;
		}
		return null;
	}
	
	public static Direction charToDirection(String dir){
		return charToDirection(dir.charAt(0));
	}
	
	public static WorldSim loadWorldFromFile(BufferedReader reader, CarAddedListener cal, PedestrianAddedListener pal) throws IOException{
		String widthStr = reader.readLine();
		String heightStr = reader.readLine();
		WorldSim createdSim = new WorldSim(Integer.parseInt(widthStr), Integer.parseInt(heightStr));

		createdSim.addCarAddedListener(cal);
		//add pedestrian listener
		createdSim.addPedestrianListener(pal);
		
		int defaultSpeedLimit = Integer.parseInt(reader.readLine());
		String line;
		ArrayList<Direction> tmp = new ArrayList<Direction>();
		for (int y = 0; y < createdSim.getHeight(); y++){
			line = reader.readLine();
			for (int x = 0; x < createdSim.getWidth(); x++){
				switch (line.charAt(x)){
					default:
					case '|':
					case '-':
						createdSim.setCell(new NonDrivingCell(), x, y);
						break;
					case '>':
					case '<':
					case '^':
					case 'V':
						createdSim.setCell(new RoadCell(charToDirection(line.charAt(x)),null, defaultSpeedLimit), x, y);
						break;
					case '+':
						tmp.clear();
						tmp.add(Direction.north);
						tmp.add(Direction.south);
						tmp.add(Direction.east);
						tmp.add(Direction.west);
						createdSim.setCell(new RoadCell(tmp,null,defaultSpeedLimit), x, y);
						break;
					case 'p':
						createdSim.setCell(new PavementCell(), x, y);
				}
			}
		}
		line = reader.readLine();
		while (line != null){
			String[] items = line.split(" ");
			switch (items[0].toLowerCase()){
				//traffic light faces west
				case "trafficlight":
					Direction ld5 = charToDirection(items[1]);
					//the location of the traffic light
					int x5 = Integer.parseInt(items[2]);
					int y5 = Integer.parseInt(items[3]);
					Point roadEffectedLocation = new Point(x5,y5);
					//the location of the white line
					int x6 = Integer.parseInt(items[4]);
					int y6 = Integer.parseInt(items[5]);
					Point roadEffectedReference = new Point(x6,y6);
					
					//traffic light faces east
					if(ld5 == Direction.east) {
						createdSim.setCell(new TrafficLightCell(ld5, 3, roadEffectedLocation, roadEffectedReference, 1), roadEffectedLocation);
						((RoadCell)createdSim.getCell(x6, y6)).setMarking(RoadMarking.rm_HorizontalWhiteLineLeft);
						break;
					}
					//traffic light faces west
					else if(ld5 == Direction.west) {
						createdSim.setCell(new TrafficLightCell(ld5, 3, roadEffectedLocation,  roadEffectedReference, 3), roadEffectedLocation);
						((RoadCell)createdSim.getCell(x6, y6)).setMarking(RoadMarking.rm_HorizontalWhiteLineRight);
						break;
					}
					//traffic light faces north
					else if(ld5 == Direction.north) {
						createdSim.setCell(new TrafficLightCell(ld5, 3, roadEffectedLocation,  roadEffectedReference, 4), roadEffectedLocation);
						((RoadCell)createdSim.getCell(x6, y6)).setMarking(RoadMarking.rm_VerticalWhiteLineDown);
						break;
					}
					//traffic light faces south
					else if(ld5 == Direction.south) {
						createdSim.setCell(new TrafficLightCell(ld5, 3, roadEffectedLocation,  roadEffectedReference, 2), roadEffectedLocation);
						((RoadCell)createdSim.getCell(x6, y6)).setMarking(RoadMarking.rm_VerticalWhiteLineUp);
						break;
					}
					break;
				case "zebracrossing":
					//item 1 zebra crossing direction
					//item 2 3 the point of the road cell 
					Direction ld4 = charToDirection(items[1]);
					int x4 = Integer.parseInt(items[2]); 
					int y4 = Integer.parseInt(items[3]);
					if(ld4 == Direction.east || ld4 == Direction.west) {
						((RoadCell)createdSim.getCell(x4,y4)).setMarking(RoadMarking.rm_Zebra_Horizontal);
					}
					else if(ld4 == Direction.north || ld4 == Direction.south) {
						((RoadCell)createdSim.getCell(x4,y4)).setMarking(RoadMarking.rm_Zebra_Vertical);
					}
					break;
				case "solidline":
					// item 1 solid line direction
					// item 2 3 the point of the road cell
					Direction ld3 = charToDirection(items[1]);
					int x3 = Integer.parseInt(items[2]); 
					int y3 = Integer.parseInt(items[3]);
					if(ld3 == Direction.east) {
						((RoadCell)createdSim.getCell(x3,y3)).setMarking(RoadMarking.rm_solid_line_east);
					}
					else if(ld3 == Direction.west) {
						((RoadCell)createdSim.getCell(x3,y3)).setMarking(RoadMarking.rm_solid_line_west);
					}
					else if(ld3 == Direction.north) {
						((RoadCell)createdSim.getCell(x3,y3)).setMarking(RoadMarking.rm_solid_line_north);
					}
					else if(ld3 == Direction.south) {
						((RoadCell)createdSim.getCell(x3,y3)).setMarking(RoadMarking.rm_solid_line_south);
					}
					break;
				case "solidwhiteline":
					// item 1 solid white line direction
					// item 2 3 the point of road cell 
					Direction ld2 = charToDirection(items[1]);
					int x2 = Integer.parseInt(items[2]); 
					int y2 = Integer.parseInt(items[3]);
					if(ld2 == Direction.east) {
						((RoadCell)createdSim.getCell(x2,y2)).setMarking(RoadMarking.rm_solid_white_line_east);
					}
					else if(ld2 == Direction.west) {
						((RoadCell)createdSim.getCell(x2,y2)).setMarking(RoadMarking.rm_solid_white_line_west);
					}
					else if(ld2 == Direction.north) {
						((RoadCell)createdSim.getCell(x2,y2)).setMarking(RoadMarking.rm_solid_white_line_north);				
					}
					else if(ld2 == Direction.south) {
						((RoadCell)createdSim.getCell(x2,y2)).setMarking(RoadMarking.rm_solid_white_line_south);	
					}
					break;
				case "dottedline":
					// item 1 road line direction
					// item 2 3 point of the road cell
					Direction ld1 = charToDirection(items[1]);
					int x1 = Integer.parseInt(items[2]); 
					int y1 = Integer.parseInt(items[3]);
					if(ld1 == Direction.east) {
						((RoadCell)createdSim.getCell(x1,y1)).setMarking(RoadMarking.rm_dotted_line_east);
					}
					else if(ld1 == Direction.west) {
						((RoadCell)createdSim.getCell(x1,y1)).setMarking(RoadMarking.rm_dotted_line_west);
					}
					else if(ld1 == Direction.north) {
						((RoadCell)createdSim.getCell(x1,y1)).setMarking(RoadMarking.rm_dotted_line_north);
					}
					else if(ld1 == Direction.south) {
						((RoadCell)createdSim.getCell(x1,y1)).setMarking(RoadMarking.rm_dotted_line_south);
					}
					break;
				case "hardshoulder":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_hard_shoulder);
					break;	
				case "pavement":
					//item 1 pavement kerb direction
					//item 2, 3 Point of the pavement
					Direction pd = charToDirection(items[1]);
					int ppx = Integer.parseInt(items[2]); 
					int ppy = Integer.parseInt(items[3]);
					if(pd == Direction.east) {
						((PavementCell)createdSim.getCell(ppx,ppy)).setMarking(PavementMarking.pm_kerb_east);
					}
					else if(pd == Direction.west) {
						((PavementCell)createdSim.getCell(ppx, ppy)).setMarking(PavementMarking.pm_kerb_west);
					}
					else if(pd == Direction.north) {
						((PavementCell)createdSim.getCell(ppx, ppy)).setMarking(PavementMarking.pm_kerb_north);
					}
					else if(pd == Direction.south) {
						((PavementCell)createdSim.getCell(ppx, ppy)).setMarking(PavementMarking.pm_kerb_south);
					}
					break;
				case "car":
					if (items.length <= 6){	
						// item 1 car name
						// item 2 3 car start position
						// item 4 5 car end position
						createdSim.addCar(items[1], new Point(Integer.parseInt(items[2]), Integer.parseInt(items[3])),new Point(Integer.parseInt(items[4]),Integer.parseInt(items[5])));
					}
					else{
						// item 1 car name
						// item 2 3 car start position
						// item 4 5 car end position
						// item 6 identifier
						createdSim.addCar(items[1], new Point(Integer.parseInt(items[2]), Integer.parseInt(items[3])), new Point(Integer.parseInt(items[4]), Integer.parseInt(items[5])),items[6] );
					}
					break;
				case "pedestrian":
					//add pedestrian
					// item 1 pedestrian name
					// item 2 3 ,pedestrian start position
					// item 4 5 ,pedestrian end position
					// item 6, pedestrian moving direction
					createdSim.addPedestrain(items[1], new Point(Integer.parseInt(items[2]),Integer.parseInt(items[3])),new Point(Integer.parseInt(items[4]), Integer.parseInt(items[5])), new Point(Integer.parseInt(items[6]), Integer.parseInt(items[7])),charToDirection(items[8]));
					break;
			}
			line = reader.readLine();
		}
		return createdSim;
	}
}
