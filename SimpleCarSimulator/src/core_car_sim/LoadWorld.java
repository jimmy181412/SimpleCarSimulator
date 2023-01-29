package core_car_sim;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
	
	public static WorldSim loadWorldFromFile(BufferedReader reader, CarAddedListener cal) throws IOException{
		String widthStr = reader.readLine();
		String heightStr = reader.readLine();
		WorldSim createdSim = new WorldSim(Integer.parseInt(widthStr), Integer.parseInt(heightStr));
		createdSim.addCarAddedListener(cal);
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
						createdSim.setCell(new RoadCell(charToDirection(line.charAt(x)), false, null, defaultSpeedLimit), x, y);
						break;
					case '+':
						tmp.clear();
						tmp.add(Direction.north);
						tmp.add(Direction.south);
						tmp.add(Direction.east);
						tmp.add(Direction.west);
						createdSim.setCell(new RoadCell(tmp, false, null, defaultSpeedLimit), x, y);
						break;
					case 'p':
						tmp.clear();
						tmp.add(Direction.north);
						tmp.add(Direction.south);
						tmp.add(Direction.east);
						tmp.add(Direction.west);
						createdSim.setCell(new RoadCell(tmp,true,null,defaultSpeedLimit),x,y);
						break;
				}
			}
		}
		line = reader.readLine();
		while (line != null){
			String[] items = line.split(" ");
			switch (items[0].toLowerCase()){
				//traffic light faces west
				case "trll":	
					int trlx = Integer.parseInt(items[2]);
					int trly = Integer.parseInt(items[3]);
					int stopsX = Integer.parseInt(items[4]);
					int stopsY = Integer.parseInt(items[5]);
					createdSim.setCell(new TrafficLightCell(charToDirection(items[6]), 3, new Point(stopsX, stopsY), new Point(stopsX - trlx, stopsY - trly),1), new Point(trlx, trly));
					((RoadCell)createdSim.getCell(stopsX, stopsY)).setMarking(RoadMarking.rm_HorizontalWhiteLineRight);
					break;
				// traffic light faces south
				case "trld":
					trlx = Integer.parseInt(items[2]);
					trly = Integer.parseInt(items[3]);
					stopsX = Integer.parseInt(items[4]);
					stopsY = Integer.parseInt(items[5]);
					createdSim.setCell(new TrafficLightCell(charToDirection(items[6]), 3, new Point(stopsX, stopsY), new Point(stopsX - trlx, stopsY - trly),2), new Point(trlx, trly));
					((RoadCell)createdSim.getCell(stopsX, stopsY)).setMarking(RoadMarking.rm_VerticalWhiteLineUp);
					break;
				// traffic light faces north
				case "trlu":
					trlx = Integer.parseInt(items[2]);
					trly = Integer.parseInt(items[3]);
					stopsX = Integer.parseInt(items[4]);
					stopsY = Integer.parseInt(items[5]);
					createdSim.setCell(new TrafficLightCell(charToDirection(items[6]), 3, new Point(stopsX, stopsY), new Point(stopsX - trlx, stopsY - trly),3), new Point(trlx, trly));
					((RoadCell)createdSim.getCell(stopsX, stopsY)).setMarking(RoadMarking.rm_VerticalWhiteLineDown);
					break;
				//traffic light faces east
				case "trlr":
					trlx = Integer.parseInt(items[2]);
					trly = Integer.parseInt(items[3]);
					stopsX = Integer.parseInt(items[4]);
					stopsY = Integer.parseInt(items[5]);
					createdSim.setCell(new TrafficLightCell(charToDirection(items[6]), 3, new Point(stopsX, stopsY), new Point(stopsX - trlx, stopsY - trly),4), new Point(trlx, trly));
					((RoadCell)createdSim.getCell(stopsX, stopsY)).setMarking(RoadMarking.rm_HorizontalWhiteLineLeft);
					break;
				case "zebrah":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_Zebra_Horizontal);
					break;
				case "zebrav":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_Zebra_Vertical);
					break;
				case "busline":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_BusLine);
					break;
				case "solidlineright":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_solid_line_right);
					break;
				case "solidlineleft":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_solid_line_left);
					break;
				case "solidlineup":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_solid_line_up);
					break;
				case "solidlinedown":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_solid_line_down);
					break;	
				case "swlr":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_solid_white_line_right);
					break;
				case "swll":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_solid_white_line_left);
					break;
				case "swlu":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_solid_white_line_up);
					break;
				case "swld":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_solid_white_line_down);
					break;
				case "dottedlineright":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_dotted_line_right);
					break;
				case "dottedlineleft":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_dotted_line_left);
					break;
				case "dottedlineup":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_dotted_line_up);
					break;
				case "dottedlinedown":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_dotted_line_down);
					break;
				case "hardshoulder":
					((RoadCell)createdSim.getCell(Integer.parseInt(items[1]),Integer.parseInt(items[2]))).setMarking(RoadMarking.rm_hard_shoulder);
					break;
				case "car":
					if (items.length <= 6){	
						createdSim.addCar(items[1], new Point(Integer.parseInt(items[2]), Integer.parseInt(items[3])),new Point(Integer.parseInt(items[4]),Integer.parseInt(items[5])));
					}
					else{
						createdSim.addCar(items[1], new Point(Integer.parseInt(items[2]), Integer.parseInt(items[3])), new Point(Integer.parseInt(items[4]), Integer.parseInt(items[5])),items[6] );
					}
					break;
			}
			line = reader.readLine();
		}
		return createdSim;
	}
}
