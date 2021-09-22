package cih.dsi.loans.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {
	private String url;
	private String username;
	private String pswd;
	
	public DaoFactory(String url, String username, String pswd) {
		this.url = url;
		this.username = username;
		this.pswd = pswd;
	}
	
	public static DaoFactory getInstance() {
		try{Class.forName("com.mysql.cj.jdbc.Driver");
		} 
		catch(ClassNotFoundException e) {
			e.printStackTrace(); 
		}
		return new DaoFactory("jdbc:mysql://localhost:3306/loans_users?serverTimezone=UTC", "root", "root");
		
	}
	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url,username,pswd);
		}
	
	public UtilisateurDao getUtilisateurDao() {
		return new UtilisateurDaoImpl(this);
	}
}









