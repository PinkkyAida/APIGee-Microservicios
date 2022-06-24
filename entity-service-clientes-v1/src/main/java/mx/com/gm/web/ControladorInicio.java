package mx.com.gm.web;

import lombok.extern.slf4j.Slf4j;
import mx.com.gm.domain.Persona;
import mx.com.gm.service.PersonaService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Objects;

import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import aj.org.objectweb.asm.Type;

@Controller
@Slf4j
public class ControladorInicio {

	@Autowired
	private PersonaService customerService;

	//Consulta
	@RequestMapping(value = "/Customers", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> inicio() throws FileNotFoundException, IOException, JSONException {
		JSONArray array = customerService.listaPersonas1();
		return new ResponseEntity<String>(array.toString(), HttpStatus.OK);

	}

	//Creación
	@RequestMapping(value = "/Customers", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Persona> guardar(@RequestBody Persona customer)
			throws FileNotFoundException, IOException, JSONException {
		Persona response = customerService.guardar(customer);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);

	}

	// Actualización
	@RequestMapping(value= "/Customers/{idCustomer}",method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<?> editar(@RequestBody Persona customer, @PathVariable int idCustomer)
			throws IOException, JSONException {
		customer = customerService.update(customer, idCustomer);
		if (Objects.isNull(customer)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
		} else {
			return ResponseEntity.status(HttpStatus.CREATED).body(customer);
		}
	}

	// Eliminación
	@RequestMapping(value= "/Customers/{idCustomer}",method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<?> eliminar(@PathVariable("idCustomer") @Valid int idCustomer)
			throws FileNotFoundException, IOException, JSONException {
		Boolean respuesta = customerService.eliminar(idCustomer);
		if (respuesta) {
			return ResponseEntity.status(HttpStatus.OK).body("Cliente Eliminado");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
		}

	}

}
