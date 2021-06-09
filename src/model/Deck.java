package model;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;

public abstract class Deck {

	protected Stack<Carte> cartes;
	
	Deck(Carte[] cartes) {
		this.cartes = new Stack<Carte>();
		for (Carte c : cartes) {
			this.cartes.push(c);
		}
	}
	
	Deck() {
		this.cartes = new Stack<Carte>();
	}
	
	public boolean deckVide() {
		return cartes.empty();
	}
	
	public int nbCartes() {
		return cartes.size();
	}
	
	public void addCarte(Carte c) {
		cartes.push(c);
	}
	
	public int[] getArray() {
		Object[] arrayCartes = cartes.toArray();
		int [] res = new int[arrayCartes.length];
		for (int i = 0; i < arrayCartes.length; i++) {
			res[i] = ((Carte) arrayCartes[i]).getDistance();
		}
		return res;
	}
	
	public void setDeck(JSONArray CartesInt) {
		int i = 0;
		while (!CartesInt.isNull(i)) {
			
			try {
				cartes.add(new Carte(CartesInt.getInt(i)));
			} catch (IncorrectCarteException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
	}
	
	public abstract Deck copySimple();
}
