package Global;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Configuration {
	private static Configuration instance;
	public final static int BG = 0;
	public final static int CARTES = 1;
	public final static int MENU = 2;
	public final static int REGLES = 3;
	public final static int PLATEAU = 4;
	public final static int DALLES = 5;
	public final static int MANCHES = 6;
	public final static int ESCRIMEURS = 7;
	public static Font Century;
	
	private Configuration() {
		try {
			Century = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("res/CENTURY.TTF")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
	}
	
	public static Configuration instance() {
		if (instance == null) {
			instance = new Configuration();
		} 
		return instance;
	}
	
	public static InputStream charge(String nom, int type) {
		String path = "Images" + File.separator;
		switch (type) {
			case BG:
				path += "Background";
				break;
			case CARTES:
				path += "Cartes";
				break;
			case MENU: 
				path += "Menu";
				break;
			case REGLES: 
				path += "Regles";
				break;
			case PLATEAU:
				path += "Plateau";
				break;
			case DALLES: 
				path += "Plateau" + File.separator + "Dalles";
				break;
			case MANCHES: 
				path += "Manches";
				break;
			case ESCRIMEURS: 
				path += "Escrimeurs";
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + type);
		}
		path += File.separator;
		return ClassLoader.getSystemClassLoader().getResourceAsStream(path + nom);
	}
}
