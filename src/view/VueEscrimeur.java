package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import Global.Configuration;
import Patterns.Observateur;
import model.Carte;
import model.Escrimeur;
import model.Plateau;

public class VueEscrimeur extends JPanel {
	
	private CollecteurEvenements controle;
	Escrimeur e;
	private VueManche vueManche;
	private VueMain vueMain;
	private JButton btnPasserTour;
	public VueEscrimeur(CollecteurEvenements controle, Plateau p, Escrimeur e, Boolean showFace, int nbMancheMax) {
		this.controle = controle;
		this.e = e;
		start(nbMancheMax, showFace);
	}
	
	public void start(int nbMancheMax, Boolean showFace) {
		setOpaque(false);
		setPreferredSize(new Dimension(1000, 300));
		setLayout(new BorderLayout());
		JPanel carteGrid = new JPanel(new GridLayout());
		carteGrid.setOpaque(false);
		carteGrid.setBorder(new EmptyBorder(30, 30, 30, 30));
		//add(carteGrid, BorderLayout.WEST);
		vueMain = new VueMain(e.getNbCartes(), showFace);
		add(vueMain);
				
		JPanel panelMancheBtn = new JPanel();
		panelMancheBtn.setPreferredSize(new Dimension(1000, 74));
		panelMancheBtn.setOpaque(false);
		
		btnPasserTour = new ButtonCustom("Passer tour", "cadre", new Dimension(290, 60), new Font(Configuration.Century.getFamily(), Font.PLAIN, 20));
		btnPasserTour.setVerticalTextPosition(SwingConstants.CENTER);
		btnPasserTour.addActionListener(new AdaptateurCommande(controle, "PasserTour"));
		btnPasserTour.setVisible(false);
		vueManche = new VueManche(nbMancheMax, e.getMancheGagner(), e.getIsGaucher());
		
		if (e.getIndice() == Escrimeur.GAUCHER) {
			panelMancheBtn.setLayout(new FlowLayout(FlowLayout.LEFT));
			panelMancheBtn.add(vueManche);
			panelMancheBtn.add(btnPasserTour);
			add(panelMancheBtn, BorderLayout.NORTH);
		} else {
			panelMancheBtn.setLayout(new FlowLayout(FlowLayout.RIGHT));
			panelMancheBtn.add(btnPasserTour);
			panelMancheBtn.add(vueManche);
			add(panelMancheBtn, BorderLayout.SOUTH);
		}
		
	}
	
	public void actualise(Boolean showFace, Boolean peutPasserTour) {
		vueManche.setNbWins(e.getMancheGagner());
		vueMain.actualise(showFace);
		btnPasserTour.setVisible(showFace && peutPasserTour);
	}
	
	public void actualise(Carte[] cartes, Boolean showFace, Boolean peutPasserTour) {
		vueManche.setNbWins(e.getMancheGagner());
		vueMain.actualise(cartes, showFace);
		btnPasserTour.setVisible(showFace && peutPasserTour);
	}
	
	public VueMain getVueMain() {
		return vueMain;
	}
	
	public Point[] extractPosCarte(ArrayList<Integer> points) {
		return vueMain.extractPosCarte(points, (e.getIndice() == Escrimeur.GAUCHER ? 74 : 0));
	}
	
	public boolean getShowFace() {
		return vueMain.getShowFace();
	}
	
	public void setShowFace(boolean showFace) {
		vueMain.actualise(showFace);
	}
}