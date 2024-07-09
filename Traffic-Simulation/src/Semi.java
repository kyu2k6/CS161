package Traffic;
//Semi code
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

public class Semi extends Vehicle {
	
	Image myimage;
	
	public Semi(int newx, int newy) {
		super(newx, newy);
		width = 120;
		height = 40;
		speed = 5;
		try {
			myimage = ImageIO.read(new File("512-5122348_truck-top-view-png-for-kids-truck-top.png"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	public void paintMe(Graphics g) {
		g.drawImage(myimage, x, y, null);
	}
}
