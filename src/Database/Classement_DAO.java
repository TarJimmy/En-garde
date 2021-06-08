package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Classement_DAO {
	
	private final static String url = "jdbc:sqlite:src/Database/En_garde.db";
	

	private static Connection connect() {  
        // SQLite connection string  
        Connection conn = null;  
        try {  
            conn = DriverManager.getConnection(url);  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
        return conn;  
    }
	
	public static ResultSet getPodium() {
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
        	Connection conn = connect();
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
        	Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            // return results
            return rs;
            

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
	}
	
	public void insertJoueur(String nom) {
        String sql = "INSERT INTO Joueur(nom) VALUES(?)";
        
        sql = "SELECT * FROM Joueur WHERE nom = '" + nom + "';";
        try{
	        Connection conn = this.connect();
	        Statement stmt  = conn.createStatement();
	        ResultSet rs    = stmt.executeQuery(sql);
	        if (rs.next()) {
	        	
	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, nom);
	            pstmt.executeUpdate();
	           
	        }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
	

	 //	gagnant = 'nulle' | 'Gaucher' | 'Droitier'
	 public void insertMatch(String nomJoueurG, String nomJoueurD,int mancheGagnerGauche,int mancheGagnerDroit,String Gagnant) {
	        String sql = "INSERT INTO Match(nomJoueurG, nomJoueurD,mancheGagnerGauche,mancheGagnerDroit,Gagnant) VALUES(?,?,?,?,?)";

	        try{
	            Connection conn = this.connect();
	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, nomJoueurG);
	            pstmt.setString(2, nomJoueurD);
	            pstmt.setInt(3, mancheGagnerGauche);
	            pstmt.setInt(4, mancheGagnerDroit);
	            pstmt.setString(5, Gagnant);
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	    }
	
	
	
}
