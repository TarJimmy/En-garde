package view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

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
	 * Cree un bouton JButton
	 * @param name : le nom du bouton
	 * @return le bouton name genere
	 */
	
	private static JButton Button (String name) {
		JButton button;
		try {
			button = new JButton(name);	
			
			if (name=="JOUER"){
				ImageIcon banner;
				
				banner = new ImageIcon(ImageIO.read(Configuration.charge("cadre.png", Configuration.MENU)));
				button = new JButton(name, banner);
				button.addMouseListener(new AdaptateurBouton(collecteurMenu, "cadre", button, 0));
			} else {
				button = new JButton(name);
				button.addMouseListener(new AdaptateurBouton(collecteurMenu, "pasCadre", button, 400));
			}
			
			button.setHorizontalTextPosition(SwingConstants.CENTER);
			button.setVerticalAlignment(SwingConstants.TOP);
			button.setFont(new Font("Century", Font.PLAIN, 40));
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
		JLabel menu_fond = null;
		try {
			menu_fond = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge("Menu_EnGarde.png", Configuration.MENU))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		menu_fond.setLayout( new GridBagLayout() );
		JButton play = Button("JOUER");
		play.addActionListener(new AdaptateurCommande(InterfaceGraphiqueMenu.collecteurMenu, "jouerMenu"));
		JButton charge = Button("Charger Partie");
		JButton demo = Button("Tutoriel");
		demo.addActionListener(new AdaptateurCommande(InterfaceGraphiqueMenu.collecteurMenu, "tuto"));
		charge.addActionListener(new AdaptateurCommande(InterfaceGraphiqueMenu.collecteurMenu, "chargePartieMenu"));
		JButton settings = Button("Parametres");
		settings.addActionListener(new AdaptateurCommande(InterfaceGraphiqueMenu.collecteurMenu, "parametres"));
		JButton rules = Button("Regles du jeu");
		rules.addActionListener(new AdaptateurCommande(InterfaceGraphiqueMenu.collecteurMenu, "regles"));
		JButton exit = Button("Quitter");
		exit.addActionListener(new AdaptateurCommande(InterfaceGraphiqueMenu.collecteurMenu, "exit"));
		
		c = GridConstraints(0,0);
		menu_fond.add(play, c);
		c = GridConstraints(0,1);
		menu_fond.add(charge, c);
		c = GridConstraints(0,2);
		menu_fond.add(demo, c);
		c = GridConstraints(0,3);
		menu_fond.add(settings, c);
		c = GridConstraints(0,4);
		menu_fond.add(rules, c);
		c = GridConstraints(0,5);
		menu_fond.add(exit, c);
		
		fenetreMenu.add(menu_fond);
		fenetreMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetreMenu.setSize(1280, 1024);
		fenetreMenu.setResizable(false);
		fenetreMenu.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		InterfaceGraphiqueMenu.demarrer(new ControlerAutre());
	}
}
