package core_car_sim;
import java.util.*;

import java.util.Map.Entry;
import core_car_sim.AbstractCell.CellType;
import examples.ExampleAICar;
import examples.ExampleTestingCar;

public class WorldSim{
	private AbstractCell[][] world;

	// size of the world
	private int width;
	private int height;
	private int visibility = 3;

	public ArrayList<CarAddedListener> carAddedListeners = new ArrayList<>();
	public ArrayList<AbstractCar> cars = new ArrayList<>();
	//(store car and its position key value pair)
	public HashMap<AbstractCar, Point> carPositions = new HashMap<>();


	// Pedestrian
	public ArrayList<PedestrianAddedListener> pals = new ArrayList<>();
	public ArrayList<Pedestrian> pedestrians = new ArrayList<>();
	public HashMap<Pedestrian, Point> pedestrianPositions = new HashMap<>();

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
			//update pedestrians in this step
			pedestrianMovementPhase();
		}
	}

	private void updateCells(){
		for (AbstractCell[] row : world){
			for (AbstractCell cell : row){
				cell.stepSim();
			}
		}
	}

	private void pedestrianMovementPhase() {
		// all routes of pedestrians in the current grid world
		HashMap<Pedestrian, Deque<Direction>> AllRoutes = new HashMap<>();
		for(Pedestrian p : pedestrians) {
			if(!p.isFinished(this.getPedestrianPosition(p))) {
				ArrayDeque<Direction> route = p.getSimulationRoute();
				AllRoutes.put(p, route);
			}
		}
		for(Pedestrian p: pedestrians) {
			//get the current position of the pedestrian
			Point currentPosition = this.pedestrianPositions.get(p);
			if(!p.isFinished(currentPosition)) {
				Direction d = AllRoutes.get(p).pop();
				currentPosition.moveDirection(d);
				//update the position
				pedestrianPositions.put(p, currentPosition);
			}
			//reset the pedestrian
			else {
				int x = p.getReferencePosition().getX();
				int y = p.getReferencePosition().getY();
				Point origin = new Point(x,y);
				pedestrianPositions.put(p, origin);
			}
		}
	}

	private void carMovementPhase(){
		//all routes of cars in current grid world
		HashMap<AbstractCar, Deque<Direction>> allRoutes = new HashMap<>();
		for (AbstractCar car : cars){
			if (!car.isCrashed()){
				// if the car is finished then there is no need to generate the route for the car
				if(!car.isFinished(carPositions.get(car))) {
					//currently we got a road cell path that the car should go through;
					ArrayList<RoadCell> path = car.search(this);
					ArrayDeque<Direction> path1 = car.getPathByRoadCell(path);
					car.setPMD(car.getCMD());
					//current moving direction of the car is the first direction in the stack
					car.setCMD(path1.peek());
					car.setCurrentMovingDirectionList(path1);

					// The screen that car is able to see
					WorldSim visibleWorld = car.getVisibleWorldForPosition(this, carPositions.get(car));
					// The position that car in its own visible world
					Point carReferencePoint = new Point(visibility, visibility);
					//update beliefs and intentions for the car
					car.visibleWorldUpdate(visibleWorld, carReferencePoint);

					ArrayDeque<Direction> route = car.getSimulationRoute(this);
					allRoutes.put(car, route);
				}
			}
		}
		//for each route entry add position as key, then add car id as pair
		HashMap<AbstractCar, Point> finalPositions = carPositions;
		//Check for invalid routes / crashes
		HashMap<Point, AbstractCar> checkPositions = new HashMap<>();
		boolean finishedChecking = false;
		// the car finished their journeys
		HashSet<AbstractCar> carsFinished = new HashSet<>();
		while (!finishedChecking){
			//Positions only need to be checked at a point in time
			checkPositions.clear();
			//check every car in the grid world
			for (AbstractCar car : cars){
				if(!car.isFinished(carPositions.get(car))) {
					if (!car.isCrashed() && !allRoutes.get(car).isEmpty()){
						//get the current position of the selected car in the grid world
						Point currentPosition = finalPositions.get(car);
						//get the next direction for the selected car
						Direction nextDirection = allRoutes.get(car).pop();
						//change the current moving direction
						currentPosition.moveDirection(nextDirection);
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
					//reset car position

					if(car.getClass() == ExampleAICar.class){
						int x = car.getReferencePosition().getX();
						int y = car.getReferencePosition().getY();
						Point origin = new Point(x,y);
						carPositions.put(car, origin);
					}
					carsFinished.add(car);

				}
			}
			finishedChecking = carsFinished.size() == cars.size();
		}
		//Move to new position
		for (AbstractCar car : cars){
			carPositions.put(car, finalPositions.get(car));
			car.setCurrentPosition(finalPositions.get(car));
		}
	}

	//-----------------------------------------------------------------
	//get width of the world
	public int getWidth(){
		return this.width;
	}
	public void setWidth(int width){
		this.width = width;
	}

	//get height of the world
	public int getHeight(){
		return this.height;
	}

	public void setHeight(int height){
		this.height = height;
	}

	//get the visibility of the world
	public int getVisibility() {
		return this.visibility;
	}

	public void setVisibility(int visibility){
		this.visibility = visibility;
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
	public int getSpeedLimit(int x, int y){
		if (getCell(x, y).getCellType() == CellType.ct_road){
			return ((RoadCell)getCell(x, y)).getSpeedLimit();
		}
		return 0;
	}
	public void addCarAddedListener(CarAddedListener cal){carAddedListeners.add(cal);
	}

	public void addCar(String name, Point startPos,Point endPos, Point referencePos, Direction d){
		for (CarAddedListener cal : carAddedListeners){
			AbstractCar createdCar = cal.createCar(name, startPos, endPos, referencePos, d);
			cars.add(createdCar);
			carPositions.put(createdCar, startPos);
		}
	}

	public void addCar(String name, Point startPos,Point endPos, Point referencePos, Direction d, String av){
		for (CarAddedListener cal : carAddedListeners){
			AbstractCar createdCar = cal.createCar(name, startPos, endPos, referencePos, d,av);
			cars.add(createdCar);
			carPositions.put(createdCar, startPos);
		}
	}

	//check whether there are cars at position (x,y)
	public boolean containsCar(int x, int y){
		Point p = new Point(x, y);
		for(Entry<AbstractCar, Point> entry : carPositions.entrySet()) {
			if(entry.getValue().equals(p)) {
				return true;
			}
		}
		return false;
	}

	//get the car at position (x,y)
	public AbstractCar getCarAtPosition(int x, int y){
		Point p = new Point(x,y);
		for(Entry<AbstractCar, Point> entry : carPositions.entrySet()) {
			if(entry.getValue().equals(p)) {
				return entry.getKey();
			}
		}
		return null;
	}

	//get all cars in the world
	public ArrayList<AbstractCar> getCars(){
		return cars;
	}

	public void setCars(ArrayList<AbstractCar> cars){
		this.cars = cars;
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

	public HashMap<AbstractCar,Point> getCarPositionsList(){
		return carPositions;
	}

	public void addPedestrianListener(PedestrianAddedListener pal) {
		pals.add(pal);
	}

	//add pedestrian to the world
	public void addPedestrian(String name, Point startPos, Point endPos, Point referencePos, Direction d) {
		for(PedestrianAddedListener pal : pals) {
			Pedestrian p = pal.createPedestrians(name, startPos, endPos,referencePos,d);
			pedestrians.add(p);
			pedestrianPositions.put(p,startPos);
		}
	}

	//get all pedestrian in the world
	public ArrayList<Pedestrian> getPedestrian(){
		return this.pedestrians;
	}

	public Point getPedestrianPosition(Pedestrian p) {
		return this.pedestrianPositions.get(p);
	}

	public HashMap<Pedestrian, Point> getPedestrianPositionList(){
		return this.pedestrianPositions;
	}

	public boolean containsPedestrian(int x, int y) {
		Point p = new Point(x,y);
		for(Entry<Pedestrian, Point> entry : pedestrianPositions.entrySet()) {
			if(entry.getValue().equals(p)) {
				return true;
			}
		}
		return false;
	}

	//get the car at position (x,y)
	public Pedestrian getPedestrianAtPosition(int x, int y){
		Point p = new Point(x,y);
		for(Entry<Pedestrian, Point> entry : pedestrianPositions.entrySet()) {
			if(entry.getValue().equals(p)) {
				return entry.getKey();
			}
		}
		return null;
	}
}