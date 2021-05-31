package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import Global.Configuration;
import model.Escrimeur;
import model.Plateau;


public class VuePlateau extends JComponent {

	public static final String IMAGECASEPATH = "../Images/Case";
	
	private Graphics2D drawable;
	private int NBCases;
	private Image[] imgDalles;
	private Plateau p;
	private Image imgGaucher;
	private Image imgDroitier;
	private Image background;
	VuePlateau(Plateau p) {
		this.p = p;
		this.NBCases = p.getNbCase();
		imgDalles = new Image[NBCases];
		try {
			this.background = ImageIO.read(Configuration.charge("Background.png", Configuration.PLATEAU));
			this.imgGaucher = ImageIO.read(Configuration.charge("Gaucher.png", Configuration.ESCRIMEURS));
			this.imgDroitier = ImageIO.read(Configuration.charge("Droitier.png", Configuration.ESCRIMEURS));
			for (int i = 0; i < NBCases; i++) {
				imgDalles[i] = ImageIO.read(Configuration.charge("D" + (i % 25 + 1) + ".png", Configuration.DALLES));
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setPreferredSize(new Dimension(getWidth(), 300));
		//setLayout(new FlowLayout(FlowLayout.CENTER));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		drawable = (Graphics2D) g;
	
		int largeur = getWidth();
		int hauteur = getHeight();
		
		setOpaque(false);
		drawable.drawImage(background, 0, 0, largeur, hauteur, null);
		tracerPlateau();
	}
	
	void tracerPlateau() {
		int HeigthPoint = 10;
		int posGaucher = p.getPosition(Escrimeur.GAUCHER);
		int posDroitier =  p.getPosition(Escrimeur.DROITIER);
	
		final int widthEs = 150;
		final int heightEs = 214;
		
		final int widthDalle = 60;
		final int heightDalle = 281;
		
		//On sauvegarde les informations des differents escrimeurs sans les dessiner, pour eviter que les dalles se dessine dessus
		// On les redessines donc à la fin
		int sauvGaucher = 0;
		int sauvDroitier = 0;
		
		for (int i = 0; i < NBCases; i++) {
			int WidthPoint = 15 + (getWidth()/NBCases) * i;

			drawable.drawImage(imgDalles[i], WidthPoint, HeigthPoint, widthDalle, heightDalle, null);
			
			//On sauvegarde les informations des differents escrimeurs sans les dessiner, pour eviter que les dalles se dessine dessus
			// On les redessines donc à la fin
			if (i == posGaucher - 1) {
				sauvGaucher = WidthPoint -35;
			} else if (i == posDroitier - 1) {
				sauvDroitier = WidthPoint - 55;
			}
		}
		drawable.drawImage(imgGaucher, sauvGaucher, HeigthPoint, widthEs, heightEs, null);
		drawable.drawImage(imgDroitier, sauvDroitier, HeigthPoint, widthEs, heightEs, null);
	}
}
