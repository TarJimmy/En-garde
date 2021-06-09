package view;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Global.Configuration;
import Patterns.Observateur;
import model.Carte;
import controller.ControlerJeu;
import model.Escrimeur;
import model.IA;
import model.Jeu;
import model.Jeu.Action;

public class InterfaceGraphiqueJeu implements Runnable, Observateur {
	
	public static int ajoutVitesse = 0;
	
	private final int hauteurFenetre = 900;
	private final int largeurFenetre = 1600;
	
	private final Point posDeckPioche = new Point(1000 + VueDeck.posCarteDeckPioche.x, 600 + VueDeck.posCarteDeckPioche.y);
	private final Point posDeckDefausse = new Point(1000 + VueDeck.posCarteDeckDefausse.x, 600 + VueDeck.posCarteDeckDefausse.y);
	
	private static JFrame frame;
	CollecteurEvenements controle;
	
	private Jeu jeu;
	VueEscrimeur[] vueEscrimeurs;
	
	VueDeck vueDecks;
	
	VuePlateau vuePlateau;
	
	VueInfoJeu vueInfoJeu;
	
	
	private PanelAnimation panelAnimation;
	
	private static boolean jeuPresent = false;
	
	private InterfaceGraphiqueJeu(CollecteurEvenements controle, Jeu jeu) {
		this.controle = controle;
		this.jeu = jeu;
		jeu.ajouteObservateur(this);
	}
	
	public static void demarrer(CollecteurEvenements control, Jeu jeu) {
		if (jeuPresent) {
			frame.setVisible(false);
			frame.dispose();
		}
		SwingUtilities.invokeLater(new InterfaceGraphiqueJeu(control, jeu));
		jeuPresent = true;
	}
	

	public static void demarrer(ControlerJeu controlerJeu) {
		if (jeuPresent) {
			frame.setVisible(false);
			frame.dispose();
		}
		SwingUtilities.invokeLater(new InterfaceGraphiqueJeu(controlerJeu, controlerJeu.getJeu()));
		jeuPresent = true;
	}
	
	/**
	 * Ferme la fenetre de jeu
	 */
	public static void close() {
		if (frame != null) {
			frame.setVisible(false);
			frame.dispose();
		}
	}
	
	private Point[] getPosCartesModifierRecemment(int indice, LinkedList<Integer> indicesCartesADeplacer) {
		Point[] points = vueEscrimeurs[indice].extractPosCarte(indicesCartesADeplacer);
		Point[] pointsRes = new Point[points.length];
		int x = indice == Escrimeur.GAUCHER ? 0 : 600;
		int y = indice == Escrimeur.GAUCHER ? 600 : 0;
		for (int i = 0; i < points.length; i++) {
			pointsRes[i] = new Point(points[i].x + x, points[i].y + y);
		}
		return pointsRes; 
	}
	@Override
	public void miseAJour() {
		Action action = jeu.getActionCourante();
			
		System.out.println(action);
		switch (action) {
			case ACTUALISER_JEU:
			case ACTUALISER_PLATEAU: // Actualise les cases accessibles du plateau
				HashSet<Integer> casesJouables = jeu.casesJouables();
				int indiceCurrent = jeu.getIndiceCurrentEscrimeur();
				vueEscrimeurs[indiceCurrent].setBtnPasserTourVisibility(jeu.isIACurrent() && casesJouables.contains(-1));
				vueEscrimeurs[(indiceCurrent + 1) % 2].setBtnPasserTourVisibility(false);
				vuePlateau.actualise(casesJouables, jeu.getCurrentEscrimeur());
				if (action == Action.ACTUALISER_PLATEAU) { // Stop si ce n'est pas  un changement de tour
					controle.commande("ActionTerminer");
					break;
				}
			case ACTUALISER_DECK: // Actualise les decks
				vueDecks.actualise(jeu.popLastCarteDeck());
				if (action == Action.ACTUALISER_DECK) { // Stop si ce n'est pas  un changement de tour
					controle.commande("ActionTerminer");
					break;
				}
			case ACTUALISER_ESCRIMEUR:
			case ACTUALISER_ESCRIMEUR_DROITIER :
				vueEscrimeurs[Escrimeur.DROITIER].actualise(jeu.popShowCarte(Escrimeur.DROITIER), (jeu.isIA(Escrimeur.DROITIER) && jeu.getIsTourGaucher() == false || jeu.getShowAllCartes());
				if (action == Action.ACTUALISER_ESCRIMEUR_DROITIER) { // Stop si ce n'est pas  un changement de tour ou l'actualisation des 2 mains
					controle.commande("ActionTerminer");
					break;
				}
			case ACTUALISER_ESCRIMEUR_GAUCHER:
				vueEscrimeurs[Escrimeur.GAUCHER].actualise(jeu.popShowCarte(Escrimeur.GAUCHER), (jeu.isIA(Escrimeur.GAUCHER) && jeu.getIsTourGaucher()) || jeu.getShowAllCartes());
				controle.commande("ActionTerminer");
				break;
			case ACTUALISER_ESCRIMEUR_SANS_BUTTON:
				vueEscrimeurs[Escrimeur.GAUCHER].setBtnPasserTourVisibility(false);
				vueEscrimeurs[Escrimeur.DROITIER].setBtnPasserTourVisibility(false);
				controle.commande("ActionTerminer");
				break;
			case ANIMATION_DEPLACER_ESCRIMEUR:
				controle.animation("Ajouter", vuePlateau.generateAnimationDeplaceJoueur(jeu.getIndiceCurrentEscrimeur()));
				break;
			case ACTUALISER_PLATEAU_SANS_CASE:
				vuePlateau.actualise(jeu.getCurrentEscrimeur());
				controle.commande("ActionTerminer");
				break;
			case ACTUALISER_PLATEAU_MEME_MODE:
				vuePlateau.repaint();
				controle.commande("ActionTerminer");
				break;
			case ACTUALISER_ESCRIMEUR_MEME_MODE:
				vueEscrimeurs[Escrimeur.GAUCHER].actualise();
				vueEscrimeurs[Escrimeur.DROITIER].actualise();
				break;
			case ANIMATION_PIOCHER:
				int indicePiocher = jeu.popListeIndiceEscrimeurPiocheCarte();
				LinkedList<Integer> indicesCartesAPiocher = jeu.popListeIndicesPiocheRecemment(indicePiocher);
				LinkedList<Integer> distancesListPiocher = jeu.popListeDistancesPiocheRecemment(indicePiocher);
				Point[] destinationsPiocher = getPosCartesModifierRecemment(indicePiocher, indicesCartesAPiocher);
				Point[] departsPiocher = new Point[destinationsPiocher.length];
				int[] distancesPiocher = new int[departsPiocher.length];
				
				for (int i = 0; i < departsPiocher.length; i++) {
					departsPiocher[i] = posDeckPioche;
					distancesPiocher[i] = distancesListPiocher.get(i);
				}
				controle.animation("Ajouter", panelAnimation.generateAnimationDeplacerCartes(departsPiocher, destinationsPiocher, distancesPiocher, null, indicePiocher));
				break;
			case ANIMATION_DEFAUSSER:
				int indiceDefausser = jeu.popListeIndiceEscrimeurDefausseCarte();
				LinkedList<Integer> indicesCartesADefausser = jeu.popListeIndiceDefausseRecemment(indiceDefausser);
				LinkedList<Integer> distancesListDefausser = jeu.popListeDistancesDefausseRecemment(indiceDefausser);
				Carte[] etatMain = jeu.popEtatMainDefausse(indiceDefausser);
				Point[] departsDefausser = getPosCartesModifierRecemment(indiceDefausser, indicesCartesADefausser);
				Point[] destinationsDefausser = new Point[departsDefausser.length];
				int[] distancesDefausser = new int[departsDefausser.length];
				for (int i = 0; i < departsDefausser.length; i++) {
					destinationsDefausser[i] = posDeckDefausse;
					distancesDefausser[i] = distancesListDefausser.get(i);
				}
				controle.animation("Ajouter",panelAnimation.generateAnimationDeplacerCartes(departsDefausser, destinationsDefausser, distancesDefausser, etatMain, indiceDefausser));
				break;
			case ANIMATION_FIN_MANCHE:
				controle.animation("Ajouter", panelAnimation.generateAnimationFinManche(jeu.getIndiceWinnerManche()));
				break;
			case ANIMATION_LANCER:
				controle.animation("Lancer", null);
				break;
			case ANIMATION_CHANGER_TOUR:
				controle.animation("Ajouter", panelAnimation.generateAnimationChangementJoueur());
				break;
			case IA_JOUE:
				System.out.println("lance IA switch");
				panelAnimation.generateCoupIA(jeu.getIACurrent());
				controle.commande("ActionTerminer");
			default:
				break;
			}
	}
	
	public void run() {
		frame = new JFrame("En Garde !");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setIconImage(Configuration.imgIcone);
		JLabel background = new JLabel();
		
		try {
			background.setIcon(new ImageIcon(ImageIO.read(Configuration.charge("Background.png", Configuration.BG))));
			background.setLayout(new GridLayout(3, 1));
			background.setPreferredSize(new Dimension(largeurFenetre, hauteurFenetre));
			frame.add(background);
			frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageIO.read(Configuration.charge("curseur.png", Configuration.AUTRES)),new Point(0,0),"Mon curseur"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		vueEscrimeurs = new VueEscrimeur[2];
		// Haut
		JPanel panelTop = new JPanel(new BorderLayout());
		panelTop.setOpaque(false);
		// Haut -> Gauche
		panelTop.add(new VueInfoJeu(jeu.getEscrimeurGaucher().getNom(), jeu.getEscrimeurDroitier().getNom(), controle), BorderLayout.WEST);
		// Haut -> Droit
		vueEscrimeurs[Escrimeur.DROITIER] = new VueEscrimeur(controle, jeu.getPlateau(), jeu.getEscrimeurDroitier(), !jeu.getIsTourGaucher(), jeu.getNbManchesPourVictoire());
		panelTop.add(vueEscrimeurs[Escrimeur.DROITIER],BorderLayout.EAST);
		
		// Bas
		JPanel panelBot = new JPanel(new BorderLayout());
		panelBot.setOpaque(false);
		
		// Bas -> Gauche
		vueEscrimeurs[Escrimeur.GAUCHER] = new VueEscrimeur(controle, jeu.getPlateau(), jeu.getEscrimeurGaucher(), jeu.getIsTourGaucher(), jeu.getNbManchesPourVictoire());
		panelBot.add(vueEscrimeurs[Escrimeur.GAUCHER], BorderLayout.WEST);
		
		// Bas -> Droite
		vueDecks = new VueDeck(jeu.getDeckPioche(), jeu.getDeckDefausse());	
		panelBot.add(vueDecks, BorderLayout.EAST);
		
		// Centre
		vuePlateau = new VuePlateau(jeu.getPlateau(), controle, vueEscrimeurs[Escrimeur.GAUCHER].getVueMain(), vueEscrimeurs[Escrimeur.DROITIER].getVueMain());
				
		background.add(panelTop);
		background.add(vuePlateau);
		background.add(panelBot);
		
		panelAnimation = new PanelAnimation(largeurFenetre, hauteurFenetre);
		final JPanel glass = (JPanel) frame.getGlassPane();
		glass.setLayout(new GridBagLayout());
	    glass.setVisible(true);
	    glass.add(panelAnimation);
		frame.pack();
		background.setFocusable(true);
		background.requestFocus(); 
		frame.setVisible(true);
		controle.commande("PageInitialiser");
	}
	@SuppressWarnings("serial")
	public class PanelAnimation extends JComponent implements Animateur {
		private int animActif;
		private Point ptDebutPanelAnimation;
		private final Dimension sizeImgPanelAnimation = new Dimension(600, 400);
		private BufferedImage[] imgFinDeManche;
		private int winnerManche;
		
		private Point[] posADessiner;
		private int[] distanceCarteADessiner;
		private BufferedImage[] cartesPossibles;
		private BufferedImage imgDos;
		
		private BufferedImage imgChangementTour;
		private Carte[] etatMain;
		private int indiceEscrimeurCarte;
		private boolean showFace;
		private boolean[] sauveShowFaceVue;
		public PanelAnimation(int largeur, int hauteur) {
			super();
			setPreferredSize(new Dimension(largeur, hauteur));
			setVisible(true);
			setOpaque(false);
			try {
				imgFinDeManche = new BufferedImage[3];
				imgFinDeManche[Jeu.EGAUCHER] = ImageIO.read(Configuration.charge("findeManche_Gaucher.png" , Configuration.AUTRES));
				imgFinDeManche[Jeu.EDROITIER] = ImageIO.read(Configuration.charge("findeManche_Droitier.png" , Configuration.AUTRES));
				imgFinDeManche[Jeu.EGALITE] = ImageIO.read(Configuration.charge("findeManche_Nulle.png" , Configuration.AUTRES));
				imgChangementTour = ImageIO.read(Configuration.charge("changementJoueur.png", Configuration.AUTRES));
				cartesPossibles = new BufferedImage[5];
				for (int x = 0; x < 5; x++) {
					cartesPossibles[x] = ImageIO.read(Configuration.charge((x + 1) + ".png", Configuration.CARTES));
				}
				imgDos = ImageIO.read(Configuration.charge("Dos.png", Configuration.CARTES));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ptDebutPanelAnimation = new Point();
			ptDebutPanelAnimation.y = 250;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			Graphics2D drawable = (Graphics2D)g;
			switch (animActif) {
				case Animation.CHANGEMENT_JOUEUR:
					drawable.drawImage(imgChangementTour, ptDebutPanelAnimation.x, ptDebutPanelAnimation.y, sizeImgPanelAnimation.width, sizeImgPanelAnimation.height, null);
					break;
				case Animation.ANIM_FIN_MANCHE:
					drawable.drawImage(imgFinDeManche[winnerManche], ptDebutPanelAnimation.x, ptDebutPanelAnimation.y, sizeImgPanelAnimation.width, sizeImgPanelAnimation.height, null);
					drawable.setFont(new Font("Century", Font.PLAIN, 50));
					String text = winnerManche == Jeu.EGALITE ? "Manche nulle" : jeu.getEscrimeurs()[winnerManche].getNom();
					FontMetrics fm = drawable.getFontMetrics();
		            int x = ptDebutPanelAnimation.x - (fm.stringWidth(text) / 2) + (sizeImgPanelAnimation.width / 2);
		            drawable.drawString(text, x, ptDebutPanelAnimation.y + 150);
		            drawable.drawLine(ptDebutPanelAnimation.x, ptDebutPanelAnimation.y + ptDebutPanelAnimation.y / 2, ptDebutPanelAnimation.x + fm.stringWidth(text), ptDebutPanelAnimation.y + ptDebutPanelAnimation.y / 2);
					break;
				case Animation.ANIM_CARTES:
					for (int i = 0; i < posADessiner.length; i++) {
						BufferedImage img = showFace ? cartesPossibles[distanceCarteADessiner[i] - 1] : imgDos;
						drawable.drawImage(img, posADessiner[i].x, posADessiner[i].y, VueMain.largeurCarte,  VueMain.hauteurCarte, null);
					}
					break;
				default:
					break;
			}
		}
		
		public Animation generateAnimationDeplacerCartes(Point[] departs, Point[] destinations, int[] distanceCarte, Carte[] etatMain, int indiceEscrimeur) {
			if (departs.length != destinations.length || departs.length != distanceCarte.length) {
				throw new IllegalArgumentException("La taille des tableaux doivent être égal");
			}
			this.etatMain = etatMain;
			this.indiceEscrimeurCarte = indiceEscrimeur;
			posADessiner = departs;
			distanceCarteADessiner = distanceCarte; // Va peut etre poser un probleme si les action senchaine trop vite
			Point[] distances = new Point[departs.length];
			for (int i = 0; i < departs.length; i++) {
				distances[i] = new Point(destinations[i].x - departs[i].x, destinations[i].y - departs[i].y);
			}
			return new AnimationDeplacerCarte(controle, this, departs, distances, indiceEscrimeur, distanceCarteADessiner, vueEscrimeurs[indiceEscrimeurCarte].getVueMain(), etatMain);
		} 
		
		public Animation generateAnimationFinManche(int winnerManche) {
			this.winnerManche = winnerManche;
			ptDebutPanelAnimation.x = -sizeImgPanelAnimation.width;
			int distance = 1600 + sizeImgPanelAnimation.width;
			return new AnimationRectiligneAvecPause(controle, this, ptDebutPanelAnimation.x, distance, Animation.ANIM_FIN_MANCHE, 2000 + ajoutVitesse, (float)(1.0 / (200.0 + ajoutVitesse / 10)));
		}
		
		public Animation generateAnimationChangementJoueur() {
			ptDebutPanelAnimation.x = -sizeImgPanelAnimation.width;
			int distance = 1600 + sizeImgPanelAnimation.width;
			return new AnimationRectiligneAvecPause(controle, this, ptDebutPanelAnimation.x, distance, Animation.CHANGEMENT_JOUEUR, 2000 + ajoutVitesse, (float)(1.0 / (200.0 + ajoutVitesse / 10)));
		}

		public Animation generateCoupIA(IA iA) {
			System.out.println("lance IA");
			return new AnimationIAJoue(controle, this, iA);
		}
		
		@Override
		public void finAnimation(Animation animation) {
			animActif = animation.ANIM_NONE;
			
			if (animActif == Animation.CHANGEMENT_JOUEUR) {
				vueEscrimeurs[Escrimeur.GAUCHER].setShowFace(sauveShowFaceVue[Escrimeur.GAUCHER]);
				vueEscrimeurs[Escrimeur.DROITIER].setShowFace(sauveShowFaceVue[Escrimeur.DROITIER]);
			}
			
			controle.animation("Terminer", animation);
		}

		@Override
		public void debutAnimation(int typeAnimation) {
			animActif = typeAnimation;
			if (animActif == Animation.CHANGEMENT_JOUEUR) {
				sauveShowFaceVue = new boolean[2];
				sauveShowFaceVue[Escrimeur.GAUCHER] = vueEscrimeurs[Escrimeur.GAUCHER].getShowFace();
				sauveShowFaceVue[Escrimeur.DROITIER] = vueEscrimeurs[Escrimeur.DROITIER].getShowFace();
				vueEscrimeurs[Escrimeur.GAUCHER].setShowFace(false);
				vueEscrimeurs[Escrimeur.DROITIER].setShowFace(false);
				vuePlateau.actualise(jeu.getCurrentEscrimeur());
			}
		};
		
		public void deplacePanel(int newX) {
			ptDebutPanelAnimation.x = newX;
			repaint();
		}
		
		public void deplaceCartes(Point[] newPoints, int indiceEscrimeur, int[] distanceCarteADessiner) {
			this.distanceCarteADessiner = distanceCarteADessiner;
			posADessiner = newPoints;
			this.showFace = vueEscrimeurs[indiceEscrimeurCarte].getShowFace();
			repaint();
		}

		public void joueCoup(int numCase, int nbCarte) {
			controle.clickCase(numCase, nbCarte);
		}
	}
}
