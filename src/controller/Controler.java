package controller;

import java.util.ArrayList;

import view.CollecteurEvenements;

public abstract class Controler implements CollecteurEvenements {

	@Override
	public boolean commande(String c) {
		// TODO Auto-generated method stub
		System.out.println("commande not found");
		return false;
	}

	@Override
	public boolean clickSouris(String s, int l, int c) {
		// TODO Auto-generated method stub
		System.out.println("clickSouris not found");
		return false;
	}

	@Override
	public boolean jouerCartes(ArrayList<Integer> cartesAjouer) {
		System.out.println("jouerCartes not found");
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean selectTypeCarte(String c) {
		System.out.println("selectCarte not found");
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean clickCase(int x) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
