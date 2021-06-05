import Database.DB_DAO;
import Global.Configuration;
import controller.ControlerAutre;
import view.InterfaceGraphiqueMenu;

public class EnGarde {
	public static void main(String[] args) {
		Configuration.instance();
		DB_DAO db = new DB_DAO();
    	db.createNewDatabase();
    	db.createTables();
		InterfaceGraphiqueMenu.demarrer(new ControlerAutre());
	}
}
