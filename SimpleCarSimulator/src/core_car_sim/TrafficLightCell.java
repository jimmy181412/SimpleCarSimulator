package core_car_sim;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;

public class TrafficLightCell extends AbstractInformationCell{	
	
	public class TrafficLightCellInformation{
		public boolean redOn;
		public boolean yellowOn;
		public boolean greenOn;
		public ArrayList<Point> effected_area = new ArrayList<>();
		public Point stopAt;
		public Point stopAtReference;
	}
	
	@Serial
	private static final long serialVersionUID = -686102171772509852L;
	private TrafficLightCellInformation lightSituation = new TrafficLightCellInformation();
	private int timeToChange = 20;
	private int currentTime;
	private float transparency = 1;
	public JLabel icon;
	private Image redLight;
	private Image greenLight;
	private Image yellowLight;
	private Image defaultLight;
	private Image currentLight;
	private ImageIcon redLightIcon;
	private ImageIcon greenLightIcon;
	private ImageIcon yellowLightIcon;
	private ImageIcon defaultLightIcon;


	public TrafficLightCell(Direction _faces, 
							int _visibleFrom,
							Point roadEffectLocation, 
							Point roadEffectReference, 
							int position,
							String greenLightPath,
							String yellowLightPath,
							String redLightPath) throws IOException {
		
		super(_faces, _visibleFrom);
		redLightIcon = new ImageIcon(
				redLightPath
		);

		greenLightIcon = new ImageIcon(
				greenLightPath
		);

		yellowLightIcon = new ImageIcon(
				yellowLightPath
		);

		redLight = redLightIcon.getImage().getScaledInstance(40,40,Image.SCALE_SMOOTH);
		greenLight = greenLightIcon.getImage().getScaledInstance(40,40,Image.SCALE_SMOOTH);
		yellowLight = yellowLightIcon.getImage().getScaledInstance(40,40,Image.SCALE_SMOOTH);

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
			currentTime = 5;
		}
		else if(position == 3) {
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
			currentTime = 10;
		}
		else if(position == 4) {
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
			currentTime = 15;
		}
	}
	
	@Override
	public TrafficLightCellInformation getInformation(){
		return lightSituation;
	}

	@Override
	public void stepSim(){
		currentTime++;

		redLight = redLightIcon.getImage().getScaledInstance(getWidth(),getHeight(),Image.SCALE_SMOOTH);
		greenLight = greenLightIcon.getImage().getScaledInstance(getWidth(),getHeight(),Image.SCALE_SMOOTH);
		yellowLight = yellowLightIcon.getImage().getScaledInstance(getWidth(),getHeight(),Image.SCALE_SMOOTH);

		if(currentTime >= 0 && currentTime < 16){
			lightSituation.redOn = true;
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
		}
		else if(currentTime == 16){
			lightSituation.yellowOn = true;
			lightSituation.greenOn = false;
			lightSituation.redOn = false;
			
		}
		else if(currentTime > 15 && currentTime < timeToChange) {
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
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
		if(lightSituation.greenOn){
			currentLight = greenLight;
		}
		else if (lightSituation.redOn && !lightSituation.yellowOn){

			currentLight = redLight;
		}
		else if (!lightSituation.redOn && lightSituation.yellowOn){

			currentLight = yellowLight;
		}
		else{

		}
		g2d.setColor(Color.gray);
		g2d.fillRect(0,0,getWidth(),getHeight());
		g2d.drawImage(currentLight,0,0,this);
		this.transparency = 1;
	}

	@Override
	public InformationCell getInformationType(){
		return InformationCell.ic_trafficLight;
	}

	public float getTransparency() {
		return transparency;
	}

	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}		
}
