package view;

import java.util.ArrayList;

public interface CollecteurEvenements {
	boolean commande(String c);
	boolean clickSouris(String s, int l, int c);
	boolean selectTypeCarte(String c);
	boolean jouerCartes(ArrayList<Integer> carteAAjouer);
	boolean clickCase(int x);
}
