package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

public class InterfaceGraphiqueActionAnnexe extends WindowAdapter implements Runnable, Observateur {
	
	private static JFrame fenetreActionAnnexe;
	private static CollecteurEvenements controle;
	private static Boolean fenetreActive = false;
	private InterfaceGraphiqueActionAnnexe(CollecteurEvenements controle) {
		InterfaceGraphiqueActionAnnexe.controle = controle;
	}
	
	/**
	 * Ouvre la fenetre de triche
	 * @param control
	 */
	public static void demarrer(CollecteurEvenements control) {
		if (!fenetreActive) {
			fenetreActive = true;
			SwingUtilities.invokeLater(new InterfaceGraphiqueActionAnnexe(control));
		} 
	}
	
	/**
	 * Ferme la fenetre de triche
	 */
	public static void close() {
		if (fenetreActionAnnexe!=null) {
			fenetreActionAnnexe.setVisible(false);
			fenetreActionAnnexe.dispose();
		}
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
					label.setText("Actions annexes");
					label.setFont(new Font(Configuration.Century.getFamily(), Font.PLAIN, 25));
					label.setBounds(67, 15, 250, 75);
					break;
				default:
					banner = new ImageIcon(new ImageIcon(ImageIO.read(Configuration.charge("cadre4.png", Configuration.MENU))).getImage().getScaledInstance(200, 50, Image.SCALE_SMOOTH));
					label = new JLabel(banner);
					label.setText(name);
					label.setForeground(Color.WHITE);
					label.setFont(new Font(Configuration.Century.getFamily(), Font.PLAIN, 20));
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
	
	@Override
	public void miseAJour() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Ecriture du contenu de la fenetre Menu
	 */
	@Override
	public void run() {
		try {
		fenetreActionAnnexe = new JFrame("EN GARDE ! - Panel de triche");
		JLabel contentPane = null;
		contentPane = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge("ActionAnnexe.png", Configuration.MENU))));
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		fenetreActionAnnexe.setContentPane(contentPane);
		contentPane.setLayout(null);
		Font font = new Font("Century", Font.PLAIN, 15);
		JLabel titre = Label("Titre");
		contentPane.add(titre);
		
		JLabel partieEnCours = Label("Partie en cours :");
		partieEnCours.setBounds(92, 180, 200, 50);
		contentPane.add(partieEnCours);
		
		JButton nouvellePartie = new ButtonCustom("Nouvelle la partie", "cadre2", new Dimension(200, 50), font);
		nouvellePartie.addActionListener(new AdaptateurCommande(controle, "nouvellePartie"));
		nouvellePartie.setBounds(92, 240, 200, 50);
		contentPane.add(nouvellePartie);
		
		JButton sauvPartie = new ButtonCustom("Sauvegarder la partie", "cadre2", new Dimension(200, 50), font);
		sauvPartie.addActionListener(new AdaptateurCommande(controle, "sauvPartie"));
		sauvPartie.setBounds(92, 300, 200, 50);
		contentPane.add(sauvPartie);
		
		JButton chargePartie = new ButtonCustom("Charger une partie", "cadre2", new Dimension(200, 50), font);
		chargePartie.addActionListener(new AdaptateurCommande(controle, "chargePartie"));
		chargePartie.setBounds(92, 360, 200, 50);
		contentPane.add(chargePartie);
		
		JLabel dernierCoup = Label("Dernier coup :");
		dernierCoup.setBounds(92, 430, 200, 50);
		contentPane.add(dernierCoup);
		
		JButton annuleCoup = new ButtonCustom("Annuler le coup", "cadre2", new Dimension(200, 50), font);
		annuleCoup.addActionListener(new AdaptateurCommande(controle, "annuleCoup"));
		annuleCoup.setBounds(92, 490, 200, 50);
		contentPane.add(annuleCoup);
		
		JButton refaireCoup = new ButtonCustom("Refaire le dernier coup", "cadre2", new Dimension(200, 50), font);
		refaireCoup.addActionListener(new AdaptateurCommande(controle, "refaireCoup"));
		refaireCoup.setBounds(92, 550, 200, 50);
		contentPane.add(refaireCoup);
		
		JButton fermer = new ButtonCustom("FERMER", "cadre3", new Dimension(150, 50), font);
		fermer.setBounds(10, 650, 150, 50);
		fermer.addActionListener(new AdaptateurCommande(controle, "close"));
		contentPane.add(fermer);
		
		fenetreActionAnnexe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		fenetreActionAnnexe.setBounds(100, 100, 400, 750);
		fenetreActionAnnexe.setResizable(false);
		fenetreActionAnnexe.setVisible(true);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		fenetreActive = false;
		close();
	}
	
	
}
