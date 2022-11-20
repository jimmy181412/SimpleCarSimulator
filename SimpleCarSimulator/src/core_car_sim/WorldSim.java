package core_car_sim;

import java.util.ArrayDeque;
import java.util.ArrayList;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.w3c.dom.Node;

import core_car_sim.AbstractCell.CellType;


public class WorldSim
{
	private AbstractCell[][] world;
	private ArrayList<CarAddedListener> carAddedListeners = new ArrayList<CarAddedListener>();
	private ArrayList<AbstractCar> cars = new ArrayList<AbstractCar>();
	// all cars position
	private HashMap<AbstractCar, Point> carPositions = new HashMap<AbstractCar, Point>();
	
	private int width;
	private int height;
	private int visability = 2;
	
	public WorldSim(int x, int y)
	{
		width = x;
		height = y;
		world = new AbstractCell[x][y];
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public void addCarAddedListener(CarAddedListener cal)
	{
		carAddedListeners.add(cal);
	}
	
	public void resetCarPositions()
	{
		for (AbstractCar car : cars)
		{
			carPositions.put(car, car.getStartingPosition());
		}
	}
	
	public void simulate(int numOfSteps)
	{
		for (int i = 0; i < numOfSteps; i++)
		{
			updateCells();
			carMovementPhase();
		}
	}
	
	public int speedLimit(int x, int y)
	{
		if (getCell(x, y).getCellType() == CellType.ct_road)
		{
			return ((RoadCell)getCell(x, y)).getSpeedLimit();
		}
		return 0;
	}
	
	private AbstractCar getCarAtPosition(int x, int y)
	{
		for (AbstractCar c : cars)
		{
			if (carPositions.get(c).compareTo(new Point(x, y)) == 0)
			{
				return c;
			}
		}
		return null;
	}
	
	public boolean containsCar(int x, int y)
	{
		for (Point p : carPositions.values())
		{
			if (p.getX() ==  x && p.getY() == y)
			{
				return true;
			}
		}
		return false;
	}

	private void carMovementPhase()
	{
		//Let car view world
		HashMap<AbstractCar, Deque<Direction>> allRoutes = new HashMap<AbstractCar, Deque<Direction>>();
		
		for (AbstractCar car : cars)
		{
			if (!car.isCrashed())
			{
				// The screen that car is able to see
				WorldSim visibleWorld = getVisibleWorldForPosition(carPositions.get(car), true);
				// The position that car in its own visible world
				Point carReferencePoint = new Point(visability,visability);
				car.visibleWorldUpdate(visibleWorld, carReferencePoint);
				
				
				ArrayDeque<RoadCell> path = search(car);	
		
				Deque<Direction> route = car.getSimulationRoute();	
					
				allRoutes.put(car, route);
			}
		}
		
		//for each route entry add position as key, then add car id as pair
		HashMap<AbstractCar, Point> finalPositions = carPositions;	
		//Check for invalid routes / crashes 
		HashMap<Point, AbstractCar> checkPositions = new HashMap<Point, AbstractCar>();
		boolean finishedChecking = false;
		HashSet<AbstractCar> carsFinished = new HashSet<AbstractCar>();
		while (!finishedChecking)
		{
			//Positions only need to be checked at a point in time
			checkPositions.clear();
			for (AbstractCar car : cars)
			{
				if (!car.isCrashed() && !allRoutes.get(car).isEmpty())
				{
					Point currentPosition = finalPositions.get(car);
					Direction nextDirection = allRoutes.get(car).pop();
					currentPosition.moveDirection(nextDirection, getWidth(), getHeight());
					
					if (checkPositions.containsKey(currentPosition))
					{
						//Crash
						car.setCrashed(true);
						checkPositions.get(currentPosition).setCrashed(true);
					}
					else if (!getCell(currentPosition.getX(),currentPosition.getY()).isDriveable())
					{
						//Crash
						car.setCrashed(true);
					}
					else
					{
						checkPositions.put(currentPosition, car);
					}
					finalPositions.put(car, currentPosition);
				}
				else
				{
					carsFinished.add(car);
				}
			}
			finishedChecking = carsFinished.size() == cars.size();
		}
		
		//Move to new position
		for (AbstractCar car : cars)
		{
			
			carPositions.put(car, finalPositions.get(car));
		}
		
	}

	
	private WorldSim getVisibleWorldForPosition(Point currentPosition, boolean looped)
	{	
		
		//(Field of view)FOV of a car
		WorldSim visWorld = new WorldSim((visability * 2) + 1,(visability * 2) + 1);
		visWorld.carAddedListeners = carAddedListeners;   
		visWorld.cars = cars;
		
		//car positions need to be adjusted to visible world
		visWorld.carPositions = new HashMap<AbstractCar, Point>();
		int worldX;
		int worldY;
		for (Entry<AbstractCar, Point> cp : carPositions.entrySet())
		{
			//Adjust car positions based on current cars position
			visWorld.carPositions.put(cp.getKey(), new Point((cp.getValue().getX() - currentPosition.getX()) + visability, 
																(cp.getValue().getY() - currentPosition.getY()) + visability));
		}
		for (int x = 0-visability; x <= visability; x++)
		{
			for (int y = 0-visability; y <= visability; y++)
			{
				worldX = currentPosition.getX() + x;
				worldY = currentPosition.getY() + y;
				if (worldX < 0 || worldX >= getWidth() || worldY < 0 || worldY >= getHeight())
				{
					if (!looped)
					{
						visWorld.setCell(new NonVisibleCell(), x+visability, y+visability);
					}
					else
					{
						worldX = (worldX < 0) ? getWidth() + worldX : worldX % getWidth();
						worldY = (worldY < 0) ? getHeight() + worldY : worldY % getHeight();
						visWorld.setCell(getCell(worldX, worldY), x+visability, y+visability);
					}
				}
				else
				{
					visWorld.setCell(getCell(worldX, worldY), x+visability, y+visability);
				}
			}
		}
		return visWorld;
	}

	private void updateCells()
	{
		for (AbstractCell[] row : world)
		{
			for (AbstractCell cell : row)
			{
				cell.stepSim();
			}
		}
		
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

	public void addCar(String name, Point startPos,Point endPos)
	{
		for (CarAddedListener cal : carAddedListeners)
		{
			AbstractCar createdCar = cal.createCar(name, startPos, endPos);
			cars.add(createdCar);
			carPositions.put(createdCar, startPos );
		}
	}

	public void addCar(String name, Point point,Point endPos, String av)
	{
		for (CarAddedListener cal : carAddedListeners)
		{
			AbstractCar createdCar = cal.createCar(name, point, endPos,av);
			cars.add(createdCar);
			carPositions.put(createdCar, point);
		}
		
	}

	public boolean allFinished()
	{
		for (AbstractCar car : cars)
		{
			if (!car.isCrashed() && !car.isFinished(carPositions.get(car)))
			{
				return false;
			}
		}
		return true;
	}

	public ArrayList<AbstractCar> getCars() 
	{
		return cars;
	}

	public Point getCarPosition(AbstractCar car) 
	{
		return carPositions.get(car);
	}
	
	

	
	public WorldSim getWorldWithValueForACar(AbstractCar car)  {
	
		WorldSim copyWorld = copyWorld();	
		//start position of this car
		Point startPosition = car.getStartingPosition();
		//end position of this car
		Point endPosition = car.getEndPosition();
		
		Point otherPosition;
		int X_gdistance, Y_gdistance, G_value, X_hdistance, Y_hdistance, H_value, F_value;
		
		for(int i = 0; i < copyWorld.width ; i++) {
			for(int j = 0; j < copyWorld.height; j++) {
				otherPosition = new Point(i,j);
//				if( !startPosition.equals(otherPosition) && !endPosition.equals(otherPosition)) {	
					if(copyWorld.getCell(i,j).getCellType()  == CellType.ct_road) {
						RoadCell rc = (RoadCell) copyWorld.getCell(i,j);
							
						X_gdistance = Math.abs(otherPosition.getX() - startPosition.getX());
						Y_gdistance = Math.abs(otherPosition.getY() - startPosition.getY());
					
						// get g value of this position
						G_value = X_gdistance + Y_gdistance;
					 
						X_hdistance = Math.abs(otherPosition.getX() - endPosition.getX());
						Y_hdistance = Math.abs(otherPosition.getY() - endPosition.getY());
					
						H_value = X_hdistance + Y_hdistance;
					
						
						F_value =  G_value + H_value;
						
							rc.setGValue(G_value);
							rc.setFValue(F_value);
							
							System.out.println("Position is: " + rc.getPosition().getX() + " "  + rc.getPosition().getY() + "\n" +
									
							"F value is: " + rc.getFValue()
							+ " G value is: " + rc.getGValue()
							);		
//				}
			}
			}
		}
		
		return copyWorld;
	}
	
	public ArrayDeque<RoadCell> search(AbstractCar car) {
		
		WorldSim world = getWorldWithValueForACar(car);

	
		ArrayList<RoadCell> checkedList  = new ArrayList<>();
		ArrayList<RoadCell> openList = new ArrayList<>();
		
		
		//start position of this car
		Point startPosition = car.getStartingPosition();
		//end position of this car
		Point endPosition = car.getEndPosition();
		
		System.out.println(startPosition.getX() +  " " + startPosition.getY());
		System.out.println(endPosition.getX() +  " " + endPosition.getY());
		
		RoadCell startCell = (RoadCell)world.getCell(startPosition.getX(), startPosition.getY());
		RoadCell endCell = (RoadCell)world.getCell(endPosition.getX(), endPosition.getY());
		
		//at the start, the current cell is the start cell
		RoadCell currentCell = startCell;
		ArrayDeque<RoadCell> routeList = new ArrayDeque<>();
				
		boolean goalReached = false;
		
		openList.add(currentCell);
		
		
				
		while(!goalReached) {
			currentCell.setAsChecked();
			checkedList.add(currentCell);
			openList.remove(currentCell);
			
			//get the current position of current cell
			Point currentPosition = currentCell.getPosition();
			
		
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
			if(y + 1 <= world.getHeight()) {
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
			if(x + 1 <= world.getWidth()) {
				if(world.getCell(x + 1, y).getCellType() == CellType.ct_road) {
					openCell((RoadCell)world.getCell(x + 1,y), currentCell, openList);
				}
				
			}
			
			
//			System.out.println("open list size is: " + openList.size());
//			System.out.println("checked list size is: " + checkedList.size());
			
		
			// we need to find the best cell that the car will choose to go
			
			int bestCellFValue = Integer.MAX_VALUE;
			RoadCell bestCell = null ;
			for(int i = 0; i < openList.size(); i++) {
				if(openList.get(i).getFValue() < bestCellFValue) {
					bestCellFValue = openList.get(i).getFValue();
					bestCell = openList.get(i);
				}
				else if (openList.get(i).getFValue() == bestCellFValue){
					if(openList.get(i).getGValue() < bestCell.getGValue()) {
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
		if( rc.checked == false && rc.open == false) {
			//if the cell is not opened yet, add it to open
			rc.setAsOpen();
			rc.setParent(currentCell);
			openList.add(rc);	
		}
	}
	
	public ArrayDeque<RoadCell> backtrack(RoadCell startCell, RoadCell endCell) {
		
		ArrayDeque<RoadCell> routeList  = new ArrayDeque<>();
		
		RoadCell currentCell = endCell;
		while(!currentCell.getPosition().equals(startCell.getPosition())) {
			currentCell = currentCell.parent;
			routeList.push(currentCell); 		
		}
		
//		System.out.println("path size is: " + routeList.size());
//		
//		int i = 0;
//		int j = routeList.size();
//	
//		while (i < j) {
//			
//			RoadCell rc = routeList.pop();
//			System.out.println(rc.getPosition().getX() + " " + rc.getPosition().getY());
//		
//			i++;
//		}
		return routeList;
	}
	
	// copy of current word
	public WorldSim copyWorld() {
		WorldSim copyWorld = new WorldSim(this.width,this.height);
		copyWorld.carAddedListeners = carAddedListeners;
		copyWorld.cars = cars;
		copyWorld.width = this.width;
		copyWorld.height = this.height;
		
		for(int i = 0; i < this.width; i ++) {
			for(int j = 0; j < this.height;j++) {
				Point cellPosition = new Point(i,j);
				AbstractCell cell = getCell(i,j);
				cell.setPosition(cellPosition);
				copyWorld.setCell(cell,cellPosition) ;
			}
		}
		
		return copyWorld;		
	}
}
