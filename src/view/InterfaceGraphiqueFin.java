package view;

import java.awt.Font;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Global.Configuration;
import Patterns.Observateur;
import controller.ControlerAutre;

/**
 * 
 * @author Laetitia & Delphine
 *
 */
public class InterfaceGraphiqueFin implements Runnable, Observateur {
	
	private static JFrame fenetreFin;
	private static CollecteurEvenements controle;
	
	private InterfaceGraphiqueFin(CollecteurEvenements controle) {
		InterfaceGraphiqueFin.controle = controle;
	}
	
	/**
	 * Ouvre la fenetre Fin
	 * @param control
	 */
	public static void demarrer(CollecteurEvenements control) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueFin(control));
	}
	
	/**
	 * Ferme la fenetre Fin
	 */
	public static void close() {
		if(fenetreFin!=null) {
			fenetreFin.setVisible(false);
			fenetreFin.dispose();
		}
	}
	
	/**
	 * Cree un bouton JButton
	 * @param name : le nom du bouton
	 * @return le bouton "name" genere
	 */
	private static JButton Button (String name) {
		JButton button = null;
		ImageIcon banner;
		
		try {
			banner = new ImageIcon(new ImageIcon(ImageIO.read(Configuration.charge("cadre2.png", Configuration.MENU))).getImage().getScaledInstance(195, 40, Image.SCALE_SMOOTH));
			button = new JButton(name, banner);
			button.setFont(new Font(Configuration.Century.getFamily(), Font.PLAIN, 15));	
			button.setHorizontalTextPosition(SwingConstants.CENTER);
			button.addMouseListener(new AdaptateurBouton(controle, "cadre2", button, 195));
			button.setFocusPainted(false);
			button.setBorderPainted(false);
			button.setContentAreaFilled(false);
			button.setOpaque(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return button;
	}

	@Override
	public void miseAJour() {
		// TODO Auto-generated method stub
	}

	/**
	 * Ecriture du contenu de la fenetre Fin
	 */
	@Override
	public void run() {
		fenetreFin = new JFrame("EN GARDE ! - Fin de la partie");
		JLabel contentPane = new JLabel(new ImageIcon("res/Images/Menu/Fin.png"));
		//JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		fenetreFin.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		///Boutons
		JButton btnMenu = Button("MENU");
		btnMenu.addActionListener(new AdaptateurCommande(controle, "menu"));
		btnMenu.setBounds(50, 350, 200, 40);
		contentPane.add(btnMenu);
		
		JButton btnRecommencer = Button("Recommencer");
		btnRecommencer.addActionListener(new AdaptateurCommande(controle, "rejouer"));
		btnRecommencer.setBounds(342, 350, 200, 40);
		contentPane.add(btnRecommencer);
		
		JButton btnQuit = Button("Quitter");
		btnQuit.addActionListener(new AdaptateurCommande(controle, "exit"));
		btnQuit.setBounds(634, 350, 200, 40);
		contentPane.add(btnQuit);
		
		fenetreFin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetreFin.setBounds(100, 100, 900, 450);
		fenetreFin.setResizable(false);
		fenetreFin.setVisible(true);
	}

	public static void main(String[] args) {
		InterfaceGraphiqueFin.demarrer(new ControlerAutre());
	}
}

