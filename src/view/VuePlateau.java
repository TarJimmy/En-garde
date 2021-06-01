package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import Global.Configuration;
import Patterns.Observateur;
import model.Escrimeur;
import model.Plateau;


@SuppressWarnings("serial")
public class VuePlateau extends JComponent {

	public static final String IMAGECASEPATH = "../Images/Case";
	
	private Graphics2D drawable;
	private int NBCases;
	private Plateau p;
	private Image imgGaucher;
	private Image imgDroitier;
	private Image imgGaucherBlack;
	private Image imgDroitierBlack;
	private Image background;
	private CollecteurEvenements controle;
	private HashSet<Integer> caseClickable;
	private Boolean gaucherDoitJouer;
	
	VuePlateau(Plateau p, CollecteurEvenements controle) {
		caseClickable = new HashSet<>();
		gaucherDoitJouer = true;
		init(p, controle);
	}
	
	VuePlateau(Plateau p, CollecteurEvenements controle, HashSet<Integer> caseAccessible, Boolean gaucherDoitJouer) {
		this.gaucherDoitJouer = gaucherDoitJouer;
		this.controle = controle;
		caseClickable = caseAccessible;
		init(p, controle);
	}
	
	private void init(Plateau p, CollecteurEvenements controle) {
		this.controle = controle;
		this.p = p;
		this.NBCases = p.getNbCase();
		try {
			this.background = ImageIO.read(Configuration.charge("Background.png", Configuration.PLATEAU));
			this.imgGaucher = ImageIO.read(Configuration.charge("Gaucher.png", Configuration.ESCRIMEURS));
			this.imgDroitier = ImageIO.read(Configuration.charge("Droitier.png", Configuration.ESCRIMEURS));
			this.imgGaucherBlack = ImageIO.read(Configuration.charge("GaucherB.png", Configuration.ESCRIMEURS));
			this.imgDroitierBlack = ImageIO.read(Configuration.charge("DroitierB.png", Configuration.ESCRIMEURS));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setPreferredSize(new Dimension(getWidth(), 300));
		addMouseListener(new AdaptateurCase(controle, NBCases, caseClickable, this));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		drawable = (Graphics2D) g;
	
		int largeur = getWidth();
		int hauteur = getHeight();
		
		setOpaque(false);
		drawable.drawImage(background, 0, 0, largeur, hauteur, null);
		try {
			tracerPlateau();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void tracerPlateau() throws IOException {
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
		int espaceCase = getWidth() / NBCases;
		for (int i = 0; i < NBCases; i++) {
			int posX = i * espaceCase;
			int posXImage = posX + (espaceCase - widthDalle) / 2;
			int num = i + 1;
			Image imgDalle = ImageIO.read(Configuration.charge((caseClickable.contains(num) ? "#" : "") + "D" + num + ".png", Configuration.DALLES));
			
			drawable.drawImage(imgDalle, posXImage, HeigthPoint, widthDalle, heightDalle, null);
			
			//On sauvegarde les informations des differents escrimeurs sans les dessiner, pour eviter que les dalles se dessine dessus
			// On les redessines donc à la fin
			if (i == posGaucher - 1) {
				sauvGaucher = posXImage - 35;
			} else if (i == posDroitier - 1) {
				sauvDroitier = posXImage - 55;
			}
		}
		if (gaucherDoitJouer) {
			drawable.drawImage(imgGaucher, sauvGaucher, HeigthPoint, widthEs, heightEs, null);
			drawable.drawImage(imgDroitierBlack, sauvDroitier, HeigthPoint, widthEs, heightEs, null);
		} else {
			drawable.drawImage(imgGaucherBlack, sauvGaucher, HeigthPoint, widthEs, heightEs, null);
			drawable.drawImage(imgDroitier, sauvDroitier, HeigthPoint, widthEs, heightEs, null);
		}
	}

	public void actualise(HashSet<Integer> caseClickable, Boolean gaucherDoitJouer) {
		this.gaucherDoitJouer = gaucherDoitJouer;
		this.caseClickable.clear();
		Iterator<Integer> it = caseClickable.iterator();
	      while(it.hasNext()) {
	    	  this.caseClickable.add(it.next());
	      }
	      repaint();
	}
}
