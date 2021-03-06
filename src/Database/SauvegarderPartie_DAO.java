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
	
	private final static String url = "jdbc:sqlite:En_garde.db";
	public static Statement stmt;
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
	        		+ "indiceCurrentJoueur = ?,"
	        		+ "indicePremierJouerPartie = ?,"
	        		+ "dateMatch = DATE('now','localtime') "
	                + "WHERE idPartie = ?";
		
		} else {
			sql = "INSERT OR REPLACE INTO SauvegarderPartie(nomJoueurG,nomJoueurD,mancheGagnerGauche,mancheGagnerDroit"
					+ ",posJoueurG,posJoueurD,nbCasesJeu,nbManchesWin,mainGaucherJSON,mainDroitierJSON,DefausseJSON,"
					+ "PiocheJSON,posDepartGauche,posDepartDroit,modeSimple,TypeEscrimeurG,TypeEscrimeurD,CartesMaxJoueur,"
					+ "AnimationAutoriser,indicePremierJoueur,indiceCurrentJoueur,indicePremierJouerPartie,dateMatch) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,DATE('now','localtime'))";  
		}
   
        try{  
            Connection conn = this.connect();  
            PreparedStatement pstmt = conn.prepareStatement(sql);  
            
            pstmt.setString(1, jeu.getEscrimeurGaucher().getNom().toLowerCase());  
            pstmt.setString(2, jeu.getEscrimeurDroitier().getNom().toLowerCase());
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
            pstmt.setInt(20, jeu.getindicePremierJoueurManche());  
            pstmt.setInt(21, jeu.getIndiceCurrentEscrimeur());  
            pstmt.setInt(22, jeu.getIndicePremierJoueurPartie());  
            if (idJeu != -1) {
            	pstmt.setInt(23, idJeu);  
            }
            pstmt.executeUpdate();  
            System.out.println("Partie sauvegard?\n");

            pstmt.close();
            if (idJeu != -1) {
            	return idJeu;
            } else {
            	sql = "SELECT * FROM SauvegarderPartie ORDER BY idPartie DESC LIMIT 1";
                
                Statement stmt  = conn.createStatement();
                ResultSet rs    = stmt.executeQuery(sql);
                int id = rs.getInt("idPartie");
                rs.close();
                stmt.close();
                return id;
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
            stmt  = conn.createStatement();
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

	public void supprimerPartieSauvegardee(int id) {
		String sql = "DELETE FROM SauvegarderPartie WHERE idPartie = '" + id + "';";
		try {
        	Connection conn = this.connect();
            Statement stmt  = conn.createStatement();
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	 
	
}