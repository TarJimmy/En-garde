package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Global.Configuration;
import Patterns.Observateur;
import controller.ControlerAutre;
import controller.ControlerJeu;
import model.Escrimeur;
import model.Jeu;

/**
 * 
 * @author Laetitia & Delphine
 *
 */
public class InterfaceGraphiqueFin implements Runnable, Observateur {
	
	private static JFrame fenetreFin;
	private static CollecteurEvenements controle;
	private Escrimeur escrimeur;
	private String imageFin;
	
	private InterfaceGraphiqueFin(CollecteurEvenements controle, Escrimeur escrimeur) {
		InterfaceGraphiqueFin.controle = controle;
		this.escrimeur = escrimeur;
	}
	
	/**
	 * Ouvre la fenetre Fin
	 * @param control
	 */
	public static void demarrer(CollecteurEvenements control, Escrimeur escrimeur) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueFin(control,escrimeur));
	}
	
	/**
	 * Ferme la fenetre Fin
	 */
	public static void close() {
		if(fenetreFin!=null) {
			fenetreFin.setVisible(false);
			fenetreFin.dispose();
		}
	}

	@Override
	public void miseAJour() {
		// TODO Auto-generated method stub
	}

	/**
	 * Ecriture du contenu de la fenetre Fin
	 */
	@Override
	public void run() {
		fenetreFin = new JFrame("EN GARDE ! - Fin de la partie");
		JLabel contentPane;
		try {
			if(escrimeur.getIsGaucher()){imageFin = "FinJoueurGauche.png";} else {imageFin = "FinJoueurDroit.png";}
			contentPane = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge(imageFin, Configuration.MENU))));
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			fenetreFin.setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JLabel nomGagnant = new JLabel(escrimeur.getNom(), SwingConstants.CENTER);
			nomGagnant.setFont(new Font("Century", Font.PLAIN, 35));
			nomGagnant.setBounds(225, 180, 450, 40);
			contentPane.add(nomGagnant);
			
			JLabel fin = new JLabel("remporte la partie !", SwingConstants.CENTER);
			fin.setFont(new Font("Century", Font.PLAIN, 35));
			fin.setBounds(225, 240, 450, 40);
			contentPane.add(fin);
			
			Font font = new Font("Century", Font.PLAIN, 15);
			Dimension dim = new Dimension(200, 40);
			
			///Boutons
			JButton btnMenu = new ButtonCustom("MENU", "cadre2", dim, font);
			btnMenu.addActionListener(new AdaptateurCommande(controle, "Menu"));
			btnMenu.setBounds(50, 350, 200, 40);
			contentPane.add(btnMenu);
			
			JButton btnRecommencer = new ButtonCustom("Jouer match revanche", "cadre2", dim, font);
			btnRecommencer.addActionListener(new AdaptateurCommande(controle, "nouveauMatch"));
			btnRecommencer.setBounds(342, 350, 200, 40);
			contentPane.add(btnRecommencer);
			
			JButton btnQuit = new ButtonCustom("Quitter", "cadre2", dim, font);
			btnQuit.addActionListener(new AdaptateurCommande(controle, "QuitterJeu"));
			btnQuit.setBounds(634, 350, 200, 40);
			contentPane.add(btnQuit);
			fenetreFin.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageIO.read(Configuration.charge("curseur.png", Configuration.AUTRES)),new Point(0,0),"Mon curseur"));
		} catch (HeadlessException | IndexOutOfBoundsException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		fenetreFin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetreFin.setBounds(100, 100, 900, 450);
		fenetreFin.setResizable(false);
		fenetreFin.setVisible(true);
	}
}

