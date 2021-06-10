package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurSupprimerPartie implements ActionListener {
	private CollecteurEvenements control;
	private int id;
	
	AdaptateurSupprimerPartie(CollecteurEvenements controle, int id) {
		control = controle;
		this.id = id;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		InterfaceGraphiqueChargerPartie.close();
		control.supprimerPartie(id);
	}
}
