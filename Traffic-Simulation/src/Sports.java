package Traffic;
//sports code
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sports extends Vehicle{
	
	Image myimage;

	public Sports(int newx, int newy) {
		super(newx, newy);
		width = 40;
		height = 20;	
		speed = 12;
		try {
			myimage = ImageIO.read(new File("images.jfif"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

			
	public void paintMe(Graphics g) {
		g.drawImage(myimage, x, y, null);
	}
}
