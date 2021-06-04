package Database;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;

import model.Jeu;

public class SauvegarderPartie_DAO {
	
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
	
	public void sauvegardeJeu(Jeu jeu) {  
        String sql = "INSERT OR REPLACE INTO SauvegarderPartie(IdJoueurG,IdJoueurD,mancheGagnerGauche,mancheGagnerDroit,posJoueurG,posJoueurD,mainGaucherJSON,mainDroitierJSON,DefausseJSON,PiocheJSON) VALUES(?,?,?,?,?,?,?,?)";  
   
        try{  
            Connection conn = this.connect();  
            PreparedStatement pstmt = conn.prepareStatement(sql);  
            pstmt.setString(1, jeu.getEscrimeurGaucher().getNom());  
            pstmt.setString(2, jeu.getEscrimeurDroitier().getNom());
            pstmt.setInt(3, jeu.getEscrimeurGaucher().getMancheGagner()); 
            pstmt.setInt(4, jeu.getEscrimeurDroitier().getMancheGagner());
            pstmt.setInt(5, jeu.getPlateau().getPosition(0));  
            pstmt.setInt(6, jeu.getPlateau().getPosition(1));  
          
            
            
            try {
				JSONArray DefaussejsArray = new JSONArray(jeu.getDeckDefausse().getArray());
				JSONArray DeckjsArray = new JSONArray(jeu.getDeckPioche().getArray());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            
            pstmt.executeUpdate();  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    }
	
	public ResultSet getAll() {
		String sql = "SELECT * FROM SauvegarderPartie";
        
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
	
	public ResultSet getIdJeu(int idJeu) {
		String sql = "SELECT * FROM SauvegarderPartie WHERE id = '" + idJeu + "'";
        
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
