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
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.border.LineBorder;

import Global.Configuration;
import Patterns.Observateur;
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
	
	VueDeck(DeckPioche deckPioche, DeckDefausse deckDefausse) {
		this.deckPioche = deckPioche;
		this.deckDefausse = deckDefausse;
		setPreferredSize(new Dimension(600, 300));
		setOpaque(false);
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		drawable = (Graphics2D) g;
		try {
			tracerDeck(deckPioche, 110, 65, false);
			tracerDeck(deckDefausse, 335, 65, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void tracerDeck(Deck deck, int posx, int posy, Boolean isDefausse) throws IOException {
		int nbCarte = deck.nbCartes();
		Image imgFond = ImageIO.read(Configuration.charge((isDefausse ? "Defausse" : "Pioche") + ".png", Configuration.CARTES));
		drawable.drawImage(imgFond, posx, posy, largeurDeck, hauteurDeck, null);
		if (nbCarte > 0) {
			if (nbCarte > 1) {
				Image img = ImageIO.read(Configuration.charge("DP" + (nbCarte > 4 ? 4 : nbCarte - 1) + ".png", Configuration.CARTES));
				drawable.drawImage(img, posx + (isDefausse ? 12 : 17), posy + (isDefausse ? 50 : 42), 120, 170, null);
			}
			if (isDefausse) {
				int distance = ((DeckDefausse) deck).consulterCarteVisible().getDistance();
				Image imgVisible = ImageIO.read(Configuration.charge(distance +  ".png", Configuration.CARTES));
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
}
