package Global;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
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
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemClassLoader().getResourceAsStream("CENTURY.ttf")));
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
		return ClassLoader.getSystemClassLoader().getResourceAsStream(nom);
	}
}
