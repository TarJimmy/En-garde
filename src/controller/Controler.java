package controller;

import java.util.ArrayList;

import view.Animation;
import view.CollecteurEvenements;

public abstract class Controler implements CollecteurEvenements {


	@Override
	public boolean animation(String typeAnimation, Animation anim) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean commande(String c) {
		// TODO Auto-generated method stub
		System.out.println("commande not found");
		return false;
	}

	@Override
	public boolean clickCase(int x) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
