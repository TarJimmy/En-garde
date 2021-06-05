package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public abstract class Animation implements ActionListener {
	public static final int ANIM_NONE = 0;
	public static final int ANIM_CARTES = 1;
	public static final int ANIM_FIN_MANCHE = 2;
	public static final int ANIM_ESCRIMEUR = 3;
	
	protected Timer timer;
	protected CollecteurEvenements collecteur;
	protected double progres, vitesseAnim;;
	protected Animateur animateur;
	protected int typeAnimation;
	/**
	 * 
	 * @param collecteur
	 * @param vitesseAnim, entre 0 et 1, correspond le pas d'avancement jusqu'à 1 (= fin animation)
	 */
	Animation(CollecteurEvenements collecteur, Animateur animateur, int typeAnimation) {
		this.collecteur = collecteur;
		this.vitesseAnim = 0.02d;
		this.progres = 0;
		this.animateur = animateur;
		this.typeAnimation = typeAnimation;
		timer = new Timer(10, this);
	}
	
	Animation(CollecteurEvenements collecteur, Animateur animateur, int typeAnimation, int delay) {
		this.collecteur = collecteur;
		this.vitesseAnim = 0.1d;
		this.progres = 0;
		this.animateur = animateur;
		this.typeAnimation = typeAnimation;
		timer = new Timer(delay, this);
	}
	
	Animation(CollecteurEvenements collecteur, Animateur animateur, int typeAnimation, int delay, float vitesseAnim) {
		this.collecteur = collecteur;
		this.vitesseAnim = vitesseAnim;
		this.progres = 0;
		this.animateur = animateur;
		this.typeAnimation = typeAnimation;
		timer = new Timer(delay, this);
	}

	/**
	 * Correspond à l'animation, définit par les fils
	 * @param int progres correspond à l'état courant du progrès
	 */
	public abstract void anim(double progres);
	
	public void demarre() {
		timer.start();
		animateur.debutAnimation(typeAnimation);
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
		anim(Math.round(progres*1000.0)/1000.0);
	}


	
}

