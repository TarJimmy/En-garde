package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
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

public class InterfaceGraphiqueChargerPartie implements Runnable {

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
	
	private static ButtonCustom createButtonChargerPartie(String name, int x, int y) {
		ButtonCustom button = new ButtonCustom(name, "contourChargePartie", new Dimension(150, 150), new Font("Century", Font.PLAIN, 15));
		button.setBounds(x, y, 150, 150);
		parties.add(button);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		return button;
	}

	@Override
	public void run() {
		fenetreChargerPartie = new JFrame("EN GARDE ! - CHARGER UNE PARTIE");
		JLabel contentPane = null;
		String id = "Vide";
		try {
			contentPane = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge("fondChargePartie.png", Configuration.MENU))));
			fenetreChargerPartie.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageIO.read(Configuration.charge("curseur.png", Configuration.AUTRES)),new Point(0,0),"Mon curseur"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		fenetreChargerPartie.setContentPane(contentPane);
		contentPane.setLayout(null);
		JButton Partie1 = createButtonChargerPartie(id, 395, 322);
		JButton Partie2 = createButtonChargerPartie(id, 217, 322);
		JButton Partie3 = createButtonChargerPartie(id, 35, 322);
		JButton Partie4 = createButtonChargerPartie(id, 395, 161);
		JButton Partie5 = createButtonChargerPartie(id, 217, 161);
		JButton Partie6 = createButtonChargerPartie(id, 35, 161);
		JButton annuler = new ButtonCustom("Annuler", "cadre4", new Dimension(150, 40), new Font("Century", Font.PLAIN, 15));
		annuler.addActionListener(new AdaptateurCommande(controle, "annuler"));
		annuler.setBounds(35, 500, 150, 40);
		annuler.setForeground(Color.WHITE);
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
