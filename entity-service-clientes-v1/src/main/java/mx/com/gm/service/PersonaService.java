package mx.com.gm.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.gm.domain.Persona;

public interface PersonaService {

	public JSONArray listaPersonas1() throws FileNotFoundException, IOException, JSONException;

	public Persona guardar(Persona persona) throws FileNotFoundException, IOException, JSONException;

	public Boolean eliminar(int idPersona) throws FileNotFoundException, IOException, JSONException;

	public Persona update(Persona persona, int idPersona) throws IOException, JSONException;

}
