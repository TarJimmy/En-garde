package view;

import java.awt.Image;
import java.io.InputStream;

import javax.imageio.ImageIO;

import Global.Configuration;

public class ImageSwing {
	Image img;

	ImageSwing(InputStream in) {
		setImage(in);
	}
	
	public void setImage(InputStream in) {
		try {
			// Chargement d'une image utilisable dans Swing
			img = ImageIO.read(in);
		} catch (Exception e) {
			System.out.println(in + " Impossible de charger l'image");
			System.exit(1);
		}
	}

	public Image image() {
		return img;
	}
}

