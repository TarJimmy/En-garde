package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurChargerPartie implements ActionListener {
	
	private CollecteurEvenements control;
	private int id;
	
	AdaptateurChargerPartie(CollecteurEvenements controle, int id) {
		control = controle;
		this.id = id;
	}


	
	@Override
	public void actionPerformed(ActionEvent e) {
		control.chargerPartie(id);
	}
}
