package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Global.Configuration;
import Patterns.Observateur;
import model.Carte;

public class VueMain extends JComponent {

	private Carte[] cartes;
	private final int largeurSocle = 160;
	private final int hauteurSocle = 203;
	private Graphics2D drawable;
	
	private Image imgSocle;
	
	public VueMain(Carte[] cartes) {
		this.cartes = cartes;
		this.setPreferredSize(new Dimension(1000, 300));
		setPreferredSize(new Dimension(120, 150));
		try {
			imgSocle = ImageIO.read(Configuration.charge("socle.png", Configuration.CARTES));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
	    drawable = (Graphics2D)g;
	
	    int largeurSocle = (getWidth() - 30) / this.cartes.length;
	    int hauteur = getHeight();
	    
	    try {
		    for (int i = 0; i < this.cartes.length; i++) {
				generateSocleCarte(i, largeurSocle, hauteur, this.cartes[i] != null ? this.cartes[i].getDistance() : 0);
		    }
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void generateSocleCarte(int indice, int largeur, int hauteur, int distance) throws IOException {
		int posXSocle = 15 + indice * largeur + (largeur - largeurSocle) / 2;
		int posYSocle = (hauteur - hauteurSocle) / 2;
		//Socle
		drawable.drawImage(imgSocle, posXSocle, posYSocle, largeurSocle, hauteurSocle, null);
		if (distance > 0) {
			int largeurCarte = 98;
			int hauteurCarte = 150;
			Image img = ImageIO.read(Configuration.charge(distance + ".png", Configuration.CARTES));
			drawable.drawImage(img, posXSocle + ((largeurSocle - largeurCarte) / 2), posYSocle + ((hauteurSocle - hauteurCarte) / 2), largeurCarte, hauteurCarte, null);
		}
	}
}
