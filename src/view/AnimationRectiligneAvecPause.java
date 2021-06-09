package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import view.InterfaceGraphiqueJeu.PanelAnimation;

public class AnimationRectiligneAvecPause extends Animation {

	int depart, distance;
	PanelAnimation panelAnimation;
	Timer timerPause;
	private int delayPause;
	final ActionListener listenerPause = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			timerPause.stop();
			timer.restart();
		}
	};
	AnimationRectiligneAvecPause(CollecteurEvenements collecteur, Animateur animateur, int depart, int distance, int typeAnimation) {
		super(collecteur, animateur, typeAnimation, 5, 0.005f);
		this.depart = depart;
		this.panelAnimation = (PanelAnimation)animateur;
		this.distance = distance;
		this.delayPause = 2000;
	}
	
	AnimationRectiligneAvecPause(CollecteurEvenements collecteur, Animateur animateur, int depart, int distance, int typeAnimation, int delayPause) {
		super(collecteur, animateur, typeAnimation, 5, 0.005f);
		this.depart = depart;
		this.panelAnimation = (PanelAnimation)animateur;
		this.distance = distance;
		this.delayPause = delayPause;
	}
	
	AnimationRectiligneAvecPause(CollecteurEvenements collecteur, Animateur animateur, int depart, int distance, int typeAnimation, int delayPause, float vitesse) {
		super(collecteur, animateur, typeAnimation, 5, vitesse);
		this.depart = depart;
		this.panelAnimation = (PanelAnimation)animateur;
		this.distance = distance;
		this.delayPause = delayPause;
	}

	@Override
	public void anim(double progres) {
		if (progres == 0.5) {
			timer.stop();
			super.progres += 0.005f;
			timerPause = new Timer(delayPause, listenerPause); 
			timerPause.setRepeats(false);
		    timerPause.start();
		}
		int distanceAjouter = (int) Math.round((progres * distance));
		int newX = (int) Math.round(depart + distanceAjouter);
		panelAnimation.deplacePanel(newX);
	}

}
