package view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import view.InterfaceGraphiqueJeu.PanelAnimation;

public class AnimationCarte extends Animation {
	private List<Point> departs;
	private List<Point> distances;
	private PanelAnimation panelAnimation;
	AnimationCarte(CollecteurEvenements collecteur, Animateur animateur, List<Point> departs, List<Point> distances) {
		super(collecteur, animateur, Animation.ANIM_CARTES);
		if (departs.size() != distances.size()) {
			throw new IllegalArgumentException("La taille des départs et destinations doivent être égal");
		}
		this.departs = departs;
		this.distances = distances;
		this.panelAnimation = (PanelAnimation)animateur;
	}

	@Override
	public void anim(double progres) {
		//int distanceAjouter = (int) Math.round((progres * distance));
		ArrayList<Point> newPoint = new ArrayList<>();
		Iterator<Point> itDepart = departs.iterator();
		Iterator<Point> itDistance = distances.iterator();
		while(itDepart.hasNext()) {
			Point depart = itDepart.next();
			Point distance = itDistance.next();
			newPoint.add(new Point((int)(depart.x + distance.x * progres), (int)(depart.y + distance.y * progres)));
		}
		panelAnimation.deplaceCartes(newPoint);
	}

}
