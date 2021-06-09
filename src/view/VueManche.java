package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import Global.Configuration;

public class VueManche extends JComponent {
	
	Graphics2D drawable;
	int nbWins;
	private BufferedImage[] images;
	private Boolean isGaucher;
	private BufferedImage jeton;
	VueManche(int nbManches,int nbWins, Boolean isGaucher) {
		this.nbWins = nbWins;
		this.isGaucher = isGaucher;
		images = new BufferedImage[nbManches];
		try {
			for(int i = 0; i < nbManches; i++) {
				images[i] = ImageIO.read(Configuration.charge("M" + (i + 1) + ".png", Configuration.MANCHES));
			}
			jeton = ImageIO.read(Configuration.charge("Jeton" + (isGaucher ? "G" : "D") + ".png", Configuration.MANCHES));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setPreferredSize(new Dimension(600, 70));
		setOpaque(false);
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		drawable = (Graphics2D) g;
		tracerManches();
	}
	
	public void setNbWins(int nbWins) {
		this.nbWins = nbWins;
		repaint();
	}
	
	public void tracerManches() {
		int sizeManche = 50;
		int y = isGaucher ? 20 : 0;
		int x = isGaucher ? 0 : getWidth() - ((images.length + 2) * (sizeManche + 15));
		
		for (int i = 0; i < images.length; i++) {
			x += sizeManche + 15;
			drawable.drawImage(images[i], x, y, sizeManche, sizeManche, null);
			if (nbWins == i + 1) {
				drawable.drawImage(jeton, x, y, sizeManche, sizeManche, null);
			}
		}
	}
}
