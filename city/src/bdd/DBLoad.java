package bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** charge la ville et ses composants depuis la base de donnée, fonction de chargement totale et de chargement 
 * partiels pour les types par exemple  */
public class DBLoad {
	
	/** verifie si il existe une ville avec cette identifiant dans la base de donnée */
	public static boolean existVille(Connection connect, int id){
		int result = -1;
		if(connect != null){
			PreparedStatement stm = null;
			ResultSet rs = null;
			try {
				stm = connect.prepareStatement("SELECT id_ville FROM ville where id_ville ="+id);
				rs = stm.executeQuery();
				while(rs.next()){
					result = rs.getInt("id_ville");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Database.closeStatement(stm);
			if(result != -1){
				return true;
			}
		}
		return false;
	}
	/** retourne le dernier identifiant de la table ville */
	public static int getMaxIDVille(Connection connect){
		int result = 0;
		if(connect != null){
			PreparedStatement stm = null;
			ResultSet rs = null;
			try {
				stm = connect.prepareStatement("SELECT max(id_ville) FROM ville");
				rs = stm.executeQuery();
				while(rs.next()){
					result = rs.getInt("id_ville");
				}
			} catch (SQLException e) {
			}
			Database.closeStatement(stm);
		}
		return result;
	}
	/** retourne le dernier identifiant de la table quartier */
	public static int getMaxIDQuartier(Connection connect){
		int result = 0;
		if(connect != null){
			PreparedStatement stm = null;
			ResultSet rs = null;
			try {
				stm = connect.prepareStatement("SELECT max(id_quart) AS last FROM quartier");
				rs = stm.executeQuery();
				while(rs.next()){
					result = rs.getInt("last");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Database.closeStatement(stm);
		}
		return result;
	}
	/** retourne le dernier identifiant de la table bloc */
	public static int getMaxIDBloc(Connection connect){
		int result = 0;
		if(connect != null){
			PreparedStatement stm = null;
			ResultSet rs = null;
			try {
				stm = connect.prepareStatement("SELECT max(id_bloc) as last FROM bloc");
				rs = stm.executeQuery();
				while(rs.next()){
					result = rs.getInt("last");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Database.closeStatement(stm);
		}
		return result;
	}
	/** retourne le dernier identifiant de la table batiment */
	public static int getMaxIDBatiment(Connection connect){
		int result = 0;
		if(connect != null){
			PreparedStatement stm = null;
			ResultSet rs = null;
			try {
				stm = connect.prepareStatement("SELECT max(id_bat) as last FROM batiment");
				rs = stm.executeQuery();
				while(rs.next()){
					result = rs.getInt("last");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Database.closeStatement(stm);
		}
		return result;
	}
}
