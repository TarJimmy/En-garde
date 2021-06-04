package model;

/**
 * 
 * @author Laetitia & Delphine
 *
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Global.Parametre;
public class SauvegardeParametre {

	private static String map;
	private static List<String> joueur1 = new ArrayList<>();
	private static List<String> joueur2 = new ArrayList<>();
	private static String modeAttaque;
	private static String manches;
	private static String cartes_max;
	private static List<String> valeurs_cartes = new ArrayList<>();
	private static String animation;
	
	public SauvegardeParametre(){
		map = null;
		joueur1 = null;
		joueur2 = null;
		modeAttaque = null;
		manches = null;
		cartes_max = null;
		valeurs_cartes = null;
	}
	
	/**
	 * Sauvegarde des parametres
	 * Sauvegarde des valeurs du formulaire dans un fichier .txt
	 * @param map : taille du terrain de jeu
	 * @param nomJ1 : identifiant du joueur 1
	 * @param typeJ1 : type du joueur 1
	 * @param posJ1 : position, sur le terrain, du joueur 1
	 * @param nomJ2 : identifiant du joueur 2
	 * @param typeJ2 : type du joueur 2
	 * @param posJ2 : position, sur le terrain, du joueur 2
	 * @param modeAttaque : basique ou avance
	 * @param manches : nombre de manche pour gagner
	 * @param carteMax : nombre de cartes maximum autorise en main
	 * @param carte1 : nombre de cartes de valeur 1
	 * @param carte2 : nombre de cartes de valeur 2
	 * @param carte3 : nombre de cartes de valeur 3
	 * @param carte4 : nombre de cartes de valeur 4
	 * @param carte5 : nombre de cartes de valeur 5
	 */
	public static void sauvegarderParametres(String map, String nomJ1, String typeJ1, String posJ1, String nomJ2, String typeJ2, String posJ2, String modeAttaque, String manches, String carteMax, String carte1, String carte2, String carte3, String carte4, String carte5, String animation){
		String filePath = Parametre.instance().dirPara + "/parametres.txt";
		try {
			// Create new file if needed
			File file = new File(filePath);
			file.createNewFile();
			// Write to the file
			FileWriter fWriter = new FileWriter(filePath);
			
			fWriter.write(map+"\n");
			fWriter.write(nomJ1+"\n"+typeJ1+"\n"+posJ1+"\n");
			fWriter.write(nomJ2+"\n"+typeJ2+"\n"+posJ2+"\n");
			fWriter.write(modeAttaque+"\n");
			fWriter.write(manches+"\n");
			fWriter.write(carteMax+"\n");
			fWriter.write(carte1+"\n"+carte2+"\n"+carte3+"\n"+carte4+"\n"+carte5+"\n");
			fWriter.write(animation+"\n");
			
			fWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
 
	}
	
	/**
	 * Chargement des parametres
	 * Extraction des valeurs du fichier .txt renseignant les parametres par defaut du formulaire
	 * vers les variables privees de SaveSettings
	 */
	public static void chargerParametres() {
		try {
			File file = new File(Parametre.instance().dirPara + "/parametres.txt");
			Scanner fReader = new Scanner(file);
			List<String> j1 = new ArrayList<>();
			List<String> j2 = new ArrayList<>();
			List<String> vc = new ArrayList<>();
			// Initialisation options du terrain
			map = fReader.nextLine().trim();
			for (int i=0;i<6;i++) {
				String s = fReader.nextLine().trim();
				if (i<3) {j1.add(s);}
				else {j2.add(s);}
			}
			joueur1 = j1;
			joueur2 = j2;
			// Initialisation options du jeu
			modeAttaque = fReader.nextLine().trim();
			manches = fReader.nextLine().trim();
			cartes_max = fReader.nextLine().trim();
			for (int i=0;i<5;i++ ) {
				String s = fReader.nextLine().trim();
				vc.add(s);
			}
			valeurs_cartes = vc;
			animation = fReader.nextLine().trim();
			
			fReader.close();
			
		} catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	}
	
	/**
	 * Recuperation des parametres
	 * @return la liste de string des parametres enregistres
	 */
	public static List<String> getSettings() {
		List<String> settings = new ArrayList<>();
		settings.add(map);
		for (String value1 : joueur1) { settings.add((value1)); }
		for (String value2 : joueur2) { settings.add(value2); }
		settings.add(modeAttaque);
		settings.add(manches);
		settings.add(cartes_max);
		for (String value3 : valeurs_cartes) {settings.add(value3);}
		settings.add(animation);
		return settings;
	}
}
