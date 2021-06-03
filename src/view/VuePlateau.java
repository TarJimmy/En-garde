package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;

import Global.Configuration;
import Patterns.Observateur;
import model.Escrimeur;
import model.Plateau;


@SuppressWarnings("serial")
public class VuePlateau extends JPanel implements Animateur {

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
	private int indiceEcrimeursCourant;
	
	private Point[] positionEscrimeurs;
	
	private Boolean animActif;
	
	private final int posYComponent = 10;
	
	final int widthEs = 150;
	final int heightEs = 214;
	
	final int widthDalle = 60;
	final int heightDalle = 281;
	
	private int espaceCase;
	private JSpinner spinner;
	
	VuePlateau(Plateau p, CollecteurEvenements controle) {
		caseClickable = new HashSet<>();
		indiceEcrimeursCourant = Escrimeur.GAUCHER;
		init(p, controle);
	}
	
	VuePlateau(Plateau p, CollecteurEvenements controle, HashSet<Integer> caseAccessible, int indiceEcrimeursCourant) {
		this.indiceEcrimeursCourant = indiceEcrimeursCourant;
		this.controle = controle;
		caseClickable = caseAccessible;
		init(p, controle);
	}
	
	private void init(Plateau p, CollecteurEvenements controle) {
		this.controle = controle;
		this.p = p;
		this.NBCases = p.getNbCase();
		this.positionEscrimeurs = new Point[2];
		this.positionEscrimeurs[0] = new Point(-1, posYComponent);
		this.positionEscrimeurs[1] = new Point(-1, posYComponent);
		this.animActif = false;
		this.spinner = new JSpinner();
		((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
		setLayout(null);
		espaceCase = 1600 / NBCases;
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
		add(spinner);
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
		int posGaucher = p.getPosition(Escrimeur.GAUCHER);
		int posDroitier =  p.getPosition(Escrimeur.DROITIER);
		for (int i = 1; i <= NBCases; i++) {
			int posX = (i - 1) * espaceCase;
			int posXImage = posX + (espaceCase - widthDalle) / 2;
			int num = i;
			Image imgDalle = ImageIO.read(Configuration.charge((caseClickable.contains(num) ? "#" : "") + "D" + num + ".png", Configuration.DALLES));
			
			drawable.drawImage(imgDalle, posXImage, posYComponent, widthDalle, heightDalle, null);
			
			//On sauvegarde les informations des differents escrimeurs sans les dessiner, pour eviter que les dalles se dessine dessus
			// On les redessines donc à la fin
			if (!animActif) {
				if (i == posGaucher) {
					positionEscrimeurs[Escrimeur.GAUCHER].x = posXImage - 35;
				} else if (i == posDroitier) {
					positionEscrimeurs[Escrimeur.DROITIER].x = posXImage - 55;
				}
			}
		}
		if (indiceEcrimeursCourant == Escrimeur.GAUCHER) {
			drawable.drawImage(imgGaucher, positionEscrimeurs[Escrimeur.GAUCHER].x, posYComponent, widthEs, heightEs, null);
			drawable.drawImage(imgDroitierBlack, positionEscrimeurs[Escrimeur.DROITIER].x, posYComponent, widthEs, heightEs, null);
		} else {
			drawable.drawImage(imgGaucherBlack, positionEscrimeurs[Escrimeur.GAUCHER].x, posYComponent, widthEs, heightEs, null);
			drawable.drawImage(imgDroitier, positionEscrimeurs[Escrimeur.DROITIER].x, posYComponent, widthEs, heightEs, null);
		}
	}

	public void actualise(HashSet<Integer> caseClickable, Escrimeur e) {
		this.indiceEcrimeursCourant = e.getIndice();
		this.caseClickable.clear();
		Iterator<Integer> it = caseClickable.iterator();
		while(it.hasNext()) {
			this.caseClickable.add(it.next());
		}
		int otherIndiceEscrimeur = (indiceEcrimeursCourant + 1) % 2;
	    Boolean atkPossible = caseClickable.contains(p.getPosition(otherIndiceEscrimeur));
	    if (atkPossible) {
	    	int max = p.getNbCartesAttaque(e);
			spinner.setModel(new SpinnerNumberModel(max, 1, max, 1));
			System.out.println(p.getPosition(otherIndiceEscrimeur) * espaceCase + espaceCase / 2);
			spinner.setBounds((p.getPosition(otherIndiceEscrimeur) - 1) * espaceCase + espaceCase / 2 - 18, posYComponent + 150 - 15, 36, 30);
			spinner.setVisible(true);
	    } else {
	    	  spinner.setVisible(false);
	      }
	      repaint();
	}
	
	public Animation generateAnimationDeplaceJoueur(int indiceEcrimeursCourant) {
		int espaceCase = (getWidth() / NBCases);	
		int posEscrimeur = positionEscrimeurs[indiceEcrimeursCourant].x;
		int caseDestination = p.getPosition(indiceEcrimeursCourant) * espaceCase;
		int caseSource = Math.abs((positionEscrimeurs[indiceEcrimeursCourant].x + widthEs / 2) / espaceCase + 1) * espaceCase;
		
		int distance = caseDestination - caseSource;
		
		return new AnimationEscrimeur(controle, this, posEscrimeur, distance, indiceEcrimeursCourant);
	}
	
	public void deplace(int x, int indice) {
		positionEscrimeurs[indice].x = x;
		repaint();
	}
	
	public int getPosEscrimeur(int indiceEcrimeur) {
		return positionEscrimeurs[indiceEcrimeur].x;
	}

	@Override
	public void finAnimation(Animation animation) {
		animActif = false;
		repaint();
		controle.animation("Terminer", animation);
	}
	
	public void activeModeAnimation() {
		animActif = true;
	}
	
	public int getNbCarteSelect() {
		return (int)spinner.getValue();
	}
}
