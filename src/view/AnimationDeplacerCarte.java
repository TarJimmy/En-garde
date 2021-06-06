package view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import view.InterfaceGraphiqueJeu.PanelAnimation;

public class AnimationDeplacerCarte extends Animation {
	private Point[] departs;
	private Point[] distances;
	private PanelAnimation panelAnimation;
	private int indiceEscrimeur;
	AnimationDeplacerCarte(CollecteurEvenements collecteur, Animateur animateur, Point[] departs, Point[] distances, int indiceEscrimeur) {
		super(collecteur, animateur, Animation.ANIM_CARTES);
		this.departs = departs;
		this.distances = distances;
		this.panelAnimation = (PanelAnimation)animateur;
		this.indiceEscrimeur = indiceEscrimeur;
	}

	@Override
	public void anim(double progres) {
		//int distanceAjouter = (int) Math.round((progres * distance));
		Point[] newPoint = new Point[departs.length];
		for (int i = 0; i < departs.length; i++) {
			Point depart = departs[i];
			Point distance = distances[i];
			newPoint[i] = new Point((int)(depart.x + distance.x * progres), (int)(depart.y + distance.y * progres));
		}
		panelAnimation.deplaceCartes(newPoint, indiceEscrimeur);
	}

}
