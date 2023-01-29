package core_car_sim;

import java.util.ArrayList;

public abstract class AbstractInformationCell extends AbstractCell{
	
	public enum InformationCell{
		ic_trafficLight
	}

	private static final long serialVersionUID = 6253765837690795987L;
	//Which way the information faces
	public ArrayList<Direction> faces = new ArrayList<Direction>();
	public ArrayList<Integer> visibleFrom = new ArrayList<Integer>();
	public int visibilityChange = 0;
	
	public AbstractInformationCell(ArrayList<Direction> _faces, ArrayList<Integer> _visibleFrom){
		super(CellType.ct_information);
		this.faces = _faces;
		this.visibleFrom = _visibleFrom;
	}
	
	public AbstractInformationCell(Direction _faces, int _visibleFrom){
		super(CellType.ct_information);
		this.faces.add(_faces);
		this.visibleFrom.add(_visibleFrom);
	}

	public int isVisibleFrom(Direction direction){
		return Math.max(visibleFrom.get(faces.indexOf(direction)) - visibilityChange, 0); 
	}
	
	public void reduceVisibilityBy(int amount){
		visibilityChange = amount;
	}
	
	public ArrayList<Direction> getFaces(){
		return faces;
	}
	
	@Override
	public boolean isDriveable(){
		return false;
	}
	
	public abstract Object getInformation();
	public abstract InformationCell getInformationType();
}
