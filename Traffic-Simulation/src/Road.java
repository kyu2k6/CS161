package Traffic;
//Making the road
import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Road extends JPanel{
	
	final int LANE_HEIGHT = 120;
	final int ROAD_WIDTH = 800;
	ArrayList<Vehicle> cars = new ArrayList<Vehicle>();
	int carCount = 0;
	
	public Road() {
		super();
	}

	
	public void addCar(Vehicle v) {
		cars.add(v);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.WHITE);
		for (int a = LANE_HEIGHT; a < LANE_HEIGHT * 4;  a = a + LANE_HEIGHT) { //which lane
			for (int b = 0; b < getWidth(); b = b + 40) { //which line
				g.fillRect(b, a, 30, 5);
			}
		}
		//Draw cars
		for (int a = 0; a < cars.size(); a++) {
			cars.get(a).paintMe(g);
		}
	}
	
	public void step() {
		for (int a = 0; a < cars.size(); a++) {
			Vehicle v = cars.get(a);
			if (collision(v.getX()+v.getSpeed(), v.getY(), v) == false) {//there's no collision 
				v.setX(v.getX() + v.getSpeed());
				if (v.getX() > ROAD_WIDTH) {
					if (collision(0, v.getY(), v) == false) {
						v.setX(0);
						carCount++;
					}
				}
			}
			else {// car ahead!
				if ((v.getY() > 40) && 
						(collision(v.getX(), v.getY() - LANE_HEIGHT, v)) == false) {
					v.setY(v.getY() - LANE_HEIGHT);
				}
				else if ((v.getY() < 40 + 3 * LANE_HEIGHT) && 
							(collision(v.getX(), v.getY() - LANE_HEIGHT, v)) == false) {
					v.setY(v.getY() + LANE_HEIGHT);
				}
			}
		}
	}
	
	public boolean collision(int x, int y, Vehicle v) {
		for (int a = 0; a < cars.size(); a++) {
			Vehicle u = cars.get(a);
			if (y == u.getY()) { //if I'm in the same lane
				if (u.equals(v) == false) {
					if (x < u.getX() + u.getWidth() && //my left side is left of his right side
							x + v.getWidth() > u.getX()) { //my right side is right of his left side
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public int getCarCount() {
		return carCount;  
	}
	
	public void resetCarCount() {
		carCount = 0;
	}
}
