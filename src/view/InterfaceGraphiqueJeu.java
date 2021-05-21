package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Patterns.Observateur;
import model.Jeu;
import model.Plateau;

public class InterfaceGraphiqueJeu implements Runnable, Observateur {
	
	private JFrame frame;
	CollecteurEvenements controle;
	
	private Jeu jeu;
	
	VueEscrimeur mainGaucher;
	VueEscrimeur mainDroitier;
	
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
		// TODO Auto-generated method stub
		mainGaucher.setShowFace(jeu.getIsTourGaucher());
		mainDroitier.setShowFace(!jeu.getIsTourGaucher());
		mainGaucher.miseAJour();
		mainDroitier.miseAJour();
	}
	
	public void run() {
		frame = new JFrame("En Garde !");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1600, 900);
		frame.setResizable(false);
		
		
		
		
		
		// Haut
//		JPanel panelHaut = new JPanel(new GridBagLayout());
		mainDroitier = new VueEscrimeur(jeu.getPlateau(), jeu.getEscrimeurDroitier(), !jeu.getIsTourGaucher());
//		panelHaut.add(mainDroitier);
		frame.add(mainDroitier,BorderLayout.NORTH);
		
			
		
//		JPanel panelBas = new JPanel(new BorderLayout());
		mainGaucher = new VueEscrimeur(jeu.getPlateau(), jeu.getEscrimeurGaucher(), jeu.getIsTourGaucher());
//		panelHaut.add(mainDroitier);
		frame.add(mainGaucher,BorderLayout.SOUTH);
		
		plateau = new VuePlateau(jeu.getPlateau());
		
		frame.add(plateau);
		miseAJour();

		frame.setVisible(true);
	}

	
}
