package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurCommande implements ActionListener {

	private CollecteurEvenements control;
	private String commande;
	private Boolean canEnregistrer;
	
	AdaptateurCommande(CollecteurEvenements controle, String cmd) {
		control = controle;
		commande = cmd;
	}
	
	AdaptateurCommande(CollecteurEvenements controle, String cmd, Boolean canEnregistrer) {
		control = controle;
		commande = cmd;
		this.canEnregistrer = canEnregistrer;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (commande == "sauvePara" && canEnregistrer) {
			control.commande(commande);		
		} else {
			control.commande(commande);	
		}
		
	}

}
