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
	private JButton btnOpenPanelAnnexe;
	private JButton quitterJeu;
	private JButton menu;
	
	@SuppressWarnings("serial")
	public VueInfoJeu(String nomEscrimeurGaucher, String nomEscrimeurDroitier, CollecteurEvenements controle) {
		this.nomEscrimeurGaucher = nomEscrimeurGaucher;
		this.nomEscrimeurDroitier = nomEscrimeurDroitier;
		this.controle = controle;
		setLayout(new GridLayout(3, 1));
		setOpaque(false);
		JPanel bottom = new JPanel(new GridLayout(1, 3));
		try {
			add(createLabelEscrimeur(nomEscrimeurGaucher));
			add(createLabelEscrimeur(nomEscrimeurDroitier));
			add(bottom);
			btnOpenPanelAnnexe = new JButton(new ImageIcon(ImageIO.read(Configuration.charge("cadre.png", Configuration.MENU)))) {
				
				@Override
				protected void paintComponent(Graphics g) {
					g.setFont(new Font("Century", Font.PLAIN, 40));
					g.drawString("Ouvrir la fene^tre de triche", 140, getHeight() / 2 + 10);
				}
				
			};
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		btnOpenPanelAnnexe = Button("Actions Annexes");
		btnOpenPanelAnnexe.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				InterfaceGraphiqueActionAnnexe.demarrer(controle);
			}
		});
		
		menu = Button("Retour au menu");
		menu.addActionListener(new AdaptateurCommande(controle, "Menu"));
		
		quitterJeu = Button("Quitter le jeu");
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
		JLabel label = new JLabel(new ImageIcon(ImageIO.read(Configuration.charge("cadre2.png", Configuration.MENU)))) {
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setFont(new Font("Century", Font.PLAIN, 40));
				g.drawString(nom, 140, getHeight() / 2 + 10);
			}
			
		};
		label.setOpaque(false);
		
		return label;
	}

	private JButton Button (String name) {
		JButton button;
		ImageIcon banner;
		
		try {
			switch (name){
				case "Actions Annexes":
					banner = new ImageIcon(new ImageIcon(ImageIO.read(Configuration.charge("cadre4.png", Configuration.MENU))).getImage().getScaledInstance(190, 40, Image.SCALE_SMOOTH));
					button = new JButton(name, banner);
					button.setFont(new Font("Century", Font.PLAIN, 15));
					button.setForeground(Color.WHITE);
					button.addMouseListener(new AdaptateurBouton(controle, "cadre4", button, 190));
					break;
				default:
					banner = new ImageIcon(new ImageIcon(ImageIO.read(Configuration.charge("cadre3.png", Configuration.MENU))).getImage().getScaledInstance(190, 40, Image.SCALE_SMOOTH));
					button = new JButton(name, banner);
					button.setFont(new Font("Century", Font.PLAIN, 15));
					button.addMouseListener(new AdaptateurBouton(controle, "cadre3", button, 190));
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
}
