package view;

import model.IA;
import view.InterfaceGraphiqueJeu.PanelAnimation;

public class AnimationIAJoue extends Animation {

	IA Ia;
	PanelAnimation panelAnimation;
	
	AnimationIAJoue(CollecteurEvenements collecteur, Animateur animateur, IA Ia) {
		super(collecteur, animateur, Animation.IA_JOUE_COUP, 1000);
		this.Ia = Ia;
		timer.setRepeats(false);
		this.panelAnimation = (PanelAnimation)animateur;
		System.out.println("IA Demarre");
		demarre();
	}

	@Override
	public void anim(double progres) {
		int[] coupAJouer = Ia.getChoixCoup();
		System.out.println("Ia choix coup");
		panelAnimation.joueCoup(coupAJouer[0], coupAJouer[1]);
	}

}
