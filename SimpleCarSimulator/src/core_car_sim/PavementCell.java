package core_car_sim;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import core_car_sim.RoadCell.RoadMarking;

public class PavementCell extends AbstractCell{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<PavementMarking> pavementMarkings = new ArrayList<>();
	private float transparency = 1;

	public enum PavementMarking{
		pm_kerb_east,
		pm_kerb_west,
		pm_kerb_north,
		pm_kerb_south
	}
	
	public PavementCell() {
		super(CellType.ct_pavement);
		// TODO Auto-generated constructor stub
	}
	
	// setter and getter of roadMarkings
	public void setMarking(PavementMarking rm){
		this.pavementMarkings.add(rm);
	}
	
	public ArrayList<PavementMarking> getRoadMarkings(){
		return this.pavementMarkings;
	}

	@Override
	public void stepSim() {
		// TODO Auto-generated method stub	
	}

	@Override
	public boolean isDriveable() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
		g2d.setColor(Color.gray);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		for(PavementMarking pm: pavementMarkings) {
			if(pm == PavementMarking.pm_kerb_east) {
				g2d.setColor(Color.gray.darker());
				g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(getWidth()-5,0,getWidth()-5,getHeight());
			}
			else if(pm == PavementMarking.pm_kerb_west) {
				g2d.setColor(Color.gray.darker());
				g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(5,0,5,getHeight());
			}
			else if(pm == PavementMarking.pm_kerb_north) {
				g2d.setColor(Color.gray.darker());
				g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(0,5,getWidth(),5);
			}
			else if(pm == PavementMarking.pm_kerb_south) {
				g2d.setColor(Color.gray.darker());
				g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(0,getHeight()-5,getWidth(),getHeight()-5);
			}
		  }
		this.transparency = 1;
		}
	

	public void setTransparency(float f) {
		// TODO Auto-generated method stub
		this.transparency = f;
	}
	}
