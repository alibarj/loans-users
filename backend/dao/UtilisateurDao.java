package cih.dsi.loans.dao;

import java.sql.SQLException;
import java.util.List;
import cih.dsi.loans.beans.Utilisateur;

public interface UtilisateurDao {

	List<Utilisateur> getAllUtilisateurs () throws SQLException;
	Utilisateur getUtilisateur(String id) throws SQLException;
	int addUtilisateur(Utilisateur utilisateur) throws SQLException;
	int updateUtilisateur(Utilisateur utilisateur) throws SQLException;
	int deleteUtilisateur(String id) throws SQLException;
}
