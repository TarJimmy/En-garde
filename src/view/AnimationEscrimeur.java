package view;

import javax.swing.*;

import model.Escrimeur;


public class AnimationEscrimeur extends Animation {
	private VuePlateau vuePlateau;
	private int distance, depart;
	
	private int indiceEcrimeursCourant;
	
	public AnimationEscrimeur(CollecteurEvenements collecteur,  Animateur animateur, int depart, int distance, int indiceEcrimeursCourant) {
		super(collecteur, animateur, Animation.ANIM_ESCRIMEUR);
		this.vuePlateau = (VuePlateau)animateur;
		this.depart = depart;
		this.distance = distance;
		this.indiceEcrimeursCourant = indiceEcrimeursCourant;
	}
	
	@Override
	public void anim(double progres) {
		int distanceAjouter = (int) Math.round((progres * distance));
		int newX = (int) Math.round(depart + distanceAjouter);
		vuePlateau.deplace(newX, indiceEcrimeursCourant);
	}
}
