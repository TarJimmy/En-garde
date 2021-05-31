package view;

import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

import Global.Configuration;


public class VueCarte extends JComponent {
	
	private final String nameCarteDos = "Dos.png";
	
	private Graphics2D drawable;
	
	private Boolean showFace;
	private BufferedImage imageFace;
	private BufferedImage imageDos;

	public VueCarte(Boolean showFace, int distance) {
		try {                
			this.imageDos = ImageIO.read(Configuration.charge(this.nameCarteDos, Configuration.CARTES));
			this.imageFace = ImageIO.read(Configuration.charge(distance + ".png", Configuration.CARTES));
			changeCoter(showFace);
       } catch (IOException ex) {
    	   ex.printStackTrace();
       }
	}

	public void repaintCarte(int distance, Boolean showFace) {
		//On recharge l'image en cas de modification
		try {
			this.imageFace = ImageIO.read(Configuration.charge(distance + ".png", Configuration.CARTES));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		changeCoter(showFace);
		repaint();
	}
	
	public void repaintCarte(int distance) {
		//On recharge l'image en cas de modification
		try {
			this.imageFace = ImageIO.read(Configuration.charge(distance + ".png", Configuration.CARTES));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint();
	}
	
	public void repaintCarte(Boolean showFace) {
		changeCoter(showFace);
		repaint();
	}
	
	public void changeCoter(Boolean showFace) {
		this.showFace = showFace;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		drawable = (Graphics2D) g;
		//g..setStroke(new BasicStroke(6));
		Image iCarte = showFace ? imageFace : imageDos;
		setSize(98, 150);
		int largeur = getWidth();
		int hauteur = getHeight();
		// On efface tout
		drawable.clearRect(0, 0, largeur, hauteur);
		
		drawable.drawImage(iCarte, 0, 0, largeur, hauteur, null);
	}
}
