package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Global.Configuration;
import Patterns.Observateur;
import model.Jeu;
import model.Jeu.Action;

public class InterfaceGraphiqueJeu implements Runnable, Observateur {
	
	private JFrame frame;
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
	
	@Override
	public void miseAJour() {
		Action action = jeu.getActionCourante();
		System.out.println("Lance " + action);
		switch (action) {
			case CHANGER_TOUR:
			case ACTUALISER_PLATEAU: // Actualise les cases accessibles du plateau
				vuePlateau.actualise(jeu.casesJouables(), jeu.getIndiceCurrentEscrimeur());
				if (action != Action.CHANGER_TOUR) { // Stop si ce n'est pas  un changement de tour
					controle.commande("ActionTerminer");
					break;
				}
			case DEFAUSSER:
			case PIOCHER:
			case ACTUALISER_DECK: // Actualise les decks
				vueDecks.repaint();
				if (action != Action.CHANGER_TOUR) { // Stop si ce n'est pas  un changement de tour
					controle.commande("ActionTerminer");
					break;
				}
			case ACTUALISER_MAINS:
			case ACTUALISER_MAIN_DROITIER:
				mainDroitier.actualise(jeu.getIsTourGaucher() == false);
				if (action != Action.CHANGER_TOUR && action != Action.ACTUALISER_MAINS) { // Stop si ce n'est pas  un changement de tour ou l'actualisation des 2 mains
					controle.commande("ActionTerminer");
					break;
				}
			case ACTUALISER_MAIN_GAUCHER:
				mainGaucher.actualise(jeu.getIsTourGaucher());
				controle.commande("ActionTerminer");
				break;
			case ANIMATION_DEPLACER_ESCRIMEUR:
				controle.animation("Ajouter", vuePlateau.generateAnimationDeplaceJoueur(jeu.getIndiceCurrentEscrimeur()));
				break;
			case ANIMATION_PIOCHER:
				controle.animation("Ajouter", panelAnimation.generateAnimationDeplacerCartes());
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
			background.setPreferredSize(new Dimension(1600, 900));
			frame.add(background);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Haut
		JPanel panelTop = new JPanel(new BorderLayout());
		panelTop.setOpaque(false);
		// Haut -> Gauche
		panelTop.add(new VueInfoJeu(jeu.getEscrimeurGaucher().getNom(), jeu.getEscrimeurDroitier().getNom()), BorderLayout.WEST);
		// Haut -> Droit
		mainDroitier = new VueEscrimeur(controle, jeu.getPlateau(), jeu.getEscrimeurDroitier(), !jeu.getIsTourGaucher());
		panelTop.add(mainDroitier,BorderLayout.EAST);
		
		// Centre
		vuePlateau = new VuePlateau(jeu.getPlateau(), controle);
		
		// Bas
		JPanel panelBot = new JPanel(new BorderLayout());
		panelBot.setOpaque(false);
		
		// Bas -> Gauche
		mainGaucher = new VueEscrimeur(controle, jeu.getPlateau(), jeu.getEscrimeurGaucher(), jeu.getIsTourGaucher());
		panelBot.add(mainGaucher, BorderLayout.WEST);
		
		// Bas -> Droite
		vueDecks = new VueDeck(jeu.getDeckPioche(), jeu.getDeckDefausse());	
		panelBot.add(vueDecks, BorderLayout.EAST);
		
		
		background.add(panelTop);
		background.add(vuePlateau);
		background.add(panelBot);
		
		panelAnimation = new PanelAnimation();
		panelAnimation.setPreferredSize(new Dimension(1600, 900));
		frame.setGlassPane(panelAnimation);
		
		miseAJour();
		frame.pack();
		frame.setVisible(true);
	}
	
	@SuppressWarnings("serial")
	public class PanelAnimation extends JComponent implements Animateur{
		
		public PanelAnimation() {
			super();
			setVisible(true);
			setOpaque(false);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			Graphics2D drawable = (Graphics2D)g;
			drawable.fillRect(getWidth() / 2 - 100, getHeight() / 2, 500, 300);
		}
		
		public Animation generateAnimationDeplacerCartes() {
			return new Animation(controle, panelAnimation) {
				
				@Override
				public void anim(double progres) {
					// TODO Auto-generated method stub
					
				}
			};
		}
		
		public void deplaceCartes(ArrayList<Point> newPoints) {
			
		}

		@Override
		public void finAnimation(Animation animation) {
			frame.repaint();
			controle.animation("Terminer", animation);
		}
		
	}
}
