package view;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import Global.Configuration;

public class AdaptateurBouton extends MouseAdapter {
	private String commande;
	CollecteurEvenements control;
	private JButton bouton;
	private ImageIcon image;
	private int width;
	private int height = 40;
	
	public AdaptateurBouton(CollecteurEvenements controller, String commande, JButton bouton, int width) {
		this.commande = commande;
		this.control = controller;
		this.bouton = bouton;
		this.width = width;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		try {
			if (commande == "pasCadre") {
				image = new ImageIcon(new ImageIcon(ImageIO.read(Configuration.charge("cadre4.png", Configuration.MENU))).getImage().getScaledInstance(width, 40, Image.SCALE_SMOOTH));
				bouton.setForeground(new Color(250,250,250));
			} else {
				image = new ImageIcon(ImageIO.read(Configuration.charge(commande+"_actif.png", Configuration.MENU)));
				if (commande != "cadre"){image = new ImageIcon(image.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));}
			}
			bouton.setIcon(image);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		try {
			if (commande == "pasCadre") {
				image = null;
				bouton.setForeground(new Color(51,51,51));
			}
			else {
				image = new ImageIcon(ImageIO.read(Configuration.charge(commande+".png", Configuration.MENU)));
				if (commande != "cadre"){image = new ImageIcon(image.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));}
			}
			bouton.setIcon(image);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
	}

}
