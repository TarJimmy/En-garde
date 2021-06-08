package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
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
import controller.ControlerAutre;

/**
 * 
 * @author Laetitia & Delphine
 *
 */
public class InterfaceGraphiqueMenu implements Runnable {
	
	private static JFrame fenetreMenu;
	private static CollecteurEvenements collecteurMenu;
	private GridBagConstraints c;
	
	private InterfaceGraphiqueMenu(CollecteurEvenements controle) {
		InterfaceGraphiqueMenu.collecteurMenu = controle;
	}
	
	/**
	 * Ouvre la fenetre Menu
	 * @param control
	 */
	public static void demarrer(CollecteurEvenements control) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueMenu(control));
	}
	
	/**
	 * Ferme la fenetre Menu
	 */
	public static void close() {
		fenetreMenu.setVisible(false);
		fenetreMenu.dispose();
	}
	
	/**
	 * Permet de definir l'emplacement et l'alignement (centre) d'un objet dans la fenetre Menu
	 * @param x : abscisse
	 * @param y : ordonnee
	 * @return la contrainte d'emplacement et d'alignement de l'objet
	 */
	private static GridBagConstraints GridConstraints (int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = x;
		c.gridy = y;
		
		return c;
	}
	
	/**
	 * Ecriture du contenu de la fenetre Menu
	 */
	@Override
	public void run() {
		fenetreMenu = new JFrame("EN GARDE ! - MENU PRINCIPAL");
		fenetreMenu.setIconImage(Configuration.imgIcone);
		JLabel menu_fond = null;
		try {
			fenetreMenu.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageIO.read(Configuration.charge("curseur.png", Configuration.AUTRES)),new Point(0,0),"Mon curseur"));
			menu_fond = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge("Menu_EnGarde.png", Configuration.MENU))));
			menu_fond.setLayout( new GridBagLayout());
			menu_fond.setBorder(new EmptyBorder(0, 0, 0, 0));
			JButton play = new ButtonCustom("JOUER", "cadre", new Dimension(400, 100));
			play.addActionListener(new AdaptateurCommande(collecteurMenu, "jouerMenu"));
			ImageIcon imgBtnBanal = new ImageIcon(ImageIO.read(Configuration.charge("cadre3.png", Configuration.MENU)).getScaledInstance(400, 50, Image.SCALE_SMOOTH));
			JButton charge = new ButtonCustom("Charger Partie", null, imgBtnBanal, new Dimension(400, 50));
			JButton classement = new ButtonCustom("Classement", null, imgBtnBanal, new Dimension(400, 50));
			classement.addActionListener(new AdaptateurCommande(collecteurMenu, "classement"));
			JButton demo = new ButtonCustom("Tutoriel", null, imgBtnBanal, new Dimension(400, 50));
			demo.addActionListener(new AdaptateurCommande(collecteurMenu, "tuto"));
			charge.addActionListener(new AdaptateurCommande(collecteurMenu, "chargePartieMenu"));
			JButton settings = new ButtonCustom("Parametres", null, imgBtnBanal, new Dimension(400, 50));
			settings.addActionListener(new AdaptateurCommande(collecteurMenu, "parametres"));
			JButton rules = new ButtonCustom("Regles du jeu", null, imgBtnBanal, new Dimension(400, 50));
			rules.addActionListener(new AdaptateurCommande(collecteurMenu, "regles"));
			JButton exit = new ButtonCustom("Quitter", null, imgBtnBanal, new Dimension(400, 50));
			exit.addActionListener(new AdaptateurCommande(collecteurMenu, "exit"));
			
			c = GridConstraints(0,0);
			menu_fond.add(play, c);
			c = GridConstraints(0,1);
			menu_fond.add(charge, c);
			c = GridConstraints(0,2);
			menu_fond.add(classement, c);
			c = GridConstraints(0,3);
			menu_fond.add(demo, c);
			c = GridConstraints(0,4);
			menu_fond.add(settings, c);
			c = GridConstraints(0,5);
			menu_fond.add(rules, c);
			c = GridConstraints(0,6);
			menu_fond.add(exit, c);
			
			fenetreMenu.add(menu_fond);
			fenetreMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			fenetreMenu.setSize(1280, 1024);
			fenetreMenu.setResizable(false);
			fenetreMenu.setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
