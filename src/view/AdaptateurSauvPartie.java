package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurSauvPartie implements ActionListener {
	
	private CollecteurEvenements control;
	private int id;
	private String choix;
	
	AdaptateurSauvPartie(CollecteurEvenements controle, int id, String choix) {
		control = controle;
		this.id = id;
		this.choix = choix;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(choix.equals("Charger")) {control.chargerPartie(id);}
		else if (choix.equals("Supprimer")) {control.supprimerPartie(id);}
	}
}
