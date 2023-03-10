package visual_car_sim;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import core_car_sim.AbstractCar;
import core_car_sim.CarAddedListener;
import core_car_sim.Direction;
import core_car_sim.LoadWorld;
import core_car_sim.NonDrivingCell;
import core_car_sim.PavementCell;
import core_car_sim.Pedestrian;
import core_car_sim.PedestrianAddedListener;
import core_car_sim.Point;
import core_car_sim.RoadCell;
import core_car_sim.TrafficLightCell;
import core_car_sim.WorldSim;
import examples.ExampleAICar;
import examples.ExampleTestingCar;


//<a href="https://www.flaticon.com/free-icons/ui" title="ui icons">Ui icons created by icon wind - Flaticon</a>
public class CarSimGUI
{
	public class Simulate implements Runnable
	{
		int delay = 0;
		int until = 0;
		int stepsSimulated = 0;
		public Simulate(int delayTime, int noOfSteps)
		{
			delay = delayTime;
			until = noOfSteps;
		}
		
		@Override
		public void run()
		{
			int i = 0;
			boolean finished = false;
			while (!finished)
			{
				simworld.simulate(1);
				try
				{
					Thread.sleep(delay);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				updateGUIWorld();
				lblNewLabel.setText("Steps simulated: " + ++stepsSimulated);

				
				finished = until == 0 ? simworld.allFinished() : until == ++i;
//				System.out.println(simworld.allFinished());
//				System.out.println(finished);
			}
		}
		
	};
	
	private JFrame frame;
	private JLabel lblNewLabel;
	private JFileChooser loadWorldDialog = new JFileChooser();
	private WorldSim simworld;
	private JPanel pnlWorld = new JPanel();
	private Executor simulationThread = Executors.newSingleThreadExecutor();
	private CarAddedListener cal;
	private PedestrianAddedListener pal;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CarSimGUI window = new CarSimGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CarSimGUI() {
		initialize();
		cal = new CarAddedListener() {
			@Override
			public AbstractCar createCar(String name, Point startingLoca, Point endingLoca, Point referenceLoca, Direction d)
			{
				//AI controlled car (car not tested)
				return new ExampleAICar(startingLoca, endingLoca, referenceLoca
				,d
				,System.getProperty("user.dir") + "/SimpleCarSimulator/resources/imagesIcon/basicCarImage1.png"
				,System.getProperty("user.dir") + "/SimpleCarSimulator/resources/imagesIcon/basicCarImage2.png"
				,System.getProperty("user.dir") + "/SimpleCarSimulator/resources/imagesIcon/basicCarImage3.png"
				,System.getProperty("user.dir") + "/SimpleCarSimulator/resources/imagesIcon/basicCarImage4.png");
			}
	
			@Override
			public AbstractCar createCar(String name, Point startingLoca, Point endingLoca, Point referenceLoca,Direction d, String av)
			{
				return new ExampleTestingCar(startingLoca, endingLoca, referenceLoca
				,d
				,System.getProperty("user.dir") + "/SimpleCarSimulator/resources/imagesIcon/AICarImage1.png"
				,System.getProperty("user.dir") + "/SimpleCarSimulator/resources/imagesIcon/AICarImage2.png"
				,System.getProperty("user.dir") + "/SimpleCarSimulator/resources/imagesIcon/AICarImage3.png"
				,System.getProperty("user.dir") + "/SimpleCarSimulator/resources/imagesIcon/AICarImage4.png");
			}
		};
		
		pal = new PedestrianAddedListener() {
			@Override
			public Pedestrian createPedestrians(String name,Point startingLoca,Point endingLoca,Point referenceLoca, Direction d) {
				if (d == Direction.north) {
					return new Pedestrian(startingLoca, endingLoca, referenceLoca, d, System.getProperty("user.dir") + "/SimpleCarSimulator/resources/imagesIcon/Pedestrian1.png");
				} else if (d == Direction.south) {
					return new Pedestrian(startingLoca, endingLoca, referenceLoca, d, System.getProperty("user.dir") + "/SimpleCarSimulator/resources/imagesIcon/Pedestrian2.png");
				} else if (d == Direction.east) {
					return new Pedestrian(startingLoca, endingLoca, referenceLoca, d, System.getProperty("user.dir") + "/SimpleCarSimulator/resources/imagesIcon/Pedestrian3.png");
				} else{
					return new Pedestrian(startingLoca, endingLoca, referenceLoca, d, System.getProperty("user.dir") + "/SimpleCarSimulator/resources/imagesIcon/Pedestrian4.png");
				}
			}

		};
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 966, 615);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		JButton btnNewButton = new JButton("Load Simulation");
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Run Simulation");
		panel.add(btnNewButton_1);
		
		ButtonGroup bg = new ButtonGroup();
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("until finished");
		rdbtnNewRadioButton.setSelected(true);
		panel.add(rdbtnNewRadioButton);
		bg.add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("set number");
		panel.add(rdbtnNewRadioButton_1);
		bg.add(rdbtnNewRadioButton_1);
		
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
		panel.add(spinner);
		
		lblNewLabel = new JLabel("New label");
		panel.add(lblNewLabel);
	
		frame.getContentPane().add(pnlWorld, BorderLayout.CENTER);
		pnlWorld.setLayout(new GridLayout(3, 3, 0, 0));
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
//					if (loadWorldDialog.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
//					{
//						BufferedReader br = new BufferedReader(new FileReader(loadWorldDialog.getSelectedFile()));
//						simworld = LoadWorld.loadWorldFromFile(br, cal);
//						pnlWorld.setLayout(new GridLayout(simworld.getHeight(), simworld.getWidth(), 1, 1));
//						updateGUIWorld();
					BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/SimpleCarSimulator/src/examples/example1.txt"));
					simworld = LoadWorld.loadWorldFromFile(br, cal, pal);
					pnlWorld.setLayout(new GridLayout(simworld.getHeight(), simworld.getWidth(), 1, 1));
					updateGUIWorld();
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
				if (rdbtnNewRadioButton.isSelected())
				{
					simulationThread.execute(new Simulate(50, 0));
				}
				else
				{
					simulationThread.execute(new Simulate(50, (Integer)spinner.getValue()));
				}
			}
		});
		
		// button used to show visible world of the self-driving vehicle
        JButton show_visible_world = new JButton("show visible world");
        show_visible_world.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                
                int width = simworld.getWidth();
                int height = simworld.getHeight();
                int visibility = simworld.getVisibility();
                
                for(AbstractCar car: simworld.getCars()) {
                    if(car.getClass() == ExampleTestingCar.class) {
                        Point carPosition = simworld.getCarPosition(car);
						System.out.println("current car position is: ");
						System.out.println(carPosition.getX() + " " + carPosition.getY());
                        //get the car visible world
                        ArrayList<Point> visibleCells = new ArrayList<>();
                        for(int i = carPosition.getX() - visibility; i <= carPosition.getX() + visibility;i++) {
                            for(int j = carPosition.getY() - visibility; j <= carPosition.getY() + visibility; j++) {
                                // for the cell out of boundary
                                if( (i < 0) || (i >= width) || (j < 0) || ( j >= height)) {
                                   
                                }
                                else {
                                   visibleCells.add(new Point(i,j));
                                }
            
                            }
                        }
                        
                 
                        for(int m = 0; m < width; m++) {
                            for(int n = 0; n < height;n++) {
                                if(visibleCells.contains(new Point(m,n))) {
                                	simworld.getCell(m, n).setVisible(true);
                                }
                                else {
                                   if(simworld.getCell(m, n).getClass() == NonDrivingCell.class) {
                                	   NonDrivingCell ndc =  (NonDrivingCell)simworld.getCell(m, n);
                                	   ndc.setTransparency((float) 0.5);	 
                                   }
                                   else if(simworld.getCell(m, n).getClass() == RoadCell.class) {
                                	   RoadCell rc = (RoadCell)simworld.getCell(m, n);
                                	   rc.setTransparency((float) 0.5);
                                   }
                                   else if(simworld.getCell(m, n).getClass() == TrafficLightCell.class) {
                                	   TrafficLightCell tlc = (TrafficLightCell)simworld.getCell(m, n);
                                	   tlc.setTransparency((float)0.5);
                                   }
                                   else if(simworld.getCell(m, n).getClass() == PavementCell.class) {
                                	   PavementCell pc = (PavementCell)simworld.getCell(m,n);
                                	   pc.setTransparency((float)0.5);
                                   }
                                }
                            }
                        }
                  }
              }
                pnlWorld.repaint();
          }
        });
        
        panel.add(show_visible_world);
	}
	
	private void updateGUIWorld()
	{
		pnlWorld.removeAll();
		
		// get pnl's height and weight in pxis
		int pnlWidth = pnlWorld.getWidth();
		int pnlHeight = pnlWorld.getHeight();
		
		//get simulated world's height and width
		int simWidth = simworld.getWidth();
		int simHeight = simworld.getHeight();
		
		// height and width of each cell
		int cWidth = pnlWidth / simWidth;
		int cHeight = pnlHeight / simHeight;
		
		//adjust cell width and cell height for car and pedestrian icons
		int iconWidth = (int) (cWidth / 1.5);
		int iconHeight = (int) (cHeight / 1.5);
		for (int y = 0; y < simworld.getHeight(); y++)
		{
			for (int x = 0; x < simworld.getWidth(); x++)
			{
				simworld.getCell(x, y).removeAll();
				pnlWorld.add(simworld.getCell(x, y));
			}
		}
		//update cars
		for (AbstractCar car : simworld.getCars()){
			Point p = simworld.getCarPosition(car);

			//check world cell's direction of the current
			if(simworld.getCell(p.getX(), p.getY()).getClass() == RoadCell.class){
				RoadCell rc = (RoadCell)simworld.getCell(p.getX(), p.getY());
				if(rc.getTravelDirection().size() == 1){
					Direction d = rc.getTravelDirection().get(0);
					if(d == Direction.north){
						car.setCurrentIcon(car.getNorthCarIcon());
					}
					else if(d == Direction.south){
						car.setCurrentIcon(car.getSouthCarIcon());
					}
					else if(d == Direction.east){
						car.setCurrentIcon(car.getEastCarIcon());
					}
					else if(d == Direction.west){
						car.setCurrentIcon(car.getWestCarIcon());
					}
				}
				//if the car is on a multiple directions cell
				else{
					Direction d = car.getCMD();
					if(d == Direction.north){
						car.setCurrentIcon(car.getNorthCarIcon());
					}
					else if(d == Direction.south){
						car.setCurrentIcon(car.getSouthCarIcon());
					}
					else if(d == Direction.east){
						car.setCurrentIcon(car.getEastCarIcon());
					}
					else if(d == Direction.west){
						car.setCurrentIcon(car.getWestCarIcon());
					}
				}
			}

			ImageIcon iicon1 = car.getCarIcon();
			Image img1 = iicon1.getImage();
			//adjust size
			Image newimg1 = img1.getScaledInstance(iconWidth,iconHeight,java.awt.Image.SCALE_SMOOTH);
			iicon1 = new ImageIcon(newimg1);
			JLabel icon1 = new JLabel(iicon1);
			simworld.getCell(p.getX(), p.getY()).add(icon1);
			
		}
		//update pedestrians
		for(Pedestrian p : simworld.getPedestrian()) {
			Point point = simworld.getPedestrianPosition(p);
			ImageIcon iicon2 = p.getPedestrianIcon();
			Image img2 = iicon2.getImage();
			Image newimg2 = img2.getScaledInstance(iconWidth,iconHeight,java.awt.Image.SCALE_SMOOTH);
			iicon2 = new ImageIcon(newimg2);
			JLabel icon2 = new JLabel(iicon2);
			simworld.getCell(point.getX(), point.getY()).add(icon2);
			}
		
		pnlWorld.revalidate();
		pnlWorld.repaint();
	}
}
