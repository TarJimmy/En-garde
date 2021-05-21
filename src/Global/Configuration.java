package Global;

import java.io.File;
import java.io.InputStream;

public class Configuration {
	private static Configuration instance;
	
	public static InputStream charge(String nom) {
		return ClassLoader.getSystemClassLoader().getResourceAsStream(nom);
	}

	private Configuration() {
		
	}
	
	public static Configuration instance() {
		if (instance == null) {
			instance = new Configuration();
		} 
		return instance;
	}
	
	public static String getFolderCartes() {
		return "Images" + File.separator + "Cartes" + File.separator;
	}
	public static String getFolderRegles() {
		return "Images" + File.separator + "Regles" + File.separator;
	}
	public static String getFolderMenu() {
		return "Images" + File.separator + "Menu" + File.separator;
	}
}
