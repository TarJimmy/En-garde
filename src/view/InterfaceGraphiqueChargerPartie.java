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
		if(fenetreChargerPartie!=null) {
			fenetreChargerPartie.setVisible(false);
			fenetreChargerPartie.dispose();
		}
	}
	
	private static ButtonCustom createButtonChargerPartie(String name, int x, int y) {
		ButtonCustom button = new ButtonCustom(name, "contourChargePartie", new Dimension(150, 150), new Font("Century", Font.PLAIN, 15));
		button.setBounds(x, y, 150, 150);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		return button;
	}

	@Override
	public void run() {
		fenetreChargerPartie = new JFrame("EN GARDE ! - CHARGER UNE PARTIE");
		fenetreChargerPartie.setIconImage(Configuration.imgIcone);
		JLabel contentPane = null;
		String name = "Vide";
		try {
			contentPane = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge("fondChargePartie.png", Configuration.MENU))));
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
		parties.add(createButtonChargerPartie(name, x1, y1));
		parties.add(createButtonChargerPartie(name, x2, y1));
		parties.add(createButtonChargerPartie(name, x3, y1));
		parties.add(createButtonChargerPartie(name, x1, y2));
		parties.add(createButtonChargerPartie(name, x2, y2));
		parties.add(createButtonChargerPartie(name, x3, y2));

		JButton annuler = new ButtonCustom("Annuler", "cadre4", new Dimension(150, 40), new Font("Century", Font.PLAIN, 15));
		annuler.addActionListener(new AdaptateurCommande(controle, "annuler"));
		annuler.setBounds(35, 500, 150, 40);
		annuler.setForeground(Color.WHITE);
		try {
			rs = sp_dao.getAll();
			for(int i=0; i<6; i++) {
				JButton button = parties.get(i);
				rs.next();
				name = "<HTML><BODY><center>"+rs.getString("idPartie")+"<BR>"+rs.getString("nomJoueurG")+"<BR>"+rs.getString("nomJoueurD")+"</center></BODY></HTML>";
				button.setText(name);
				button.setHorizontalAlignment(SwingConstants.CENTER);
				button.addActionListener(new AdaptateurChargerPartie(controle,rs.getInt("idPartie")));
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
