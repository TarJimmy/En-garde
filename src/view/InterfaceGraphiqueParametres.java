package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private static String saveHumainJ1;
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
	private static String saveHumainJ2;
	private static ButtonGroup buttonGroup_TypeJ2;
	private static JRadioButton btnRadioJ2_Humain;
	private static JRadioButton btnRadioJ2_Facile;
	private static JRadioButton btnRadioJ2_Moyenne;
	private static JRadioButton btnRadioJ2_Difficile;
	private JLabel labelPositionJ2;
	private static JSpinner spinner_positionJ2;
	//Jeu
	private JLabel titreSectionJeu;
	private static JCheckBox btn_animation;
	private JLabel labelJoueurDebut;
	private static ButtonGroup buttonGroup_JoueurDebut;
	private static JRadioButton joueurDebut_gauche;
	private static JRadioButton joueurDebut_droite;
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
	private JLabel warning_J1;
	private JLabel warning_J2;
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
	private static String animation;
	private static String joueurDebut;
	private Boolean canEnregistrer;
	private Boolean J1;
	private Boolean J2;
	
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
		animation = settings.get(15);
		joueurDebut = settings.get(16);
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
	public static void close() {
		if (fenetreParametres!=null) {
			fenetreParametres.setVisible(false);
			fenetreParametres.dispose();
		}
	}

	/**
	 * Mise a jour des valeurs par defaut du formulaire
	 * @param settings : liste des valeurs du formulaire
	 */
	public static void majParametres(List<String> settings) {
		settings(settings);
		setRadioButtons(typeJ1,typeJ2,modeAttaque,animation,joueurDebut);
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
	 * Mise a true du radio bouton selon la valeur du parametre
	 * @param typeJ1 : valeur du type du joueur 1 (humain ou IA)
	 * @param typeJ2 : valeur du type du joueur 2 (humain ou IA)
	 * @param modeAttaque : valeur par defaut du mode d'attaque (basique ou avance)
	 */
	private static void setRadioButtons(String typeJ1, String typeJ2, String modeAttaque, String animation, String joueurDebut) {
		switch (typeJ1) {
			case "HUMAIN" : btnRadioJ1_Humain.setSelected(true);txtJoueur1.setEditable(true);break;
			case "IA_FACILE" : btnRadioJ1_Facile.setSelected(true);txtJoueur1.setEditable(false);break;
			case "IA_MOYENNE" : btnRadioJ1_Moyenne.setSelected(true);txtJoueur1.setEditable(false);break;
			case "IA_DIFFICILE" : btnRadioJ1_Difficile.setSelected(true);txtJoueur1.setEditable(false);break;
		}
		switch (typeJ2) {
			case "HUMAIN" : btnRadioJ2_Humain.setSelected(true);txtJoueur2.setEditable(true);break;
			case "IA_FACILE" : btnRadioJ2_Facile.setSelected(true);txtJoueur2.setEditable(false);break;
			case "IA_MOYENNE" : btnRadioJ2_Moyenne.setSelected(true);txtJoueur2.setEditable(false);break;
			case "IA_DIFFICILE" : btnRadioJ2_Difficile.setSelected(true);txtJoueur2.setEditable(false);break;
		}
		switch (modeAttaque) {
			case "Basique" : modeAttaque_Basique.setSelected(true);break;
			case "Avance" : modeAttaque_Avance.setSelected(true);break;
		}
		switch (animation) {
			case "Actif" : btn_animation.setSelected(true);break;
			case "NonActif" : btn_animation.setSelected(false);break;
		}
		switch (joueurDebut) {
			case "Gauche" : joueurDebut_gauche.setSelected(true);break;
			case "Droit" : joueurDebut_droite.setSelected(true);break;
		}
	}
	
	/**
	 * Affichage d'un warning si de la zone de texte est vide
	 * @param txtJoueur : zone de texte e verifier
	 * @param warning : le text e afficher
	 */
	private void warningField(String j,JTextField txtJoueur, JLabel warning, ButtonGroup buttonGroup) {
		txtJoueur.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {warn();}

			@Override
			public void removeUpdate(DocumentEvent e) {warn();}

			@Override
			public void changedUpdate(DocumentEvent e) {warn();}

			private void warn() {
				if (txtJoueur.getText().isEmpty()) {
					if (j == "j1"){J1 = false;}
					else if (j == "j2"){J2 = false;}
					canEnregistrer = false;
					warnEnregistrement.setVisible(true);
					btnSaveSettings.setVisible(false);
					warning.setVisible(true);
				} else {
					if (j == "j1"){J1 = true;}
					else if (j == "j2"){J2 = true;}
					warning.setVisible(false);
				}
				if(J1&&J2) {
					canEnregistrer = true;
					warnEnregistrement.setVisible(false);
					btnSaveSettings.setVisible(true);
				}
				if (buttonGroup.getSelection().getActionCommand().equals("HUMAIN") ) {
					if (j == "j1"){saveHumainJ1 = txtJoueur.getText();}
					else if (j == "j2"){saveHumainJ2 = txtJoueur.getText();}
				}
			}
			
		});
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
		J1 = true;
		J2 = true;
		fenetreParametres = new JFrame("EN GARDE ! - PARAMETRES");
		Font font = new Font("Tohama", Font.BOLD, 12);
		JLabel contentPane = null;
		try {
		contentPane = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge("parametres.png", Configuration.MENU))));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		fenetreParametres.setContentPane(contentPane);
		fenetreParametres.setIconImage(Configuration.imgIcone);
		fenetreParametres.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageIO.read(Configuration.charge("curseur.png", Configuration.AUTRES)),new Point(0,0),"Mon curseur"));
		} catch (HeadlessException | IndexOutOfBoundsException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		contentPane.setLayout(null);
		
		/// Section 1 - Terrain
		titreSectionTerrain = new JLabel("Terrain");
		titreSectionTerrain.setFont(new Font("Century", Font.BOLD, 20));
		titreSectionTerrain.setBounds(45, 45, 100, 37);
		contentPane.add(titreSectionTerrain);
		
		//// Option - Taille du terrain
		labelTerrain = new JLabel("Taille du terrain");
		labelTerrain.setBounds(55, 95, 100, 20);
		labelTerrain.setFont(font);
		contentPane.add(labelTerrain);
		spinner_map = new JSpinner();
		spinner_map.setModel(new SpinnerNumberModel(map, 13, 25, 1));
		spinner_map.setBounds(174, 95, 46, 20);
		setSpinnerTransparent(spinner_map);
		contentPane.add(spinner_map);
		
		//// Options - Escrimeur Gauche
		labelJ1 = new JLabel("Escrimeur Gauche");
		labelJ1.setBounds(55, 140, 130, 20);
		labelJ1.setFont(font);
		
		/////nom du joueur 1
		txtJoueur1 = new JTextField();
		txtJoueur1.setDocument(new JTextFieldLimit(15));
		txtJoueur1.setText(nomJoueur1);
		txtJoueur1.setFont(font);
		txtJoueur1.setBounds(175, 140, 130, 20);
		txtJoueur1.setBorder(null);
		txtJoueur1.setOpaque(false);
		txtJoueur1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (buttonGroup_TypeJ1.getSelection().getActionCommand().equals("HUMAIN") ) {
					saveHumainJ1 = txtJoueur1.getText();
				}
			}
			
		});
		contentPane.add(txtJoueur1);
		txtJoueur1.setColumns(10);
		warning_J1 = new JLabel("ATTENTION : le nom du joueur ne peut pas \u00EAtre vide");
		warning_J1.setFont(new Font("Tohama", Font.BOLD, 14));
		warning_J1.setForeground(new Color(102, 34, 0));
		warning_J1.setBounds(330, 140, 400, 20);
		warning_J1.setVisible(false);
		contentPane.add(warning_J1);
		
		/////type du joueur 1
		saveHumainJ1 = nomJoueur1;
		buttonGroup_TypeJ1 = new ButtonGroup();
		btnRadioJ1_Humain = new JRadioButton("Humain");
		btnRadioJ1_Humain.setActionCommand("HUMAIN");
		btnRadioJ1_Humain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txtJoueur1.setText(saveHumainJ1);
				txtJoueur1.setEditable(true);
			}
			
		});
		setRadioTransparent(btnRadioJ1_Humain);
		buttonGroup_TypeJ1.add(btnRadioJ1_Humain);
		btnRadioJ1_Humain.setBounds(175, 170, 100, 25);
		contentPane.add(btnRadioJ1_Humain);
		btnRadioJ1_Facile = new JRadioButton("IA Facile");
		btnRadioJ1_Facile.setActionCommand("IA_FACILE");
		btnRadioJ1_Facile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton facile = (JRadioButton) e.getSource();
				txtJoueur1.setText(facile.getActionCommand());
				if (btnRadioJ2_Facile.isSelected()) {txtJoueur2.setText(txtJoueur1.getText()+"_2");}
				else if (!btnRadioJ2_Humain.isSelected()){txtJoueur2.setText(buttonGroup_TypeJ2.getSelection().getActionCommand());}
				txtJoueur1.setEditable(false);
			}
		});
		setRadioTransparent(btnRadioJ1_Facile);
		buttonGroup_TypeJ1.add(btnRadioJ1_Facile);
		btnRadioJ1_Facile.setBounds(275, 170, 100, 25);
		contentPane.add(btnRadioJ1_Facile);
		btnRadioJ1_Moyenne = new JRadioButton("IA Moyenne");
		btnRadioJ1_Moyenne.setActionCommand("IA_MOYENNE");
		btnRadioJ1_Moyenne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton moyenne = (JRadioButton) e.getSource();
				txtJoueur1.setText(moyenne.getActionCommand());
				if (btnRadioJ2_Moyenne.isSelected()) {txtJoueur2.setText(txtJoueur1.getText()+"_2");}
				else if (!btnRadioJ2_Humain.isSelected()){txtJoueur2.setText(buttonGroup_TypeJ2.getSelection().getActionCommand());}
				txtJoueur1.setEditable(false);
			}
		});
		setRadioTransparent(btnRadioJ1_Moyenne);
		buttonGroup_TypeJ1.add(btnRadioJ1_Moyenne);
		btnRadioJ1_Moyenne.setBounds(375, 170, 100, 25);
		contentPane.add(btnRadioJ1_Moyenne);
		btnRadioJ1_Difficile = new JRadioButton("IA Difficile");
		btnRadioJ1_Difficile.setActionCommand("IA_DIFFICILE");
		btnRadioJ1_Difficile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton difficile = (JRadioButton) e.getSource();
				txtJoueur1.setText(difficile.getActionCommand());
				if (btnRadioJ2_Difficile.isSelected()) {txtJoueur2.setText(txtJoueur1.getText()+"_2");}
				else if (!btnRadioJ2_Humain.isSelected()){txtJoueur2.setText(buttonGroup_TypeJ2.getSelection().getActionCommand());}
				txtJoueur1.setEditable(false);
			}
		});
		setRadioTransparent(btnRadioJ1_Difficile);
		buttonGroup_TypeJ1.add(btnRadioJ1_Difficile);
		btnRadioJ1_Difficile.setBounds(475, 170, 100, 25);
		contentPane.add(btnRadioJ1_Difficile);
		
		/////emplacement du joueur 1 au debut de la partie
		labelPositionJ1 = new JLabel("D\u00E9part :");
		labelPositionJ1.setBounds(600, 170, 50, 20);
		labelPositionJ1.setFont(font);
		contentPane.add(labelPositionJ1);
		spinner_positionJ1 = new JSpinner();
		spinner_positionJ1.setModel(new SpinnerNumberModel(posJ1, 1, map-1, 1));
		spinner_positionJ1.setBounds(660, 170, 40, 20);
		setSpinnerTransparent(spinner_positionJ1);
		contentPane.add(spinner_positionJ1);
		
		//// Options - Escrimeur Droit
		labelJ2 = new JLabel("Escrimeur Droit");
		labelJ2.setBounds(55, 207, 130, 14);
		labelJ2.setFont(font);
		contentPane.add(labelJ2);
		
		/////nom du joueur 2
		saveHumainJ2 = nomJoueur2;
		txtJoueur2 = new JTextField();
		txtJoueur2.setDocument(new JTextFieldLimit(15));
		txtJoueur2.setText(nomJoueur2);
		txtJoueur2.setColumns(10);
		txtJoueur2.setFont(font);
		txtJoueur2.setBorder(null);
		txtJoueur2.setOpaque(false);
		txtJoueur2.setBounds(175, 204, 130, 20);
		contentPane.add(txtJoueur2);
		warning_J2 = new JLabel("ATTENTION : le nom du joueur ne peut pas \u00EAtre vide");
		warning_J2.setFont(new Font("Tohama", Font.BOLD, 14));
		warning_J2.setForeground(new Color(102, 34, 0));
		warning_J2.setBounds(330, 204, 400, 14);
		warning_J2.setVisible(false);
		contentPane.add(warning_J2);
		
		/////type du joueur 2
		buttonGroup_TypeJ2 = new ButtonGroup();
		btnRadioJ2_Humain = new JRadioButton("Humain");
		btnRadioJ2_Humain.setActionCommand("HUMAIN");
		btnRadioJ2_Humain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txtJoueur2.setText(saveHumainJ2);
				txtJoueur2.setEditable(true);
			}
			
		});
		setRadioTransparent(btnRadioJ2_Humain);
		buttonGroup_TypeJ2.add(btnRadioJ2_Humain);
		btnRadioJ2_Humain.setBounds(175, 237, 100, 25);
		contentPane.add(btnRadioJ2_Humain);
		btnRadioJ2_Facile = new JRadioButton("IA Facile");
		btnRadioJ2_Facile.setActionCommand("IA_FACILE");
		btnRadioJ2_Facile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton facile = (JRadioButton) e.getSource();
				txtJoueur2.setText(facile.getActionCommand());
				if (btnRadioJ1_Facile.isSelected()) {txtJoueur2.setText(txtJoueur2.getText()+"_2");}
				txtJoueur2.setEditable(false);
			}
		});
		setRadioTransparent(btnRadioJ2_Facile);
		buttonGroup_TypeJ2.add(btnRadioJ2_Facile);
		btnRadioJ2_Facile.setBounds(275, 237, 100, 25);
		contentPane.add(btnRadioJ2_Facile);
		btnRadioJ2_Moyenne = new JRadioButton("IA Moyenne");
		btnRadioJ2_Moyenne.setActionCommand("IA_MOYENNE");
		btnRadioJ2_Moyenne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton moyenne = (JRadioButton) e.getSource();
				txtJoueur2.setText(moyenne.getActionCommand());
				if (btnRadioJ1_Moyenne.isSelected()) {txtJoueur2.setText(txtJoueur2.getText()+"_2");}
				txtJoueur2.setEditable(false);
			}
		});
		setRadioTransparent(btnRadioJ2_Moyenne);
		buttonGroup_TypeJ2.add(btnRadioJ2_Moyenne);
		btnRadioJ2_Moyenne.setBounds(375, 237, 100, 25);
		contentPane.add(btnRadioJ2_Moyenne);
		btnRadioJ2_Difficile = new JRadioButton("IA Difficile");
		btnRadioJ2_Difficile.setActionCommand("IA_DIFFICILE");
		btnRadioJ2_Difficile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton difficile = (JRadioButton) e.getSource();
				txtJoueur2.setText(difficile.getActionCommand());
				if (btnRadioJ1_Difficile.isSelected()) {txtJoueur2.setText(txtJoueur2.getText()+"_2");}
				txtJoueur2.setEditable(false);
			}
		});
		setRadioTransparent(btnRadioJ2_Difficile);
		buttonGroup_TypeJ2.add(btnRadioJ2_Difficile);
		btnRadioJ2_Difficile.setBounds(475, 237, 100, 25);
		contentPane.add(btnRadioJ2_Difficile);
		
		/////emplacement du joueur 2 au debut de la partie
		labelPositionJ2 = new JLabel("D\u00E9part :");
		labelPositionJ2.setBounds(600, 237, 50, 20);
		labelPositionJ2.setFont(font);
		contentPane.add(labelPositionJ2);
		spinner_positionJ2 = new JSpinner();
		spinner_positionJ2.setModel(new SpinnerNumberModel(posJ2, 2, map, 1));
		spinner_positionJ2.setBounds(660, 237, 40, 20);
		setSpinnerTransparent(spinner_positionJ2);
		contentPane.add(spinner_positionJ2);
		contentPane.add(labelJ1);
		
		/// Section 2 - Jeu
		titreSectionJeu = new JLabel("Jeu");
		titreSectionJeu.setFont(new Font("Century", Font.BOLD, 20));
		titreSectionJeu.setBounds(49, 278, 46, 20);
		contentPane.add(titreSectionJeu);
		
		////Option - Animations actives
		btn_animation = new JCheckBox("  Activer les animations");
		btn_animation.setBounds(50, 315, 200, 23);
		btn_animation.setOpaque(false);
		btn_animation.setFocusPainted(false);
		btn_animation.setFont(font);
		contentPane.add(btn_animation);
		
		////Option - Joueur qui commance
		labelJoueurDebut = new JLabel("Joueur qui commence :");
		labelJoueurDebut.setFont(font);
		labelJoueurDebut.setBounds(55, 350, 150, 23);
		contentPane.add(labelJoueurDebut);
		buttonGroup_JoueurDebut = new ButtonGroup();
		joueurDebut_gauche = new JRadioButton("Gauche");
		joueurDebut_gauche.setActionCommand("Gauche");
		buttonGroup_JoueurDebut.add(joueurDebut_gauche);
		setRadioTransparent(joueurDebut_gauche);
		joueurDebut_gauche.setBounds(200, 350, 75, 23);
		contentPane.add(joueurDebut_gauche);
		joueurDebut_droite = new JRadioButton("Droit");
		joueurDebut_droite.setActionCommand("Droit");
		buttonGroup_JoueurDebut.add(joueurDebut_droite);
		setRadioTransparent(joueurDebut_droite);
		joueurDebut_droite.setBounds(276, 350, 75, 23);
		contentPane.add(joueurDebut_droite);
		
		////Option - Mode d'attaque
		labelModeAttaque = new JLabel("Mode d'attaque :");
		labelModeAttaque.setFont(font);
		labelModeAttaque.setBounds(55, 385, 100, 23);
		contentPane.add(labelModeAttaque);
		buttonGroup_ModeAttaque = new ButtonGroup();
		modeAttaque_Basique = new JRadioButton("Basique");
		modeAttaque_Basique.setActionCommand("Basique");
		buttonGroup_ModeAttaque.add(modeAttaque_Basique);
		setRadioTransparent(modeAttaque_Basique);
		modeAttaque_Basique.setBounds(170, 385, 75, 23);
		contentPane.add(modeAttaque_Basique);
		modeAttaque_Avance = new JRadioButton("Avanc\u00E9");
		modeAttaque_Avance.setActionCommand("Avance");
		buttonGroup_ModeAttaque.add(modeAttaque_Avance);
		setRadioTransparent(modeAttaque_Avance);
		modeAttaque_Avance.setBounds(246, 385, 75, 23);
		contentPane.add(modeAttaque_Avance);
		
		////Option - Nombre de manches
		labelNbManches = new JLabel("Nombre de manches :");
		labelNbManches.setFont(font);
		labelNbManches.setBounds(55, 425, 130, 20);
		contentPane.add(labelNbManches);
		spinner_manches = new JSpinner();
		spinner_manches.setModel(new SpinnerNumberModel(manches, 1, 8, 1));
		spinner_manches.setBounds(200, 425, 39, 20);
		setSpinnerTransparent(spinner_manches);
		contentPane.add(spinner_manches);
		
		////Option - Nombre maximum de cartes en mains
		labelCartesMax = new JLabel("Nombre maximum de cartes en mains :");
		labelCartesMax.setFont(font);
		labelCartesMax.setBounds(55, 460, 243, 20);
		contentPane.add(labelCartesMax);
		spinner_cartesMax = new JSpinner();
		spinner_cartesMax.setModel(new SpinnerNumberModel(cartes_max, 3, 7, 1));
		spinner_cartesMax.setBounds(300, 460, 39, 20);
		setSpinnerTransparent(spinner_cartesMax);
		contentPane.add(spinner_cartesMax);
		
		////Option - Nombre de cartes par valeurs
		labelNbCartesValeurs = new JLabel("Nombre de cartes par valeurs :");
		labelNbCartesValeurs.setFont(font);
		labelNbCartesValeurs.setBounds(55, 500, 212, 14);
		contentPane.add(labelNbCartesValeurs);
		
		/////cartes de valeur 1
		labelCartes1 = new JLabel("Cartes 1  :");
		labelCartes1.setFont(font);
		labelCartes1.setBounds(55, 540, 80, 20);
		contentPane.add(labelCartes1);
		spinner_cartes1 = new JSpinner();
		spinner_cartes1.setModel(new SpinnerNumberModel(cartes1, 0, 10, 1));
		spinner_cartes1.setBounds(120, 540, 40, 20);
		setSpinnerTransparent(spinner_cartes1);
		contentPane.add(spinner_cartes1);
		
		/////cartes de valeur 2
		labelCartes2 = new JLabel("Cartes 2  :");
		labelCartes2.setFont(font);
		labelCartes2.setBounds(220, 540, 80, 20);
		contentPane.add(labelCartes2);
		spinner_cartes2 = new JSpinner();
		spinner_cartes2.setModel(new SpinnerNumberModel(cartes2, 0, 10, 1));
		spinner_cartes2.setBounds(285, 540, 40, 20);
		setSpinnerTransparent(spinner_cartes2);
		contentPane.add(spinner_cartes2);
		
		/////cartes de valeur 3
		labelCartes3 = new JLabel("Cartes 3  :");
		labelCartes3.setFont(font);
		labelCartes3.setBounds(385, 540, 80, 20);
		contentPane.add(labelCartes3);
		spinner_cartes3 = new JSpinner();
		spinner_cartes3.setModel(new SpinnerNumberModel(cartes3, 0, 10, 1));
		spinner_cartes3.setBounds(450, 540, 40, 20);
		setSpinnerTransparent(spinner_cartes3);
		contentPane.add(spinner_cartes3);
		
		/////cartes de valeur 4
		labelCartes4 = new JLabel("Cartes 4  :");
		labelCartes4.setFont(font);
		labelCartes4.setBounds(550, 540, 80, 20);
		contentPane.add(labelCartes4);
		spinner_cartes4 = new JSpinner();
		spinner_cartes4.setModel(new SpinnerNumberModel(cartes4, 0, 10, 1));
		spinner_cartes4.setBounds(615, 540, 40, 20);
		setSpinnerTransparent(spinner_cartes4);
		contentPane.add(spinner_cartes4);
		
		/////cartes de valeur 5
		labelCartes5 = new JLabel("Cartes 5 :");
		labelCartes5.setFont(font);
		labelCartes5.setBounds(725, 540, 80, 20);
		contentPane.add(labelCartes5);
		spinner_cartes5 = new JSpinner();
		spinner_cartes5.setModel(new SpinnerNumberModel(cartes5, 0, 10, 1));
		spinner_cartes5.setBounds(790, 540, 40, 20);
		setSpinnerTransparent(spinner_cartes5);
		contentPane.add(spinner_cartes5);
		
		///Boutons
		warnEnregistrement = new JLabel("Enregistrement impossible");
		warnEnregistrement.setFont(new Font("Tohama", Font.BOLD, 14));
		warnEnregistrement.setForeground(new Color(102, 34, 0));
		warnEnregistrement.setBounds(100, 600, 266, 37);
		warnEnregistrement.setVisible(false);
		contentPane.add(warnEnregistrement);
		btnSaveSettings = new ButtonCustom("Enregistrer les parametres", "cadre3", new Dimension(266, 37), new Font("Century", Font.PLAIN, 14));
		btnSaveSettings.addActionListener(new AdaptateurCommande(controle, "sauvePara", canEnregistrer));
		btnSaveSettings.setBounds(55, 600, 266, 37);
		contentPane.add(btnSaveSettings);
		
		JButton btnRestoreSettings = new ButtonCustom("Retablir les parametres","cadre3", new Dimension(266, 37), new Font("Century", Font.PLAIN, 14));
		btnRestoreSettings.addActionListener(new AdaptateurCommande(controle, "restaurePara"));
		btnRestoreSettings.setBounds(331, 600, 266, 37);
		contentPane.add(btnRestoreSettings);
		
		JButton btnPlay = new ButtonCustom("MENU", "cadre4", new Dimension(195, 37), new Font("Century", Font.PLAIN, 15));
		btnPlay.setForeground(Color.white);
		btnPlay.addActionListener(new AdaptateurCommande(controle, "menu"));
		btnPlay.setBounds(634, 600, 195, 37);
		contentPane.add(btnPlay);
		
		//setRadioButtons
		setRadioButtons(typeJ1,typeJ2,modeAttaque,animation,joueurDebut);
		
		//warningField
		warningField("j1",txtJoueur1, warning_J1, buttonGroup_TypeJ1);
		warningField("j2",txtJoueur2, warning_J2, buttonGroup_TypeJ2);
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
				setSpinnerTransparent(spinner_positionJ1);
				setSpinnerTransparent(spinner_positionJ2);
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
				setSpinnerTransparent(spinner_positionJ1);
				setSpinnerTransparent(spinner_positionJ2);
			}
		});
		
		//fenetreParametres.add(para_fond);
		fenetreParametres.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetreParametres.setBounds(100, 100, 900, 700);
		fenetreParametres.setResizable(false);
		fenetreParametres.setVisible(true);
	}

	private void setRadioTransparent(JRadioButton btn) {
		btn.setOpaque(false);
		btn.setFocusPainted(false);
		btn.setFont(new Font("Tohama", Font.BOLD, 12));
		
	}

	private void setSpinnerTransparent(JSpinner spinner) {
		spinner.setBorder(null);
		spinner.setOpaque(false);
		spinner.getEditor().setOpaque(false);
		((JSpinner.NumberEditor)spinner.getEditor()).getTextField().setOpaque(false);
		((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
		spinner.setFont(new Font("Century", Font.BOLD, 14));
		
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
			case "animation":
				if(btn_animation.isSelected()) {return "Actif";}
				else {return "NonActif";}
			case "joueurDebut":
				return buttonGroup_JoueurDebut.getSelection().getActionCommand();
			default:
				System.err.println("Parametre introuvable !");
				return "";
		}
	}
}
