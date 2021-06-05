package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
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
	
	private final int largeurPanel = 1000;
	private final int hauteurPanel = 210;
	
	public final static int largeurCarte = 98;
	public final static int hauteurCarte = 150;
	
	private Carte[] cartes;
	private final int largeurSocle = 160;
	private final int hauteurSocle = 203;
	private Graphics2D drawable;
	private Boolean showFace;
	private BufferedImage imgSocle;
	private BufferedImage[] imgCartes;
	private BufferedImage imgDos;
	private BufferedImage[] imgCartesSelect;
	private BufferedImage[] imgCartesAttaque;
	
	private int distanceSelect;
	private boolean isAttaqueDefense;
	
	public Point[] posCartes;
	public Point[] posSocles;
	
	private int espaceCarte;
	
	public VueMain(Carte[] cartes, Boolean showFace) {
		this.showFace = showFace;
		this.cartes = cartes;
		this.distanceSelect = -1;
		this.isAttaqueDefense = false;
		this.setPreferredSize(new Dimension(largeurPanel, hauteurPanel));
		this.espaceCarte = (largeurPanel - 30) / this.cartes.length;
		try {
			imgSocle = ImageIO.read(Configuration.charge("socle.png", Configuration.CARTES));
			imgDos = ImageIO.read(Configuration.charge("Dos.png", Configuration.CARTES));
			imgCartes = new BufferedImage[5];
			imgCartesSelect = new BufferedImage[5];
			imgCartesAttaque = new BufferedImage[5];
			for (int i = 0; i < 5; i++) {
				imgCartes[i] = ImageIO.read(Configuration.charge((i + 1) + ".png", Configuration.CARTES));
				imgCartesSelect[i] = ImageIO.read(Configuration.charge((i + 1) + "_actif.png", Configuration.CARTES));
				imgCartesAttaque[i] = ImageIO.read(Configuration.charge((i + 1) + "_attaque.png", Configuration.CARTES)); // ----A changer
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		final int posYSocle = (hauteurPanel - hauteurSocle) / 2;
		
		this.posCartes = new Point[cartes.length];
		this.posSocles = new Point[cartes.length];
		for (int i = 0; i < cartes.length; i++) {
			int posXSocle = 15 + i * espaceCarte + (espaceCarte - espaceCarte) / 2;
			this.posSocles[i] = new Point(posXSocle, posYSocle);
			this.posCartes[i] = new Point(posXSocle + ((espaceCarte - largeurCarte) / 2), posYSocle + ((hauteurSocle - hauteurCarte) / 2));
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
	    drawable = (Graphics2D)g;
	    
	    for (int i = 0; i < this.cartes.length; i++) {
			int posXSocle = 15 + i * espaceCarte + (espaceCarte - espaceCarte) / 2;
			//int posYSocle = (hauteurPanel - hauteurSocle) / 2;
			// Dessine Socle
			drawable.drawImage(imgSocle, posSocles[i].x, posSocles[i].y, espaceCarte, hauteurSocle, null);
			if (this.cartes[i] != null) {
				int distance = this.cartes[i].getDistance();
				Image img;
				if (showFace) {
					if (distance == distanceSelect) {
						img = isAttaqueDefense ? imgCartesAttaque[distance - 1] : imgCartesSelect[distance - 1];
					} else {
						img = imgCartes[distance - 1];
					}
				} else {
					img = imgDos;
				}
				drawable.drawImage(img, posCartes[i].x, posCartes[i].y, largeurCarte, hauteurCarte, null);
			}
		}
	}
	
	public void setDistanceSelect(int newValue, boolean isAttaqueDefense) {
		this.distanceSelect = newValue;
		this.isAttaqueDefense = isAttaqueDefense;
		repaint();
	}
	
	public void actualise(Boolean showFace) {
		this.showFace = showFace;
		repaint();
	}
}
