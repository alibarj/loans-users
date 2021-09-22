package cih.dsi.loans;


import org.json.JSONException;
import org.json.JSONObject; //Used to recieve json data in doPost

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; //used to send data in doGet

import cih.dsi.loans.beans.Utilisateur;


public class JsonSerializer implements Serializable {

	private ObjectMapper mapper;

	public JsonSerializer(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public String serialize(Utilisateur u) throws JsonProcessingException{

		try {
			String json = mapper.writeValueAsString(u);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw e;
		}

	}
	

	public Utilisateur deserialize(String stream) throws Exception{
		try {
			JSONObject jsonObject = new JSONObject(stream);
			Utilisateur u = mapper.convertValue(jsonObject, Utilisateur.class);
			return u;
			

		}catch (Exception e) {
			e.printStackTrace();
			throw e;
			}
		
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}
}
