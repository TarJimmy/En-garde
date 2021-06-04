import Global.Configuration;
import controller.ControlerAutre;
import view.InterfaceGraphiqueMenu;

public class EnGarde {
	public static void main(String[] args) {
		Configuration.instance();
		InterfaceGraphiqueMenu.demarrer(new ControlerAutre());
	}
}
