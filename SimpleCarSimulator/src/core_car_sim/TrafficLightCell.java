package core_car_sim;

import java.awt.*;
import java.util.ArrayList;

public class TrafficLightCell extends AbstractInformationCell
{
	/**
	 * Auto generated serialID
	 */
	private static final long serialVersionUID = -686102171772509852L;

	public class TrafficLightCellInformation
	{
		public boolean redOn;
		public boolean yellowOn;
		public boolean greenOn;
		public ArrayList<Point> effected_area = new ArrayList<Point>();

		public Point stopAt;
		public Point stopAtReference;
	}
	
	private TrafficLightCellInformation lightSituation = new TrafficLightCellInformation();
	private int timeToChange = 8;
	private int currentTime = 0;

	
	public TrafficLightCell(Direction _faces, int _visibleFrom,Point roadEffectLocation, Point roadEffectReference, int position)
	{
		super(_faces, _visibleFrom);
		//The location of trafficLight
		lightSituation.stopAt = roadEffectLocation;
		//The location of the white line
		lightSituation.stopAtReference = roadEffectReference;
		
		// The position of a traffic light **
		if(position == 1) {
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
		}
		else if(position == 2) {
			
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;	
		}
		else if(position == 3) {
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
		}
		else if(position == 4) {
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
		}
		
	}
	
	@Override
	public TrafficLightCellInformation getInformation()
	{
		return lightSituation;
	}

	@Override
	public void stepSim()
	{
		currentTime++;
		if (lightSituation.yellowOn)
		{
			if (lightSituation.redOn)
			{
				lightSituation.greenOn = true;
				lightSituation.redOn = false;
				lightSituation.yellowOn = false;
			}
			else
			{
				lightSituation.greenOn = false;
				lightSituation.redOn = true;
				lightSituation.yellowOn = false;	
			}
		}
		else if (currentTime > timeToChange)
		{
			lightSituation.yellowOn = true;
			lightSituation.greenOn = false;
			currentTime = 0;
		}
		
	}
	@Override
	public void paintComponent(Graphics g)
	{
		if (lightSituation.greenOn)
		{
			g.setColor(Color.GREEN);
		}
		else if (lightSituation.redOn && !lightSituation.yellowOn)
		{
			g.setColor(Color.RED);
		}
		else if (!lightSituation.redOn && lightSituation.yellowOn)
		{
			g.setColor(Color.YELLOW);
		}
		else
		{
			g.setColor(Color.BLUE);
		}
		g.fillOval(0, 0, getWidth()-1, getHeight()-1);
	}

	@Override
	public InformationCell getInformationType()
	{
		return InformationCell.ic_trafficLight;
	}		
}
