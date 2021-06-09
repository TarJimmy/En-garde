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
import controller.ControlerAutre;
import Database.Classement_DAO;
import Database.DB_DAO;
import Database.SauvegarderPartie_DAO;

public class InterfaceGraphiqueClassement implements Runnable {

	private static JFrame fenetreClassement;
	private static CollecteurEvenements controle;
	private static List<JLabel> labels = new ArrayList<>();
	protected final Classement_DAO classDAO = new Classement_DAO();
	ResultSet rs;
	private InterfaceGraphiqueClassement(CollecteurEvenements controle) {
		InterfaceGraphiqueClassement.controle = controle;
	}
	
	/**
	 * Ouvre la fenetre ChargerPartie
	 * @param control
	 */
	public static void demarrer(CollecteurEvenements control) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueClassement(control));
	}
	
	/**
	 * Ferme la fenetre ChargerPartie
	 */
	public static void close() {
		if(fenetreClassement!=null) {
			fenetreClassement.setVisible(false);
			fenetreClassement.dispose();
		}
	}
	
	private static JLabel createLabelChargerPartie(String name, String place, int y) throws IOException {
		ImageIcon banner = new ImageIcon(new ImageIcon(ImageIO.read(Configuration.charge("podium_"+place+".png", Configuration.CLASSEMENT))).getImage().getScaledInstance(350, 50, Image.SCALE_SMOOTH));
		JLabel label = new JLabel(banner);
		label.setText(name);
		label.setFont(new Font("Century", Font.PLAIN, 18));
		label.setBounds(50, y, 350, 50);
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		return label;
	}

	@Override
	public void run() {
		fenetreClassement = new JFrame("EN GARDE ! - Classement");
		JLabel contentPane = null;
		String name = "Vide";
		try {
			contentPane = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge("classement.png", Configuration.CLASSEMENT))));
			fenetreClassement.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageIO.read(Configuration.charge("curseur.png", Configuration.AUTRES)),new Point(0,0),"Mon curseur"));
		
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			fenetreClassement.setContentPane(contentPane);
			labels.add(createLabelChargerPartie("1. "+name, "OR", 170));
			labels.add(createLabelChargerPartie("2. "+name, "ARGENT", 230));
			labels.add(createLabelChargerPartie("3. "+name, "BRONZE", 290));
			labels.add(createLabelChargerPartie("4. "+name, "AUTRES", 350));
			labels.add(createLabelChargerPartie("5. "+name, "AUTRES", 410));
			labels.add(createLabelChargerPartie("6. "+name, "AUTRES", 470));
			labels.add(createLabelChargerPartie("7. "+name, "AUTRES", 530));
			labels.add(createLabelChargerPartie("8. "+name, "AUTRES", 590));
			labels.add(createLabelChargerPartie("9. "+name, "AUTRES", 650));
			labels.add(createLabelChargerPartie("10. "+name, "AUTRES", 710));
	
			JButton annuler = new ButtonCustom("Fermer", "cadre4", new Dimension(150, 40), new Font("Century", Font.PLAIN, 15));
			annuler.addActionListener(new AdaptateurCommande(controle, "annuler"));
			annuler.setBounds(35, 800, 150, 40);
			annuler.setForeground(Color.WHITE);
			contentPane.add(annuler);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			rs = classDAO.getPodium();
			for(int i=0; i<10; i++) {
				JLabel label = labels.get(i);
				rs.next();
				name = "<html>" + capitalize(rs.getString("nom"))+" : "+ rs.getString("win")+"<strong> V</strong> "+ rs.getString("defaite") + " <strong>D</strong></html>";

				label.setText(name);
			}
			rs.close();
			Classement_DAO.stmt.close();
		} catch (SQLException e) {
		}
		
		for(JLabel label : labels) {
			contentPane.add(label);
		}

		fenetreClassement.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fenetreClassement.setBounds(100, 100, 450, 900);
		fenetreClassement.setResizable(false);
		fenetreClassement.setVisible(true);
		
	}
	
	private String capitalize(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	public static void main(String[] args) {
		Configuration.instance();
		DB_DAO db = new DB_DAO();
    	db.createNewDatabase();
    	db.createTables();
		InterfaceGraphiqueClassement.demarrer(new ControlerAutre());
	}

}
