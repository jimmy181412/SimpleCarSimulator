package core_car_sim;

import java.util.ArrayDeque;
import java.util.ArrayList;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;



import core_car_sim.AbstractCell.CellType;
import examples.ExampleAICar;
import examples.ExampleTestingCar;


public class WorldSim{
	private AbstractCell[][] world;
	
	// size of the world
	private int width;
	private int height;
	private int visability = 2;
	
	private ArrayList<CarAddedListener> carAddedListeners = new ArrayList<CarAddedListener>();
	private ArrayList<AbstractCar> cars = new ArrayList<AbstractCar>();
	
	// all cars positions in the world
	private HashMap<AbstractCar, Point> carPositions = new HashMap<AbstractCar, Point>();
	
	public WorldSim(int x, int y){
		this.width = x;
		this.height = y;
		this.world = new AbstractCell[x][y];
	}
	
	public void simulate(int numOfSteps){
		for (int i = 0; i < numOfSteps; i++){
			//update the cells in this step
			updateCells();
			// update cars in this step
			carMovementPhase();
		}
	}
	
	private void updateCells(){
		for (AbstractCell[] row : world){
			for (AbstractCell cell : row){
				cell.stepSim();
			}
		}	
	}
	
	private void carMovementPhase(){
		//Let car view world
		
		//all routes of cars in current grid world
		HashMap<AbstractCar, Deque<Direction>> allRoutes = new HashMap<AbstractCar, Deque<Direction>>();
		
		for (AbstractCar car : cars){
			if (!car.isCrashed()){
				// if the car is finished then there is no need to generate the route for the car
				if(!car.isFinished(carPositions.get(car))) {
					AbstractCar car1 = (AbstractCar)car;
					Point currentPosition = getCarPosition(car1);
					car1.setCurrentPosition(currentPosition);
					
					//currently we got a road cell path that the car should go through;
					ArrayList<RoadCell> path = search(car1);

					ArrayDeque<Direction> path1 = new ArrayDeque<>();

					// check two adjacent cells,
					for(int i = 0; i < path.size() - 1; i++){
						int j = i + 1;
						RoadCell rc1 = path.get(i);
						RoadCell rc2 = path.get(j);
						
						Point rc1_p = rc1.getPosition();
						Point rc2_p = rc2.getPosition();
									
						//east
						if(rc1_p.getX() -1 == rc2_p.getX() && rc1_p.getY() == rc2_p.getY()) {
							path1.push(Direction.east);
						}
						//west
						else if(rc1_p.getX() + 1 == rc2_p.getX() && rc1_p.getY() == rc2_p.getY()) {
							path1.push(Direction.west);
						}
						//north
						else if(rc1_p.getX()== rc2_p.getX() && rc1_p.getY() + 1  == rc2_p.getY()) {
							path1.push(Direction.north);
						}
						//south
						else if(rc1_p.getX() == rc2_p.getX() && rc1_p.getY() - 1 == rc2_p.getY()) {
							path1.push(Direction.south);
						}
					}
				
					car1.setPMD(car1.getCMD());
					//current moving direction of the car is the first direction in the stack
					Direction cmd = path1.peek();
					car1.setCMD(cmd);
				
					// The screen that car is able to see
					WorldSim visibleWorld = getVisibleWorldForPosition(carPositions.get(car1), true);
					// The position that car in its own visible world
					Point carReferencePoint = new Point(visability,visability);
					car1.visibleWorldUpdate(visibleWorld, carReferencePoint);
				
					ArrayDeque<Direction> route = new ArrayDeque<>();
					route = car1.getSimulationRoute();
					allRoutes.put(car, route);
				}
			}
		}
		
		//for each route entry add position as key, then add car id as pair
		HashMap<AbstractCar, Point> finalPositions = carPositions;
		
		//Check for invalid routes / crashes 
		HashMap<Point, AbstractCar> checkPositions = new HashMap<Point, AbstractCar>();
		boolean finishedChecking = false;
		
		// the car finished their journeys
		HashSet<AbstractCar> carsFinished = new HashSet<AbstractCar>();
		
		while (!finishedChecking){
			//Positions only need to be checked at a point in time
			checkPositions.clear();
			
			//check every cars in the grid world
			for (AbstractCar car : cars){
				if(!car.isFinished(carPositions.get(car))) {
					if (!car.isCrashed() && !allRoutes.get(car).isEmpty()){
						//get the current position of the selected car in the grid world
						Point currentPosition = finalPositions.get(car);
						
//						System.out.println("-----------------------------");
//						System.out.println(currentPosition.getX() + " " + currentPosition.getY());
//						System.out.println("-----------------------------");
						
						//get the next direction for the selected car
						Direction nextDirection = allRoutes.get(car).pop();
						
						//change the current moving direction
						currentPosition.moveDirection(nextDirection);
						
//						currentPosition.moveDirection(nextDirection, getWidth(), getHeight());
//						System.out.println("-----after changing---");
//						System.out.println(currentPosition.getX() + " " + currentPosition.getY());
//						System.out.println("-----------------------------");
						
						if (checkPositions.containsKey(currentPosition)){
							//Crash
							car.setCrashed(true);
							//get the current position's car and mark it as crashed
							checkPositions.get(currentPosition).setCrashed(true);
						}
						// if the current position's cell is not driveable, mark the car as crashed
						else if (!getCell(currentPosition.getX(),currentPosition.getY()).isDriveable()){
							car.setCrashed(true);
						}
						// if all fine, add the position and the car to the checked position hashmap 
						else{
							checkPositions.put(currentPosition, car);
						}
						//update the finalPositions list for given car
						finalPositions.put(car, currentPosition);
					}
					else{
						carsFinished.add(car);
					}
				}
				else {
					carsFinished.add(car);
				}
			
			}
			finishedChecking = carsFinished.size() == cars.size();		
		}
		
//		System.out.println("+++++++++++++++++++++++");
//		for(Entry<AbstractCar,Point> entry : finalPositions.entrySet()) {
//			System.out.println(entry.getValue().getX() + " " + entry.getValue().getY());
//		}
		
		//Move to new position
		for (AbstractCar car : cars){	
			carPositions.put(car, finalPositions.get(car));
		}
	}

	private WorldSim getVisibleWorldForPosition(Point currentPosition, boolean looped){		
		//(Field of view)FOV of a car
		WorldSim visWorld = new WorldSim((visability * 2) + 1,(visability * 2) + 1);
		visWorld.carAddedListeners = carAddedListeners;   
		visWorld.cars = cars;
		
		//car positions need to be adjusted to visible world
		visWorld.carPositions = new HashMap<AbstractCar, Point>();
		int worldX;
		int worldY;
		for (Entry<AbstractCar, Point> cp : carPositions.entrySet()){
			//Adjust car positions based on current cars position
			visWorld.carPositions.put(cp.getKey(), new Point((cp.getValue().getX() - currentPosition.getX()) + visability, 
															(cp.getValue().getY() - currentPosition.getY()) + visability));
		}
		for (int x = 0-visability; x <= visability; x++){
			for (int y = 0-visability; y <= visability; y++){
				worldX = currentPosition.getX() + x;
				worldY = currentPosition.getY() + y;
				if (worldX < 0 || worldX >= getWidth() || worldY < 0 || worldY >= getHeight()){
					if (!looped){
						visWorld.setCell(new NonVisibleCell(), x+visability, y+visability);
					}
					else{
						worldX = (worldX < 0) ? getWidth() + worldX : worldX % getWidth();
						worldY = (worldY < 0) ? getHeight() + worldY : worldY % getHeight();
						visWorld.setCell(getCell(worldX, worldY), x+visability, y+visability);
					}
				}
				else{
					visWorld.setCell(getCell(worldX, worldY), x+visability, y+visability);
				}
			}
		}
		return visWorld;
	}

	
	public WorldSim getWorldWithValueForACar(AbstractCar car){
		WorldSim copyWorld = copyWorld();
		
		//current position of the car
		//current position will act as the start position of the car 
		Point currentPosition = car.getCurrentPosition();
		//end position of this car
		Point endPosition = car.getEndPosition();
		
		int X_gdistance, Y_gdistance, G_value, X_hdistance, Y_hdistance, H_value, F_value;
		for(int i = 0; i < copyWorld.width ; i++){
			for(int j = 0; j < copyWorld.height; j++){
					if(copyWorld.getCell(i,j).getCellType() == CellType.ct_road){
						Point otherPosition = new Point(i,j);
						RoadCell rc = (RoadCell) copyWorld.getCell(i,j);
						
						X_gdistance = Math.abs(otherPosition.getX() - currentPosition.getX());
						Y_gdistance = Math.abs(otherPosition.getY() - currentPosition.getY());
						X_hdistance = Math.abs(otherPosition.getX() - endPosition.getX());
						Y_hdistance = Math.abs(otherPosition.getY() - endPosition.getY());
						
						// get g value of this position, g value is the distance from the start position to the current position.
						G_value = X_gdistance + Y_gdistance;
						// get h value of this position, h value is the distance from the current position to the end position.  
						H_value = X_hdistance + Y_hdistance;
						// get the f value of this position, f value is the sum of g value and f value
						F_value =  G_value + H_value;
						
						// set g value and f value to this cell
						rc.setGValue(G_value);
						rc.setFValue(F_value);
						System.out.println("Position is: " + rc.getPosition().getX() + " "  + rc.getPosition().getY() + "\n" +				
						"F value is: " + rc.getFValue()
						+ " G value is: " + rc.getGValue()
						);		
				}
			}
		}
		return copyWorld;
	}
	
	public ArrayList<RoadCell> search(AbstractCar car){	
		WorldSim world = getWorldWithValueForACar(car);
		
		// Initialize the RoadCell in the world(set every RoadCell as unchecked and unopened)
		for(int m = 0 ; m < world.width; m++){
			for(int n = 0 ; n < world.height; n++){
				if(world.getCell(m, n).getCellType() == CellType.ct_road){
					RoadCell rc = (RoadCell) world.getCell(m, n);
					rc.setAsUnChecked();
					rc.setAsUnOpened();
					rc.parent = null;
				}
			}
		}

		ArrayList<RoadCell> checkedList  = new ArrayList<>();
		ArrayList<RoadCell> openList = new ArrayList<>();
		ArrayList<RoadCell> routeList = new ArrayList<>();
		boolean goalReached = false;
			
		//start position of this car
		Point startPosition = car.getCurrentPosition();
		//end position of this car
		Point endPosition = car.getEndPosition();
		//current position of this car
		Point currentPosition = car.getCurrentPosition();
		
		System.out.println(currentPosition.getX() + " " + currentPosition.getY());
		
		//the cell that the self-driving car starts
		RoadCell startCell = (RoadCell)world.getCell(startPosition.getX(), startPosition.getY());
		//the cell that the self-driving car ends 
		RoadCell endCell = (RoadCell)world.getCell(endPosition.getX(), endPosition.getY());
		//the cell that the self-driving car currently at
		RoadCell currentCell = (RoadCell) world.getCell(currentPosition.getX(), currentPosition.getY());

		while(!goalReached) {
			currentCell.setAsChecked();
			checkedList.add(currentCell);
			openList.remove(currentCell);
			
			currentPosition = currentCell.getPosition();

			int x,y;
			x = currentPosition.getX();
			y = currentPosition.getY();
			
			// open the up Cell(north)
			if(y - 1 >= 0) {
				if(world.getCell(x, y - 1).getCellType() == CellType.ct_road) {
					openCell((RoadCell)world.getCell(x,y - 1), currentCell, openList);
				}	
			}
			//open the down Cell(south)
			if(y + 1 < world.getHeight()) {
				if(world.getCell(x, y + 1).getCellType() == CellType.ct_road) {
					openCell((RoadCell)world.getCell(x,y + 1), currentCell, openList);
				}		
			}
			// open the left Cell(west)
			if(x - 1 >= 0) {
				if(world.getCell(x - 1, y).getCellType() == CellType.ct_road) {
					openCell((RoadCell)world.getCell(x - 1,y), currentCell, openList);
				}		
			}
			// open the right Cell (east)
			if(x + 1 < world.getWidth()) {
				if(world.getCell(x + 1, y).getCellType() == CellType.ct_road) {
					openCell((RoadCell)world.getCell(x + 1,y), currentCell, openList);
				}			
			}
			
			// we need to find the best cell that the car will choose to go
			int bestCellFValue = Integer.MAX_VALUE;
			RoadCell bestCell = null;
			for(int i = 0; i < openList.size(); i++) {
				// compare f value
				if(openList.get(i).getFValue() < bestCellFValue) {
					bestCellFValue = openList.get(i).getFValue();
					bestCell = openList.get(i);
				}
				// if f value is equal, we compare g value
				else if (openList.get(i).getFValue() == bestCellFValue){
					if(openList.get(i).getGValue() < bestCell.getGValue()) {
						bestCell = openList.get(i);
					}
					else if(openList.get(i).getGValue() == bestCell.getGValue()) {
						bestCell = openList.get(i);
					}
				}
			}	
		
			currentCell = bestCell;
			
			
			if(currentCell.getPosition() == endCell.getPosition()) {
				goalReached = true;
				routeList = backtrack(startCell,endCell);
			}
		}
		return routeList;
	}
	
	public void openCell(RoadCell rc, RoadCell currentCell, ArrayList<RoadCell> openList) {
		if( rc.checked == false && rc.open == false){
			//if the cell is not opened yet, add it to open
			rc.setAsOpen();
			rc.setParent(currentCell);
			openList.add(rc);	
		}
	}
	
	public ArrayList<RoadCell> backtrack(RoadCell startCell, RoadCell endCell) {
		ArrayList<RoadCell> routeList  = new ArrayList<>();	
		RoadCell currentCell = endCell;
		routeList.add(endCell);
		while(!currentCell.getPosition().equals(startCell.getPosition())){
			currentCell = currentCell.parent;
			routeList.add(currentCell); 	
		}
		return routeList;
	}
	
	// copy of current word
	public WorldSim copyWorld() {
		WorldSim copyWorld = new WorldSim(this.width,this.height);
		copyWorld.carAddedListeners = carAddedListeners;
		copyWorld.cars = cars;
		copyWorld.width = this.width;
		copyWorld.height = this.height;
		for(int i = 0; i < this.width; i ++){
			for(int j = 0; j < this.height;j++){
				Point cellPosition = new Point(i,j);
				AbstractCell cell = getCell(i,j);
				cell.setPosition(cellPosition);
				copyWorld.setCell(cell,cellPosition) ;
			}
		}
		return copyWorld;		
	}
	

	//-----------------------------------------------------------------
	//get width of the world
	public int getWidth(){
		return this.width;
	}
	
	//get height of the world
	public int getHeight(){
		return this.height;
	}
	
	public AbstractCell getCell(int x, int y) 
	{
		return world[x][y];
	}

	public void setCell(AbstractCell cell, Point pt)
	{
		setCell(cell, pt.getX(), pt.getY());
	}
	
	public void setCell(AbstractCell cell, int x, int y)
	{
		world[x][y] = cell;
	}

	
	//get the speed limit at position (x,y)
	public int speedLimit(int x, int y){
		if (getCell(x, y).getCellType() == CellType.ct_road){
			return ((RoadCell)getCell(x, y)).getSpeedLimit();
		}
		return 0;
	}
	
	//-----------------------------------------------------------------
	
	public void addCarAddedListener(CarAddedListener cal){
		carAddedListeners.add(cal);
	}
	
	public void addCar(String name, Point startPos,Point endPos){
		for (CarAddedListener cal : carAddedListeners){
			AbstractCar createdCar = cal.createCar(name, startPos, endPos);
			cars.add(createdCar);
			carPositions.put(createdCar, startPos);
		}
	}

	public void addCar(String name, Point startPos,Point endPos, String av){
		for (CarAddedListener cal : carAddedListeners){
			AbstractCar createdCar = cal.createCar(name, startPos, endPos,av);
			cars.add(createdCar);
			carPositions.put(createdCar, startPos);
		}
	}
	
	//check whether there are cars at position (x,y)
	public boolean containsCar(int x, int y){
		for (Point p : carPositions.values()){
			if (p.getX() ==  x && p.getY() == y){
				return true;
			}
		}
		return false;
	}
	
	//get the car at position (x,y)
	public AbstractCar getCarAtPosition(int x, int y){
		for (AbstractCar c : cars){
			if (containsCar(x,y)){
				return c;
			}
		}
		return null;
	}
	
	//get all cars in the world
	public ArrayList<AbstractCar> getCars(){
		return cars;
	}

	//get the position of a car
	public Point getCarPosition(AbstractCar car){
		return carPositions.get(car);
	}
	
	//reset all cars positions to their start positions
	public void resetCarPositions(){
		for (AbstractCar car : cars){
			carPositions.put(car, car.getStartingPosition());
		}
	}
	
	public boolean allFinished(){
		for (AbstractCar car : cars){
			if (!car.isFinished(carPositions.get(car))){
				return false;
			}
		}
		return true;
	}
}