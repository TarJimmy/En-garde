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
 * @author La�titia & Delphine
 *
 */
public class InterfaceGraphiqueMenu implements Runnable {
	
	private static JFrame fenetreMenu;
	private CollecteurEvenements collecteurMenu;
	private GridBagConstraints c;
	
	private InterfaceGraphiqueMenu(CollecteurEvenements controle) {
		this.collecteurMenu = controle;
	}
	
	/**
	 * Ouvre la fen�tre Menu
	 * @param control
	 */
	public static void demarrer(CollecteurEvenements control) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueMenu(control));
	}
	
	/**
	 * Ferme la fen�tre Menu
	 */
	public static void close() {
		fenetreMenu.setVisible(false);
		fenetreMenu.dispose();
	}
	
	/**
	 * Cr�e un bouton JButton
	 * @param name : le nom du bouton
	 * @return le bouton name g�n�r�
	 */
	
	private static JButton Button (String name) {
		JButton button;
		
		if (name=="JOUER"){
			ImageIcon banner = new ImageIcon(Configuration.charge("cadre.png", Configuration.MENU));
			button = new JButton(name, banner);
			button.setHorizontalTextPosition(SwingConstants.CENTER);
		} else {
			button = new JButton(name);	
		}
		button.setFont(new Font("Century", Font.PLAIN, 40));
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		
		return button;
	}
	
	/**
	 * Permet de d�finir l'emplacement et l'alignement (centr�) d'un objet dans la fen�tre Menu
	 * @param x : abscisse
	 * @param y : ordonn�e
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
	 * �criture du contenu de la fen�tre Menu
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
		play.addActionListener(new AdaptateurCommande(this.collecteurMenu, "play"));
		JButton charge = Button("Charger Partie");
		JButton demo = Button("Tutoriel");
		demo.addActionListener(new AdaptateurCommande(this.collecteurMenu, "tuto"));
		charge.addActionListener(new AdaptateurCommande(this.collecteurMenu, "chargeGame"));
		JButton settings = Button("Param�tres");
		settings.addActionListener(new AdaptateurCommande(this.collecteurMenu, "settings"));
		JButton rules = Button("R�gles du jeu");
		rules.addActionListener(new AdaptateurCommande(this.collecteurMenu, "rules"));
		JButton exit = Button("Quitter");
		exit.addActionListener(new AdaptateurCommande(this.collecteurMenu, "exit"));
		
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
		fenetreMenu.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		InterfaceGraphiqueMenu.demarrer(new ControlerMenu());
	}
}
