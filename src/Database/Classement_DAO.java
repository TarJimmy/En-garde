package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Classement_DAO {
	
	private final String url = "jdbc:sqlite:src/Database/En_garde.db";
	

	private Connection connect() {  
        // SQLite connection string  
        Connection conn = null;  
        try {  
            conn = DriverManager.getConnection(url);  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
        return conn;  
    }
	
	public ResultSet getPodium() {
		String sql = "WITH nbWin AS (\n"
	            +"SELECT nom, count(*) as win\n"
	            +"FROM Match,Joueur\n"
	            +"Where (nom = nomJoueurG and Gagnant = 'Gaucher') or (nom = nomJoueurD and Gagnant = 'Droitier')\n"
	            +"GROUP BY nom\n"
	            +"), nbNull AS (\n"
	            +"SELECT nom, count(*) as nulle\n"
	            +"FROM Match,Joueur\n"
	            +"Where (nom = nomJoueurG and Gagnant = 'nulle') or (nom = nomJoueurD and Gagnant = 'nulle')\n"
	            +"GROUP BY nom\n"
	            +"), nbDefaite AS (\n"
	            +"SELECT nom, count(*) as defaite\n"
	            +"FROM Match,Joueur\n"
	            +"Where (nom = nomJoueurG and Gagnant = 'Droitier') or (nom = nomJoueurD and Gagnant = 'Gaucher')\n"
	            +"GROUP BY nom)\n"

	            +"SELECT Joueur.nom, win, defaite, nulle\n"
	            +"FROM Joueur INNER JOIN nbWin USING (nom) INNER JOIN nbNull USING (nom) INNER JOIN nbDefaite USING (nom)\n"
	            +"GROUP BY Joueur.nom\n"
	            +"ORDER BY win LIMIT 10;";
        
        try {
        	Connection conn = this.connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            // return results
            return rs;
            

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
	}
	
	public ResultSet getAll() {
        String sql = "WITH nbWin AS (\n"
	            +"SELECT nom, count(*) as win\n"
	            +"FROM Match,Joueur\n"
	            +"Where (nom = nomJoueurG and Gagnant = 'Gaucher') or (nom = nomJoueurD and Gagnant = 'Droitier')\n"
	            +"GROUP BY nom\n"
	            +"), nbNull AS (\n"
	            +"SELECT nom, count(*) as nulle\n"
	            +"FROM Match,Joueur\n"
	            +"Where (nom = nomJoueurG and Gagnant = 'nulle') or (nom = nomJoueurD and Gagnant = 'nulle')\n"
	            +"GROUP BY nom\n"
	            +"), nbDefaite AS (\n"
	            +"SELECT nom, count(*) as defaite\n"
	            +"FROM Match,Joueur\n"
	            +"Where (nom = nomJoueurG and Gagnant = 'Droitier') or (nom = nomJoueurD and Gagnant = 'Gaucher')\n"
	            +"GROUP BY nom)\n"

	            +"SELECT Joueur.nom, win, defaite, nulle\n"
	            +"FROM Joueur INNER JOIN nbWin USING (nom) INNER JOIN nbNull USING (nom) INNER JOIN nbDefaite USING (nom)\n"
	            +"GROUP BY Joueur.nom;";
        
        try {
        	Connection conn = this.connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            // return results
            return rs;
            

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
	}
	
	
	
}
