package view;

import java.util.ArrayList;

public interface CollecteurEvenements {
	boolean commande(String c);
	boolean clickCase(int x, int nbCarte);
	boolean animation(String typeAnimation, Animation anim);
	boolean chargerPartie(int id);
	boolean supprimerPartie(int id);
}
