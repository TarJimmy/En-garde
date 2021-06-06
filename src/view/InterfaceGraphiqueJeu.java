package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
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
import model.Escrimeur;
import model.Jeu;
import model.Jeu.Action;

public class InterfaceGraphiqueJeu implements Runnable, Observateur {
	
	private final int hauteurFenetre = 900;
	private final int largeurFenetre = 1600;
	private static JFrame frame;
	CollecteurEvenements controle;
	
	private Jeu jeu;
	VueEscrimeur mainGaucher;
	VueEscrimeur mainDroitier;
	
	VueDeck vueDecks;
	
	VuePlateau vuePlateau;
	
	VueInfoJeu vueInfoJeu;
	
	
	private PanelAnimation panelAnimation;
	
	private InterfaceGraphiqueJeu(CollecteurEvenements controle, Jeu jeu) {
		this.controle = controle;
		this.jeu = jeu;
		jeu.ajouteObservateur(this);
	}
	
	public static void demarrer(CollecteurEvenements control, Jeu jeu) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueJeu(control, jeu));
	}
	
	/**
	 * Ferme la fenetre de jeu
	 */
	public static void close() {
		if(frame!=null) {
			frame.setVisible(false);
			frame.dispose();
		}
	}
	
	@Override
	public void miseAJour() {
		Action action = jeu.getActionCourante();
		switch (action) {
			case ACTUALISER_JEU:
			case ACTUALISER_PLATEAU: // Actualise les cases accessibles du plateau
				vuePlateau.actualise(jeu.casesJouables(), jeu.getCurrentEscrimeur());
				if (action != Action.ACTUALISER_JEU) { // Stop si ce n'est pas  un changement de tour
					controle.commande("ActionTerminer");
					break;
				}
			case ACTUALISER_DECK: // Actualise les decks
				vueDecks.repaint();
				if (action != Action.ACTUALISER_JEU) { // Stop si ce n'est pas  un changement de tour
					controle.commande("ActionTerminer");
					break;
				}
			case ACTUALISER_ESCRIMEUR:
			case ACTUALISER_ESCRIMEUR_DROITIER :
				System.out.println("Droitier passer tour " + jeu.getPeutPasserTour());
				mainDroitier.actualise(jeu.getIsTourGaucher() == false, jeu.getPeutPasserTour());
				if (action != Action.ACTUALISER_JEU && action != Action.ACTUALISER_ESCRIMEUR) { // Stop si ce n'est pas  un changement de tour ou l'actualisation des 2 mains
					controle.commande("ActionTerminer");
					break;
				}
			case ACTUALISER_ESCRIMEUR_GAUCHER:
				System.out.println("Gaucher passer tour " + jeu.getPeutPasserTour());

				mainGaucher.actualise(jeu.getIsTourGaucher(), jeu.getPeutPasserTour());
				controle.commande("ActionTerminer");
				break;
			case ANIMATION_DEPLACER_ESCRIMEUR:
				controle.animation("Ajouter", vuePlateau.generateAnimationDeplaceJoueur(jeu.getIndiceCurrentEscrimeur()));
				break;
			case ANIMATION_PIOCHER:
				controle.animation("Ajouter", panelAnimation.generateAnimationDeplacerCartes());
			case ANIMATION_FIN_MANCHE:
				controle.animation("Ajouter", panelAnimation.generateAnimationFinManche(jeu.getIndiceWinnerManche()));
				break;
			case ANIMATION_LANCER:
				controle.animation("Lancer", null);
				break;
			
			default:
				System.out.println("Action non reconnu");
				break;
			}
	}
	
	public void run() {
		frame = new JFrame("En Garde !");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
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
		// Haut
		JPanel panelTop = new JPanel(new BorderLayout());
		panelTop.setOpaque(false);
		// Haut -> Gauche
		panelTop.add(new VueInfoJeu(jeu.getEscrimeurGaucher().getNom(), jeu.getEscrimeurDroitier().getNom(), controle), BorderLayout.WEST);
		// Haut -> Droit
		mainDroitier = new VueEscrimeur(controle, jeu.getPlateau(), jeu.getEscrimeurDroitier(), !jeu.getIsTourGaucher(), jeu.getNbManchesPourVictoire());
		panelTop.add(mainDroitier,BorderLayout.EAST);
		
		// Bas
		JPanel panelBot = new JPanel(new BorderLayout());
		panelBot.setOpaque(false);
		
		// Bas -> Gauche
		mainGaucher = new VueEscrimeur(controle, jeu.getPlateau(), jeu.getEscrimeurGaucher(), jeu.getIsTourGaucher(), jeu.getNbManchesPourVictoire());
		panelBot.add(mainGaucher, BorderLayout.WEST);
		
		// Bas -> Droite
		vueDecks = new VueDeck(jeu.getDeckPioche(), jeu.getDeckDefausse());	
		panelBot.add(vueDecks, BorderLayout.EAST);
		
		// Centre
		vuePlateau = new VuePlateau(jeu.getPlateau(), controle, mainGaucher.getVueMain(), mainDroitier.getVueMain());
				
		background.add(panelTop);
		background.add(vuePlateau);
		background.add(panelBot);
		
		panelAnimation = new PanelAnimation(largeurFenetre, hauteurFenetre);
		//panelAnimation.setVisible(false);
		final JPanel glass = (JPanel) frame.getGlassPane();
		glass.setLayout(new GridBagLayout());
	    glass.setVisible(true);
	    glass.add(panelAnimation);
		miseAJour();
		frame.pack();
		frame.setVisible(true);
	}
	@SuppressWarnings("serial")
	public class PanelAnimation extends JComponent implements Animateur {
		private int animActif;
		private Point ptFinDeManche;
		private final Dimension sizeImgWinner = new Dimension(600, 400);
		private LinkedList<Integer> listeAnimation;
		private BufferedImage[] imgFinDeManche;
		private int winnerManche;
		
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ptFinDeManche = new Point();
			ptFinDeManche.y = 250;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			Graphics2D drawable = (Graphics2D)g;
			if (animActif == Animation.ANIM_FIN_MANCHE) {
				drawable.drawImage(imgFinDeManche[winnerManche], ptFinDeManche.x, ptFinDeManche.y, sizeImgWinner.width, sizeImgWinner.height, null);
				drawable.setFont(new Font("Century", Font.PLAIN, 50));
				if (winnerManche == Jeu.NONE) {
					drawable.drawString("Manche nulle", ptFinDeManche.x + 200, ptFinDeManche.y + 150);
				} else {
					drawable.drawString(jeu.getEscrimeurs()[winnerManche].getNom(), ptFinDeManche.x + 220, ptFinDeManche.y + 150);
				}
			}
		}
		
		public Animation generateAnimationDeplacerCartes() {
			return new Animation(controle, panelAnimation, Animation.ANIM_NONE) {
				
				@Override
				public void anim(double progres) {
					// TODO Auto-generated method stub
					
				}
			};
		}
		
		public Animation generateAnimationFinManche(int winnerManche) {
			this.winnerManche = winnerManche;
			ptFinDeManche.x = -sizeImgWinner.width;
			int distance = 1600 + sizeImgWinner.width;
			return new AnimationFinManche(controle, this, ptFinDeManche.x, distance);
		}

		@Override
		public void finAnimation(Animation animation) {
			animActif = animation.ANIM_NONE;
			repaint();
			controle.animation("Terminer", animation);
		}

		@Override
		public void debutAnimation(int typeAnimation) {
			System.out.println("Type animation : " + typeAnimation);
			animActif = typeAnimation;
		};
		
		public void deplaceFinDeManche(int newX) {
			ptFinDeManche.x = newX;
			repaint();
		}
		
		public void deplaceCartes(ArrayList<Point> newPoints) {
			
		}
	}
}
