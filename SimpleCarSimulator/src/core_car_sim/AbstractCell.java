package core_car_sim;

import javax.swing.JPanel;

/*
 * Abstract cell of the car simulator
 */
public abstract class AbstractCell extends JPanel{
	/**
	 * Auto generated serial
	 */
	private static final long serialVersionUID = -1866861919859124549L;

	public enum CellType{
		ct_blank,
		ct_road, 
		ct_information,
		ct_non_visible, 
		ct_pavement
	}
	
	private CellType cellType;
	private Point cellPosition;
	public abstract void stepSim();
	public abstract boolean isDriveable();
	
	public AbstractCell(CellType ct){
		cellType = ct;
	}
	
	public CellType getCellType(){
		return cellType;
	}
	
	public void setPosition(Point position){
		this.cellPosition = position;
	}
	public Point getPosition() {
		return cellPosition;
	}
	
}
