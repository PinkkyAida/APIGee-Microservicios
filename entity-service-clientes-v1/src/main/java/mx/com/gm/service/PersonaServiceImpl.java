package mx.com.gm.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import mx.com.gm.domain.Persona;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Service
public class PersonaServiceImpl implements PersonaService {


	private String ruta = "\\workspace\\JsonSpring\\customers.json";
	

	@Override
	@Transactional
	public Persona guardar(Persona persona) throws IOException, JSONException {

		int id = 0;
		JSONArray json = read();

		if (json.length() == 0) { // No hay ningun elemento en la lista
			id = 0;
		} else {
			// Seteamos el ID para que sea autoincrementable
			id = (int) (json.getJSONObject(json.length() - 1).opt("idPersona")) + 1; // Obteniendo el id del ultimo
			persona.setIdPersona(id); // Se asigna Id

		}

		// Pequeña transformacion de un objeto java a Gson y por ultimo a JSONObject
		Gson gson = new Gson();
		String personaJson = gson.toJson(persona);
		JSONObject personaJson2 = new JSONObject(personaJson);
		json.put(personaJson2); // Se añade el nuevo objeto al array de jsons

		// Escribimos el nuevo objeto sobre el archivo Json

		write(json);

		// Regresamos el objeto persona para la respuesta en el controlador
		return persona;
	}

	@Override
	@Transactional
	public Boolean eliminar(int idPersona) throws IOException, JSONException {
		Boolean bandera = false;

		JSONArray json = read();

		// Buscamos el ID de la persona a eliminar

		for (int i = 0; i < json.length(); i++) {
			if (idPersona == (int) json.getJSONObject(i).opt("idPersona")) {
				json.remove(i);
				bandera = true;
				// Sobreescribir el archivo JSON y terminamos
				write(json);
				return bandera;

			} else {
				bandera = false;
			}
		}
		return bandera;

	}

	@Override
	@Transactional(readOnly = true)
	public JSONArray listaPersonas1() throws IOException, JSONException {
		// TODO Auto-generated method stub
		return read();

	}

	@Override
	@Transactional
	public Persona update(Persona persona, int idPersona) throws IOException, JSONException {
		int posicion = -1;
		

		System.out.println("Objeto " + persona);
		// Creamos el objeto persona de java para convertirlo a JSON
		Persona personaJson = new Persona();
		personaJson.setIdPersona(idPersona);
		personaJson.setNombre(persona.getNombre());
		personaJson.setEmail(persona.getEmail());
		personaJson.setApellido(persona.getApellido());
		personaJson.setTelefono(persona.getTelefono());

		System.out.println(personaJson);
		//Lectura y conversion del archivo a JSONArray
		JSONArray json = read();
		// Buscamos a la persona por ID
		for (int i = 0; i < json.length(); i++) {
			if (idPersona == (int) json.getJSONObject(i).opt("idPersona")) {
				posicion = i;
				JSONObject gson = new JSONObject(personaJson);
				json.put(i, gson);
				// Sobreescribimos el nuevo file JSON y terminamos
				write(json);
				return personaJson;

			} else {
				posicion = i;

			}

		}

		// Condicion para saber si recorrimos todo el arreglo de los usuarios y no
		// encontramos coincidencia
		// Eso indica que es un objeto nuevo a insertar
		if ((posicion < json.length())) {
			System.out.println("nuevo Objeto");
			JSONObject gson = new JSONObject(personaJson);
			json.put(gson);
			System.out.println(json);
			// Sobreescribimos el nuevo file JSON y terminamos
			write(json);
			return personaJson;
		}

		return personaJson;
	}

	public JSONArray read() throws IOException, JSONException {
		String content;
		// Leemos el archivo en un string
		BufferedReader reader = new BufferedReader(new FileReader(ruta));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		String ls = System.getProperty("line.separator");
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		// delete the last new line separator
		// Comprobar el archivo vacio
		System.out.println(stringBuilder.length());
		if (stringBuilder.length() - 1 <= 0) {
			reader.close();
			content = "[]";
		} else {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			reader.close();
			content = stringBuilder.toString();

		}
		// convert to json array
		JSONArray json = new JSONArray(content);
		return json;
	}

	public void write(JSONArray json) {
		try {
			String test = json.toString();
			File file = new File(ruta);

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(test);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
