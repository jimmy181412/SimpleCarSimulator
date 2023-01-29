package core_car_sim;

import java.awt.*;
import java.util.ArrayList;

public class TrafficLightCell extends AbstractInformationCell{	
	
	public class TrafficLightCellInformation{
		public boolean redOn;
		public boolean yellowOn;
		public boolean greenOn;
		public ArrayList<Point> effected_area = new ArrayList<Point>();
		public Point stopAt;
		public Point stopAtReference;
	}
	
	private static final long serialVersionUID = -686102171772509852L;
	private TrafficLightCellInformation lightSituation = new TrafficLightCellInformation();
	private int timeToChange = 12;
	private int currentTime;

	public TrafficLightCell(Direction _faces, 
							int _visibleFrom,
							Point roadEffectLocation, 
							Point roadEffectReference, 
							int position){
		
		super(_faces, _visibleFrom);
		//The location of trafficLight
		this.lightSituation.stopAt = roadEffectLocation;
		//The location of the white line
		this.lightSituation.stopAtReference = roadEffectReference;
		
		// The position of a traffic light **
		if(position == 1){
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
			currentTime = 0;
		}
		else if(position == 2) {
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;	
			currentTime = 3;
		}
		else if(position == 3) {
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
			currentTime = 6;
		}
		else if(position == 4) {
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
			currentTime = 9;
		}
	}
	
	@Override
	public TrafficLightCellInformation getInformation(){
		return lightSituation;
	}

	@Override
	public void stepSim(){
		currentTime++;
		
		if(currentTime >= 0 && currentTime < 10){
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
		}
		else if(currentTime == 10){
			lightSituation.yellowOn = true;
			lightSituation.greenOn = false;
			lightSituation.redOn = false;
			
		}
		else if(currentTime > 10 && currentTime < timeToChange) {
			lightSituation.greenOn = true;
			lightSituation.yellowOn = false;
			lightSituation.redOn = false;	
		}
		else if(currentTime == timeToChange) {
			lightSituation.greenOn = true;
			lightSituation.yellowOn = false;
			lightSituation.redOn = false;
			//reset the time
			currentTime = 0;
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
		if(lightSituation.greenOn){
			g.setColor(Color.GREEN);
		}
		else if (lightSituation.redOn && !lightSituation.yellowOn){
			g.setColor(Color.RED);
		}
		else if (!lightSituation.redOn && lightSituation.yellowOn){
			g.setColor(Color.YELLOW);
		}
		else{
			g.setColor(Color.BLUE);
		}
		g.fillOval(0, 0, getWidth()-1, getHeight()-1);
	}

	@Override
	public InformationCell getInformationType(){
		return InformationCell.ic_trafficLight;
	}		
}
