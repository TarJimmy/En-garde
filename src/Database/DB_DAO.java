package Database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB_DAO {

	private final String url = "jdbc:sqlite:src/Database/En_garde.db";

	public void createNewDatabase() {

        try {

            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("Database created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

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





    public void createTables() {
        // SQLite connection string

        // SQL statement for creating a new table
        String sqlJoueur = "CREATE TABLE IF NOT EXISTS Joueur (\n"
                + " nom text NOT NULL PRIMARY KEY\n"
                + ");";

        String sqlJoueurCarte = "CREATE TABLE IF NOT EXISTS CarteJoueur (\n"
                + " IdJoueur integer NOT NULL,\n"
                + " IdCarte integer NOT NULL,\n"
                + " PRIMARY KEY (IdJoueur, IdCarte)\n"
                + ");";

        String sqlMatch = "CREATE TABLE IF NOT EXISTS Match (\n"
        		+ " id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " nomJoueurG text NOT NULL,\n"
                + " nomJoueurD text NOT NULL,\n"
                + " mancheGagnerGauche integer NOT NULL,\n"
                + " mancheGagnerDroit integer NOT NULL,\n"
                + " Gagnant text NOT NULL\n"
                + ");";

        String sqlSauvegarderPartie = "CREATE TABLE IF NOT EXISTS SauvegarderPartie (\n"
        		+ " idPartie integer PRIMARY KEY AUTOINCREMENT,\n"
                + " nomJoueurG integer NOT NULL,\n"
                + " nomJoueurD integer NOT NULL,\n"
                
                
                + " mancheGagnerGauche integer NOT NULL,\n"
                + " mancheGagnerDroit integer NOT NULL,\n"
                
				+ " nbCasesJeu integer NOT NULL,\n"
				+ " nbManchesWin integer NOT NULL,\n"
                
                + " posJoueurG integer NOT NULL,\n"
                + " posJoueurD integer NOT NULL,\n"
                
                
                + " mainGaucherJSON text NOT NULL,\n"
                + " mainDroitierJSON text NOT NULL,\n"
                + " piocheJSON text NOT NULL,\n"
                + " DefausseJSON text NOT NULL,\n"
                
                + " posDepartGauche integer NOT NULL,\n"
                + " posDepartDroit integer NOT NULL,\n"
                
                
				+ " modeSimple boolean NOT NULL,\n"
				+ " TypeEscrimeurG text NOT NULL,\n"
				+ " TypeEscrimeurD text NOT NULL,\n"
				+ " CartesMaxJoueur integer NOT NULL,\n"
				+ " AnimationAutoriser boolean NOT NULL\n,"
				+ " indicePremierJoueur int NOT NULL,\n"
				+ " indiceCurrentJoueur int NOT NULL,\n"
				+ " indicePremierJouerPartie int NOT NULL,\n"
				
				+ " dateMatch text NOT NULL\n"
                
                + ");";







        try{
        	Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sqlJoueur);
            stmt.execute(sqlJoueurCarte);
            stmt.execute(sqlMatch);
            stmt.execute(sqlSauvegarderPartie);
            System.out.println("Tables created.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    


    public void selectAll(){
        String sql = "SELECT * FROM Joueur";

        try {
        	Connection conn = this.connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                				   rs.getString("nom") +  "\t" +
                                   rs.getString("type") + "\t" +
                                   rs.getInt("position") + "\t" +
                                   rs.getInt("nbManches"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * @param args the command line arguments
     * @throws ClassNotFoundException
     */


}
