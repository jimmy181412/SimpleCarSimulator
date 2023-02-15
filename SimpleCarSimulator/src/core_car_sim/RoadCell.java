package core_car_sim;

import java.io.Serial;
import java.util.ArrayList;

import java.awt.*;

public class RoadCell extends AbstractCell{
	public enum RoadMarking{
		rm_Zebra_Vertical,
		rm_Zebra_Horizontal,
		rm_HorizontalWhiteLineLeft, //Holds 
		rm_HorizontalWhiteLineRight,
		rm_VerticalWhiteLineUp,
		rm_VerticalWhiteLineDown,
		rm_solid_line_east,
		rm_solid_line_west,
		rm_solid_line_north,
		rm_solid_line_south,
		rm_solid_white_line_east,
		rm_solid_white_line_west,
		rm_solid_white_line_north,
		rm_solid_white_line_south,
		rm_dotted_line_east,
		rm_dotted_line_west,
		rm_dotted_line_north,
		rm_dotted_line_south,
		rm_hard_shoulder
		
	}
	@Serial
	private static final long serialVersionUID = -3908736198953808153L;
	
	// attributes used for A star search
	private int f_value = 10000;
	private int g_value = 500;
	private int h_value = 500;
	public boolean checked = false;
	public boolean open = false;
	public RoadCell parent;
	
	private float transparency = 1;

	
	// the direction that the car can go at this road cell
	private ArrayList<Direction> travelDirection = new ArrayList<Direction>();
	// the road markings at this road cell
	private ArrayList<RoadMarking> roadMarkings = new ArrayList<RoadMarking>();
	
	private int speedLimit;
	
	public RoadCell(ArrayList<Direction> directions, ArrayList<RoadMarking> markings, int _speedLimit){
		super(CellType.ct_road);
		this.travelDirection.addAll(directions);
		if(markings != null) {this.roadMarkings.addAll(markings);}
		this.speedLimit = _speedLimit;
	}
	
	public RoadCell(Direction _direction, ArrayList<RoadMarking> markings, int _speedLimit){
		super(CellType.ct_road);
		this.travelDirection.add(_direction);
		if(markings != null) {this.roadMarkings.addAll(markings);}
		this.speedLimit = _speedLimit;
	}

	@Override
	public void stepSim(){
		// TODO Auto-generated method stub
	}
	
	public ArrayList<Direction> getTravelDirection(){
		return travelDirection;
	}
	
	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}
	
	// setter and getter of roadMarkings
	public void setMarking(RoadMarking rm){
		this.roadMarkings.add(rm);
	}
	
	public ArrayList<RoadMarking> getRoadMarkings(){
		return roadMarkings;
	}

	public int getSpeedLimit(){
		return speedLimit;
	}
	
	// set and get of g value
	public void setGvalue(int value){
		this.g_value = value;
	}
	
	public int getGvalue() {
		return this.g_value;
	}

	// getter and setter of f value
	public void setFvalue(int value) {
		this.f_value = value;
	}
	
	public int getFvalue() {
		return this.f_value;
	}
	
	// getter and setter of h value
	public void setHvalue(int value) {
		this.h_value = value;
	}
	public int getHvalue() {
		return this.h_value;
	}
	
	public void setAsChecked(){
		this.checked = true;
	}
	
	public void setAsOpen(){
		this.open = true;
	}
	
	public void setAsUnChecked(){
		this.checked = false;
	}
	
	public void setAsUnOpened(){
		this.open = false;
	}
	
	// getter and setter of parent
	public RoadCell getParent(){
		return this.parent;
	}
	
	public void setParent(RoadCell rc){
		this.parent = rc;
	}
	
	@Override
	public boolean isDriveable(){
		return true;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{	
		
		Graphics2D g2d = (Graphics2D)g.create();
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
		g2d.setColor(Color.gray.darker());
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(Color.white);
		
		for(RoadMarking rm:  roadMarkings){
			if (rm  == RoadMarking.rm_HorizontalWhiteLineLeft){	
				g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(5,0,5,getHeight());
			}
			else if (rm == RoadMarking.rm_HorizontalWhiteLineRight){
				g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(getWidth()-5,0,getWidth()-5,getHeight());
			}
			else if (rm == RoadMarking.rm_VerticalWhiteLineUp){
				g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(0,5,getWidth(),5);
			}
			else if (rm == RoadMarking.rm_VerticalWhiteLineDown){
				g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(0,getHeight()-5,getWidth(),getHeight()-5);
			}
			else if(rm == RoadMarking.rm_Zebra_Vertical){
				for(int i = 0 ; i <=  getWidth(); i = i + getWidth() / 4 ) {
					for(int j = 0; j <= getHeight() ; j = j + getHeight() /4) {					
						g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
						g2d.drawLine(i,j,i+getWidth()/4,j);
					}
				}	
			}
			else if(rm == RoadMarking.rm_Zebra_Horizontal) {
				for(int i = 0 ; i <=  getWidth(); i = i + getWidth() / 4 ) {
					for(int j = 0; j <= getHeight() ; j = j + getHeight() /4) {			
						g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
						g2d.drawLine(i,j,i,j+getHeight()/4);
					}
				}		
			}
			else if(rm == RoadMarking.rm_solid_line_east) {
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(getWidth(),0,getWidth(),getHeight());
			}	
			else if(rm == RoadMarking.rm_solid_line_west){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(0,0,0,getHeight());
			}		
			else if(rm == RoadMarking.rm_solid_line_north){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(0,0,getWidth(),0);
			}	
			else if(rm == RoadMarking.rm_solid_line_south){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(0,getHeight(),getWidth(),getHeight());
			}	
			else if(rm == RoadMarking.rm_dotted_line_east) {
				
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
				g2d.drawLine(getWidth(),0,getWidth(),getHeight());
			}	
			else if(rm == RoadMarking.rm_dotted_line_west){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
				g2d.drawLine(0,0,0,getHeight());
			}
			else if(rm == RoadMarking.rm_dotted_line_north){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
				g2d.drawLine(0,0,getWidth(),0);
			}
			else if(rm == RoadMarking.rm_dotted_line_south){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
				g2d.drawLine(0,getHeight(),getWidth(),getHeight());
			}
			else if(rm == RoadMarking.rm_hard_shoulder) {
				g2d.drawString("HS",getWidth()/2,getHeight()/2);
			}
			else if(rm == RoadMarking.rm_solid_white_line_east) {
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(getWidth(),0,getWidth(),getHeight());
			}
			else if(rm == RoadMarking.rm_solid_white_line_west){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(0,0,0,getHeight());
			}
			else if(rm == RoadMarking.rm_solid_white_line_north){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(0,0,getWidth(),0);
			}
			else if(rm == RoadMarking.rm_solid_white_line_south){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(0,getHeight(),getWidth(),getHeight());
			}
		}
		if(travelDirection.size() > 1)
		{
			g2d.drawString("+",getWidth()/4, getHeight()/4);
		}
		else {
			g2d.drawString(travelDirection.get(0).toString(),getWidth()/4,getHeight()/4);
		}
		this.transparency = 1;
	}
}
