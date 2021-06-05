package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Global.Configuration;
import Patterns.Observateur;
import controller.ControlerAutre;

/**
 * 
 * @author Laetitia & Delphine
 *
 */
public class InterfaceGraphiqueFin implements Runnable, Observateur {
	
	private static JFrame fenetreFin;
	private static CollecteurEvenements controle;
	
	private InterfaceGraphiqueFin(CollecteurEvenements controle) {
		InterfaceGraphiqueFin.controle = controle;
	}
	
	/**
	 * Ouvre la fenetre Fin
	 * @param control
	 */
	public static void demarrer(CollecteurEvenements control) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueFin(control));
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
		JLabel contentPane = new JLabel(new ImageIcon("res/Images/Menu/Fin.png"));
		//JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		fenetreFin.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		///Boutons
		JButton btnMenu = new ButtonCustom("MENU", "cadre2", new Dimension(200, 40), new Font(Configuration.Century.getFamily(), Font.PLAIN, 15));
		btnMenu.addActionListener(new AdaptateurCommande(controle, "menu"));
		btnMenu.setBounds(50, 350, 200, 40);
		contentPane.add(btnMenu);
		
		JButton btnRecommencer = new ButtonCustom("Recommencer", "cadre2", new Dimension(200, 40), new Font(Configuration.Century.getFamily(), Font.PLAIN, 15));
		btnRecommencer.addActionListener(new AdaptateurCommande(controle, "rejouer"));
		btnRecommencer.setBounds(342, 350, 200, 40);
		contentPane.add(btnRecommencer);
		
		JButton btnQuit = new ButtonCustom("Quitter", "cadre2", new Dimension(200, 40), new Font(Configuration.Century.getFamily(), Font.PLAIN, 15));
		btnQuit.addActionListener(new AdaptateurCommande(controle, "exit"));
		btnQuit.setBounds(634, 350, 200, 40);
		contentPane.add(btnQuit);
		
		fenetreFin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetreFin.setBounds(100, 100, 900, 450);
		fenetreFin.setResizable(false);
		fenetreFin.setVisible(true);
	}

	public static void main(String[] args) {
		InterfaceGraphiqueFin.demarrer(new ControlerAutre());
	}
}

