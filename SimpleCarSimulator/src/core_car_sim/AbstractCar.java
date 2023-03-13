package core_car_sim;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;


import javax.swing.ImageIcon;


// the father of all kind of cars
// Abstract RoTRA
// 
public abstract class AbstractCar{
	public enum CarType{
		car_large,
		car_small,
		car_AI
	}

	private CarType carType;
	private Point startingPosition;
	private Point endPosition;
	private Point referencePosition;
	private  Point currentPosition;

	private int speed;
	protected ImageIcon carIcon;
	public boolean crashed;

	public Direction cmd;
	public Direction pmd;
	public ArrayDeque<Direction> currentMovingDirectionList = new ArrayDeque<>();

	protected ImageIcon northCarIcon;
	protected ImageIcon southCarIcon;
	protected ImageIcon eastCarIcon;
	protected ImageIcon westCarIcon;
	
	protected abstract void visibleWorldUpdate(WorldSim visibleWorld, Point location);
	protected abstract ArrayDeque<Direction> getSimulationRoute(WorldSim world);
	protected abstract boolean isFinished(Point point);
	
	public AbstractCar(Point startPos, Point endPos,Point referencePosition, int startingSpeed, Direction initialDirection,String fileImage1,String fileImage2, String fileImage3, String fileImage4, CarType ct){
		this.startingPosition = startPos;
		this.endPosition = endPos;
		this.currentPosition = startPos;
		this.referencePosition = referencePosition;
		this.speed = startingSpeed;
		this.cmd = initialDirection;
		this.pmd = initialDirection;
		if(initialDirection == Direction.north){
			this.carIcon = new ImageIcon(fileImage1);
		}
		else if(initialDirection == Direction.south){
			this.carIcon = new ImageIcon(fileImage2);
		}
		else if(initialDirection == Direction.east){
			this.carIcon = new ImageIcon(fileImage3);
		}
		else if(initialDirection == Direction.west){
			this.carIcon = new ImageIcon(fileImage4);
		}
		this.northCarIcon = new ImageIcon(fileImage1);
		this.southCarIcon = new ImageIcon(fileImage2);
		this.eastCarIcon = new ImageIcon(fileImage3);
		this.westCarIcon = new ImageIcon(fileImage4);
		this.carType = ct;
	}
	
	// getter and setter of current moving direction and previous moving direction of the car
	public void setCMD(Direction cmd){
		this.cmd = cmd;
	}
	
	public Direction getCMD(){
		return this.cmd;
	}
	
	public void setPMD(Direction pmd){
		this.pmd = pmd;
	}
	
	public Direction getPMD(){
		return this.pmd;
	}
	
	//getter and setter of speed
	public int getSpeed(){
		return speed;
	}
	
	public void setSpeed(int speed){
		this.speed = speed;
	}
	
	// getters and setters of positions.. 
	public Point getStartingPosition(){
		return startingPosition;
	}
	
	public Point getEndPosition() {
		return endPosition;
	}
	
	public Point getCurrentPosition(){
		return this.currentPosition;
	}
	
	public void setEndPosition(Point endPosition){
		this.endPosition = endPosition;
	}

	public void setStartingPosition(Point startingPosition){
		this.startingPosition = startingPosition;
	}
	
	public void setCurrentPosition(Point currentPosition){
		this.currentPosition = currentPosition;
	}
	// getter and setter of crash
	public boolean isCrashed(){
		return crashed;
	}
	
	public void setCrashed(boolean crashed){
		this.crashed = crashed;
	}
	
	public ImageIcon getCarIcon(){
		return carIcon;
	}

	public ArrayDeque<Direction> getCurrentMovingDirectionList(){
		return this.currentMovingDirectionList;
	}

	public void setCurrentMovingDirectionList(ArrayDeque<Direction> currentMovingDirectionList){
		this.currentMovingDirectionList = currentMovingDirectionList;
	}

	public CarType getCarType(){
		return  this.carType;
	}

	public Point getReferencePosition() {
		return referencePosition;
	}
	public void setReferencePosition(Point referencePosition) {
		this.referencePosition = referencePosition;
	}

	public WorldSim copyWorld(WorldSim world) {
		WorldSim copyWorld = new WorldSim(world.getWidth(),world.getHeight());
		copyWorld.setWidth(world.getWidth());
		copyWorld.setHeight(world.getHeight());
		copyWorld.setVisibility(world.getVisibility());
		for(int i = 0; i < world.getWidth(); i ++){
			for(int j = 0; j < world.getHeight();j++){
				Point cellPosition = new Point(i,j);
				AbstractCell cell = world.getCell(i,j);
				cell.setPosition(cellPosition);
				copyWorld.setCell(cell,cellPosition) ;
			}
		}
		return copyWorld;
	}

	public WorldSim getWorldWithValue(WorldSim world){
		WorldSim copyWorld = copyWorld(world);
		//current position of the car
		//current position will act as the start position of the car
		Point currentPosition = this.getCurrentPosition();
		//end position of this car
		Point endPosition = this.getEndPosition();

		int X_gdistance, Y_gdistance, G_value, X_hdistance, Y_hdistance, H_value, F_value;
		for(int i = 0; i < copyWorld.getWidth() ; i++){
			for(int j = 0; j < copyWorld.getHeight(); j++){
				if(copyWorld.getCell(i,j).getCellType() == AbstractCell.CellType.ct_road){
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
					rc.setHvalue(H_value);
					rc.setGvalue(G_value);
					rc.setFvalue(F_value);
				}
			}
		}
		return copyWorld;
	}

	public ArrayList<RoadCell> search(WorldSim bigWorld){
		WorldSim world = this.getWorldWithValue(bigWorld);

		// Initialize the RoadCell in the world(set every RoadCell as unchecked and unopened)
		for(int m = 0 ; m < world.getWidth(); m++){
			for(int n = 0 ; n < world.getHeight(); n++){
				if(world.getCell(m, n).getCellType() == AbstractCell.CellType.ct_road){
					RoadCell rc = (RoadCell) world.getCell(m, n);
					rc.setAsUnChecked();
					rc.setAsUnOpened();
					rc.parent = null;
				}
			}
		}
		//initialize open list and closed list
		ArrayList<RoadCell> openList = new ArrayList<>();
		ArrayList<RoadCell> checkedList  = new ArrayList<>();
		ArrayList<RoadCell> routeList = new ArrayList<>();

		boolean goalReached = false;

		//start position of this car
		Point startPosition = this.getCurrentPosition();
		//end position of this car
		Point endPosition = this.getEndPosition();
		//current position of this car
		Point currentPosition = this.getCurrentPosition();

		//the cell that the self-driving car starts
		RoadCell startCell = (RoadCell)world.getCell(startPosition.getX(), startPosition.getY());
		//the cell that the self-driving car ends
		RoadCell endCell = (RoadCell)world.getCell(endPosition.getX(), endPosition.getY());
		//the cell that the self-driving car currently at
		RoadCell currentCell = (RoadCell) world.getCell(currentPosition.getX(), currentPosition.getY());

		while(!goalReached) {
			Objects.requireNonNull(currentCell).setAsChecked();
			checkedList.add(currentCell);
			openList.remove(currentCell);

			currentPosition = currentCell.getPosition();

			int x,y;
			x = currentPosition.getX();
			y = currentPosition.getY();

			//get all adjacent cell of current cell and add them to open list
			//--------------------------------------------------------------------------
			// open the up Cell(north)
			if(y - 1 >= 0) {
				if(world.getCell(x, y - 1).getCellType() == AbstractCell.CellType.ct_road) {
					openCell((RoadCell)world.getCell(x,y - 1), currentCell, openList);
				}
			}
			//open the down Cell(south)
			if(y + 1 < world.getHeight()) {
				if(world.getCell(x, y + 1).getCellType() == AbstractCell.CellType.ct_road) {
					openCell((RoadCell)world.getCell(x,y + 1), currentCell, openList);
				}
			}
			// open the left Cell(west)
			if(x - 1 >= 0) {
				if(world.getCell(x - 1, y).getCellType() == AbstractCell.CellType.ct_road) {
					openCell((RoadCell)world.getCell(x - 1,y), currentCell, openList);
				}
			}
			// open the right Cell (east)
			if(x + 1 < world.getWidth()) {
				if(world.getCell(x + 1, y).getCellType() == AbstractCell.CellType.ct_road) {
					openCell((RoadCell)world.getCell(x + 1,y), currentCell, openList);
				}
			}
			//--------------------------------------------------------------------------

			// we need to find the best cell that the car will choose to go
			int bestCellFValue = Integer.MAX_VALUE;
			RoadCell bestCell = null;
			for (RoadCell roadCell : openList) {
				// compare f value
				if (roadCell.getFvalue() < bestCellFValue) {
					bestCellFValue = roadCell.getFvalue();
					bestCell = roadCell;
				}
				// if f value is equal, we compare g value
				else if (roadCell.getFvalue() == bestCellFValue) {
					if (roadCell.getGvalue() < (bestCell != null ? bestCell.getGvalue() : 0)) {
						bestCell = roadCell;
					}

				}
			}
			currentCell = bestCell;
			if(currentCell != null && currentCell.getPosition().equals(endCell.getPosition())) {
				goalReached = true;
				routeList = backtrack(startCell,endCell);
			}
		}
		return routeList;
	}

	public void openCell(RoadCell rc, RoadCell currentCell, ArrayList<RoadCell> openList) {
		if( !rc.checked && !rc.open){
			//if the cell is not opened yet, add it to open
			rc.setAsOpen();
			// penalty if the self-driving car goes to a wrong direction
			int p = 100;
			//check the driving direction between the child cell and current cell
			ArrayList<Direction> dl1 = currentCell.getTravelDirection();
			ArrayList<Direction> dl2 = rc.getTravelDirection();

			if(dl1.size() == 1 && dl2.size() == 1) {
				Direction d1 = dl1.get(0);
				int h = rc.getHvalue();
				int g = rc.getGvalue();
				// moving in the same row
				if(currentCell.getPosition().getX() == rc.getPosition().getX()) {
					if(currentCell.getPosition().getY() < rc.getPosition().getY() && d1 != Direction.west) {
						h = h + p;
						rc.setHvalue(h);
						rc.setFvalue(g + h);
					}
					else if(currentCell.getPosition().getY() > rc.getPosition().getY() && d1 != Direction.east) {
						h = h + p;
						rc.setHvalue(h);
						rc.setFvalue(g + h);
					}
				}
				// moving in the same column
				else if(currentCell.getPosition().getY() == rc.getPosition().getY()) {
					if(currentCell.getPosition().getX() < rc.getPosition().getX() && d1 != Direction.north) {
						h = h + p;
						rc.setHvalue(h);
						rc.setFvalue(g + h);
					}
					else if(currentCell.getPosition().getX() > rc.getPosition().getX() && d1 != Direction.south) {
						h = h + p;
						rc.setHvalue(h);
						rc.setFvalue(g + h);
					}
				}

			}
			rc.setParent(currentCell);
			openList.add(rc);
		}
	}

	public ArrayList<RoadCell> backtrack(RoadCell startCell, RoadCell endCell) {
		ArrayList<RoadCell> path = new ArrayList<>();
		RoadCell currentCell = endCell;
		path.add(endCell);
		while(!currentCell.getPosition().equals(startCell.getPosition())){
			currentCell = currentCell.parent;
			path.add(currentCell);
		}
		return path;
	}

	public void setCurrentIcon(ImageIcon carIcon){
		this.carIcon = carIcon;
	}

	public ImageIcon getCurrentIcon(){
		return this.carIcon;
	}

	public ImageIcon getNorthCarIcon(){
		return this.northCarIcon;
	}

	public ImageIcon getSouthCarIcon(){
		return this.southCarIcon;
	}

	public ImageIcon getEastCarIcon(){
		return this.eastCarIcon;
	}

	public ImageIcon getWestCarIcon(){
		return this.westCarIcon;
	}
}
