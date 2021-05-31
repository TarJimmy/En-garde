package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Patterns.Observateur;
import model.Escrimeur;
import model.Plateau;

public class VueEscrimeur extends JPanel implements Observateur {
	
	Escrimeur e;
	private Boolean showFace;
	private VueManche vueManche;
	private VueMain vueMain;
	public VueEscrimeur(Plateau p, Escrimeur e, Boolean showFace) {
		this.e = e;
		this.showFace = showFace;
		this.e.ajouteObservateur(this);
		start();
	}
	
	public void start() {
		setOpaque(false);
		setPreferredSize(new Dimension(1000, 280));
		setLayout(new BorderLayout());
		JPanel carteGrid = new JPanel(new GridLayout());
		carteGrid.setOpaque(false);
		carteGrid.setPreferredSize(new Dimension(1000, 210));
		carteGrid.setBorder(new EmptyBorder(30, 30, 30, 30));
		//add(carteGrid, BorderLayout.WEST);
		vueMain = new VueMain(e.getCartes());
		
		vueManche = new VueManche(5, e.getMancheGagner(), e.getIsGaucher());
		
		add(vueManche, (e.getIsGaucher() ? BorderLayout.NORTH : BorderLayout.SOUTH));
		add(vueMain);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		/*
		int distance;
		for (int i = 0; i < nbCartesMax; i++) {
			distance = e.getCarte(i).getDistance();
			setCarte(i, distance);
			cartes[i].repaintCarte(distance, showFace);
		}
		*/
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