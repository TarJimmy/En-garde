package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import Patterns.Observateur;
import model.Escrimeur;
import model.Plateau;

public class VueEscrimeur extends JPanel {
	
	private CollecteurEvenements controle;
	Escrimeur e;
	private Boolean showFace;
	private VueManche vueManche;
	private VueMain vueMain;
	private JButton btnPasserTour;
	public VueEscrimeur(CollecteurEvenements controle, Plateau p, Escrimeur e, Boolean showFace, int nbMancheMax) {
		this.controle = controle;
		this.e = e;
		this.showFace = showFace;
		start(nbMancheMax);
	}
	
	public void start(int nbMancheMax) {
		setOpaque(false);
		setPreferredSize(new Dimension(1000, 300));
		setLayout(new BorderLayout());
		JPanel carteGrid = new JPanel(new GridLayout());
		carteGrid.setOpaque(false);
		carteGrid.setPreferredSize(new Dimension(1000, 210));
		carteGrid.setBorder(new EmptyBorder(30, 30, 30, 30));
		//add(carteGrid, BorderLayout.WEST);
		vueMain = new VueMain(e.getCartes(), showFace);
		add(vueMain);
				
		JPanel panelBtn = new JPanel();
		panelBtn.setPreferredSize(new Dimension(1000, 74));
		panelBtn.setOpaque(false);
		
		btnPasserTour = new JButton("Passer tour");
		btnPasserTour.setPreferredSize(new Dimension(300, 70));
		System.out.println(controle.getClass());
		btnPasserTour.addActionListener(new AdaptateurCommande(controle, "PasserTour"));
		vueManche = new VueManche(nbMancheMax, e.getMancheGagner(), e.getIsGaucher());
		
		if (e.getIndice() == Escrimeur.GAUCHER) {
			panelBtn.setLayout(new FlowLayout(FlowLayout.LEFT));
			panelBtn.add(vueManche);
			panelBtn.add(btnPasserTour);
			add(panelBtn, BorderLayout.NORTH);
		} else {
			panelBtn.setLayout(new FlowLayout(FlowLayout.RIGHT));
			panelBtn.add(btnPasserTour);
			panelBtn.add(vueManche);
			add(panelBtn, BorderLayout.SOUTH);
		}
		
	}
	
	public void actualise(Boolean showFace, Boolean peutPasserTour) {
		System.out.println("Actualiser escrimeurs");
		this.showFace = showFace;
		vueManche.setNbWins(e.getMancheGagner());
		vueMain.actualise(showFace);
		btnPasserTour.setVisible(showFace && peutPasserTour);
	}
}