package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private static List<JButton> supprimer = new ArrayList<JButton>();
	ResultSet rs;
	SauvegarderPartie_DAO sp_dao;
	private InterfaceGraphiqueChargerPartie(CollecteurEvenements controle) {
		InterfaceGraphiqueChargerPartie.controle = controle;
		sp_dao = new SauvegarderPartie_DAO();
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
		if(fenetreChargerPartie != null) {
			fenetreChargerPartie.setVisible(false);
			fenetreChargerPartie.dispose();
		}
	}
	
	private static ButtonCustom createButtonPartie(String name, String choix, int x, int y) {
		ButtonCustom button = new ButtonCustom(name, "contour"+choix+"Partie", new Dimension(150, 150), new Font("Century", Font.PLAIN, 15));
		button.setBounds(x, y, 150, 150);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		return button;
	}

	@Override
	public void run() {
		fenetreChargerPartie = new JFrame("EN GARDE ! - PARTIES SAUVEGARDEES");
		fenetreChargerPartie.setIconImage(Configuration.imgIcone);
		JLabel contentPane = null;
		String name = "Vide";
		String supp = "Supprime";
		String charge = "Charge";
		try {
			contentPane = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge("PartiesSauv.png", Configuration.MENU))));
			fenetreChargerPartie.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageIO.read(Configuration.charge("curseur.png", Configuration.AUTRES)),new Point(0,0),"Mon curseur"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		fenetreChargerPartie.setContentPane(contentPane);
		int x1 = 35;
		int x2 = 217;
		int x3 = 395;
		int y1 = 161;
		int y2 = 322;
		
		parties.add(createButtonPartie(name, charge, x1, y1));
		supprimer.add(createButtonPartie(name, supp, x1, y1));
		
		parties.add(createButtonPartie(name, charge, x2, y1));
		supprimer.add(createButtonPartie(name, supp, x2, y1));
		
		parties.add(createButtonPartie(name, charge, x3, y1));
		supprimer.add(createButtonPartie(name, supp, x3, y1));
		
		parties.add(createButtonPartie(name, charge, x1, y2));
		supprimer.add(createButtonPartie(name, supp, x1, y2));
		
		parties.add(createButtonPartie(name, charge, x2, y2));
		supprimer.add(createButtonPartie(name, supp, x2, y2));
		
		parties.add(createButtonPartie(name, charge, x3, y2));
		supprimer.add(createButtonPartie(name, supp, x3, y2));

		JButton annuler = new ButtonCustom("Annuler", "cadre4", new Dimension(150, 40), new Font("Century", Font.PLAIN, 15));
		annuler.addActionListener(new AdaptateurCommande(controle, "annuler"));
		annuler.setBounds(35, 500, 150, 40);
		annuler.setForeground(Color.WHITE);
		
		JButton supprimerPartie = new ButtonCustom("Supprimer une partie", "cadre3", new Dimension(300, 40), new Font("Century", Font.PLAIN, 15));
		supprimerPartie.addActionListener(new ActionListener() {
			private Boolean clic = false;
			@Override
			public void actionPerformed(ActionEvent e) {
				for(JButton Partie : parties) { Partie.setVisible(clic); }
				for(JButton Supp : supprimer) { Supp.setVisible(!clic); }
				clic = !clic;
				JButton button = (JButton) e.getSource();
				button.setText((clic ? "Charger " : "Supprimer ") + "une partie");
			}
			
		});
		supprimerPartie.setBounds(217, 500, 332, 40);
		
		majParties();
		
		for(JButton Partie : parties) {
			contentPane.add(Partie);
		}
		for(JButton Supp : supprimer) {
			Supp.setVisible(false);
			Supp.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						majParties();
					}
					
				});
			contentPane.add(Supp);
		}
		contentPane.add(annuler);
		contentPane.add(supprimerPartie);

		fenetreChargerPartie.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fenetreChargerPartie.setBounds(100, 100, 600, 600);
		fenetreChargerPartie.setResizable(false);
		fenetreChargerPartie.setVisible(true);
		
	}
	
	private void majParties() {
		try {
			for(JButton Partie : parties) {Partie.setName("Vide");}
			for(JButton Supp : supprimer) {Supp.setName("Vide");}
			rs = sp_dao.getAll();
			for(int i=0; i<6; i++) {
				JButton buttonCharge = parties.get(i);
				JButton buttonSupp = supprimer.get(i);
				rs.next();
				String name = "<html><body>"
						+ "<section style=\"text-align: center;\">\r\n"
						+ "    <div style=\"margin: 3px auto; font-weight: 500; font-family: serif;\">\r\n"
						+ "    " + rs.getString("dateMatch") + "\r\n"
						+ "    </div>\r\n"
						+ "    <div style=\"margin: 3px auto; font-weight: 400; font-family: serif;\">\r\n"
						+ "    " + rs.getString("nomJoueurG") + "\r\n"
						+ "    </div>\r\n"
						+ "    <div style=\"margin: 3px auto; font-weight: 400; font-family: serif;\">\r\n"
						+ "    " + rs.getString("nomJoueurD") + "\r\n"
						+ "    </div>\r\n"
						+ "</section>"
						+ "</body></html>";
				buttonCharge.setText(name);
				buttonSupp.setText(name);
				buttonCharge.setHorizontalAlignment(SwingConstants.CENTER);
				buttonSupp.setHorizontalAlignment(SwingConstants.CENTER);
				buttonCharge.addActionListener(new AdaptateurChargerPartie(controle,rs.getInt("idPartie")));
				buttonSupp.addActionListener(new AdaptateurSupprimerPartie(controle,rs.getInt("idPartie")));
			}
			rs.close();
			SauvegarderPartie_DAO.stmt.close();
		} catch (SQLException e) {
		}
	}

}