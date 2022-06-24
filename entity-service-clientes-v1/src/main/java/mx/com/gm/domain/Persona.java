package mx.com.gm.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.json.JSONObject;

import lombok.Data;

@Data
public class Persona implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idPersona;

	@NotEmpty // Para trabajar con cadenas ya que valida que la cadena no sea vacia en cambio
				// notnull si acepta cadenas vacias
	private String nombre;
	@NotEmpty
	private String apellido;
	@NotEmpty
	private String email;
	@NotEmpty
	private String telefono;
}
