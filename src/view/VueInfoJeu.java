package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Global.Configuration;

public class VueInfoJeu extends JPanel {
	
	private String nomEscrimeurGaucher;
	private String nomEscrimeurDroitier;
	private CollecteurEvenements controle;
	private ButtonCustom btnOpenPanelAnnexe;
	private ButtonCustom quitterJeu;
	private ButtonCustom menu;
	
	@SuppressWarnings("serial")
	public VueInfoJeu(String nomEscrimeurGaucher, String nomEscrimeurDroitier, CollecteurEvenements controle) {
		this.nomEscrimeurGaucher = nomEscrimeurGaucher;
		this.nomEscrimeurDroitier = nomEscrimeurDroitier;
		this.controle = controle;
		setLayout(new GridLayout(3, 1));
		setOpaque(false);
		JPanel bottom = new JPanel(new GridLayout(1, 3));
		Dimension dimensionBtn = new Dimension(190, 40);
		Font fontBtn = new Font(Configuration.Century.getFamily(), Font.PLAIN, 15);
		try {
			add(createLabelEscrimeur(nomEscrimeurGaucher));
			add(createLabelEscrimeur(nomEscrimeurDroitier));
			add(bottom);
			btnOpenPanelAnnexe = new ButtonCustom("Actions Annexes", "cadre4", dimensionBtn, fontBtn);
			btnOpenPanelAnnexe.setForeground(Color.white);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		btnOpenPanelAnnexe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				InterfaceGraphiqueActionAnnexe.demarrer(controle);
			}
		});
		
		menu = new ButtonCustom("Retour au menu", "cadre3", dimensionBtn, fontBtn);
		menu.addActionListener(new AdaptateurCommande(controle, "Menu"));
		
		quitterJeu = new ButtonCustom("Quitter le jeu", "cadre3", dimensionBtn, fontBtn);
		quitterJeu.addActionListener(new AdaptateurCommande(controle, "QuitterJeu"));
		
		bottom.add(btnOpenPanelAnnexe);
		bottom.add(menu);
		bottom.add(quitterJeu);
		bottom.setOpaque(false);
		setBackground(Color.red);
		setPreferredSize(new Dimension(600, 300));
		repaint();
	}

	public JLabel createLabelEscrimeur(String nom) throws IOException {
		@SuppressWarnings("serial")
		JLabel label = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge("cadre2.png", Configuration.MENU)))) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setFont(new Font(Configuration.Century.getFamily(), Font.PLAIN, 40));
				g.drawString(nom, 140, getHeight() / 2 + 10);
			}
		};
		label.setOpaque(false);
		
		return label;
	}
}
