package view;

import view.InterfaceGraphiqueJeu.PanelAnimation;

public class AnimationFinManche extends Animation {

	int depart, distance;
	PanelAnimation panelAnimation;
	AnimationFinManche(CollecteurEvenements collecteur, Animateur animateur, int depart, int distance) {
		super(collecteur, animateur, Animation.ANIM_FIN_MANCHE, 100, 0.05f);
		this.panelAnimation = (PanelAnimation)animateur;
		this.distance = distance;
	}

	@Override
	public void anim(double progres) {
		if (progres > 0.48d && progres < 0.52d) {
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
