package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import view.InterfaceGraphiqueJeu.PanelAnimation;

public class AnimationFinManche extends Animation {

	int depart, distance;
	PanelAnimation panelAnimation;
	Timer timerPause;
	final ActionListener listenerPause = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			timerPause.stop();
			timer.restart();
			System.out.println("cocuou");
		}
	};
	AnimationFinManche(CollecteurEvenements collecteur, Animateur animateur, int depart, int distance) {
		super(collecteur, animateur, Animation.ANIM_FIN_MANCHE, 5, 0.005f);
		this.depart = depart;
		this.panelAnimation = (PanelAnimation)animateur;
		this.distance = distance;
	}

	@Override
	public void anim(double progres) {
		if (progres == 0.5) {
			timer.stop();
			super.progres += 0.005f;
			timerPause = new Timer(2000, listenerPause); 
			timerPause.setRepeats(false);
		    timerPause.start();
		}
		int distanceAjouter = (int) Math.round((progres * distance));
		int newX = (int) Math.round(depart + distanceAjouter);
		panelAnimation.deplaceFinDeManche(newX);
	}

}
