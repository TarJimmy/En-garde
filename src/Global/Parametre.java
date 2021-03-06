package Global;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author Laetitia & Delphine
 *
 */
public class Parametre {
	public final String dirPara = "Parametres"; 
	
	private static Parametre instance = null;
	
	/**
	 * Creation d'un dossier et fichier de parametres
	 */
	private Parametre() {
		File dossier = new File("Parametres"); 
	    boolean res = dossier.mkdir();
	    if (res) {
	    	System.out.println("Le dossier Parametres a été créé.");
	      	String filePath = dirPara + "/parametres.txt";
			try {
				// Create new file if needed
				File file = new File(filePath);
				file.createNewFile();
				// Write to the file
				FileWriter fWriter = new FileWriter(filePath);
				
				/*Taille terrian*/fWriter.write("23\n");
				/*Escrimeur Gauche*/fWriter.write("Joueur 1\nHUMAIN\n1\n");
				/*Escrimeur Droit*/fWriter.write("Joueur 2\nIA_FACILE\n23\n");
				/*Mode d'attaque*/fWriter.write("Basique\n");
				/*Nombre de manches*/fWriter.write("5\n");
				/*Nombre maximum de cartes en mains*/fWriter.write("5\n");
				/*Nombre de cartes par valeurs (1,2,3,4,5)*/fWriter.write("5\n5\n5\n5\n5\n");
				/*Activer ou desactiver les animations*/fWriter.write("Actif\n");
				/*Joueur qui commence*/fWriter.write("Gauche\n");
				
				fWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
	    }
	}
	
	public static Parametre instance() {
		if (instance == null) {
			instance = new Parametre();
		}
		return instance;
	}
}
