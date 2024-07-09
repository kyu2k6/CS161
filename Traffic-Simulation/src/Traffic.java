//Kevin Yu
//5-2-22
//Create your own traffic by adding cars.
package Traffic;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Traffic implements ActionListener, Runnable{
	
	JFrame frame = new JFrame("Traffic Simulation");
	Road road = new Road();
	//South container
	JButton start = new JButton("Start");
	JButton stop = new JButton("Stop");
	JLabel throughput = new JLabel("Throughput: 0");
	Container south = new Container();
	//West container
	JButton semi = new JButton("Add Semi");
	JButton suv = new JButton("Add SUV");
	JButton sports = new JButton("Add Sports");
	Container west = new Container();
	boolean running = false;
	int carcount = 0;
	long startTime = 0;
	
	public Traffic() {
		frame.setSize(1000, 550);
		frame.setLayout(new BorderLayout());
		frame.add(road, BorderLayout.CENTER);
		
		south.setLayout(new GridLayout(1,3));
		south.add(start);
		start.addActionListener(this);
		south.add(stop);
		stop.addActionListener(this);
		south.add(throughput);
		frame.add(south, BorderLayout.SOUTH);
		
		west.setLayout(new GridLayout(3,1)); 
		west.add(semi);
		semi.addActionListener(this);
		west.add(suv);
		suv.addActionListener(this);
		west.add(sports);
		sports.addActionListener(this);
		frame.add(west, BorderLayout.WEST);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		frame.repaint();
	}
	
	public static void main(String[] args) {
		new Traffic();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(start)) {
			if (running == false) {
				running = true;
				road.resetCarCount();
				startTime = System.currentTimeMillis();
				Thread t = new Thread(this);
				t.start();
			}
		}
		if (event.getSource().equals(stop)) {
			running = false;	 
		}
		if (event.getSource().equals(semi)) {
			Semi semi = new Semi(0, 30);
			road.addCar(semi);
			for (int x = 0; x < road.ROAD_WIDTH; x = x + 20) {
				for (int y = 40; y < 480; y = y + 120) {
					semi.setX(x);
					semi.setY(y);
					if (road.collision(x, y, semi) == false) {
						frame.repaint();
						return;
					}
				}
			}
		}
		if (event.getSource().equals(sports)) {
			Sports sports = new Sports(0, 30);
			road.addCar(sports);
			for (int x = 0; x < road.ROAD_WIDTH; x = x + 20) {
				for (int y = 40; y < 480; y = y + 120) {
					sports.setX(x);
					sports.setY(y);
					if (road.collision(x, y, sports) == false) {
						frame.repaint();
						return;
					}
				}
			}
		}
		if (event.getSource().equals(suv)) {
			SUV suv = new SUV(0, 30);
			road.addCar(suv);
			for (int x = 0; x < road.ROAD_WIDTH; x = x + 20) {
				for (int y = 40; y < 480; y = y + 120) {
					suv.setX(x);
					suv.setY(y);
					if (road.collision(x, y, suv) == false) {
						frame.repaint();
						return;
					}
				}
			}
		}
	}

	@Override
	public void run() {
		while (running == true) {
			road.step();
			carcount = road.getCarCount();
			double throughputCalc = carcount / (1000 * (double)(System.currentTimeMillis() - startTime));
			throughput.setText("Throughput: " + throughputCalc);
			frame.repaint();
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
	