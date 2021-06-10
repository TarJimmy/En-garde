package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Classement_DAO {
	
	private final static String url = "jdbc:sqlite:En_garde.db";
	

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
                +"), nbDefaite AS (\n"
                +"SELECT nom, count(*) as defaite\n"
                +"FROM Match,Joueur\n"
                +"Where (nom = nomJoueurG and Gagnant = 'Droitier') or (nom = nomJoueurD and Gagnant = 'Gaucher')\n"
                +"GROUP BY nom)\n"

                +"SELECT Joueur.nom, IFNULL(win,0) as win, IFNULL(defaite,0) as defaite\n"
                +"FROM Joueur LEFT JOIN nbWin USING (nom) LEFT JOIN nbDefaite USING (nom)\n"
                +"GROUP BY Joueur.nom\n"
                +"ORDER BY win DESC,defaite ASC LIMIT 10;";
        
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
	            +"), nbDefaite AS (\n"
	            +"SELECT nom, count(*) as defaite\n"
	            +"FROM Match,Joueur\n"
	            +"Where (nom = nomJoueurG and Gagnant = 'Droitier') or (nom = nomJoueurD and Gagnant = 'Gaucher')\n"
	            +"GROUP BY nom)\n"

				+"SELECT Joueur.nom, IFNULL(win,0) as win, IFNULL(defaite,0) as defaite\n"
				+"FROM Joueur LEFT JOIN nbWin USING (nom) LEFT JOIN nbDefaite USING (nom)\n"
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
        
        String sql2 = "SELECT * FROM Joueur WHERE nom = '" + nom.toLowerCase() + "';";
        try{
	        Connection conn = this.connect();
	        Statement stmt  = conn.createStatement();
	        ResultSet rs    = stmt.executeQuery(sql2);
	        if (!rs.next()) {
	        	
	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, nom.toLowerCase());
	            pstmt.executeUpdate();
	            
	        }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
	

	 //	gagnant = 'Gaucher' | 'Droitier'
	 public void insertMatch(String nomJoueurG, String nomJoueurD,int mancheGagnerGauche,int mancheGagnerDroit,String Gagnant) {
	        String sql = "INSERT INTO Match(nomJoueurG, nomJoueurD,mancheGagnerGauche,mancheGagnerDroit,Gagnant) VALUES(?,?,?,?,?)";

	        try{
	            Connection conn = this.connect();
	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, nomJoueurG.toLowerCase());
	            pstmt.setString(2, nomJoueurD.toLowerCase());
	            pstmt.setInt(3, mancheGagnerGauche);
	            pstmt.setInt(4, mancheGagnerDroit);
	            pstmt.setString(5, Gagnant);
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	    }
	
	    public static void main(String[] args) {
	    	Classement_DAO c_dao = new Classement_DAO();
//	    	c_dao.insertJoueur("jimmy");
//	    	c_dao.insertJoueur("telmo");
	    	c_dao.insertJoueur("macron");
	    	c_dao.insertJoueur("la gifle de macron");
	    	c_dao.insertMatch("macron","la gifle de macron",3,4,"Droitier");
//	    	c_dao.insertMatch("la gifle de macron","macron",6,4,"Gaucher");
//	    	c_dao.insertMatch("jimmy","telmo",0,7,"Droitier");
//	    	c_dao.insertMatch("jimmy","telmo",3,4,"Gaucher");
	    	ResultSet rs = c_dao.getPodium();
	    	try {
				while (rs.next()) {
				    System.out.println(rs.getString("nom") +  "\t" +
				                       rs.getInt("win") + "\t" +
				                       rs.getInt("defaite"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	    }
	
}
