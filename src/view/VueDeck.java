package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.border.LineBorder;

import Global.Configuration;
import Patterns.Observateur;
import model.Carte;
import model.Deck;
import model.DeckDefausse;
import model.DeckPioche;

public class VueDeck extends JComponent {
	
	public final static Point posCarteDeckDefausse = new Point(370, 120);
	public final static Point posCarteDeckPioche = new Point(160, 120);
	DeckPioche deckPioche;
	DeckDefausse deckDefausse;
	private final int largeurDeck = 179;
	private final int hauteurDeck = 228;
	Graphics2D drawable;
	private int distanceCarteVisible;
	private int nbCarteDefausse;
	private int nbCartePioche;
	BufferedImage imgDefausse;
	BufferedImage imgPioche;
	BufferedImage[] cartesPossibles;
	
	VueDeck(DeckPioche deckPioche, DeckDefausse deckDefausse) {
		this.deckPioche = deckPioche;
		this.deckDefausse = deckDefausse;
		nbCarteDefausse = 0;
		nbCartePioche = 0;
		try {
			imgDefausse = ImageIO.read(Configuration.charge("Defausse.png", Configuration.CARTES));
			imgPioche = ImageIO.read(Configuration.charge("Pioche.png", Configuration.CARTES));
			cartesPossibles = new BufferedImage[5];
			for (int i = 0; i < cartesPossibles.length; i++) {
				cartesPossibles[i] = ImageIO.read(Configuration.charge((i + 1) + ".png", Configuration.CARTES));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setPreferredSize(new Dimension(600, 300));
		setOpaque(false);
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		drawable = (Graphics2D) g;
		try {
			tracerDeck(110, 65, false, nbCartePioche);
			tracerDeck(335, 65, true, nbCarteDefausse);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void tracerDeck(int posx, int posy, Boolean isDefausse, int nbCarte) throws IOException {
		Image imgFond = isDefausse ? imgDefausse : imgPioche;
		drawable.drawImage(imgFond, posx, posy, largeurDeck, hauteurDeck, null);
		if (nbCarte > 0) {
			if (nbCarte > 1) {
				Image img = ImageIO.read(Configuration.charge("DP" + (nbCarte > 4 ? 4 : nbCarte - 1) + ".png", Configuration.CARTES));
				drawable.drawImage(img, posx + (isDefausse ? 12 : 17), posy + (isDefausse ? 50 : 42), 120, 170, null);
			}
			if (isDefausse) {
				Image imgVisible = cartesPossibles[distanceCarteVisible - 1];
				posCarteDeckDefausse.x = posx + 24 + 3 * (nbCarte > 4 ? 3 : nbCarte - 1);
				posCarteDeckDefausse.y = posy + 58 - 3 * (nbCarte > 4 ? 3 : nbCarte - 1);
				drawable.drawImage(imgVisible, posCarteDeckDefausse.x, posCarteDeckDefausse.y, 98, 150, null);
			} else {
				posCarteDeckPioche.x = posx + 24 + 3 * (nbCarte > 4 ? 3 : nbCarte - 1);
				posCarteDeckPioche.y = posy + 58 - 3 * (nbCarte > 4 ? 3 : nbCarte - 1);
			}
		}
		drawable.setFont(new Font("Arial", Font.BOLD, 30));
		drawable.drawString("" + nbCarte, posx + largeurDeck - 30 - (nbCarte >= 10 ? 10 : 0), posy + 35); 
	}

	public void actualise(Integer[] cartesDeck) {
		nbCartePioche = cartesDeck[0];
		nbCarteDefausse = cartesDeck[1];
		distanceCarteVisible = cartesDeck[2];
		repaint();
	}
}
