package bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import social.Personne;
import structure.Batiment;
import structure.Bloc;
import structure.Quartier;

/** sauvegarde la ville */
public class DBSave {
/*===================================AJOUT D'ELEMENTS===================================*/
/*---------------------------------STRUCTURE DE LA VILLE--------------------------------*/
	/** ajoute une nouvelle ville */
	public static int insertVille(Connection connect, String nom, int age, int id){
		int clef = -1;
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("INSERT INTO ville" +
					"(id_ville, nom, age) " +
					"VALUES("+id+", '"+nom+"', "+age+") " ,
					Statement.RETURN_GENERATED_KEYS);
			stm.execute();
			ResultSet rs = stm.getGeneratedKeys();
			if ( rs.next() ) {clef = rs.getInt(1);}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
		return clef;
	}
	/** ajout d'un nouveau quartier*/
	public static void insertQuartier(Connection connect, Quartier quartier){
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("INSERT INTO quartier" +
					"(id_quart, id_ville, id_faction, nom, x_size, y_size, x_center, y_center) " +
					"VALUES("+quartier.getID()+", "+quartier.getIDVille()+", "+(-1)+", '"+quartier.getNom()+"', " +
							""+quartier.getTailleX()+", "+quartier.getTailleY()+", " +
							""+quartier.getCentreX()+", "+quartier.getCentreY()+") "
							);
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
	/** ajout de nouveaux blocs */
	public static void insertBloc(Connection connect, Bloc bloc){
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("INSERT INTO bloc" +
					"(id_bloc, id_type, id_quart, x_size, y_size, x_center, y_center) " +
					"VALUES("+bloc.getID()+", "+bloc.getType()+", "+bloc.getIDQuartier()+", " +
							""+bloc.getTailleX()+", "+bloc.getTailleY()+", "+bloc.getCentreX()+", "+bloc.getCentreY()+" )"
					);
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
	/** ajout de nouveaux batiments */
	public static void insertBat(Connection connect, Batiment bat){
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("INSERT INTO batiment" +
					"(id_bat, id_type, id_faction, id_bloc, x_size, y_size, x_center, y_center) " +
					"VALUES("+bat.getID()+", "+bat.getType()+", "+(-1)+", "+bat.getIDBloc()+", " +
							""+bat.getTailleX()+", "+bat.getTailleY()+", "+bat.getCentreX()+", "+bat.getCentreY()+") "
					);
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
	/** ajout de nouvelles personnes */
	public static void insertPersonne(Connection connect, Personne personne){
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("INSERT INTO personne" +
					"(id_pers, id_quart, nom, prenom, age, esperance, sexe, qi, education, id_fac, niveau_prof, niveau_fac, vivant) " +
					"VALUES("+personne.getId()+", "+personne.getIDQuartier()+", '"+personne.getNom()+"', " +
							"'"+personne.getPrenom()+"', "+personne.getAge()+", "+personne.getEsperanceVie()+", '"+personne.getSexe()+"', " +
							""+personne.getQI()+", "+personne.getEducation()+", "+(-1)+", "+0+", "+0+", '"+true+"') "
					);
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
/*========================================MISE A JOUR=======================================*/
	/*---------------------------------STRUCTURE DE LA VILLE--------------------------------*/
	/** met à jour les champs de la ville */
	public static void updateVille(Connection connect, int id, int age){
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("UPDATE ville " +
					"SET age = "+age+" " +
					"WHERE id_ville = "+id+" " +
					"");
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
	/** met le quartier à jour */
	public static void updateQuartier(Connection connect, Quartier quartier){
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("UPDATE quartier " +
					"SET x_size = "+quartier.getTailleX()+", " +
					"y_size = "+quartier.getTailleY()+", " +
					"x_center = "+quartier.getCentreX()+", " +
					"y_center = "+quartier.getCentreY()+" " +
					"WHERE id_quart = "+quartier.getID()+" " +
					"");
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
	/** met à jour le bloc */
	public static void updateBloc(Connection connect, Bloc bloc){
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("UPDATE bloc " +
					"SET x_size = "+bloc.getTailleX()+", " +
					"y_size = "+bloc.getTailleY()+", " +
					"x_center = "+bloc.getCentreX()+", " +
					"y_center = "+bloc.getCentreY()+" " +
					"WHERE id_bloc = "+bloc.getID()+" " +
					"");
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
	/** met à jour le batiment */
	public static void updateBatiment(Connection connect, Batiment batiment){
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("UPDATE batiment " +
					"SET id_type = "+batiment.getType()+", " +
					"x_size = "+batiment.getTailleX()+", " +
					"y_size = "+batiment.getTailleY()+", " +
					"x_center = "+batiment.getCentreX()+", " +
					"y_center = "+batiment.getCentreY()+" " +
					"WHERE id_bat = "+batiment.getID()+" " +
					"");
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
/*========================================UTILITAIRES=============================================*/
	/** supprime tout les champs */
	public static void deleteAll(Connection connect){
		PreparedStatement stm = null;
		try {
			stm = connect.prepareStatement("DELETE FROM ville");
			stm.execute();
			stm = connect.prepareStatement("DELETE FROM quartier");
			stm.execute();
			stm = connect.prepareStatement("DELETE FROM bloc");
			stm.execute();
			stm = connect.prepareStatement("DELETE FROM batiment");
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Database.closeStatement(stm);
	}
}
