package view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Patterns.Observateur;

public class InterfaceGraphiqueJeu implements Runnable, Observateur {
	
	private JFrame frame;
	CollecteurEvenements controle;
	
	private InterfaceGraphiqueJeu(CollecteurEvenements controle) {
		this.controle = controle;
	}
	
	public static void demarrer(CollecteurEvenements control) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueJeu(control));
	}
	
	@Override
	public void miseAJour() {
		// TODO Auto-generated method stub
	}
	
	public void run() {
		frame = new JFrame("En Garde !");
	}
}
