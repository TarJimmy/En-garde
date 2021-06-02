package view;

import java.awt.Font;
import java.awt.Image;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Patterns.Observateur;
import controller.ControlerAutre;

/**
 * 
 * @author La�titia & Delphine
 *
 */
public class InterfaceGraphiqueFin implements Runnable, Observateur {
	
	private static JFrame fenetreFin;
	CollecteurEvenements controle;
	
	private InterfaceGraphiqueFin(CollecteurEvenements controle) {
		this.controle = controle;
	}
	
	/**
	 * Ouvre la fen�tre Fin
	 * @param control
	 */
	public static void demarrer(CollecteurEvenements control) {
		SwingUtilities.invokeLater(new InterfaceGraphiqueFin(control));
	}
	
	/**
	 * Ferme la fen�tre Fin
	 */
	public static boolean close() {
		try {
			fenetreFin.setVisible(false);
			fenetreFin.dispose();
			return true;
		} catch(NullPointerException n) {
			return false;
		}
	}
	
	/**
	 * Cr�e un bouton JButton
	 * @param name : le nom du bouton
	 * @return le bouton "name" g�n�r�
	 */
	private static JButton Button (String name) {
		JButton button;
		ImageIcon banner;
		
		banner = new ImageIcon(new ImageIcon("res/Images/Menu/cadre2.png").getImage().getScaledInstance(195, 40, Image.SCALE_SMOOTH));
		button = new JButton(name, banner);
		button.setFont(new Font("Century", Font.PLAIN, 15));		
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		
		return button;
	}

	@Override
	public void miseAJour() {
		// TODO Auto-generated method stub
	}

	/**
	 * �criture du contenu de la fen�tre Fin
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
		btnMenu.addActionListener(new AdaptateurCommande(this.controle, "menu"));
		btnMenu.setBounds(50, 350, 200, 40);
		contentPane.add(btnMenu);
		
		JButton btnRecommencer = Button("Recommencer");
		btnRecommencer.addActionListener(new AdaptateurCommande(this.controle, "retry"));
		btnRecommencer.setBounds(342, 350, 200, 40);
		contentPane.add(btnRecommencer);
		
		JButton btnQuit = Button("Quitter");
		btnQuit.addActionListener(new AdaptateurCommande(this.controle, "exit"));
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

