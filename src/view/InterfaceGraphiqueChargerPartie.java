package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import Global.Configuration;
import Patterns.Observateur;

public class InterfaceGraphiqueChargerPartie implements Runnable, Observateur {

	private static JFrame fenetreChargerPartie;
	private static CollecteurEvenements controle;
	
	private InterfaceGraphiqueChargerPartie(CollecteurEvenements controle) {
		InterfaceGraphiqueChargerPartie.controle = controle;
	}
	
	/**
	 * Ouvre la fenetre ChargerPartie
	 * @param control
	 */
	public static void demarrer(CollecteurEvenements control) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueChargerPartie(control));
	}
	
	/**
	 * Ferme la fenetre ChargerPartie
	 */
	public static void close() {
		if(fenetreChargerPartie!=null) {
			fenetreChargerPartie.setVisible(false);
			fenetreChargerPartie.dispose();
		}
	}
	
	/**
	 * Cree un bouton JButton qui permet d'acceder a une partie enregistree
	 * @param id : l'identifiant de la partie
	 * @return le bouton genere
	 */
	private static JButton Button (String id) {
		JButton button = null;
		ImageIcon banner;
		
		try {
			if(id == "Annuler") {
				banner = new ImageIcon(new ImageIcon(ImageIO.read(Configuration.charge("cadre4.png", Configuration.MENU))).getImage().getScaledInstance(150, 40, Image.SCALE_SMOOTH));
				button = new JButton(id, banner);
				button.setForeground(Color.WHITE);
				button.addMouseListener(new AdaptateurBouton(controle, "cadre4", button, 150));
				button.addActionListener(new AdaptateurCommande(controle, "annuler"));
			} else {
				banner = new ImageIcon(ImageIO.read(Configuration.charge("contourChargePartie.png", Configuration.MENU)));
				button = new JButton(id, banner);
				button.addMouseListener(new AdaptateurBouton(controle, "contourChargePartie", button, 0));
			}
			
			button.setFont(new Font(Configuration.Century.getFamily(), Font.PLAIN, 15));	
			button.setHorizontalTextPosition(SwingConstants.CENTER);
			button.setFocusPainted(false);
			button.setBorderPainted(false);
			button.setContentAreaFilled(false);
			button.setOpaque(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return button;
	}
	
	@Override
	public void miseAJour() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		fenetreChargerPartie = new JFrame("EN GARDE ! - CHARGER UNE PARTIE");
		JLabel contentPane = null;
		try {
			contentPane = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge("fondChargePartie.png", Configuration.MENU))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		fenetreChargerPartie.setContentPane(contentPane);
		contentPane.setLayout(null);
		// TODO récupérer les informations des parties sauvegardées (dates et joueurs) -> id
		// si un emplacement de partie est vide id = "Vide"
		JButton Partie1 = Button("id1");
		Partie1.setBounds(35, 161, 150, 150);
		contentPane.add(Partie1);
		
		JButton Partie2 = Button("id2");
		Partie2.setBounds(217, 161, 150, 150);
		contentPane.add(Partie2);
		
		JButton Partie3 = Button("id3");
		Partie3.setBounds(395, 161, 150, 150);
		contentPane.add(Partie3);
		
		JButton Partie4 = Button("id4");
		Partie4.setBounds(35, 322, 150, 150);
		contentPane.add(Partie4);
		
		JButton Partie5 = Button("id5");
		Partie5.setBounds(217, 322, 150, 150);
		contentPane.add(Partie5);
		
		JButton Partie6 = Button("id6");
		Partie6.setBounds(395, 322, 150, 150);
		contentPane.add(Partie6);
		
		JButton annuler = Button("Annuler");
		annuler.setBounds(35, 500, 150, 40);
		contentPane.add(annuler);

		fenetreChargerPartie.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fenetreChargerPartie.setBounds(100, 100, 600, 600);
		fenetreChargerPartie.setResizable(false);
		fenetreChargerPartie.setVisible(true);
		
	}

}
