package bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import tools.ParamsGlobals;


public class Database {

	public static Connection connect(){
		Connection connect = null;
		if(ParamsGlobals.DBSAVE){
	/*		String url = "jdbc:mysql://localhost:3306/oac";
	        String user = "root";
	        String password = "root";*/
			String url = "jdbc:mysql://synoga.dyndns.info:3306/OrdoAbChao";
	        String user = "cerealnyril";
	        String password = "wagonlit";
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connect = DriverManager.getConnection(url, user, password);
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}
		return connect;
	}
	public static void closeConnection(Connection connect){
		try {
		if (connect != null) {
			connect.close();
			}
		} catch (Exception e) {}
	}
	public static void closeStatement(Statement stm){
		try {
		if (stm != null) {
			stm.close();
			}
		} catch (Exception e) {}
	}
	public static void closeResultSet(ResultSet rs){
		try {
		if (rs != null) {
			rs.close();
			}
		} catch (Exception e) {}
	}
}
