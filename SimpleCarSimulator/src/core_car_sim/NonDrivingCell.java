package core_car_sim;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class NonDrivingCell extends AbstractCell{
	
	private static final long serialVersionUID = 3155750680055909034L;
	private Color color = Color.green.darker().darker();
	private float transparency = (float) 1.0; 
	
	public NonDrivingCell(){
		super(CellType.ct_blank);
	}

	
	
	
	@Override
	public void stepSim(){
		// TODO Auto-generated method stub
	}

	@Override
	public void paintComponent(Graphics g){
		
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
		g2d.setColor(color);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		this.transparency = 1;
	}
	
	@Override
	public boolean isDriveable(){
		return false;
	}

	public float getTransparency() {
		return transparency;
	}

	public void setTransparency(float transparency) {
		this.transparency = transparency;
		
	}
}
