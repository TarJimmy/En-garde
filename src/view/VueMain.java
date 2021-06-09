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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Global.Configuration;
import Patterns.Observateur;
import model.Carte;
import model.Escrimeur;

public class VueMain extends JComponent {
	
	private final int largeurPanel = 1000;
	private final int hauteurPanel = 210;
	
	public final static int largeurCarte = 98;
	public final static int hauteurCarte = 150;
	
	private int[] cartes;
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
	
	public VueMain(int nbCartes, Boolean showFace) {
		this.showFace = showFace;
		this.cartes = new int[nbCartes];
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
			this.cartes[i] = -1;
			int posXSocle = i * espaceCarte + (espaceCarte - largeurCarte) / 2;
			this.posSocles[i] = new Point(posXSocle, posYSocle);
			this.posCartes[i] = new Point(posXSocle + ((largeurSocle - largeurCarte) / 2), posYSocle + ((hauteurSocle - hauteurCarte) / 2));
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
	    drawable = (Graphics2D)g;
	    for (int i = 0; i < this.cartes.length; i++) {
			//int posYSocle = (hauteurPanel - hauteurSocle) / 2;
			// Dessine Socle
			drawable.drawImage(imgSocle, posSocles[i].x, posSocles[i].y, largeurSocle, hauteurSocle, null);
			if (this.cartes[i] > 0) {
				int distance = this.cartes[i];
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
		setDistanceSelect(-1, false);
		this.showFace = showFace;
		repaint();
	}
	
	public void actualise(Carte[] cartes, Boolean showFace) {
		actualise(cartes);
 		actualise(showFace);
	}
	
	public void actualise(Carte[] cartes) {
		for (int i = 0; i < cartes.length; i++) {
			this.cartes[i] = cartes[i] != null ? cartes[i].getDistance() : -1;
		}
 		actualise(this.showFace);
	}
	
	public Point[] extractPosCarte(LinkedList<Integer> listI, int ajoutY) {
		Point[] points = new Point[listI.size()];
		Iterator<Integer> it = listI.iterator();
		int i = 0;
		Integer indiceCarte;
		while (it.hasNext()) {
			indiceCarte = it.next();
			points[i] = new Point(posCartes[indiceCarte].x, posCartes[indiceCarte].y + ajoutY);
			cartes[indiceCarte] = -1;
			i++;
		}
		return points;
	}
	
	public boolean getShowFace() {
		return showFace;
	}
}
