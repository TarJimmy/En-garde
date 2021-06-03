package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public abstract class Animation implements ActionListener {
	protected Timer timer;
	protected CollecteurEvenements collecteur;
	protected double progres, vitesseAnim;;
	protected Animateur animateur;
	/**
	 * 
	 * @param collecteur
	 * @param vitesseAnim, entre 0 et 1, correspond le pas d'avancement jusqu'à 1 (= fin animation)
	 */
	Animation(CollecteurEvenements collecteur, Animateur animateur, float vitesseAnim) {
		this.collecteur = collecteur;
		this.vitesseAnim = vitesseAnim;
		this.progres = 0;
		this.animateur = animateur;
		timer = new Timer(10, this);
	}
	
	Animation(CollecteurEvenements collecteur, Animateur animateur) {
		this.collecteur = collecteur;
		this.vitesseAnim = 0.1d;
		this.progres = 0;
		this.animateur = animateur;
		timer = new Timer(10, this);
	}

	/**
	 * Correspond à l'animation, définit par les fils
	 * @param int progres correspond à l'état courant du progrès
	 */
	public abstract void anim(double progres);
	
	public void demarre() {
		timer.start();
		progres = 0;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		progres += vitesseAnim;
		if (progres > 1) {
			progres = 1;
			timer.stop();
			animateur.finAnimation(this);
		}
		anim(progres);
	}


	
}

