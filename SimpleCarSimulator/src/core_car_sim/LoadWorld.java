package core_car_sim;

import core_car_sim.PavementCell.PavementMarking;
import core_car_sim.RoadCell.RoadMarking;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoadWorld{
	public static Direction charToDirection(char dir){
		return switch (dir) {
			case '>' -> Direction.east;
			case '<' -> Direction.west;
			case '^' -> Direction.north;
			case 'V' -> Direction.south;
			default -> null;
		};
	}
	
	public static Direction charToDirection(String dir){
		return charToDirection(dir.charAt(0));
	}
	
	public static WorldSim loadWorldFromFile(BufferedReader reader, CarAddedListener cal,
											 PedestrianAddedListener pal,
											 String greenLightPath,
											 String yellowLightPath,
											 String redLightPath
	) throws IOException{
		String widthStr = reader.readLine();
		String heightStr = reader.readLine();
		WorldSim createdSim = new WorldSim(Integer.parseInt(widthStr), Integer.parseInt(heightStr));

		createdSim.addCarAddedListener(cal);
		//add pedestrian listener
		createdSim.addPedestrianListener(pal);
		
		int defaultSpeedLimit = Integer.parseInt(reader.readLine());
		String line;
		ArrayList<Direction> tmp = new ArrayList<>();
		for (int y = 0; y < createdSim.getHeight(); y++){
			line = reader.readLine();
			for (int x = 0; x < createdSim.getWidth(); x++){
				switch (line.charAt(x)) {
					case '|', '-' -> createdSim.setCell(new NonDrivingCell(), x, y);
					case '>', '<', '^', 'V' ->
							createdSim.setCell(new RoadCell(charToDirection(line.charAt(x)), null, defaultSpeedLimit), x, y);
					case '+' -> {
						tmp.clear();
						tmp.add(Direction.north);
						tmp.add(Direction.south);
						tmp.add(Direction.east);
						tmp.add(Direction.west);
						createdSim.setCell(new RoadCell(tmp, null, defaultSpeedLimit), x, y);
					}
					case 'p' -> createdSim.setCell(new PavementCell(), x, y);
					default -> createdSim.setCell(new NonDrivingCell(), x, y);
				}
			}
		}
		line = reader.readLine();
		while (line != null){
			String[] items = line.split(" ");
			switch (items[0].toLowerCase()) {
				//traffic light faces west
				case "trafficlight" -> {
					Direction ld5 = charToDirection(items[1]);
					//the location of the traffic light
					int x5 = Integer.parseInt(items[2]);
					int y5 = Integer.parseInt(items[3]);
					Point roadEffectedLocation = new Point(x5, y5);
					//the location of the white line
					int x6 = Integer.parseInt(items[4]);
					int y6 = Integer.parseInt(items[5]);
					Point roadEffectedReference = new Point(x6, y6);

					//traffic light faces east
					if (ld5 == Direction.east) {
						createdSim.setCell(new TrafficLightCell(ld5, 3, roadEffectedLocation, roadEffectedReference, 1,greenLightPath,yellowLightPath,redLightPath), roadEffectedLocation);
						((RoadCell) createdSim.getCell(x6, y6)).setMarking(RoadMarking.rm_stop_line_at_signal_east);
					}
					//traffic light faces west
					else if (ld5 == Direction.west) {
						createdSim.setCell(new TrafficLightCell(ld5, 3, roadEffectedLocation, roadEffectedReference, 3,greenLightPath,yellowLightPath,redLightPath), roadEffectedLocation);
						((RoadCell) createdSim.getCell(x6, y6)).setMarking(RoadMarking.rm_stop_line_at_signal_west);
					}
					//traffic light faces north
					else if (ld5 == Direction.north) {
						createdSim.setCell(new TrafficLightCell(ld5, 3, roadEffectedLocation, roadEffectedReference, 4,greenLightPath,yellowLightPath,redLightPath), roadEffectedLocation);
						((RoadCell) createdSim.getCell(x6, y6)).setMarking(RoadMarking.rm_stop_line_at_signal_north);
					}
					//traffic light faces south
					else if (ld5 == Direction.south) {
						createdSim.setCell(new TrafficLightCell(ld5, 3, roadEffectedLocation, roadEffectedReference, 2,greenLightPath,yellowLightPath,redLightPath), roadEffectedLocation);
						((RoadCell) createdSim.getCell(x6, y6)).setMarking(RoadMarking.rm_stop_line_at_signal_south);
					}
				}
				case "zebracrossing" -> {
					//item 1 zebra crossing direction
					//item 2 3 the point of the road cell
					Direction ld4 = charToDirection(items[1]);
					int x4 = Integer.parseInt(items[2]);
					int y4 = Integer.parseInt(items[3]);
					if (ld4 == Direction.east || ld4 == Direction.west) {
						((RoadCell) createdSim.getCell(x4, y4)).setMarking(RoadMarking.rm_Zebra_Horizontal);
					} else if (ld4 == Direction.north || ld4 == Direction.south) {
						((RoadCell) createdSim.getCell(x4, y4)).setMarking(RoadMarking.rm_Zebra_Vertical);
					}
				}
				case "edgeline" -> {
					// item 1 solid line direction
					// item 2 3 the point of the road cell
					Direction ld3 = charToDirection(items[1]);
					int x3 = Integer.parseInt(items[2]);
					int y3 = Integer.parseInt(items[3]);
					if (ld3 == Direction.east) {
						((RoadCell) createdSim.getCell(x3, y3)).setMarking(RoadMarking.rm_edge_line_east);
					} else if (ld3 == Direction.west) {
						((RoadCell) createdSim.getCell(x3, y3)).setMarking(RoadMarking.rm_edge_line_west);
					} else if (ld3 == Direction.north) {
						((RoadCell) createdSim.getCell(x3, y3)).setMarking(RoadMarking.rm_edge_line_north);
					} else if (ld3 == Direction.south) {
						((RoadCell) createdSim.getCell(x3, y3)).setMarking(RoadMarking.rm_edge_line_south);
					}
				}
				case "centreline" -> {
					// item 1 road line direction
					// item 2 3 point of the road cell
					Direction ld1 = charToDirection(items[1]);
					int x1 = Integer.parseInt(items[2]);
					int y1 = Integer.parseInt(items[3]);
					if (ld1 == Direction.east) {
						((RoadCell) createdSim.getCell(x1, y1)).setMarking(RoadMarking.rm_centre_line_east);
					} else if (ld1 == Direction.west) {
						((RoadCell) createdSim.getCell(x1, y1)).setMarking(RoadMarking.rm_centre_line_west);
					} else if (ld1 == Direction.north) {
						((RoadCell) createdSim.getCell(x1, y1)).setMarking(RoadMarking.rm_centre_line_north);
					} else if (ld1 == Direction.south) {
						((RoadCell) createdSim.getCell(x1, y1)).setMarking(RoadMarking.rm_centre_line_south);
					}
				}
				case "hazardwarningline" ->{
					//item 1 hazard warning line direction
					//item 2 3 point of the road cell
					Direction hwld = charToDirection(items[1]);
					int hwldx = Integer.parseInt(items[2]);
					int hwldy = Integer.parseInt(items[3]);
					if(hwld == Direction.east){
						((RoadCell) createdSim.getCell(hwldx,hwldy)).setMarking(RoadMarking.rm_hazard_warning_line_east);
					}
					else if(hwld == Direction.west){
						((RoadCell) createdSim.getCell(hwldx,hwldy)).setMarking(RoadMarking.rm_hazard_warning_line_west);
					}
					else if(hwld == Direction.north){
						((RoadCell) createdSim.getCell(hwldx,hwldy)).setMarking(RoadMarking.rm_hazard_warning_line_north);
					}
					else if(hwld == Direction.south){
						((RoadCell) createdSim.getCell(hwldx,hwldy)).setMarking(RoadMarking.rm_hazard_warning_line_south);
					}
				}
				case "doublewhitelinebroken" -> {
					//item 1 double white line broken direction
					//item 2 3 point of the road cell
					Direction dwlbd = charToDirection(items[1]);
					int dwlbx = Integer.parseInt(items[2]);
					int dwlby = Integer.parseInt(items[3]);
					if(dwlbd == Direction.east){
						((RoadCell) createdSim.getCell(dwlbx,dwlby)).setMarking(RoadMarking.rm_double_white_line_broken_east);
					}
					else if(dwlbd == Direction.west){
						((RoadCell) createdSim.getCell(dwlbx,dwlby)).setMarking(RoadMarking.rm_double_white_line_broken_west);
					}
					else if(dwlbd == Direction.north){
						((RoadCell) createdSim.getCell(dwlbx,dwlby)).setMarking(RoadMarking.rm_double_white_line_broken_north);
					}
					else if(dwlbd == Direction.south){
						((RoadCell) createdSim.getCell(dwlbx,dwlby)).setMarking(RoadMarking.rm_double_white_line_broken_south);
					}
				}
				case "doublewhitelinesolid" -> {
					//item 1 double white line solid direction
					//item 2 3 point of the road cell
					Direction dwlsd = charToDirection(items[1]);
					int dwlsx = Integer.parseInt(items[2]);
					int dwlsy = Integer.parseInt(items[3]);
					if(dwlsd == Direction.east){
						((RoadCell) createdSim.getCell(dwlsx,dwlsy)).setMarking(RoadMarking.rm_double_white_line_solid_east);
					}
					else if(dwlsd == Direction.west){
						((RoadCell) createdSim.getCell(dwlsx,dwlsy)).setMarking(RoadMarking.rm_double_white_line_solid_west);
					}
					else if(dwlsd == Direction.north){
						((RoadCell) createdSim.getCell(dwlsx,dwlsy)).setMarking(RoadMarking.rm_double_white_line_solid_north);
					}
					else if(dwlsd == Direction.south){
						((RoadCell) createdSim.getCell(dwlsx,dwlsy)).setMarking(RoadMarking.rm_double_white_line_solid_south);
					}
				}
				case "laneline" -> {
					//item 1 lane line direction
					//item 2 3 point of road cell
					Direction lld = charToDirection(items[1]);
					int llx = Integer.parseInt(items[2]);
					int lly = Integer.parseInt(items[3]);
					if(lld == Direction.east){
						((RoadCell) createdSim.getCell(llx,lly)).setMarking(RoadMarking.rm_lane_line_east);
					}
					else if(lld == Direction.west){
						((RoadCell) createdSim.getCell(llx,lly)).setMarking(RoadMarking.rm_lane_line_west);
					}
					else if(lld == Direction.north){
						((RoadCell) createdSim.getCell(llx,lly)).setMarking(RoadMarking.rm_lane_line_north);
					}
					else if(lld == Direction.south){
						((RoadCell) createdSim.getCell(llx,lly)).setMarking(RoadMarking.rm_lane_line_south);
					}
				}
				case "pavement" -> {
					//item 1 pavement kerb direction
					//item 2, 3 Point of the pavement
					Direction pd = charToDirection(items[1]);
					int ppx = Integer.parseInt(items[2]);
					int ppy = Integer.parseInt(items[3]);
					if (pd == Direction.east) {
						((PavementCell) createdSim.getCell(ppx, ppy)).setMarking(PavementMarking.pm_kerb_east);
					} else if (pd == Direction.west) {
						((PavementCell) createdSim.getCell(ppx, ppy)).setMarking(PavementMarking.pm_kerb_west);
					} else if (pd == Direction.north) {
						((PavementCell) createdSim.getCell(ppx, ppy)).setMarking(PavementMarking.pm_kerb_north);
					} else if (pd == Direction.south) {
						((PavementCell) createdSim.getCell(ppx, ppy)).setMarking(PavementMarking.pm_kerb_south);
					}
				}
				case "car" -> {
					if (items.length <= 9) {
						// item 1 car name
						// item 2 3 car start position
						// item 4 5 car end position
						// item 6 7 car reference position
						// item 8 car initial Direction
						createdSim.addCar(items[1], new Point(Integer.parseInt(items[2]), Integer.parseInt(items[3])), new Point(Integer.parseInt(items[4]), Integer.parseInt(items[5])), new Point(Integer.parseInt(items[6]), Integer.parseInt(items[7])),
								charToDirection(items[8]));
					} else {
						// item 1 car name
						// item 2 3 car start position
						// item 4 5 car end position
						// item 6 7 car reference position
						// item 8 car initial direction
						// item 9 identifier
						createdSim.addCar(items[1], new Point(Integer.parseInt(items[2]), Integer.parseInt(items[3])), new Point(Integer.parseInt(items[4]), Integer.parseInt(items[5])), new Point(Integer.parseInt(items[6]), Integer.parseInt(items[7])),
								charToDirection(items[8]),items[9]);
					}
				}
				case "pedestrian" ->
					//add pedestrian
					// item 1 pedestrian name
					// item 2 3 ,pedestrian start position
					// item 4 5 ,pedestrian end position
					// item 6 7 ,pedestrian reference position
					// item 8, pedestrian moving direction
						createdSim.addPedestrian(items[1], new Point(Integer.parseInt(items[2]), Integer.parseInt(items[3])), new Point(Integer.parseInt(items[4]), Integer.parseInt(items[5])), new Point(Integer.parseInt(items[6]), Integer.parseInt(items[7])), charToDirection(items[8]));
			}
			line = reader.readLine();
		}
		return createdSim;
	}
}
