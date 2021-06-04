package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import Database.SauvegarderPartie_DAO;

public class InterfaceGraphiqueChargerPartie implements Runnable, Observateur {

	private static JFrame fenetreChargerPartie;
	private static CollecteurEvenements controle;
	private static List<JButton> parties = new ArrayList<JButton>();
	ResultSet rs;
	
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
	private static JButton Button (String id, int x, int y) {
		JButton button = null;
		ImageIcon banner;
		
		try {
			if(id == "Annuler") {
				banner = new ImageIcon(new ImageIcon(ImageIO.read(Configuration.charge("cadre4.png", Configuration.MENU))).getImage().getScaledInstance(150, 40, Image.SCALE_SMOOTH));
				button = new JButton(id, banner);
				button.setForeground(Color.WHITE);
				button.setBounds(35, 500, 150, 40);
				button.addMouseListener(new AdaptateurBouton(controle, "cadre4", button, 150));
				button.addActionListener(new AdaptateurCommande(controle, "annuler"));
			} else {
				banner = new ImageIcon(ImageIO.read(Configuration.charge("contourChargePartie.png", Configuration.MENU)));
				button = new JButton(id, banner);
				button.setBounds(x, y, 150, 150);
				button.addMouseListener(new AdaptateurBouton(controle, "contourChargePartie", button, 0));
			}
			
			parties.add(button);
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
		String id = "Vide";
		try {
			contentPane = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge("fondChargePartie.png", Configuration.MENU))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		fenetreChargerPartie.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton Partie1 = Button(id, 395, 322);
		JButton Partie2 = Button(id, 217, 322);
		JButton Partie3 = Button(id, 35, 322);
		JButton Partie4 = Button(id, 395, 161);
		JButton Partie5 = Button(id, 217, 161);
		JButton Partie6 = Button(id, 35, 161);
		JButton annuler = Button("Annuler", 0, 0);
		try {
			rs = SauvegarderPartie_DAO.getAll();
			for(int i=0; i<6; i++) {
				rs.next();
				id = rs.getString("idPartie")+"\n"+rs.getString("nomJoueurG")+"\n"+rs.getString("nomJoueurD");
				parties.get(i).setText(id);
				// TODO charger la partie voulue
				// parties.get(i).addActionListener();
			}
		} catch (SQLException e) {
			System.out.println("Pas plus de parties enregistrees");
		}
		
		for(JButton Partie : parties) {
			contentPane.add(Partie);
		}
		contentPane.add(annuler);

		fenetreChargerPartie.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fenetreChargerPartie.setBounds(100, 100, 600, 600);
		fenetreChargerPartie.setResizable(false);
		fenetreChargerPartie.setVisible(true);
		
	}

}
