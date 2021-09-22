package cih.dsi.loans.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cih.dsi.loans.beans.Utilisateur;
import cih.dsi.loans.beans.Utilisateur.Evenement;;

public class UtilisateurDaoImpl implements UtilisateurDao {

	DaoFactory daoFactory;

	public UtilisateurDaoImpl(DaoFactory daofactory) {
		this.daoFactory = daofactory;
	}

	@Override
	public List<Utilisateur> getAllUtilisateurs() throws SQLException{

		List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
		//Initialisation les objets JDBC avec null pour eviter l'exception 'may not be intialized' dans le bloc finally
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement prep1 = null;
		ResultSet rs1 = null;
		PreparedStatement prep2 = null;
		ResultSet rs2 = null;

		String sqlQuery = "select UTILISATEUR_ID, NOM, PSWD, PRENOM, SERVICE,"
				+ " PROFIL_CODE, DATE_VALIDATION from utilisateur natural join utilisateur_profil";
		String sqlQuery2 = "select EVENEMENT_CODE, NIV_HABILITATION, DATE_VALIDATION from evenement"
				+ " natural join profil_evenement natural join profil natural join utilisateur_profil where UTILISATEUR_ID=?";
		String sqlQuery3 = "select EVENEMENT_CODE, NIV_HABILITATION, DATE_VALIDATION from utilisateur_evenement where UTILISATEUR_ID=?";

		try {
			con = daoFactory.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);

			while (rs.next()) {
				Utilisateur utilisateur = new Utilisateur();
				ArrayList<Utilisateur.Evenement> evenements = new ArrayList<Utilisateur.Evenement>();

				prep1 = con.prepareStatement(sqlQuery2);
				prep1.setString(1, rs.getString("UTILISATEUR_ID"));
				rs1 = prep1.executeQuery();
				while (rs1.next()) {
					evenements.add(new Evenement(rs1.getString("EVENEMENT_CODE"), rs1.getString("NIV_HABILITATION"),
							rs1.getDate("DATE_VALIDATION")));
				}

				prep2 = con.prepareStatement(sqlQuery3);
				prep2.setString(1, rs.getString("UTILISATEUR_ID"));
				rs2 = prep2.executeQuery();
				while (rs2.next()) {
					evenements.add(new Evenement(rs2.getString("EVENEMENT_CODE"), rs2.getString("NIV_HABILITATION"),
							rs2.getDate("DATE_VALIDATION")));
				}

				Utilisateur.Profil profil = utilisateur.new Profil(rs.getString("PROFIL_CODE"),
						rs.getDate("DATE_VALIDATION"));

				utilisateur.setNom(rs.getString("NOM"));
				utilisateur.setPrenom(rs.getString("PRENOM"));
				utilisateur.setService(rs.getString("SERVICE"));
				utilisateur.setId(rs.getString("UTILISATEUR_ID"));
				utilisateur.setProfil(profil);
				utilisateur.setEvenements(evenements);
				utilisateurs.add(utilisateur);
			}
			
		} catch (SQLException e) {
			System.out.println("Error code: "+e.getErrorCode());
			System.out.println(e.getMessage());
//			utilisateurs = null;
			throw e;
			
		} finally {
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException sqlEx) {
				} // ignore

				rs2 = null;
			}

			if (prep2 != null) {
				try {
					prep2.close();
				} catch (SQLException sqlEx) {
				} // ignore

				prep2 = null;
			}

			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException sqlEx) {
				} // ignore

				rs1 = null;
			}

			if (prep1 != null) {
				try {
					prep1.close();
				} catch (SQLException sqlEx) {
				} // ignore

				prep1 = null;
			}

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore

				rs = null;
			}

			if (st != null) {
				try {
					st.close();
				} catch (SQLException sqlEx) {
				} // ignore

				st = null;
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException sqlEx) {
				} // ignore

				con = null;
			}

		}

		return utilisateurs;
	}

	@Override
	public Utilisateur getUtilisateur(String id) throws SQLException {

		Connection con = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		Utilisateur utilisateur = new Utilisateur();
		Utilisateur.Profil profil = utilisateur.new Profil();
		ArrayList<Utilisateur.Evenement> evenements = new ArrayList<Utilisateur.Evenement>();

		String sqlQuery = "select UTILISATEUR_ID, NOM, PSWD, PRENOM, SERVICE, PROFIL_CODE,"
				+ " DATE_VALIDATION from utilisateur natural join utilisateur_profil where UTILISATEUR_ID=?";
		String sqlQuery2 = "select EVENEMENT_CODE, NIV_HABILITATION, DATE_VALIDATION from evenement"
				+ " natural join profil_evenement natural join profil natural join utilisateur_profil where UTILISATEUR_ID=?";
		String sqlQuery3 = "select EVENEMENT_CODE, NIV_HABILITATION, DATE_VALIDATION from utilisateur_evenement where UTILISATEUR_ID=?";

		try {
			con = daoFactory.getConnection();
			prep = con.prepareStatement(sqlQuery);
			prep.setString(1, id);
			rs = prep.executeQuery();

			if (rs.next()) {
				
				profil.setCode(rs.getString("PROFIL_CODE"));
				profil.setDateValidite(rs.getDate("DATE_VALIDATION"));
				utilisateur.setNom(rs.getString("NOM"));
				utilisateur.setPrenom(rs.getString("PRENOM"));
				utilisateur.setService(rs.getString("SERVICE"));
				utilisateur.setId(rs.getString("UTILISATEUR_ID"));
				utilisateur.setProfil(profil);
				utilisateur.setEvenements(evenements);

				prep.close();
				rs.close();
				prep = con.prepareStatement(sqlQuery2);
				prep.setString(1, id);
				rs = prep.executeQuery();
				while (rs.next()) {
					utilisateur.addEvenements(new Evenement(rs.getString("EVENEMENT_CODE"),
							rs.getString("NIV_HABILITATION"), rs.getDate("DATE_VALIDATION")));
				}

				prep.close();
				rs.close();
				prep = con.prepareStatement(sqlQuery3);
				prep.setString(1, id);
				rs = prep.executeQuery();
				while (rs.next()) {
					utilisateur.addEvenements(new Evenement(rs.getString("EVENEMENT_CODE"),
							rs.getString("NIV_HABILITATION"), rs.getDate("DATE_VALIDATION")));
				}

			} else {
				
				utilisateur = null;
			}
		}

		catch (SQLException e) {
			System.out.println(e.getErrorCode());
			System.out.println(e.getMessage());
			utilisateur = null;
			throw e;

		}

		finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore

				rs = null;
			}

			if (prep != null) {
				try {
					prep.close();
				} catch (SQLException sqlEx) {
				} // ignore

				prep = null;
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException sqlEx) {
				} // ignore

				con = null;
			}

		}

		return utilisateur;
	}

	@Override
	public int addUtilisateur(Utilisateur utilisateur) throws SQLException {
		Connection con = null;
		PreparedStatement prep = null;
		int result = 0;
		PreparedStatement prep2 = null;
		int result2 = 0;
		PreparedStatement prep3 = null;
		int result3 = 0;
		String sqlQuery = "INSERT INTO UTILISATEUR VALUES (?, ?, ?, ?, ?)";
		String sqlQuery2 = "INSERT INTO UTILISATEUR_PROFIL VALUES (?, ?, ?)";
		String sqlQuery3 = "INSERT INTO UTILISATEUR_EVENEMENT VALUES (?, ?, ?, ?)";

		try {
			con = daoFactory.getConnection();
			con.setAutoCommit(false);

			prep = con.prepareStatement(sqlQuery);
			prep.setString(1, utilisateur.getId());
			prep.setString(2, utilisateur.getNom());
			prep.setString(3, utilisateur.getPrenom());
			prep.setString(4, utilisateur.getPswd());
			prep.setString(5, utilisateur.getService());
			result = prep.executeUpdate();

			prep2 = con.prepareStatement(sqlQuery2);
			prep2.setString(1, utilisateur.getId());
			prep2.setString(2, utilisateur.getProfil().getCode());
			prep2.setDate(3, utilisateur.getProfil().getDateValidite());
			result2 = prep2.executeUpdate();

			if (utilisateur.getEvenements() != null) {
				for (Evenement ev : utilisateur.getEvenements()) {
					prep3 = con.prepareStatement(sqlQuery3);
					prep3.setString(1, utilisateur.getId());
					prep3.setString(2, ev.getCode());
					prep3.setDate(3, ev.getDate());
					prep3.setString(4, ev.getNivHabilitation());
					result3 = prep3.executeUpdate();
					
				}
			} else {
				result3 = 1;
			}

			con.commit();

		}

		catch (SQLException e) {
			System.out.println(e.getErrorCode());
			System.out.println("Error Message: " + ((SQLException) e).getMessage());
			if (con != null) {
				try {
					System.out.println("Transaction en cours d'annulation");
					con.rollback();
					System.out.println("Transaction annule");
				} catch (SQLException excep) {
					excep.printStackTrace();
					System.out.println(excep.getMessage() + " \n La transaction n'a pas ete annule");
				}
			}
			throw e;
		}


		finally {

			if (prep != null) {
				try {
					prep.close();
				} catch (SQLException sqlEx) {
				} // ignore

				prep = null;
			}

			if (prep2 != null) {
				try {
					prep2.close();
				} catch (SQLException sqlEx) {
				} // ignore

				prep2 = null;
			}

			if (prep3 != null) {
				try {
					prep3.close();
				} catch (SQLException sqlEx) {
				} // ignore

				prep3 = null;
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException sqlEx) {
				} // ignore

				con = null;
			}

		}

		return result & result2 & result3;
	}
	
	public int updateUtilisateur(Utilisateur u) throws SQLException{
		Connection con = null;
		PreparedStatement prep = null;
		int result = 0;
		PreparedStatement prep2 = null;
		int result2 = 0;
//		PreparedStatement prep3 = null;
//		int result3 = 0;
		String sqlQuery = "UPDATE UTILISATEUR SET UTILISATEUR_ID=?, NOM=?, PRENOM=?, PSWD=?, SERVICE=? WHERE UTILISATEUR_ID = ?";
		String sqlQuery2 = "UPDATE UTILISATEUR_PROFIL SET UTILISATEUR_ID=?, PROFIL_CODE=?, DATE_VALIDATION=? WHERE UTILISATEUR_ID = ?";
//		String sqlQuery3 = "UPDATE UTILISATEUR_EVENEMENT SET UTILISATEUR_ID=?, EVENEMENT_CODE=?, DATE_VALIDATION=?, NIV_HABILITATION=? WHERE UTILISATEUR_ID = ?";
		
		try {
			con = daoFactory.getConnection();
			con.setAutoCommit(false);

			prep = con.prepareStatement(sqlQuery);
			prep.setString(1, u.getId());
			prep.setString(2, u.getNom());
			prep.setString(3, u.getPrenom());
			prep.setString(4, u.getPswd());
			prep.setString(5, u.getService());
			prep.setString(6, u.getId());
			result = prep.executeUpdate();

			prep2 = con.prepareStatement(sqlQuery2);
			prep2.setString(1, u.getId());
			prep2.setString(2, u.getProfil().getCode());
			prep2.setDate(3, u.getProfil().getDateValidite());
			prep2.setString(4, u.getId());
			result2 = prep2.executeUpdate();

//			if (u.getEvenements() != null) {
//				for (Evenement ev : u.getEvenements()) {
//					prep3 = con.prepareStatement(sqlQuery3);
//					prep3.setString(1, u.getId());
//					prep3.setString(2, ev.getCode());
//					prep3.setDate(3, ev.getDate());
//					prep3.setString(4, ev.getNivHabilitation());
//					prep3.setString(5, u.getId());
//					result3 = prep3.executeUpdate();
//					
//				}
//			} else {
//				result3 = 1;
//			}

			con.commit();

		}
		
		catch (SQLException e) {
			System.out.println(e.getErrorCode());
			System.out.println("Error Message: " + ((SQLException) e).getMessage());
			if (con != null) {
				try {
					System.out.println("Transaction en cours d'annulation");
					con.rollback();
					System.out.println("Transaction annule");
				} catch (SQLException excep) {
					excep.printStackTrace();
					System.out.println(excep.getMessage() + " \n La transaction n'a pas ete annule");
				}
			}
			throw e;
		}
		
		finally {

			if (prep != null) {
				try {
					prep.close();
				} catch (SQLException sqlEx) {
				} // ignore

				prep = null;
			}

			if (prep2 != null) {
				try {
					prep2.close();
				} catch (SQLException sqlEx) {
				} // ignore

				prep2 = null;
			}

//			if (prep3 != null) {
//				try {
//					prep3.close();
//				} catch (SQLException sqlEx) {
//				} // ignore
//
//				prep3 = null;
//			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException sqlEx) {
				} // ignore

				con = null;
			}

		}

		return result & result2;
	}
	
	public int deleteUtilisateur(String id) throws SQLException{
		Connection con = null;
		PreparedStatement prep = null;
		int result = 0;
		String sqlQuery = "DELETE FROM UTILISATEUR WHERE UTILISATEUR_ID = ?";
		
		try {
			con = daoFactory.getConnection();

			prep = con.prepareStatement(sqlQuery);
			prep.setString(1, id);
			
			result = prep.executeUpdate();
		}
		
		catch (SQLException e) {
			System.out.println(e.getErrorCode());
			System.out.println("Error Message: " + ((SQLException) e).getMessage());
			
			throw e;
		}
		
		finally {
			if (prep != null) {
				try {
					prep.close();
				} catch (SQLException sqlEx) {
				} // ignore

				prep = null;
			}
			
			if (con != null) {
				try {
					con.close();
				} catch (SQLException sqlEx) {
				} // ignore

				con = null;
			}
		}
		
		return result;
	}
		
}


