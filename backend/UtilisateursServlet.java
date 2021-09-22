package cih.dsi.loans;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cih.dsi.loans.beans.Utilisateur;
import cih.dsi.loans.dao.DaoFactory;
import cih.dsi.loans.dao.UtilisateurDao;

import org.json.JSONException;
import org.json.JSONObject; //Used to recieve json data in doPost

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; //used to send data in doGet
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

@WebServlet({ "/users", "/users/*" })
public class UtilisateursServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	UtilisateurDao utilisateurDao;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    
		String requestUrl = request.getRequestURI();
		String requestedUser;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(df);
		Serializable serializer = new JsonSerializer(mapper);

		try {
			requestedUser = requestUrl.substring("loans_managers/users/".length() + 1);
		} 
		catch (StringIndexOutOfBoundsException e) {
			requestedUser = null;
		}
		
		if ((requestedUser == null) || requestedUser.isEmpty()) {
			
			System.out.println("Requesting all users...");
			List<Utilisateur> utilisateurs;
			try {
				utilisateurs = utilisateurDao.getAllUtilisateurs();

				response.setStatus(200);
					for (int i = 0; i < utilisateurs.size(); i++) {
		
						try {
							String json = serializer.serialize(utilisateurs.get(i));
							response.getOutputStream().println(json);
						}
						catch (JsonProcessingException e) {
							System.out.println("Un probleme est survenu dans une serialisation json: "+e.getMessage());
						}
					}
					
					System.out.println("Returned "+utilisateurs.size()+" users");
					
			} catch (SQLException e) {
				
				System.out.println("Could not fetch users");
				response.setStatus(500);
				if (e.getErrorCode()==0) {
					response.getWriter().write("Une erreur est survenue, La base de données est indisponnible");
				}
				else {
				response.getWriter().write("Une erreur est survenue, veuillez réessayer. "+e.getMessage());
				}
				
			}
			
				
				
			
		}
		
		else {
			
			System.out.println("Requesting user: " + requestedUser);
			try {
				Utilisateur utilisateur = utilisateurDao.getUtilisateur(requestedUser);
				
				if (utilisateur != null) {
					System.out.println("User found");
					try {
						String json = serializer.serialize(utilisateur);
						response.setStatus(200);
						response.getOutputStream().println(json);
					}catch(JsonProcessingException e) {
						response.setStatus(500);
						response.getWriter().write("Un probleme est survenu dans une serialisation json: "+e.getMessage());
					}
				}
				else {
					System.out.println("User not found");
					response.sendError(404);
				}
			}
			catch (SQLException e) {
				System.out.println("Could not fetch user");
				response.setStatus(500);
				response.getWriter().write("Une erreur est survenue, veuillez réessayer. "+e.getMessage());
			}
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		response.setCharacterEncoding("UTF-8");
		StringBuffer jb = new StringBuffer();
		String line = null;
		Utilisateur utilisateur = null;
		ObjectMapper mapper = new ObjectMapper().registerModule(new JsonOrgModule());
		Serializable serializer = new JsonSerializer(mapper);

		try {
			
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		}

		catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
			response.getWriter().write("Un problème est survenu: "+e.getMessage());
			return;
		}


		try {
			utilisateur = serializer.deserialize(jb.toString());
		}catch (Exception e) {
			response.setStatus(400);
			response.getWriter().write("Le format JSON est erroné");
			return;
		}
		if (utilisateur==null || utilisateur.getId()==null || utilisateur.getPswd()==null || utilisateur.getProfil().getCode()==null || utilisateur.getProfil().getDateValidite()==null) {
			System.out.println("Les données fournies sont incompletes");
			response.setStatus(400);
			response.getWriter().write("Les données fournies sont incompletes");
		}
		else {
			try {
				System.out.println(utilisateurDao.addUtilisateur(utilisateur));
				response.setStatus(200);
				response.getWriter().write("L'utilisateur a été ajouté");
			} catch (SQLException e) {
				System.out.println("Action echouee");
				if (e.getErrorCode() == 1062) {
					response.setStatus(400);
					response.getWriter().write("Cet Id est deja utilisé.");
				}
				else if(e.getErrorCode() == 1452) {
					response.setStatus(400);
					response.getWriter().write("Le profil ou un evenement renseigné n'existe pas.");
				}
				else {
					response.setStatus(500);
					response.getWriter().write("Un problème est survenu: "+e.getMessage());
				}
			} 
		}
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)

			throws ServletException, IOException {
		
		response.setCharacterEncoding("UTF-8");
		StringBuffer jb = new StringBuffer();
		String line = null;
		Utilisateur utilisateur = null;
		ObjectMapper mapper = new ObjectMapper().registerModule(new JsonOrgModule());
		Serializable serializer = new JsonSerializer(mapper);

		try {
			
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		}

		catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
			response.getWriter().write("Un problème est survenu: "+e.getMessage());
			return;
		}


		try {
			utilisateur = serializer.deserialize(jb.toString());
		}catch (Exception e) {
			response.setStatus(400);
			response.getWriter().write("Le format JSON est erroné");
			return;
		}
		if (utilisateur==null || utilisateur.getId()==null || utilisateur.getPswd()==null || utilisateur.getProfil().getCode()==null || utilisateur.getProfil().getDateValidite()==null) {
			System.out.println("Les donnees fournies sont incompletes");
			response.setStatus(400);
			response.getWriter().write("Les données fournies sont incompletes");
		}
		else {
			try {
				System.out.println(utilisateurDao.updateUtilisateur(utilisateur));
				response.setStatus(200);
				response.getWriter().write("L'utilisateur a été modifié");
			} catch (SQLException e) {
				System.out.println("Action echouée");
				if(e.getErrorCode() == 1452) {
					response.setStatus(400);
					response.getWriter().write("Le profil ou un evenement renseigné n'existe pas.");
				}
				else {
					response.setStatus(500);
					response.getWriter().write("Un problème est survenu: "+e.getMessage());
				}
			} 
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
		
		response.setCharacterEncoding("UTF-8");
		String requestedUser;
		String requestUrl = request.getRequestURI();
		try {
			requestedUser = requestUrl.substring("loans_managers/users/".length() + 1);
			System.out.println(requestedUser);
			int result = utilisateurDao.deleteUtilisateur(requestedUser);
			if (result==0) {
				response.setStatus(400);
				response.getWriter().write("Cet utilisateur n'existe pas, verifiez son id");
			}
			else {
				response.setStatus(200);
				response.getWriter().write("Utilisateur supprimé");
			}
		} 
		catch (StringIndexOutOfBoundsException e) {
			System.out.println("Veuillez preciser un id dans l'URL");
			response.setStatus(400);
			response.getWriter().write("Veuillez preciser un id dans l'URL");
		}
		catch (SQLException e) {
			response.setStatus(500);
			System.out.println("Un probleme est survenu dans une serialisation json: "+e.getMessage());
		}
	}
	
	@Override
	public void init() throws ServletException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		utilisateurDao = daoFactory.getUtilisateurDao();
	}

}
