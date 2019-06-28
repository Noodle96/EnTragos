package com.example.demo.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.models.entity.Cliente;
import com.example.demo.models.service.IClienteService;

@Controller //marcar la clase como controlador
public class ClienteController {
	
	@Autowired //para selecciona el bean correcto
	private IClienteService clienteService;
	
	
	//mostrar el listado de los clientes
	@RequestMapping(value="/listar", method= RequestMethod.GET )//ir a esa vista listar.jsp
	public String listar( Model model) {
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("Clientes", clienteService.findAll());
		return "listar";
		
	}
	
	
	//manda el objecto cliente al form.jsp
	@RequestMapping(value="/form" ,method = RequestMethod.GET) //get			
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente",cliente);
		model.put("titulo", "formulario Cliente");
		return "form";
	}
	
	
	//guardar a un cliente a la base de datos
	//@valid  habilita la validacion en el objeto mapeado al form
	@RequestMapping(value="/form", method = RequestMethod.POST) //post
	public String guardar(@Valid   Cliente cliente, BindingResult result, Model mode) {
		if(result.hasErrors()) {
			mode.addAttribute("titulo", "formulario Clientes errors");
			return "form";
		}
		clienteService.save(cliente);
		return "redirect:listar";
	}
	
	
	
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id ,Map<String, Object> model) {
		Cliente cliente = null;
		if(id > 0) {
			cliente = clienteService.findOne(id);
		}else {
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Clientes");
		return "form";
	}
	
	@RequestMapping(value="/eliminar/{id}")
	public  String eliminar(@PathVariable(value="id") Long id) {
		if(id > 0) {
			clienteService.delete(id);
		}
		return "redirect:/listar";
	}
	
	
	
}
