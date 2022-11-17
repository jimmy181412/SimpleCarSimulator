package core_car_sim;

import java.util.ArrayList;

import java.awt.*;
import java.awt.BasicStroke;


import java.awt.Graphics;
import java.awt.Graphics2D;


public class RoadCell extends AbstractCell
{
	/*
	 * 
	 * List of possible markings on the tarmac
	 */
	public enum RoadMarking
	{
		rm_Zebra_Vertical,
		rm_Zebra_Horizontal,
		rm_Pelican,
		rm_HorizontalWhiteLineLeft, //Holds 
		rm_HorizontalWhiteLineRight,
		rm_VerticalWhiteLineUp,
		rm_VerticalWhiteLineDown,
		rm_BusLine,
		rm_solid_line_left,
		rm_solid_line_right,
		rm_solid_line_up,
		rm_solid_line_down,
		rm_dotted_line_left,
		rm_dotted_line_right,
		rm_dotted_line_up,
		rm_dotted_line_down,
		rm_hard_shoulder
		
	}

	/*
	 * Represents tarmac
	 */


	/**
	 * 
	 */
	private static final long serialVersionUID = -3908736198953808153L;
	private ArrayList<Direction> travelDirection = new ArrayList<Direction>();
	private boolean pavement;
	private ArrayList<RoadMarking> roadMarkings = new ArrayList<RoadMarking>();
	private int speedLimit;
	
	public RoadCell(ArrayList<Direction> directions, boolean _pavement, ArrayList<RoadMarking> markings, int _speedLimit)
	{
		super(CellType.ct_road);
		travelDirection.addAll(directions);
		pavement = _pavement;
		if(markings != null) {
			roadMarkings.addAll(markings);
		}
		speedLimit = _speedLimit;
	}
	
	public RoadCell(Direction _direction, boolean _pavement, ArrayList<RoadMarking> markings, int _speedLimit)
	{
		super(CellType.ct_road);
		travelDirection.add(_direction);
		pavement = _pavement;
		if(markings != null) {
			roadMarkings.addAll(markings);
		}
		speedLimit = _speedLimit;
	}

	@Override
	public void stepSim()
	{
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Direction> getTravelDirection()
	{
		return travelDirection;
	}

	public boolean isPavement()
	{
		return pavement;
	}

	public ArrayList<RoadMarking> getRoadMarkings()
	{
		return roadMarkings;
	}

	public int getSpeedLimit()
	{
		return speedLimit;
	}
	
	public void setMarking(RoadMarking rm)
	{
		roadMarkings.add(rm);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{	
		
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(Color.WHITE);
		
		if (pavement){
			g2d.setColor(Color.GRAY);
			g2d.fillRect(0, 0, getWidth(), getHeight());
			g2d.drawString("pavement",getWidth()/2,getHeight()/2);
		}
			
		for(RoadMarking rm:  roadMarkings){
			if (rm  == RoadMarking.rm_HorizontalWhiteLineLeft)
			{	
				g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(5,0,5,getHeight());
			}
			else if (rm == RoadMarking.rm_HorizontalWhiteLineRight) {
				g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(getWidth()-5,0,getWidth()-5,getHeight());
			}
			else if (rm == RoadMarking.rm_VerticalWhiteLineUp) {
				g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(0,5,getWidth(),5);
			}
			else if (rm == RoadMarking.rm_VerticalWhiteLineDown) {
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
			else if(rm == RoadMarking.rm_BusLine) {
				g2d.drawString("BUS",50, 18);
			}
			else if(rm == RoadMarking.rm_solid_line_right) {
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(getWidth(),0,getWidth(),getHeight());
			}
			else if(rm == RoadMarking.rm_solid_line_left){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(0,0,0,getHeight());
			}
			else if(rm == RoadMarking.rm_solid_line_up){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(0,0,getWidth(),0);
			}
			else if(rm == RoadMarking.rm_solid_line_down){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(0,getHeight(),getWidth(),getHeight());
			}
			else if(rm == RoadMarking.rm_dotted_line_right) {
				
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
				g2d.drawLine(getWidth(),0,getWidth(),getHeight());
			}
			else if(rm == RoadMarking.rm_dotted_line_left){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
				g2d.drawLine(0,0,0,getHeight());
			}
			else if(rm == RoadMarking.rm_dotted_line_up){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
				g2d.drawLine(0,0,getWidth(),0);
			}
			else if(rm == RoadMarking.rm_dotted_line_down){
				g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
				g2d.drawLine(0,getHeight(),getWidth(),getHeight());
			}
			else if(rm == RoadMarking.rm_hard_shoulder) {
				g2d.drawString("HS",getWidth()/2,getHeight()/2);
			}
		}	
		if(travelDirection.size() > 1)
		{
			g2d.drawString("+",getWidth()/4, getHeight()/4);
		}
		else {
			g2d.drawString(travelDirection.get(0).toString(),getWidth()/4,getHeight()/4);
		}
	}

	@Override
	public boolean isDriveable()
	{
		return true;
	}
}
