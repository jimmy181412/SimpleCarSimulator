package core_car_sim;

import java.awt.*;
import java.io.Serial;

public class NonDrivingCell extends AbstractCell{
	
	@Serial
	private static final long serialVersionUID = 3155750680055909034L;
	private final Color color = Color.green.darker().darker();
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

	public void setTransparency(float transparency) {
		this.transparency = transparency;
		
	}
}
