package view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import Global.Configuration;
import controller.Controler;
import controller.ControlerJeu;
import controller.ControllerMenu;

public class InterfaceGraphiqueMenu implements Runnable {
	
	private JFrame fenetreMenu;
	private CollecteurEvenements collecteurMenu;
	private GridBagConstraints c;
	
	private InterfaceGraphiqueMenu(CollecteurEvenements controle) {
		this.collecteurMenu = controle;
	}
	
	public static void demarrer(CollecteurEvenements control) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueMenu(control));
	}
	
	public void close() {
		fenetreMenu.setVisible(false);
		fenetreMenu.dispose();
	}
	
	private static JButton Button (String name) throws IOException {
		JButton button;
		
		if (name=="JOUER"){
			ImageIcon banner = new ImageIcon(ImageIO.read(Configuration.charge(Configuration.getFolderMenu() + "cadre.png")));
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
	
	private static GridBagConstraints GridConstraints (int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = x;
		c.gridy = y;
		
		return c;
	}

	@Override
	public void run() {
		fenetreMenu = new JFrame("EN GARDE ! - MENU PRINCIPAL");
		JLabel menu_fond;
		try {
			menu_fond = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge(Configuration.getFolderMenu() + "Menu_EnGarde.png"))));
			menu_fond.setLayout( new GridBagLayout() );
			JButton play = Button("JOUER");
			play.addActionListener(new AdaptateurCommande(this.collecteurMenu, "play")); //adaptateurConfiguration avec l'IG en parametre ?
			JButton charge = Button("Charger Partie");
			charge.addActionListener(new AdaptateurCommande(this.collecteurMenu, "chargeGame")); //adaptateurConfiguration avec l'IG en parametre ?
			JButton settings = Button("Paramètres");
			settings.addActionListener(new AdaptateurCommande(this.collecteurMenu, "settings"));
			JButton rules = Button("Règles du jeu");
			rules.addActionListener(new AdaptateurCommande(this.collecteurMenu, "rules")); //adaptateurConfiguration avec l'IG en parametre ?
			JButton exit = Button("Quitter");
			exit.addActionListener(new AdaptateurCommande(this.collecteurMenu, "exit"));
			
			c = GridConstraints(0,0);
			menu_fond.add(play, c);
			c = GridConstraints(0,1);
			menu_fond.add(charge, c);
			c = GridConstraints(0,2);
			menu_fond.add(settings, c);
			c = GridConstraints(0,3);
			menu_fond.add(rules, c);
			c = GridConstraints(0,4);
			menu_fond.add(exit, c);
			
			fenetreMenu.add(menu_fond);
			fenetreMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			fenetreMenu.setSize(1280, 1024);
			fenetreMenu.setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		InterfaceGraphiqueMenu.demarrer(new ControllerMenu() {});
	}
}
