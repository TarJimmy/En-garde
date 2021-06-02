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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import Global.Configuration;
import Patterns.Observateur;
import controller.ControlerAutre;

public class InterfaceGraphiqueActionAnnexe implements Runnable, Observateur {
	
	private static JFrame fenetreActionAnnexe;
	CollecteurEvenements controle;
	
	private InterfaceGraphiqueActionAnnexe(CollecteurEvenements controle) {
		this.controle = controle;
	}
	
	/**
	 * Ouvre la fenetre Menu
	 * @param control
	 */
	public static void demarrer(CollecteurEvenements control) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueActionAnnexe(control));
	}
	
	/**
	 * Ferme la fenetre Menu
	 */
	public static void close() {
		fenetreActionAnnexe.setVisible(false);
		fenetreActionAnnexe.dispose();
	}
	
	/**
	 * Cree un label JLabel
	 * @param name : le nom du label
	 * @return le label name genere
	 */
	private static JLabel Label (String name) {
		JLabel label;
		ImageIcon banner;
		
		try {
			switch (name){
				case "Titre":
					banner = new ImageIcon(new ImageIcon(ImageIO.read(Configuration.charge("cadre.png", Configuration.MENU))).getImage().getScaledInstance(250, 75, Image.SCALE_SMOOTH));
					label = new JLabel(banner);
					label.setText("Panel de triche");
					label.setFont(new Font("Century", Font.PLAIN, 25));
					label.setBounds(67, 15, 250, 75);
					break;
				default:
					banner = new ImageIcon(new ImageIcon(ImageIO.read(Configuration.charge("cadre4.png", Configuration.MENU))).getImage().getScaledInstance(200, 50, Image.SCALE_SMOOTH));
					label = new JLabel(banner);
					label.setText(name);
					label.setForeground(Color.WHITE);
					label.setFont(new Font("Century", Font.PLAIN, 20));
					break;
			}
			
			label.setHorizontalTextPosition(SwingConstants.CENTER);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			
			return label;
		} catch (IOException e) {
			System.err.println("An error as occured");
			return null;
		}
	}
	
	/**
	 * Cree un bouton JButton
	 * @param name : le nom du bouton
	 * @return le bouton name genere
	 */
	private static JButton Button (String name) {
		JButton button;
		ImageIcon banner;
		
		try {
			switch (name){
				case "FERMER":
					banner = new ImageIcon(new ImageIcon(ImageIO.read(Configuration.charge("cadre3.png", Configuration.MENU))).getImage().getScaledInstance(150, 40, Image.SCALE_SMOOTH));
					button = new JButton(name, banner);
					button.setFont(new Font("Century", Font.PLAIN, 15));
					button.setBounds(10, 650, 150, 50);
					break;
				default:
					banner = new ImageIcon(new ImageIcon(ImageIO.read(Configuration.charge("cadre2.png", Configuration.MENU))).getImage().getScaledInstance(200, 40, Image.SCALE_SMOOTH));
					button = new JButton(name, banner);
					button.setFont(new Font("Century", Font.PLAIN, 12));
					break;
			}
			
			button.setHorizontalTextPosition(SwingConstants.CENTER);
			button.setFocusPainted(false);
			button.setBorderPainted(false);
			button.setContentAreaFilled(false);
			button.setOpaque(false);
			
			return button;
		} catch (IOException e) {
			System.err.println("An error as occured");
			return null;
		}
	}
	
	@Override
	public void miseAJour() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		try {
		fenetreActionAnnexe = new JFrame("EN GARDE ! - Panel de triche");
		JLabel contentPane = null;
		contentPane = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge("ActionAnnexe.png", Configuration.MENU))));
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		fenetreActionAnnexe.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = Label("Titre");
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = Label("Partie en cours :");
		lblNewLabel_1.setBounds(92, 180, 200, 50);
		contentPane.add(lblNewLabel_1);
		
		JButton nouvellePartie = Button("Nouvelle la partie");
		nouvellePartie.addActionListener(new AdaptateurCommande(this.controle, "nouvellePartie"));
		nouvellePartie.setBounds(92, 240, 200, 50);
		nouvellePartie.addMouseListener(new AdaptateurBouton(this.controle, "cadre2", nouvellePartie, 200));
		contentPane.add(nouvellePartie);
		
		JButton sauvPartie = Button("Sauvegarder la partie");
		sauvPartie.addActionListener(new AdaptateurCommande(this.controle, "sauvPartie"));
		sauvPartie.setBounds(92, 300, 200, 50);
		sauvPartie.addMouseListener(new AdaptateurBouton(this.controle, "cadre2", sauvPartie, 200));
		contentPane.add(sauvPartie);
		
		JButton chargePartie = Button("Charger une partie");
		chargePartie.addActionListener(new AdaptateurCommande(this.controle, "chargePartie"));
		chargePartie.setBounds(92, 360, 200, 50);
		chargePartie.addMouseListener(new AdaptateurBouton(this.controle, "cadre2", chargePartie, 200));
		contentPane.add(chargePartie);
		
		JLabel lblNewLabel_2 = Label("Dernier coup :");
		lblNewLabel_2.setBounds(92, 430, 200, 50);
		contentPane.add(lblNewLabel_2);
		
		JButton annuleCoup = Button("Annuler le coup");
		annuleCoup.addActionListener(new AdaptateurCommande(this.controle, "annuleCoup"));
		annuleCoup.setBounds(92, 490, 200, 50);
		annuleCoup.addMouseListener(new AdaptateurBouton(this.controle, "cadre2", annuleCoup, 200));
		contentPane.add(annuleCoup);
		
		JButton refaireCoup = Button("Refaire le dernier coup");
		refaireCoup.addActionListener(new AdaptateurCommande(this.controle, "refaireCoup"));
		refaireCoup.setBounds(92, 550, 200, 50);
		refaireCoup.addMouseListener(new AdaptateurBouton(this.controle, "cadre2", refaireCoup, 200));
		contentPane.add(refaireCoup);
		
		JButton fermer = Button("FERMER");
		fermer.addActionListener(new AdaptateurCommande(this.controle, "close"));
		fermer.addMouseListener(new AdaptateurBouton(this.controle,"cadre3", fermer, 150));
		contentPane.add(fermer);
		
		fenetreActionAnnexe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetreActionAnnexe.setBounds(100, 100, 400, 750);
		fenetreActionAnnexe.setResizable(false);
		fenetreActionAnnexe.setVisible(true);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		InterfaceGraphiqueActionAnnexe.demarrer(new ControlerAutre());
	}

}
