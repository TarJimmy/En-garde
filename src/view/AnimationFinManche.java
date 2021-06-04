package view;

import view.InterfaceGraphiqueJeu.PanelAnimation;

public class AnimationFinManche extends Animation {

	int depart, distance;
	PanelAnimation panelAnimation;
	AnimationFinManche(CollecteurEvenements collecteur, Animateur animateur, int depart, int distance) {
		super(collecteur, animateur, Animation.ANIM_FIN_MANCHE, 5, 0.005f);
		System.out.println("vgtfrd");
		this.panelAnimation = (PanelAnimation)animateur;
		this.distance = distance;
	}

	@Override
	public void anim(double progres) {
		if (progres > 0.4999d && progres < 0.5001d) {
			timer.stop();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			timer.start();
		}
		int distanceAjouter = (int) Math.round((progres * distance));
		int newX = (int) Math.round(depart + distanceAjouter);
		panelAnimation.deplaceFinDeManche(newX);
	}

}
