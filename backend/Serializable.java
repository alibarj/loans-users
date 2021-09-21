package cih.dsi.loans;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;

import cih.dsi.loans.beans.Utilisateur;

public interface Serializable {
	
	String serialize(Utilisateur u) throws JsonProcessingException;
	Utilisateur deserialize(String stream) throws Exception;

	
}
