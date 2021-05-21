package view;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Patterns.Observateur;
import model.Carte;
import model.Escrimeur;
import model.Plateau;

public class VueEscrimeur extends JPanel implements Observateur {
	
	private Graphics2D drawable;
	VueCarte[] cartes;
	Escrimeur e;
	Plateau plateau;
	private int nbCartesMax;
	private Boolean showFace;
	
	public VueEscrimeur(Plateau p, Escrimeur e, Boolean showFace) {
		this.plateau = p;
		this.e = e;
		this.showFace = showFace;
		start();
	}
	
	public void start() {
		
		nbCartesMax = e.getCartes().length;
		cartes = new VueCarte[e.getCartes().length];
		this.setLayout(new BorderLayout());
		JPanel carteGrid = new JPanel(new GridLayout());
		carteGrid.setPreferredSize(new Dimension(600, 220));
		add(carteGrid, BorderLayout.WEST);
		
		JLabel texte = new JLabel("Joueur : " + e.getNom());
		texte.setFont(new Font(texte.getName(), Font.PLAIN, 30));
        add(texte, BorderLayout.NORTH);
        
		for (int i = 0; i < nbCartesMax; i++) {
			setCarte(i, e.getCarte(i).getDistance());
			carteGrid.add(cartes[i]);
		}
		
		Box box = Box.createVerticalBox();
		add(box, BorderLayout.EAST);
	}
	
	public void setCarte(int i, int distance) {
		cartes[i] = new VueCarte(showFace, distance);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		drawable = (Graphics2D) g;
		//g..setStroke(new BasicStroke(6));
		
		int largeur = getWidth();
		int hauteur = getHeight();
		
		// On efface tout
		drawable.clearRect(0, 0, largeur, hauteur);
		
		int distance;
		for (int i = 0; i < nbCartesMax; i++) {
			distance = e.getCarte(i).getDistance();
			setCarte(i, distance);
			cartes[i].repaintCarte(distance, showFace);
		}
		drawable.drawRect(0, 0, largeur, hauteur);
	}
	
	public void setShowFace(Boolean showFace) {
		this.showFace = showFace;
	}
	
	@Override
	public void miseAJour() {
		// TODO Auto-generated method stub
		repaint();
	}
}