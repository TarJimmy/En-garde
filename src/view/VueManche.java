package view;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class VueManche extends JComponent {
	Graphics2D drawable;
	int nbManches;
	int nbWins;
	private int largeurCase, hauteurCase;
	
	VueManche(int nbManches,int nbWins) {
		this.nbManches = nbManches;
		this.nbWins = nbWins;
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		drawable = (Graphics2D) g;
		drawable.setStroke(new BasicStroke(6));
		
		int largeur  = getWidth();
		int hauteur = getHeight();
		// On efface tout
		drawable.clearRect(0, 0, largeur, hauteur);
		tracerManches();
	}
	
	public void setNbWins(int nbWins) {
		this.nbWins = nbWins;
		repaint();
	}
	
	public void tracerManches() {
		largeurCase = getWidth() ;
		hauteurCase = getHeight() / nbManches;
		// On prend le minimum
		largeurCase = Math.min(largeurCase, hauteurCase);		
		int x = 0;
		
		for (int i = 0; i < nbManches; i++) {
			int y = i * largeurCase;
			
			if (nbWins <= i) {
				drawable.fillRect(x, y, largeurCase, largeurCase);
			}
			drawable.drawRect(x, y, largeurCase, largeurCase);
			
		}
	}
}
