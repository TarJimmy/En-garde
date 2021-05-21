package model;

public class Escrimeur {
	private String nom;
	private TypeEscrimeur type;
	private Boolean isGaucher;
	private Carte[] cartes;
	private int nbCartes;
	
	public Escrimeur(String nom, TypeEscrimeur type, Boolean isGaucher, int nbCarte) {
		setNom(nom);
		this.type = type;
		this.isGaucher = isGaucher;
		this.cartes = new Carte[nbCarte];
		this.nbCartes = nbCarte;
	}
	
	public int getNbCartes() {
		return this.nbCartes;
	}
	
	public boolean manqueCarte() {
		int i = 0;
		while (i < nbCartes && cartes[i] != null) {
			i++;
		}
		return i < nbCartes;
	}
	
	private void setNom(String nom) {
		if (nom == null || nom.equals("")) {
			this.nom = "null";
		} else {
			this.nom = nom;
		}
	}
	
	public Boolean ajouterCarte(Carte c) {
		for (int i = 0; i < cartes.length; i++) {
			if (cartes[i] == null) {
				cartes[i] = c;
				return true;
			}
		}
		return false;
	}
	
	public Boolean ajouterCarteAIndiceVide(Carte c, int indice) {
		if (cartes[indice] == null) {
			cartes[indice] = c;
			return true;
		}
		return false;
	}
	
	public int supprimerCarte(Carte c) {
		for (int i = 0; i < cartes.length; i++) {
			if (cartes[i] == c) {
				for (int j = i; j < cartes.length - 1; j++) {
					cartes[j] = cartes [j+1];
				}
				cartes[cartes.length - 1] = null;
				return i;
			}
		}
		return -1;
	}
	
	public Carte[] getCartes() {
		return cartes;
	}
	
	public Carte getCarte(int i) {
		return cartes[i];
	}

	public String getNom() {
		return nom;
	}

	public TypeEscrimeur getType() {
		return type;
	}

	public Boolean getIsGaucher() {
		return isGaucher;
	}
}
