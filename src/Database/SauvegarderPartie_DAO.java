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
	
	public void sauvegardeJeu(Jeu jeu) {  
        String sql = "INSERT OR REPLACE INTO SauvegarderPartie(nomJoueurG,nomJoueurD,mancheGagnerGauche,mancheGagnerDroit,posJoueurG,posJoueurD,nbCasesJeu,nbManchesWin,mainGaucherJSON,mainDroitierJSON,DefausseJSON,PiocheJSON,posDepartGauche,posDepartDroit) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";  
   
        try{  
            Connection conn = this.connect();  
            PreparedStatement pstmt = conn.prepareStatement(sql);  
            pstmt.setString(1, jeu.getEscrimeurGaucher().getNom());  
            pstmt.setString(2, jeu.getEscrimeurDroitier().getNom());
            pstmt.setInt(3, jeu.getEscrimeurGaucher().getMancheGagner()); 
            pstmt.setInt(4, jeu.getEscrimeurDroitier().getMancheGagner());
            pstmt.setInt(5, jeu.getPlateau().getPosition(0));  
            pstmt.setInt(6, jeu.getPlateau().getPosition(1));  
            pstmt.setInt(7, jeu.getPlateau().getNbCase());  
            pstmt.setInt(8, jeu.getNbManchesPourVictoire());  
            
            
            try {
            	JSONArray mainGaucherjsArray = new JSONArray(jeu.getEscrimeurGaucher().getArrayCartes());
            	JSONArray mainDroitierjsArray = new JSONArray(jeu.getEscrimeurDroitier().getArrayCartes());
				JSONArray DefaussejsArray = new JSONArray(jeu.getDeckDefausse().getArray());
				JSONArray PiochejsArray = new JSONArray(jeu.getDeckPioche().getArray());
				pstmt.setString(9, mainGaucherjsArray.toString());  
	            pstmt.setString(10, mainDroitierjsArray.toString());  
	            pstmt.setString(11, DefaussejsArray.toString());  
	            pstmt.setString(12, PiochejsArray.toString()); 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            pstmt.setInt(13, jeu.getPositionsDepart()[0]);
            pstmt.setInt(14, jeu.getPositionsDepart()[1]);  
            
            
            pstmt.executeUpdate();  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    }
	
	public static ResultSet getAll() {
		String sql = "SELECT * FROM SauvegarderPartie";
        
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
	
	public ResultSet getIdJeu(int idJeu) {
		String sql = "SELECT * FROM SauvegarderPartie WHERE id = '" + idJeu + "'";
        
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
	 
	
}
