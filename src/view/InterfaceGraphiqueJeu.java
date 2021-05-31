package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import Global.Configuration;
import Patterns.Observateur;
import controller.ControlerJeu;
import model.Carte;
import model.DeckDefausse;
import model.DeckPioche;
import model.Escrimeur;
import model.Historique;
import model.IncorrectCarteException;
import model.IncorrectPlateauException;
import model.Jeu;
import model.Plateau;
import model.TypeEscrimeur;
import model.Jeu.Action;

public class InterfaceGraphiqueJeu implements Runnable, Observateur {
	
	private JFrame frame;
	CollecteurEvenements controle;
	
	private Jeu jeu;
	VueEscrimeur mainGaucher;
	VueEscrimeur mainDroitier;
	
	VueDeck vueDecks;
	
	VuePlateau vuePlateau;
	
	private InterfaceGraphiqueJeu(CollecteurEvenements controle, Jeu jeu) {
		this.controle = controle;
		this.jeu = jeu;
	}
	
	public static void demarrer(CollecteurEvenements control, Jeu jeu) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueJeu(control, jeu));
	}
	
	@Override
	public void miseAJour() {
		Action action = jeu.action;
		System.out.println(action.toString());
		switch (action) {
			case CHANGER_TOUR:
			case ACTUALISE_PLATEAU: // Actualise les cases accessibles du plateau
				vuePlateau.setCaseClickable(jeu.casesJouables());
				vuePlateau.repaint();
				if (action != Action.CHANGER_TOUR) { // Stop si ce n'est pas  un changement de tour
					break;
				}
			case DEFAUSSER:
			case PIOCHER:
			case ACTUALISE_DECK: // Actualise les decks
				vueDecks.repaint();
				if (action != Action.CHANGER_TOUR) { // Stop si ce n'est pas  un changement de tour
					break;
				}
			case ACTUALISE_MAINS:
			case ACTUALISE_MAIN_DROITIER:
				mainDroitier.repaint();
				if (action != Action.CHANGER_TOUR || action != Action.ACTUALISE_MAINS) { // Stop si ce n'est pas  un changement de tour ou l'actualisation des 2 mains
					break;
				}
			case ACTUALISE_MAIN_GAUCHER:
				mainGaucher.repaint();
				if (action != Action.CHANGER_TOUR) { // Stop si ce n'est pas  un changement de tour
					break;
				}
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
		
		// Haut -> Droit
		mainDroitier = new VueEscrimeur(jeu.getPlateau(), jeu.getEscrimeurDroitier(), !jeu.getIsTourGaucher());
		panelTop.add(mainDroitier,BorderLayout.EAST);
		
		// Centre
		vuePlateau = new VuePlateau(jeu.getPlateau(), controle);
		
		// Bas
		JPanel panelBot = new JPanel(new BorderLayout());
		panelBot.setOpaque(false);
		
		// Bas -> Gauche
		mainGaucher = new VueEscrimeur(jeu.getPlateau(), jeu.getEscrimeurGaucher(), jeu.getIsTourGaucher());
		panelBot.add(mainGaucher, BorderLayout.WEST);
		
		// Bas -> Droite
		vueDecks = new VueDeck(jeu.getDeckPioche(), jeu.getDeckDefausse());	
		panelBot.add(vueDecks, BorderLayout.EAST);
		
		
		background.add(panelTop);
		background.add(vuePlateau);
		background.add(panelBot);
		
		miseAJour();
		frame.pack();
		frame.setVisible(true);
	}
}
