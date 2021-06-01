package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;

public class AdaptateurCase extends MouseAdapter {
	private CollecteurEvenements controle;
	private int nbCase;
	private VuePlateau vuePlateau;
	private HashSet<Integer> caseClickable;
	
	public AdaptateurCase(CollecteurEvenements controle, int nbCase, HashSet<Integer> caseClickable, VuePlateau vuePlateau)  {
		this.controle = controle;
		this.nbCase = nbCase;
		this.vuePlateau = vuePlateau;
		this.caseClickable = caseClickable;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mousePressed(e);
		int espaceCase = vuePlateau.getWidth() / nbCase;
		int x = e.getX() / espaceCase + 1;
		
		if (caseClickable.contains(x)) {
			controle.clickCase(x);
		}
	}
}
