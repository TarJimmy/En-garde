package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Global.Configuration;
import Patterns.Observateur;

/**
 * 
 * @author Laetitia & Delphine
 *
 */
public class InterfaceGraphiqueParametres implements Runnable, Observateur {
	
	private static JFrame fenetreParametres;
	private static CollecteurEvenements controle;
	
	//Terrain
	private JLabel titreSectionTerrain;
	private JLabel labelTerrain;
	private static JSpinner spinner_map;
	////joueur 1
	private JLabel labelJ1;
	private static JTextField txtJoueur1;
	private static ButtonGroup buttonGroup_TypeJ1;
	private static JRadioButton btnRadioJ1_Humain;
	private static JRadioButton btnRadioJ1_Facile;
	private static JRadioButton btnRadioJ1_Moyenne;
	private static JRadioButton btnRadioJ1_Difficile;
	private JLabel labelPositionJ1;
	private static JSpinner spinner_positionJ1;
	////joueur 2
	private JLabel labelJ2;
	private static JTextField txtJoueur2;
	private static ButtonGroup buttonGroup_TypeJ2;
	private static JRadioButton btnRadioJ2_Humain;
	private static JRadioButton btnRadioJ2_Facile;
	private static JRadioButton btnRadioJ2_Moyenne;
	private static JRadioButton btnRadioJ2_Difficile;
	private JLabel labelPositionJ2;
	private static JSpinner spinner_positionJ2;
	//Jeu
	private JLabel titreSectionJeu;
	private JLabel labelModeAttaque;
	private static ButtonGroup buttonGroup_ModeAttaque;
	private static JRadioButton modeAttaque_Basique;
	private static JRadioButton modeAttaque_Avance;
	private JLabel labelNbManches;
	private static JSpinner spinner_manches;
	private JLabel labelCartesMax;
	private static JSpinner spinner_cartesMax;
	////Nombre de cartes par valeur
	private JLabel labelNbCartesValeurs;
	private JLabel labelCartes1;
	private static JSpinner spinner_cartes1;
	private JLabel labelCartes2;
	private static JSpinner spinner_cartes2;
	private JLabel labelCartes3;
	private static JSpinner spinner_cartes3;
	private JLabel labelCartes4;
	private static JSpinner spinner_cartes4;
	private JLabel labelCartes5;
	private static JSpinner spinner_cartes5;
	//Boutons
	private JLabel warnEnregistrement;
	private JButton btnSaveSettings;
	
	//Variables du formulaire
	private static int map;
	private static String nomJoueur1;
	private static String typeJ1;
	private static int posJ1;
	private static String nomJoueur2;
	private static String typeJ2;
	private static int posJ2;
	private static String modeAttaque;
	private static int manches;
	private static int cartes_max;
	private static int cartes1;
	private static int cartes2;
	private static int cartes3;
	private static int cartes4;
	private static int cartes5;
	private Boolean canEnregistrer;
	
	private InterfaceGraphiqueParametres(CollecteurEvenements controle) {
		InterfaceGraphiqueParametres.controle = controle;
	}
	
	/**
	 * (Re)Initialisation des valeurs par defaut du formulaire
	 * @param settings : liste des valeurs du formulaire
	 */
	private static void settings(List<String> settings) {
		map = Integer.parseInt(settings.get(0));
		nomJoueur1 = settings.get(1);
		typeJ1 = settings.get(2);
		posJ1 = Integer.parseInt(settings.get(3));
		nomJoueur2 = settings.get(4);
		typeJ2 = settings.get(5);
		posJ2 = Integer.parseInt(settings.get(6));
		modeAttaque = settings.get(7);
		manches = Integer.parseInt(settings.get(8));
		cartes_max = Integer.parseInt(settings.get(9));
		cartes1 = Integer.parseInt(settings.get(10));
		cartes2 = Integer.parseInt(settings.get(11));
		cartes3 = Integer.parseInt(settings.get(12));
		cartes4 = Integer.parseInt(settings.get(13));
		cartes5 = Integer.parseInt(settings.get(14));
	}
	
	/**
	 * Ouvre la fenetre Parametres
	 * @param settings : liste des valeurs pour l'inisialisation du formulaire 
	 * @param control
	 */
	public static void demarrer(List<String> settings, CollecteurEvenements control) {
		settings(settings);
		SwingUtilities.invokeLater(new InterfaceGraphiqueParametres(control));
	}
	
	/**
	 * Ferme la fenetre Parametres
	 */
	public static boolean close() {
		try {
			fenetreParametres.setVisible(false);
			fenetreParametres.dispose();
			return true;
		} catch(NullPointerException n) {
			return false;
		}
	}

	/**
	 * Mise e jour des valeurs par defaut du formulaire
	 * @param settings : liste des valeurs du formulaire
	 */
	public static void majParametres(List<String> settings) {
		settings(settings);
		setRadioButtons(typeJ1,typeJ2,modeAttaque);
		spinner_map.setValue(map);
		txtJoueur1.setText(nomJoueur1);
		spinner_positionJ1.setValue(posJ1);
		txtJoueur2.setText(nomJoueur2);
		spinner_positionJ2.setValue(posJ2);
		spinner_manches.setValue(manches);
		spinner_cartesMax.setValue(cartes_max);
		spinner_cartes1.setValue(cartes1);
		spinner_cartes2.setValue(cartes2);
		spinner_cartes3.setValue(cartes3);
		spinner_cartes4.setValue(cartes4);
		spinner_cartes5.setValue(cartes5);
	}
	
	/**
	 * Mise e true du radio bouton selon la valeur du parametre
	 * @param typeJ1 : valeur du type du joueur 1 (humain ou IA)
	 * @param typeJ2 : valeur du type du joueur 2 (humain ou IA)
	 * @param modeAttaque : valeur par defaut du mode d'attaque (basique ou avance)
	 */
	private static void setRadioButtons(String typeJ1, String typeJ2, String modeAttaque) {
		switch (typeJ1) {
			case "HUMAIN" : btnRadioJ1_Humain.setSelected(true);break;
			case "IA_FACILE" : btnRadioJ1_Facile.setSelected(true);break;
			case "IA_MOYENNE" : btnRadioJ1_Moyenne.setSelected(true);break;
			case "IA_DIFFICILE" : btnRadioJ1_Difficile.setSelected(true);break;
		}
		switch (typeJ2) {
			case "HUMAIN" : btnRadioJ2_Humain.setSelected(true);break;
			case "IA_FACILE" : btnRadioJ2_Facile.setSelected(true);break;
			case "IA_MOYENNE" : btnRadioJ2_Moyenne.setSelected(true);break;
			case "IA_DIFFICILE" : btnRadioJ2_Difficile.setSelected(true);break;
		}
		switch (modeAttaque) {
			case "Basique" : modeAttaque_Basique.setSelected(true);break;
			case "Avance" : modeAttaque_Avance.setSelected(true);break;
		}
		
	}
	
	/**
	 * Affichage d'un warning si de la zone de texte est vide
	 * @param txtJoueur : zone de texte e verifier
	 * @param warning : le text e afficher
	 */
	private void warningField(JTextField txtJoueur, JLabel warning) {
		txtJoueur.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {warn();}

			@Override
			public void removeUpdate(DocumentEvent e) {warn();}

			@Override
			public void changedUpdate(DocumentEvent e) {warn();}

			private void warn() {
				if (txtJoueur.getText().isEmpty()) {
					canEnregistrer = false;
					warnEnregistrement.setVisible(true);
					btnSaveSettings.setVisible(false);
					warning.setVisible(true);
				} else {
					canEnregistrer = true;
					warnEnregistrement.setVisible(false);
					btnSaveSettings.setVisible(true);
					warning.setVisible(false);
				}
			}
			
		});
	}
	
	/**
	 * Cree un bouton JButton
	 * @param name : le nom du bouton
	 * @return le bouton name genere
	 */
	private static JButton Button (String name) {
		JButton button;
		ImageIcon banner;
		
		try {
			switch (name){
				case "JOUER":
				case "MENU":
				case "FERMER":
					banner = new ImageIcon(new ImageIcon(ImageIO.read(Configuration.charge("cadre4.png", Configuration.MENU))).getImage().getScaledInstance(195, 40, Image.SCALE_SMOOTH));
					button = new JButton(name, banner);
					button.setFont(new Font("Century", Font.PLAIN, 15));
					button.setForeground(Color.WHITE);
					button.addMouseListener(new AdaptateurBouton(controle, "cadre4", button, 195));
					break;
				default:
					banner = new ImageIcon(new ImageIcon(ImageIO.read(Configuration.charge("cadre3.png", Configuration.MENU))).getImage().getScaledInstance(266, 40, Image.SCALE_SMOOTH));
					button = new JButton(name, banner);
					button.setFont(new Font("Century", Font.PLAIN, 11));
					button.addMouseListener(new AdaptateurBouton(controle, "cadre3", button, 266));
					break;
			}
			
			button.setHorizontalTextPosition(SwingConstants.CENTER);
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

	@Override
	public void miseAJour() {
		// TODO Auto-generated method stub
	}

	/**
	 * Ecriture du contenu de la fenetre Parametres
	 */
	@Override
	public void run() {
		canEnregistrer = true;
		fenetreParametres = new JFrame("EN GARDE ! - PARAMETRES");
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		fenetreParametres.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Titre principal - PAREMETRES
		JLabel titreFenetre = new JLabel("Param\u00E8tres");
		titreFenetre.setFont(new Font("Tahoma", Font.BOLD, 20));
		titreFenetre.setBounds(55, 11, 156, 43);
		contentPane.add(titreFenetre);
		
		/// Section 1 - Terrain
		titreSectionTerrain = new JLabel("Terrain");
		titreSectionTerrain.setFont(new Font("Tahoma", Font.BOLD, 14));
		titreSectionTerrain.setBounds(55, 70, 63, 37);
		contentPane.add(titreSectionTerrain);
		
		//// Option - Taille du terrain
		labelTerrain = new JLabel("Taille du terrain");
		labelTerrain.setBounds(55, 115, 100, 20);
		contentPane.add(labelTerrain);
		spinner_map = new JSpinner();
		spinner_map.setModel(new SpinnerNumberModel(map, 13, 25, 1));
		spinner_map.setBounds(174, 115, 46, 20);
		((JSpinner.DefaultEditor) spinner_map.getEditor()).getTextField().setEditable(false);
		contentPane.add(spinner_map);
		
		//// Options - Escrimeur Gauche
		labelJ1 = new JLabel("Escrimeur Gauche");
		labelJ1.setBounds(55, 140, 130, 20);
		
		/////nom du joueur 1
		txtJoueur1 = new JTextField();
		txtJoueur1.setDocument(new JTextFieldLimit(15));
		txtJoueur1.setText(nomJoueur1);
		txtJoueur1.setBounds(175, 140, 130, 20);
		contentPane.add(txtJoueur1);
		txtJoueur1.setColumns(10);
		JLabel warning_J1 = new JLabel("ATTENTION : le nom du joueur ne peut pas \u00EAtre vide");
		warning_J1.setFont(new Font("Tahoma", Font.BOLD, 11));
		warning_J1.setForeground(new Color(128, 0, 0));
		warning_J1.setBounds(330, 140, 300, 20);
		warning_J1.setVisible(false);
		contentPane.add(warning_J1);
		
		/////type du joueur 1
		btnRadioJ1_Humain = new JRadioButton("Humain");
		btnRadioJ1_Humain.setActionCommand("HUMAIN");
		buttonGroup_TypeJ1 = new ButtonGroup();
		buttonGroup_TypeJ1.add(btnRadioJ1_Humain);
		btnRadioJ1_Humain.setBounds(175, 170, 100, 25);
		contentPane.add(btnRadioJ1_Humain);
		btnRadioJ1_Facile = new JRadioButton("IA Facile");
		btnRadioJ1_Facile.setActionCommand("IA_FACILE");
		buttonGroup_TypeJ1.add(btnRadioJ1_Facile);
		btnRadioJ1_Facile.setBounds(275, 170, 100, 25);
		contentPane.add(btnRadioJ1_Facile);
		btnRadioJ1_Moyenne = new JRadioButton("IA Moyenne");
		btnRadioJ1_Moyenne.setActionCommand("IA_MOYENNE");
		buttonGroup_TypeJ1.add(btnRadioJ1_Moyenne);
		btnRadioJ1_Moyenne.setBounds(375, 170, 100, 25);
		contentPane.add(btnRadioJ1_Moyenne);
		btnRadioJ1_Difficile = new JRadioButton("IA Difficile");
		btnRadioJ1_Difficile.setActionCommand("IA_DIFFICILE");
		buttonGroup_TypeJ1.add(btnRadioJ1_Difficile);
		btnRadioJ1_Difficile.setBounds(475, 170, 100, 25);
		contentPane.add(btnRadioJ1_Difficile);
		
		/////emplacement du joueur 1 au debut de la partie
		labelPositionJ1 = new JLabel("D�part :");
		labelPositionJ1.setBounds(600, 170, 50, 20);
		contentPane.add(labelPositionJ1);
		spinner_positionJ1 = new JSpinner();
		spinner_positionJ1.setModel(new SpinnerNumberModel(posJ1, 1, map-1, 1));
		spinner_positionJ1.setBounds(660, 170, 40, 20);
		((JSpinner.DefaultEditor) spinner_positionJ1.getEditor()).getTextField().setEditable(false);
		contentPane.add(spinner_positionJ1);
		
		//// Options - Escrimeur Droit
		labelJ2 = new JLabel("Escrimeur Droit");
		labelJ2.setBounds(55, 207, 130, 14);
		contentPane.add(labelJ2);
		
		/////nom du joueur 2
		txtJoueur2 = new JTextField();
		txtJoueur2.setDocument(new JTextFieldLimit(15));
		txtJoueur2.setText(nomJoueur2);
		txtJoueur2.setColumns(10);
		txtJoueur2.setBounds(175, 204, 130, 20);
		contentPane.add(txtJoueur2);
		JLabel warning_J2 = new JLabel("ATTENTION : le nom du joueur ne peut pas \u00EAtre vide");
		warning_J2.setFont(new Font("Tahoma", Font.BOLD, 11));
		warning_J2.setForeground(new Color(128, 0, 0));
		warning_J2.setBounds(330, 204, 300, 14);
		warning_J2.setVisible(false);
		contentPane.add(warning_J2);
		
		/////type du joueur 2
		btnRadioJ2_Humain = new JRadioButton("Humain");
		btnRadioJ2_Humain.setActionCommand("HUMAIN");
		buttonGroup_TypeJ2 = new ButtonGroup();
		buttonGroup_TypeJ2.add(btnRadioJ2_Humain);
		btnRadioJ2_Humain.setBounds(175, 237, 100, 25);
		contentPane.add(btnRadioJ2_Humain);
		btnRadioJ2_Facile = new JRadioButton("IA Facile");
		btnRadioJ2_Facile.setActionCommand("IA_FACILE");
		buttonGroup_TypeJ2.add(btnRadioJ2_Facile);
		btnRadioJ2_Facile.setBounds(275, 237, 100, 25);
		contentPane.add(btnRadioJ2_Facile);
		btnRadioJ2_Moyenne = new JRadioButton("IA Moyenne");
		btnRadioJ2_Moyenne.setActionCommand("IA_MOYENNE");
		buttonGroup_TypeJ2.add(btnRadioJ2_Moyenne);
		btnRadioJ2_Moyenne.setBounds(375, 237, 100, 25);
		contentPane.add(btnRadioJ2_Moyenne);
		btnRadioJ2_Difficile = new JRadioButton("IA Difficile");
		btnRadioJ2_Difficile.setActionCommand("IA_DIFFICILE");
		buttonGroup_TypeJ2.add(btnRadioJ2_Difficile);
		btnRadioJ2_Difficile.setBounds(475, 237, 100, 25);
		contentPane.add(btnRadioJ2_Difficile);
		
		/////emplacement du joueur 2 au debut de la partie
		labelPositionJ2 = new JLabel("D�part :");
		labelPositionJ2.setBounds(600, 237, 50, 20);
		contentPane.add(labelPositionJ2);
		spinner_positionJ2 = new JSpinner();
		spinner_positionJ2.setModel(new SpinnerNumberModel(posJ2, 2, map, 1));
		spinner_positionJ2.setBounds(660, 237, 40, 20);
		((JSpinner.DefaultEditor) spinner_positionJ2.getEditor()).getTextField().setEditable(false);
		contentPane.add(spinner_positionJ2);
		contentPane.add(labelJ1);
		
		/// Section 2 - Jeu
		titreSectionJeu = new JLabel("Jeu");
		titreSectionJeu.setFont(new Font("Tahoma", Font.BOLD, 14));
		titreSectionJeu.setBounds(55, 310, 46, 14);
		contentPane.add(titreSectionJeu);
		
		////Option - Mode d'attaque
		labelModeAttaque = new JLabel("Mode d'attaque :");
		labelModeAttaque.setBounds(55, 335, 100, 23);
		contentPane.add(labelModeAttaque);
		modeAttaque_Basique = new JRadioButton("Basique");
		modeAttaque_Basique.setActionCommand("Basique");
		buttonGroup_ModeAttaque = new ButtonGroup();
		buttonGroup_ModeAttaque.add(modeAttaque_Basique);
		modeAttaque_Basique.setBounds(170, 335, 75, 23);
		contentPane.add(modeAttaque_Basique);
		modeAttaque_Avance = new JRadioButton("Avanc\u00E9");
		modeAttaque_Avance.setActionCommand("Avance");
		buttonGroup_ModeAttaque.add(modeAttaque_Avance);
		modeAttaque_Avance.setBounds(246, 335, 75, 23);
		contentPane.add(modeAttaque_Avance);
		
		////Option - Nombre de manches
		labelNbManches = new JLabel("Nombre de manches");
		labelNbManches.setBounds(55, 375, 130, 20);
		contentPane.add(labelNbManches);
		spinner_manches = new JSpinner();
		spinner_manches.setModel(new SpinnerNumberModel(manches, 1, 10, 1));
		spinner_manches.setBounds(181, 375, 39, 20);
		((JSpinner.DefaultEditor) spinner_manches.getEditor()).getTextField().setEditable(false);
		contentPane.add(spinner_manches);
		
		////Option - Nombre maximum de cartes en mains
		labelCartesMax = new JLabel("Nombre maximum de cartes en mains");
		labelCartesMax.setBounds(55, 410, 243, 20);
		contentPane.add(labelCartesMax);
		spinner_cartesMax = new JSpinner();
		spinner_cartesMax.setModel(new SpinnerNumberModel(cartes_max, 3, 7, 1));
		spinner_cartesMax.setBounds(279, 410, 39, 20);
		((JSpinner.DefaultEditor) spinner_cartesMax.getEditor()).getTextField().setEditable(false);
		contentPane.add(spinner_cartesMax);
		
		////Option - Nombre de cartes par valeurs
		labelNbCartesValeurs = new JLabel("Nombre de cartes par valeurs :");
		labelNbCartesValeurs.setBounds(55, 450, 212, 14);
		contentPane.add(labelNbCartesValeurs);
		
		/////cartes de valeur 1
		labelCartes1 = new JLabel("Cartes 1");
		labelCartes1.setBounds(55, 480, 80, 20);
		contentPane.add(labelCartes1);
		spinner_cartes1 = new JSpinner();
		spinner_cartes1.setModel(new SpinnerNumberModel(cartes1, 0, 10, 1));
		spinner_cartes1.setBounds(120, 480, 40, 20);
		((JSpinner.DefaultEditor) spinner_cartes1.getEditor()).getTextField().setEditable(false);
		contentPane.add(spinner_cartes1);
		
		/////cartes de valeur 2
		labelCartes2 = new JLabel("Cartes 2");
		labelCartes2.setBounds(220, 480, 80, 20);
		contentPane.add(labelCartes2);
		spinner_cartes2 = new JSpinner();
		spinner_cartes2.setModel(new SpinnerNumberModel(cartes2, 0, 10, 1));
		spinner_cartes2.setBounds(285, 480, 40, 20);
		((JSpinner.DefaultEditor) spinner_cartes2.getEditor()).getTextField().setEditable(false);
		contentPane.add(spinner_cartes2);
		
		/////cartes de valeur 3
		labelCartes3 = new JLabel("Cartes 3");
		labelCartes3.setBounds(385, 480, 80, 20);
		contentPane.add(labelCartes3);
		spinner_cartes3 = new JSpinner();
		spinner_cartes3.setModel(new SpinnerNumberModel(cartes3, 0, 10, 1));
		spinner_cartes3.setBounds(450, 480, 40, 20);
		((JSpinner.DefaultEditor) spinner_cartes3.getEditor()).getTextField().setEditable(false);
		contentPane.add(spinner_cartes3);
		
		/////cartes de valeur 4
		labelCartes4 = new JLabel("Cartes 4");
		labelCartes4.setBounds(550, 480, 80, 20);
		contentPane.add(labelCartes4);
		spinner_cartes4 = new JSpinner();
		spinner_cartes4.setModel(new SpinnerNumberModel(cartes4, 0, 10, 1));
		spinner_cartes4.setBounds(615, 480, 40, 20);
		((JSpinner.DefaultEditor) spinner_cartes4.getEditor()).getTextField().setEditable(false);
		contentPane.add(spinner_cartes4);
		
		/////cartes de valeur 5
		labelCartes5 = new JLabel("Cartes 5");
		labelCartes5.setBounds(715, 480, 80, 20);
		contentPane.add(labelCartes5);
		spinner_cartes5 = new JSpinner();
		spinner_cartes5.setModel(new SpinnerNumberModel(cartes5, 0, 10, 1));
		spinner_cartes5.setBounds(780, 480, 40, 20);
		((JSpinner.DefaultEditor) spinner_cartes5.getEditor()).getTextField().setEditable(false);
		contentPane.add(spinner_cartes5);
		
		///Boutons
		warnEnregistrement = new JLabel("Enregistrement des parametres impossible");
		warnEnregistrement.setFont(new Font("Tahoma", Font.BOLD, 11));
		warnEnregistrement.setForeground(new Color(128, 0, 0));
		warnEnregistrement.setBounds(70, 540, 300, 40);
		warnEnregistrement.setVisible(false);
		contentPane.add(warnEnregistrement);
		btnSaveSettings = Button("Enregistrer comme parametres par defaut");
		btnSaveSettings.addActionListener(new AdaptateurCommande(controle, "sauvePara", canEnregistrer));
		btnSaveSettings.setBounds(55, 540, 266, 37);
		contentPane.add(btnSaveSettings);
		
		JButton btnRestoreSettings = Button("Retablir les parametres par defaut");
		btnRestoreSettings.addActionListener(new AdaptateurCommande(controle, "restaurePara"));
		btnRestoreSettings.setBounds(331, 540, 266, 37);
		contentPane.add(btnRestoreSettings);
		
		JButton btnPlay = Button("MENU");
		btnPlay.addActionListener(new AdaptateurCommande(controle, "menu"));
		btnPlay.setBounds(634, 540, 195, 37);
		contentPane.add(btnPlay);
		
		//setRadioButtons
		setRadioButtons(typeJ1,typeJ2,modeAttaque);
		
		//warningField
		warningField(txtJoueur1, warning_J1);
		warningField(txtJoueur2, warning_J2);
		
		//bornesSpinnersTerrain
		spinner_map.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if ((int) spinner_map.getValue() <= (int) spinner_positionJ1.getValue()) {
					spinner_positionJ1.setModel(new SpinnerNumberModel((int) spinner_map.getValue()-1, 1, (int) spinner_map.getValue()-1, 1));
					spinner_positionJ2.setModel(new SpinnerNumberModel((int) spinner_map.getValue(), 2, (int) spinner_map.getValue(), 1));
				} else {
					spinner_positionJ1.setModel(new SpinnerNumberModel((int) spinner_positionJ1.getValue(), 1, (int) spinner_map.getValue()-1, 1));
				}
				if((int) spinner_map.getValue() <= (int) spinner_positionJ2.getValue()) {
					spinner_positionJ2.setModel(new SpinnerNumberModel((int) spinner_map.getValue(), 2, (int) spinner_map.getValue(), 1));
				} else {
					spinner_positionJ2.setModel(new SpinnerNumberModel((int) spinner_positionJ2.getValue(), 2, (int) spinner_map.getValue(), 1));
				}
				((JSpinner.DefaultEditor) spinner_positionJ1.getEditor()).getTextField().setEditable(false);
				((JSpinner.DefaultEditor) spinner_positionJ2.getEditor()).getTextField().setEditable(false);
			}
		});
		spinner_positionJ1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if ((int) spinner_positionJ1.getValue() >= (int) spinner_positionJ2.getValue()){
					spinner_positionJ2.setModel(new SpinnerNumberModel((int) spinner_positionJ1.getValue()+1, (int) spinner_positionJ1.getValue()+1, (int) spinner_map.getValue(), 1));
				} else {
					spinner_positionJ2.setModel(new SpinnerNumberModel((int) spinner_positionJ2.getValue(), (int) spinner_positionJ1.getValue()+1, (int) spinner_map.getValue(), 1));
				}
				((JSpinner.DefaultEditor) spinner_positionJ1.getEditor()).getTextField().setEditable(false);
				((JSpinner.DefaultEditor) spinner_positionJ2.getEditor()).getTextField().setEditable(false);
			}
		});
		
		//fenetreParametres.add(para_fond);
		fenetreParametres.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetreParametres.setBounds(100, 100, 900, 650);
		fenetreParametres.setResizable(false);
		fenetreParametres.setVisible(true);
	}

	/**
	 * Recuperation d'un parametre
	 * @param para : le parametres que l'on souhaite recuperer
	 * @return la valeur du parametre para
	 */
	public static String getParametre(String para) {
		switch(para) {
			case "map":
				return String.valueOf(spinner_map.getValue());
			case "nomJ1":
				return txtJoueur1.getText();
			case "typeJ1":
				return buttonGroup_TypeJ1.getSelection().getActionCommand();
			case "posJ1":
				return String.valueOf(spinner_positionJ1.getValue());
			case "nomJ2":
				return txtJoueur2.getText();
			case "typeJ2":
				return buttonGroup_TypeJ2.getSelection().getActionCommand();
			case "posJ2":
				return String.valueOf(spinner_positionJ2.getValue());
			case "modeAttaque":
				return buttonGroup_ModeAttaque.getSelection().getActionCommand();
			case "manches":
				return String.valueOf(spinner_manches.getValue());
			case "carteMax":
				return String.valueOf(spinner_cartesMax.getValue());
			case "carte1":
				return String.valueOf(spinner_cartes1.getValue());
			case "carte2":
				return String.valueOf(spinner_cartes2.getValue());
			case "carte3":
				return String.valueOf(spinner_cartes3.getValue());
			case "carte4":
				return String.valueOf(spinner_cartes4.getValue());
			case "carte5":
				return String.valueOf(spinner_cartes5.getValue());
			default:
				System.err.println("Parametre introuvable !");
				return "";
		}
	}
}
