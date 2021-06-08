package Global;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

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
	public final static int AUTRES = 8;
	public final static int TUTORIEL = 9;
	public final static int CLASSEMENT = 10;
	public static BufferedImage imgIcone;
	
	private Configuration() {
		try {
			imgIcone = ImageIO.read(charge("icon.png", Configuration.AUTRES));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res/CENTURY.TTF")));
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
			case AUTRES:
				path += "Autres";
				break;
			case TUTORIEL:
				path += "Tutoriel";
				break;
			case CLASSEMENT:
				path += "Classement";
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + type);
		}
		path += File.separator;
		return ClassLoader.getSystemClassLoader().getResourceAsStream(path + nom);
	}
}
