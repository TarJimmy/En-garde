package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdaptateurMouse extends MouseAdapter {
	private String commande;
	private int indice;
	CollecteurEvenements control;
	
	public AdaptateurMouse(CollecteurEvenements controller, String commande, int indice) {
		this.commande = commande;
		this.control = controller; 
		this.indice = indice;
	}
	
	public AdaptateurMouse(CollecteurEvenements controller, String commande) {
		this.commande = commande;
		this.control = controller; 
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		control.selectTypeCarte(commande);
	}

}
