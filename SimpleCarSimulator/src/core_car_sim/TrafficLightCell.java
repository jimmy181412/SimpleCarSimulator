/*
 *  it is building on work by Joe Collenette.
 */

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

		public boolean redYellowOn;
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
	public Image redLight;
	public Image greenLight;
	public Image yellowLight;
	public  Image redYellowLight;
	public Image defaultLight;
	public Image currentLight;
	public ImageIcon redLightIcon;
	public ImageIcon greenLightIcon;
	public ImageIcon yellowLightIcon;
	public ImageIcon defaultLightIcon;
	public ImageIcon redYellowLightIcon;


	public TrafficLightCell(Direction _faces, 
							int _visibleFrom,
							Point roadEffectLocation, 
							Point roadEffectReference, 
							int position,
							String greenLightPath,
							String yellowLightPath,
							String redLightPath,
							String redYellowPath) throws IOException {
		
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

		redYellowLightIcon = new ImageIcon(redYellowPath);

		redLight = redLightIcon.getImage().getScaledInstance(40,40,Image.SCALE_SMOOTH);
		greenLight = greenLightIcon.getImage().getScaledInstance(40,40,Image.SCALE_SMOOTH);
		yellowLight = yellowLightIcon.getImage().getScaledInstance(40,40,Image.SCALE_SMOOTH);
		redYellowLight = redYellowLightIcon.getImage().getScaledInstance(40,40,Image.SCALE_SMOOTH);

		//The location of trafficLight
		this.lightSituation.stopAt = roadEffectLocation;
		//The location of the white line
		this.lightSituation.stopAtReference = roadEffectReference;
		
		// The position of a traffic light **
		if(position == 1){
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
			lightSituation.redYellowOn = false;
			currentTime = 0;
		}
		else if(position == 2) {
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
			lightSituation.redYellowOn = false;
			currentTime = 5;
		}
		else if(position == 3) {
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
			lightSituation.redYellowOn = false;
			currentTime = 10;
		}
		else if(position == 4) {
			lightSituation.redOn = false;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
			lightSituation.redYellowOn = true;
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
		redYellowLight = redYellowLightIcon.getImage().getScaledInstance(getWidth(),getHeight(),Image.SCALE_SMOOTH);

		if(currentTime >= 0 && currentTime < 15){
			lightSituation.redOn = true;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
			lightSituation.redYellowOn = false;
		}

		else if(currentTime == 15) {
			lightSituation.redOn = false;
			lightSituation.yellowOn = false;
			lightSituation.greenOn = false;
			lightSituation.redYellowOn = true;
		}
		else if(currentTime >= 16 && currentTime < timeToChange) {
			lightSituation.greenOn = true;
			lightSituation.yellowOn = false;
			lightSituation.redOn = false;
			lightSituation.redYellowOn = false;
		}
		else if(currentTime == timeToChange) {
			lightSituation.greenOn = false;
			lightSituation.yellowOn = true;
			lightSituation.redOn = false;
			lightSituation.redYellowOn = false;

			//reset the time
			currentTime = 0;
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D)g.create();

		// Set rendering hints for better image scaling quality
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
		if(lightSituation.greenOn && !lightSituation.yellowOn && !lightSituation.redOn && !lightSituation.redYellowOn){
			currentLight = greenLight;
		}
		else if (!lightSituation.greenOn && !lightSituation.yellowOn && lightSituation.redOn && !lightSituation.redYellowOn){

			currentLight = redLight;
		}
		else if (!lightSituation.greenOn && lightSituation.yellowOn && !lightSituation.redOn && !lightSituation.redYellowOn){

			currentLight = yellowLight;
		}
		else if(!lightSituation.greenOn && !lightSituation.yellowOn && !lightSituation.redOn && lightSituation.redYellowOn){
			currentLight = redYellowLight;
		}


		g2d.setColor(Color.gray);
		g2d.fillRect(0,0,getWidth(),getHeight());



		// Calculate the new width and height based on the panel size
		int width = getWidth() ;
		int height = getHeight();

		// Calculate the position to draw the image centered
		int x = (getWidth() - width) / 2;
		int y = (getHeight() - height) / 2;


		g2d.drawImage(currentLight,x,y,width,height,this);
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
