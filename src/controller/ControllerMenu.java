package controller;

import java.io.File;

import view.InterfaceGraphiqueJeu;
import view.InterfaceGraphiqueParametres;
import view.InterfaceGraphiqueRegles;

public class ControllerMenu extends Controler {
	
	public ControllerMenu() {
		super();
	}
	
	@Override
	public boolean commande(String c) {
		switch(c) {
			case "play":
			case "chargeGame":
				//InterfaceGraphiqueJeu.demarrer(new ControlerJeu());
				break;
			case "settings":
				//InterfaceGraphiqueParametres.demarrer(new ControlerJeu());
				break;
			case "saveSettings":
				break;
			case "restoreSettings":
				break;
			case "rules":
				InterfaceGraphiqueRegles.demarrer();
				break;
			case "exit":
				System.exit(0);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + c);
		}
		return false;
	}
}
