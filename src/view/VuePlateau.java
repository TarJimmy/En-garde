package view;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import model.Plateau;


public class VuePlateau extends JComponent {

	public static final String IMAGECASEPATH = "../Images/Case";
	
	private Graphics2D drawable;
	private int NBCases;
	private Image[] imageCases;
	private Plateau p;
	private Image ImageEscr1;
	private Image ImageEscr2;
	
	VuePlateau(Plateau p) {
		this.p = p;
		this.NBCases = p.getNbCase();
		imageCases = new Image[NBCases];
		for (int i = 0; i < NBCases; i++) {
			// Need to create an inputstream
			//imageCases[i] = ImageIO.read(IMAGECASEPATH);
		}

	}
	
	@Override
	public void paintComponent(Graphics g) {
		drawable = (Graphics2D) g;
		//g..setStroke(new BasicStroke(6));
		
		int largeur = getWidth();
		int hauteur = getHeight();
		System.out.println(largeur + " " + largeur);
		// On efface tout
		drawable.clearRect(0, 0, largeur, hauteur);
		tracerPlateau();
	}
	
	void tracerPlateau() {
		int WidthCase = getWidth()/(NBCases);
		int HeightCase = (int)(getHeight() * 0.8);
		int HeigthPoint = (int)(getHeight() * 0.1);
		
		int PositionJ1 = p.getPositionGaucher();
		int PositionJ2 = p.getPositionDroitier();
				
		
		for (int i = 0; i < NBCases; i++) {
			int WidthPoint = (getWidth()/NBCases)*i;
			drawable.drawRect(WidthPoint, HeigthPoint, WidthCase, HeightCase);
			//drawable.drawImage(imageCases[i],WidthPoint, HeigthPoint, WidthCase, HeightCase,null);
			
			if(i == PositionJ1 - 1) {
				drawable.setStroke(new BasicStroke(6));
				drawable.drawRect(WidthPoint, HeigthPoint, WidthCase/2, HeightCase/2);
				drawable.setStroke(new BasicStroke(2));
				//drawable.drawImage(ImageEscr1,WidthPoint, HeigthPoint, WidthCase, HeightCase,null);
			} else if (i == PositionJ2 - 1) {
				drawable.setStroke(new BasicStroke(6));
				drawable.drawRect(WidthPoint, HeigthPoint, WidthCase/2, HeightCase/2);
				drawable.setStroke(new BasicStroke(2));
				//drawable.drawImage(ImageEscr2,WidthPoint, HeigthPoint, WidthCase, HeightCase,null);
			}
		}
		drawable.drawRect(0, 0, getWidth(), getHeight());
	}
	
	
}
