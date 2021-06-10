package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

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
	private int indiceEscrimeurCourant;
	
	private Point[] positionEscrimeurs;
	
	private Boolean animActif;
	
	private final int posYComponent = 10;
	
	final int widthEs = 150;
	final int heightEs = 214;
	
	final int widthDalle = 60;
	final int heightDalle = 281;
	
	private int espaceCase;
	private CustomJSinner spinner;
	
	private BufferedImage[] imgSelect;
	private BufferedImage[] imgNormal;
	private BufferedImage[] imgAttaqueDefense;
	private BufferedImage[] imgOver;
	private BufferedImage[] imgConseil;
	private VueMain[] vueMains;
	
	private int numCaseMouse;
	
	private boolean showCaseClickable;
	VuePlateau(Plateau p, CollecteurEvenements controle, VueMain mainGaucher, VueMain mainDroitier) {
		caseClickable = new HashSet<>();
		indiceEscrimeurCourant = Escrimeur.GAUCHER;
		init(p, controle, mainGaucher, mainDroitier);
	}
	
	VuePlateau(Plateau p, CollecteurEvenements controle, HashSet<Integer> caseAccessible, int indiceEscrimeurCourant, VueMain mainGaucher, VueMain mainDroitier) {
		this.indiceEscrimeurCourant = indiceEscrimeurCourant;
		this.controle = controle;
		caseClickable = caseAccessible;
		init(p, controle, mainGaucher, mainDroitier);
	}
	
	private void init(Plateau p, CollecteurEvenements controle, VueMain mainGaucher, VueMain mainDroitier) {
		this.controle = controle;
		this.p = p;
		this.NBCases = p.getNbCase();
		numCaseMouse = -1;
		showCaseClickable = false;
		vueMains = new VueMain[2];
		vueMains[Escrimeur.GAUCHER] = mainGaucher;
		vueMains[Escrimeur.DROITIER] = mainDroitier;
		
		numCaseMouse = -1;
		
		imgNormal = new BufferedImage[NBCases];
		imgSelect = new BufferedImage[NBCases];
		imgAttaqueDefense = new BufferedImage[NBCases];
		imgOver = new BufferedImage[NBCases];
		imgConseil = new BufferedImage[NBCases];
		try {
			for (int i = 0; i < NBCases; i++) {
				imgNormal[i] = ImageIO.read(Configuration.charge("D" + (i + 1) + ".png", Configuration.DALLES));
				imgSelect[i] = ImageIO.read(Configuration.charge("#D" + (i + 1) + ".png", Configuration.DALLES));
				imgAttaqueDefense[i] = ImageIO.read(Configuration.charge("A_D" + (i + 1) + ".png", Configuration.DALLES));
				imgOver[i] = ImageIO.read(Configuration.charge("D" + (i + 1) + "_mouse.png", Configuration.DALLES));
				imgConseil[i] = ImageIO.read(Configuration.charge("IA" + (i + 1) + ".png", Configuration.DALLES));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.positionEscrimeurs = new Point[2];
		this.positionEscrimeurs[0] = new Point(-1, posYComponent);
		this.positionEscrimeurs[1] = new Point(-1, posYComponent);
		this.animActif = false;
		this.spinner = new CustomJSinner();
		espaceCase = 1600 / NBCases;
		try {
			//this.background = ImageIO.read(Configuration.charge("Background.png", Configuration.PLATEAU));
			this.imgGaucher = ImageIO.read(Configuration.charge("Gaucher.png", Configuration.ESCRIMEURS));
			this.imgDroitier = ImageIO.read(Configuration.charge("Droitier.png", Configuration.ESCRIMEURS));
			this.imgGaucherBlack = ImageIO.read(Configuration.charge("GaucherB.png", Configuration.ESCRIMEURS));
			this.imgDroitierBlack = ImageIO.read(Configuration.charge("DroitierB.png", Configuration.ESCRIMEURS));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setPreferredSize(new Dimension(getWidth(), 300));
		addMouseListener(new AdaptateurCase(controle, NBCases, caseClickable, this));
		setLayout(null);
		add(spinner);
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int numCase = (e.getX() / espaceCase + 1);
				
				if (numCase != numCaseMouse) {
					setNumCaseMouse(numCase);
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {}
		});
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {
				setNumCaseMouse(-1);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				int numCase = (e.getX() / espaceCase + 1);
				setNumCaseMouse(numCase);
			}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
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
	
	private void setNumCaseMouse(int numCase) {
		numCaseMouse = numCase;
		repaint();
		if (caseClickable.contains(numCase)) {	
			int posECourant = p.getPosition(indiceEscrimeurCourant);
			int posEAutre = p.getPosition((indiceEscrimeurCourant + 1) % 2);
			
			int newDistanceSelect = Math.abs(numCase - (posECourant == numCase ? posEAutre : posECourant));
			
			vueMains[indiceEscrimeurCourant].setDistanceSelect(newDistanceSelect, posEAutre == numCaseMouse || posECourant == numCaseMouse);
		} else {
			vueMains[indiceEscrimeurCourant].setDistanceSelect(-1, false);
		}
	}
	
	void tracerPlateau() throws IOException {
		int posGaucher = p.getPosition(Escrimeur.GAUCHER);
		int posDroitier =  p.getPosition(Escrimeur.DROITIER);
		int indiceOtherEscrimeur = (indiceEscrimeurCourant + 1) % 2;
		int numConseil = p.getCaseAide();
		for (int i = 1; i <= NBCases; i++) {
			int posX = (i - 1) * espaceCase;
			int posXImage = posX + (espaceCase - widthDalle) / 2;
			BufferedImage imgDalle;
			if(showCaseClickable && !animActif && caseClickable.contains(i)) {
				if (numCaseMouse == i) {
					imgDalle = p.getPosition(indiceOtherEscrimeur) == i || (!animActif && p.getPosition(indiceEscrimeurCourant) == i) ? imgAttaqueDefense[i - 1] : imgOver[i - 1];
				} else {
					imgDalle = numConseil == i ? imgConseil[i - 1] : imgSelect[i - 1];
				}
			} else {
				imgDalle = imgNormal[i - 1];
			}
			
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
		if (indiceEscrimeurCourant == Escrimeur.GAUCHER) {
			drawable.drawImage(imgGaucher, positionEscrimeurs[Escrimeur.GAUCHER].x, posYComponent, widthEs, heightEs, null);
			drawable.drawImage(imgDroitierBlack, positionEscrimeurs[Escrimeur.DROITIER].x, posYComponent, widthEs, heightEs, null);
		} else {
			drawable.drawImage(imgGaucherBlack, positionEscrimeurs[Escrimeur.GAUCHER].x, posYComponent, widthEs, heightEs, null);
			drawable.drawImage(imgDroitier, positionEscrimeurs[Escrimeur.DROITIER].x, posYComponent, widthEs, heightEs, null);
		}
	}

	public void actualise(HashSet<Integer> caseClickable, Escrimeur e) {
		this.showCaseClickable = true;
		this.indiceEscrimeurCourant = e.getIndice();
		this.caseClickable.clear();
		Iterator<Integer> it = caseClickable.iterator();
		while(it.hasNext()) {
			this.caseClickable.add(it.next());
		}
		int otherIndiceEscrimeur = (indiceEscrimeurCourant + 1) % 2;
	    Boolean atkPossible = caseClickable.contains(p.getPosition(otherIndiceEscrimeur));
	    if (atkPossible) {
	    	int max = p.getNbCartesAttaque(e);
	    	spinner.setMax(max);
			spinner.setBounds((p.getPosition(indiceEscrimeurCourant) - 1) * espaceCase + espaceCase / 2 - 50, posYComponent + 150 - 65, 100, 130);
			spinner.setVisible(true);
	    } else {
	    	spinner.setVisible(false);
	    }
	    repaint();
	}
	
	public void actualise(Escrimeur e) {
		this.indiceEscrimeurCourant = e.getIndice();
		this.showCaseClickable = false;
		this.spinner.setVisible(false);
	    repaint();
	}
	
	public Animation generateAnimationDeplaceJoueur(int indiceEscrimeurCourant) {
		int espaceCase = (getWidth() / NBCases);	
		int posEscrimeur = positionEscrimeurs[indiceEscrimeurCourant].x;
		int caseDestination = p.getPosition(indiceEscrimeurCourant) * espaceCase;
		int caseSource = Math.abs((positionEscrimeurs[indiceEscrimeurCourant].x + widthEs / 2) / espaceCase + 1) * espaceCase;
		
		int distance = caseDestination - caseSource;
		
		return new AnimationEscrimeur(controle, this, posEscrimeur, distance, indiceEscrimeurCourant);
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
	
	@Override
	public void debutAnimation(int type) {
		animActif = true;
	}
	
	public int getNbCarteSelect() {
		return (int)spinner.getValue();
	}
	
	private class CustomJSinner extends JPanel {
		private final int largeurBtn = 50;
		private final int hauteurBtn = 35;
		private final Dimension dimensionPanel = new Dimension(100, 130);
		
		private BufferedImage imgBtnUp;
		private BufferedImage imgBtnUpSelect;
		
		private BufferedImage imgBtnDown;
		private BufferedImage imgBtnDownSelect;
		
		private BufferedImage imgFond;
		
		int min, max, value;
		
		private JLabel labelValue;
		private BtnNavigation btnPlus;
		private BtnNavigation btnMoins;
		
		public CustomJSinner() {
			this.min = 1;
			this.value = 0;
			this.max = 0;
			setLayout(null);
			//setBorder(new EmptyBorder(30, 0, 0, 0));
			setPreferredSize(dimensionPanel);
			
			labelValue = new JLabel();
			labelValue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			labelValue.setHorizontalTextPosition(SwingConstants.CENTER);
			labelValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			labelValue.setHorizontalAlignment(SwingConstants.CENTER);
			labelValue.setFont(new Font("Century", Font.PLAIN, 35));
			labelValue.setForeground(Color.white);
			try {
				imgFond = ImageIO.read(Configuration.charge("fondAttaque.png", Configuration.AUTRES));
				
				imgBtnUp = ImageIO.read(Configuration.charge("fleche_up.png", Configuration.AUTRES));
				imgBtnUpSelect = ImageIO.read(Configuration.charge("fleche_up_actif.png", Configuration.AUTRES));
				
				imgBtnDown = ImageIO.read(Configuration.charge("fleche_down.png", Configuration.AUTRES));
				imgBtnDownSelect = ImageIO.read(Configuration.charge("fleche_down_actif.png", Configuration.AUTRES));
				
				btnPlus = new BtnNavigation(imgBtnUp, imgBtnUpSelect, 1);
				btnMoins = new BtnNavigation(imgBtnDown, imgBtnDownSelect, -1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			add(btnPlus);
			add(btnMoins);
			add(labelValue);
			btnMoins.setBounds(50, 60 + hauteurBtn, largeurBtn, hauteurBtn);
			btnPlus.setBounds(50, 60, largeurBtn, hauteurBtn);
			labelValue.setBounds(4, 56, (int)dimensionPanel.getWidth() - largeurBtn + 10, hauteurBtn * 2);
		}
		
		public void setMax(int max) {
			this.max = max;
			setValue(max);
		}
		
		private void setValue(int newValue) {
			if (newValue >= min && newValue <= max) {
				this.value = newValue;
			}
			labelValue.setText(this.value + "");
		}
		
		public int getValue() {
			return this.value;
		}
		
		@Override
		public void paintComponent(Graphics g) {
			Graphics2D drawable = (Graphics2D)g;
			drawable.drawImage(imgFond, 0, 0, getWidth(), getHeight(), null);
		}
		
		private class BtnNavigation extends JComponent implements MouseListener {
			private BufferedImage img;
			private BufferedImage imgSelect;
			private BufferedImage imgDraw;
			
			private int addValue;
			
			public BtnNavigation(BufferedImage img, BufferedImage imgSelect, int addValue) {
				this.img = img;
				this.imgSelect = imgSelect;
				imgDraw = img;
				this.addValue = addValue;
				setPreferredSize(new Dimension(largeurBtn, hauteurBtn));
				setOpaque(false);
				addMouseListener(this);
			}
			
			@Override
			public void paintComponent(Graphics g) {
				Graphics2D drawable = (Graphics2D)g;
				drawable.drawImage(imgDraw, 0, 0, getWidth(), getHeight(), null);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				setValue(value + addValue);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				imgDraw = img;
				repaint();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				imgDraw = imgSelect;
				repaint();
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		}
	}

	public Boolean getAnimActif() {
		return animActif;
	}
	
	public boolean estCaseClickable() {
		return showCaseClickable;
	}
}
