package bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Créé les tables de la ville */
public class DBCreate {
	/** creation de toutes les tables */
	public void createTables(){
		Connection connect = Database.connect();
		if(connect != null){
			System.out.println("-- Creation table Ville ");
			this.createVille(connect);
			System.out.println("-- Creation table Quartier ");
			this.createQuartier(connect);
			System.out.println("-- Creation table Bloc ");
			this.createBloc(connect);
			System.out.println("-- Creation table Batiment ");
			this.createBatiment(connect);
			System.out.println("-- Creation table Personne ");
			this.createPersonne(connect);
			System.out.println("-- Creation table Faction ");
			this.createFaction(connect);
			System.out.println("-- Creation table Type ");
			this.createType(connect);
			DBSave.deleteAll(connect);
			Database.closeConnection(connect);
		}
	}
	
	/** creation de la table ville */
	private void createVille(Connection connect){
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("CREATE TABLE IF NOT EXISTS ville(" +
					"id_ville SMALLINT(2) NOT NULL, " +
					"nom VARCHAR(20), " +
					"age INT(20), " +
					"PRIMARY KEY(id_ville)" +
					")");
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
	/** creation de la table quartier */
	private void createQuartier(Connection connect){
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("CREATE TABLE IF NOT EXISTS quartier(" +
					"id_quart SMALLINT(2) NOT NULL, " +
					"id_ville SMALLINT(2), " +
					"id_faction SMALLINT(2), " +
					"nom varchar(20), " +
					"x_size SMALLINT(2)," +
					"y_size SMALLINT(2), " +
					"x_center SMALLINT(2), " +
					"y_center SMALLINT(2), " + 
					"cagnotte DOUBLE, " +
					"soilent DOUBLE, " +
					"PRIMARY KEY(id_quart) " +
//					"FOREIGN KEY (id_ville) REFERENCES ville(id_ville), "+
//					"FOREIGN KEY (id_faction) REFERENCES faction(id_faction)" +
					")");
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
	/** creation de la table des blocs */
	private void createBloc(Connection connect){
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("CREATE TABLE IF NOT EXISTS bloc(" +
					"id_bloc SMALLINT(3) NOT NULL, " +
					"id_type SMALLINT(2)," +
					"id_quart SMALLINT(2)," +
					"x_size SMALLINT(2)," +
					"y_size SMALLINT(2), " +
					"x_center SMALLINT(2), " +
					"y_center SMALLINT(2)," + 
					"PRIMARY KEY(id_bloc)" +
//					"FOREIGN KEY (id_quart) REFERENCES quartier(id_quart)," +
//					"FOREIGN KEY (id_type) REFERENCES type(id_type)" +
					")");
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
	/** creation des batiments */
	private void createBatiment(Connection connect) {
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("CREATE TABLE IF NOT EXISTS batiment(" +
					"id_bat SMALLINT(3) NOT NULL, " +
					"id_type SMALLINT(2), " +
					"id_bloc SMALLINT(2), " +
					"id_faction SMALLINT(2)," +
					"x_size SMALLINT(2), " +
					"y_size SMALLINT(2), " +
					"x_center SMALLINT(2), " +
					"y_center SMALLINT(2), " + 
					"model VARCHAR(20), " +
					"PRIMARY KEY(id_bat) " +
//					"FOREIGN KEY (id_bloc) REFERENCES bloc(id_bloc), " +
//					"FOREIGN KEY (id_type) REFERENCES type(id_type), " +
//					"FOREIGN KEY (id_faction) REFERENCES faction(id_faction) " +
					")");
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
	/** creation des personnes */
	private void createPersonne(Connection connect) {
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("CREATE TABLE IF NOT EXISTS personne(" +
					"id_pers SMALLINT(10) NOT NULL, " +
					"id_quart SMALLINT(2)," +
					"nom VARCHAR(10), " +
					"prenom VARCHAR(10), " +
					"age SMALLINT(4), " +
					"esperance SMALLINT(4), " +
					"sexe VARCHAR(1), " +
					"qi FLOAT(4)," +
					"education FLOAT(4), " +
//					"id_prof SMALLINT(2), " +
					"id_fac SMALLINT(2), " +
					"niveau_prof SMALLINT(1), "+
					"niveau_fac SMALLINT(1), "+
					"vivant BOOLEAN, " +
					"PRIMARY KEY(id_pers)" +
//					"FOREIGN KEY (id_quart) REFERENCES quartier(id_quart)," +
//					"FOREIGN KEY (id_prof) REFERENCES profession(id_prof)" +
//					"FOREIGN KEY (id_fac) REFERENCES faction(id_fac)" +
					")");
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
	/** creation des factions */
	private void createFaction(Connection connect) {
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("CREATE TABLE IF NOT EXISTS faction(" +
					"id_faction SMALLINT(2) NOT NULL, " +
					"nom VARCHAR(20), "+
					"ration_energie DOUBLE, " +
					"ration_nourriture DOUBLE, " +
					"ration_soilent DOUBLE, " +
					"ration_habitation DOUBLE, " +
					"ration_education DOUBLE, " +
					"PRIMARY KEY(id_faction)" +
					")");
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
	/** creation des types */
	private void createType(Connection connect) {
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("CREATE TABLE IF NOT EXISTS type(" +
					"id_type SMALLINT(2) NOT NULL, " +
					"nom VARCHAR(10), " +
					"x_size SMALLINT(2), " +
					"y_size SMALLINT(2), " +
					"PRIMARY KEY(id_type)" +
					")");
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
	/** creation de toutes les clefs etrangères */
	
}
