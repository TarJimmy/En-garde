package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.border.LineBorder;

import Global.Configuration;
import Patterns.Observateur;
import model.Deck;
import model.DeckDefausse;
import model.DeckPioche;

public class VueDeck extends JComponent implements Observateur {
	
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
	public void miseAJour() {
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
			Image img = ImageIO.read(Configuration.charge("DP" + (nbCarte > 4 ? 4 : nbCarte) + ".png", Configuration.CARTES));
			drawable.setFont(new Font("Arial", Font.BOLD, 30));
			drawable.drawString("" + nbCarte, posx + largeurDeck - 30 - (nbCarte >= 10 ? 10 : 0), posy + 35); 
			if (isDefausse) {
				drawable.drawImage(img, posx + 12, posy + 50, 120, 170, null);
				int distance = ((DeckDefausse) deck).consulterCarteVisible().getDistance();
				Image imgVisible = ImageIO.read(Configuration.charge(distance +  ".png", Configuration.CARTES));
				drawable.drawImage(imgVisible, posx + 17 + 5 * (nbCarte > 4 ? 3 : nbCarte - 1), posy + 51, 98, 150, null);
			} else {
				drawable.drawImage(img, posx + 17, posy + 42, 120, 170, null);
			}
		}
	}
}
