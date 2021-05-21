package view;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.*;

import Patterns.Observateur;
import controller.Controler;

public class InterfaceGraphiqueParametres implements Runnable, Observateur {
	
	private JFrame fenetreParametres;
	CollecteurEvenements controle;
	private GridBagConstraints c;
	
	//joueur 1
	private JLabel labelJ1;
	private JTextField textJ1 = new JTextField(10);
	private ButtonGroup groupJoueur1;
	private JRadioButton[] btnRadioJ1;
	//joueur 2
	private JLabel labelJ2;
	private JTextField textJ2 = new JTextField(10);
	private ButtonGroup groupJoueur2;
	private JRadioButton[] btnRadioJ2;
	//taille map & cartes en mains
	private JLabel taille_map;
	private JSpinner spinner_map = new JSpinner(new SpinnerNumberModel(23,10,30,1));
	private JLabel cartes_max;
	private JSpinner spinner_cartes = new JSpinner(new SpinnerNumberModel(5,2,7,1));

	private Box box;
	
	private InterfaceGraphiqueParametres(CollecteurEvenements controle) {
		this.controle = controle;
	}
	
	public static void demarrer(CollecteurEvenements control) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueParametres(control));
	}
	
	@Override
	public void miseAJour() {
		// TODO Auto-generated method stub
	}
	
	private static JButton Button (String name) {
		JButton button;
		ImageIcon banner;
		
		if (name=="JOUER"){
			banner = new ImageIcon(new ImageIcon("res/cadre.png").getImage().getScaledInstance(200, 50, Image.SCALE_SMOOTH));
		} else {
			banner = new ImageIcon(new ImageIcon("res/border.png").getImage().getScaledInstance(450, 40, Image.SCALE_SMOOTH));
		}
		
		button = new JButton(name, banner);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setFont(new Font("Century", Font.PLAIN, 20));
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		
		return button;
	}
	
	private static GridBagConstraints GridConstraints (int x, int y, int width) {
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		
		return c;
	}

	@Override
	public void run() {
		fenetreParametres = new JFrame("EN GARDE ! - PARAMETRES");
		JLabel para_fond = new JLabel(new ImageIcon("res/parametres.png"));
		para_fond.setLayout( new GridBagLayout() );
		
		labelJ1 = new JLabel("Escrimeur Gaucher");
		labelJ2 = new JLabel("Escrimeur Droitier");
		taille_map =  new JLabel("Taille du terrain");
		cartes_max = new JLabel("Cartes max en mains");
		
		groupJoueur1 = new ButtonGroup();
		btnRadioJ1 = new JRadioButton[3];
		btnRadioJ1[0] = new JRadioButton("Humain", true);
		btnRadioJ1[1] = new JRadioButton("IA Facile", false);
		btnRadioJ1[2] = new JRadioButton("IA Difficile", false);
		
		groupJoueur2 = new ButtonGroup();
		btnRadioJ2 = new JRadioButton[3];
		btnRadioJ2[0] = new JRadioButton("Humain", false);
		btnRadioJ2[1] = new JRadioButton("IA Facile", true);
		btnRadioJ2[2] = new JRadioButton("IA Difficile", false);
		
		JPanel panelJ1 = new JPanel(new GridLayout(1, 5));
		JPanel panelJ2 = new JPanel(new GridLayout(1, 5));
		JPanel panelTailleCartes = new JPanel(new GridLayout(1, 4));
		
		this.box = Box.createVerticalBox();
		box.add(panelJ1);
		box.add(panelJ2);
		box.add(panelTailleCartes);
		
		panelJ1.add(labelJ1);
		textJ1.setText("Joueur 1");
		panelJ1.add(textJ1);
		panelJ1.add(btnRadioJ1[0]);
		panelJ1.add(btnRadioJ1[1]);
		panelJ1.add(btnRadioJ1[2]);
		groupJoueur1.add(btnRadioJ1[0]);
		groupJoueur1.add(btnRadioJ1[1]);
		groupJoueur1.add(btnRadioJ1[2]);
		
		panelJ2.add(labelJ2);
		textJ2.setText("Joueur 2");
		panelJ2.add(textJ2);
		panelJ2.add(btnRadioJ2[0]);
		panelJ2.add(btnRadioJ2[1]);
		panelJ2.add(btnRadioJ2[2]);
		groupJoueur2.add(btnRadioJ2[0]);
		groupJoueur2.add(btnRadioJ2[1]);
		groupJoueur2.add(btnRadioJ2[2]);
		
		panelTailleCartes.add(taille_map);
		panelTailleCartes.add(spinner_map);
		panelTailleCartes.add(cartes_max);
		panelTailleCartes.add(spinner_cartes);
		
		JButton play = Button("JOUER");
		play.addActionListener(new AdaptateurCommande(this.controle, "play"));
		JButton saveSettings = Button("Enregistrer comme paramètres par défaut");
		saveSettings.addActionListener(new AdaptateurCommande(this.controle, "saveSettings"));//adaptateurConfiguration avec l'IG en parametre ?
		JButton restoreSettings = Button("Rétablir les paramètres par défaut");
		restoreSettings.addActionListener(new AdaptateurCommande(this.controle, "restoreSettings"));//adaptateurConfiguration avec l'IG en parametre ?
		
		c = GridConstraints(0,0,3);
		para_fond.add(box,c);
		c = GridConstraints(0,1,1);
		para_fond.add(saveSettings,c);
		c = GridConstraints(1,1,1);
		para_fond.add(restoreSettings,c);
		c = GridConstraints(2,1,1);
		para_fond.add(play,c);
		
		fenetreParametres.add(para_fond);
		fenetreParametres.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetreParametres.setSize(1280, 540);
		fenetreParametres.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		InterfaceGraphiqueParametres.demarrer(new Controler() {});
	}
}
