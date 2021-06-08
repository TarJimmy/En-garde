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
	
	public int sauvegardeJeu(Jeu jeu) {  
		String sql;
		int idJeu = jeu.getIdJeu();
		System.out.println(idJeu);
		if (idJeu != -1) {
	        sql = "UPDATE SauvegarderPartie SET nomJoueurG = ?,"
	        		+ "nomJoueurD = ?,"
	        		+ "mancheGagnerGauche = ?"
	        		+ ",mancheGagnerDroit = ?,"
	        		+ "posJoueurG = ?,"
	        		+ "posJoueurD = ?,"
	        		+ "nbCasesJeu = ?,"
	        		+ "nbManchesWin = ?,"
	        		+ "mainGaucherJSON = ?,"
	        		+ "mainDroitierJSON = ?,"
	        		+ "DefausseJSON = ?,"
	        		+ "PiocheJSON = ?,"
	        		+ "posDepartGauche = ?,"
	        		+ "posDepartDroit = ?,"
	        		+ "modeSimple = ?,"
	        		+ "TypeEscrimeurG = ?,"
	        		+ "TypeEscrimeurD = ?,"
	        		+ "CartesMaxJoueur = ?,"
	        		+ "AnimationAutoriser = ?,"
	        		+ "indicePremierJoueur = ?,"
	        		+ "indiceCurrentJoueur = ?"
	                + "WHERE idPartie = ?";
		
		} else {
			sql = "INSERT OR REPLACE INTO SauvegarderPartie(nomJoueurG,nomJoueurD,mancheGagnerGauche,mancheGagnerDroit,posJoueurG,posJoueurD,nbCasesJeu,nbManchesWin,mainGaucherJSON,mainDroitierJSON,DefausseJSON,PiocheJSON,posDepartGauche,posDepartDroit,modeSimple,TypeEscrimeurG,TypeEscrimeurD,CartesMaxJoueur,AnimationAutoriser,indicePremierJoueur,indiceCurrentJoueur) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";  
		}
   
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
            
            pstmt.setBoolean(15, jeu.getModeSimple()); 
            pstmt.setString(16,jeu.getEscrimeurGaucher().getType().name());
            pstmt.setString(17,jeu.getEscrimeurDroitier().getType().toString());
            pstmt.setInt(18, jeu.getEscrimeurDroitier().getNbCartes());  
            pstmt.setInt(19, jeu.getPositionsDepart()[1]);  
            pstmt.setInt(20, jeu.getIndicePremierJoueur());  
            pstmt.setInt(21, jeu.getIndiceCurrentEscrimeur());  
            if (idJeu != -1) {
            	pstmt.setInt(22, idJeu);  
            }
            pstmt.executeUpdate();  
            System.out.println("Partie sauvegard�\n");

            
            if (idJeu != -1) {
            	return idJeu;
            } else {
            	sql = "SELECT * FROM SauvegarderPartie BY idPartie DESC LIMIT 1";
                
                Statement stmt  = conn.createStatement();
                ResultSet rs    = stmt.executeQuery(sql);
                return rs.getInt("idPartie");
            }
            
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
        return -1;
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
		String sql = "SELECT * FROM SauvegarderPartie WHERE idPartie = '" + idJeu + "'";
        
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
