package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurCommande implements ActionListener {

	private CollecteurEvenements control;
	private String commande;
	
	AdaptateurCommande(CollecteurEvenements controle, String cmd) {
		control = controle;
		commande = cmd;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		control.commande(commande);
	}

}
