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
import model.Jeu;

public class InterfaceGraphiqueJeu implements Runnable, Observateur {
	
	private JFrame frame;
	CollecteurEvenements controle;
	
	private Jeu jeu;
	VueEscrimeur mainGaucher;
	VueEscrimeur mainDroitier;
	
	VueDeck vueDeck;
	
	VuePlateau plateau;
	
	private InterfaceGraphiqueJeu(CollecteurEvenements controle, Jeu jeu) {
		this.controle = controle;
		this.jeu = jeu;
	}
	
	public static void demarrer(CollecteurEvenements control, Jeu jeu) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueJeu(control, jeu));
	}
	
	@Override
	public void miseAJour() {
		mainGaucher.setShowFace(jeu.getIsTourGaucher());
		mainDroitier.setShowFace(!jeu.getIsTourGaucher());
		mainGaucher.miseAJour();
		mainDroitier.miseAJour();
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
		plateau = new VuePlateau(jeu.getPlateau());
		
		// Bas
		JPanel panelBot = new JPanel(new BorderLayout());
		panelBot.setOpaque(false);
		
		// Bas -> Gauche
		mainGaucher = new VueEscrimeur(jeu.getPlateau(), jeu.getEscrimeurGaucher(), jeu.getIsTourGaucher());
		panelBot.add(mainGaucher, BorderLayout.WEST);
		
		// Bas -> Droite
		VueDeck vueDecks = new VueDeck(jeu.getDeckPioche(), jeu.getDeckDefausse());	
		panelBot.add(vueDecks, BorderLayout.EAST);
		
		
		background.add(panelTop);
		background.add(plateau);
		background.add(panelBot);
		
		miseAJour();
		frame.pack();
		frame.setVisible(true);
	}

	
}
