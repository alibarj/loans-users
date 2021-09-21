package cih.dsi.loans.beans;

import java.sql.Date;
import java.util.ArrayList;

public class Utilisateur {
	
	public class Profil{
		private String code = null;
		private Date dateValidite = null;
		
		public Profil() {}
		public Profil(String code, Date date) {
			this.code = code;
			this.dateValidite = date;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public Date getDateValidite() {
			return dateValidite;
		}

		public void setDateValidite(Date date) {
			this.dateValidite = date;
		}
	}
	
	public static class Evenement {
		
		private String code;
		private String nivHabilitation;
		private Date date;
		
		public Evenement() {}
		
		public Evenement(String code, String nivHabilitation, Date date) {
			this.code = code;
			this.nivHabilitation = nivHabilitation;
			this.date = date;
		}
		
		public void clone(Evenement ev) {
			this.code = ev.code;
			this.nivHabilitation = ev.nivHabilitation;
			this.date = ev.date;
			
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getNivHabilitation() {
			return nivHabilitation;
		}

		public void setNivHabilitation(String nivHabilitation) {
			this.nivHabilitation = nivHabilitation;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		
	}
	
	private String id;
	private String nom;
	private String prenom;
	private String pswd;
	private String service;
	private Profil profil;
	private ArrayList<Evenement> evenements;
	

	public Utilisateur() {}
	
	public Utilisateur(String id, String nom, String prenom, String pswd, String service, Profil profil, ArrayList<Evenement> evs) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.pswd = pswd;
		this.service = service;
		this.profil = profil;
		this.evenements = evs;
	}
	

	public void clone(Utilisateur utilisateur) {
		this.id = utilisateur.id;
		this.nom = utilisateur.nom;
		this.prenom = utilisateur.prenom;
		this.pswd = utilisateur.pswd;
		this.service = utilisateur.service;
		this.profil = utilisateur.profil;
	}
	
	public Profil getProfil() {
		return profil;
	}
	public void setProfil(Profil profil) {
		this.profil = profil;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getPswd() {
		return pswd;
	}
	public void setPswd(String pswd) {
		this.pswd = pswd;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	
	public ArrayList<Evenement> getEvenements() {
		return evenements;
	}

	public void setEvenements(ArrayList<Evenement> evenements) {
		this.evenements = evenements;
	}
	public void addEvenements(Evenement ev) {
		this.evenements.add(ev);
	}
	
	
}
