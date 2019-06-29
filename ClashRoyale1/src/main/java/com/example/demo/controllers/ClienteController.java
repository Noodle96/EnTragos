package com.example.demo.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.entity.Cliente;
import com.example.demo.models.service.IClienteService;
import com.example.demo.util.paginator.PageRender;

@Controller //marcar la clase como controlador
public class ClienteController {
	
	@Autowired //para selecciona el bean correcto
	private IClienteService clienteService;
	
	
	//mostrar el listado de los clientes
	@RequestMapping(value="/listar", method= RequestMethod.GET )//ir a esa vista listar.jsp
	public String listar( @RequestParam(name="page", defaultValue = "0") int page,  Model model) {
		Pageable pageRequest = 	PageRequest.of(page, 5);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender("/listar",clientes);
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("Clientes", clientes);
		model.addAttribute("page", pageRender);
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
	public String guardar(@Valid   Cliente cliente, BindingResult result, Model mode,RedirectAttributes flash) {
		if(result.hasErrors()) {
			mode.addAttribute("titulo", "formulario Clientes errors");
			return "form";
		}
		String mensajeFlash = (cliente.getId() != null)? "Cliente editado con éxito!" : "Cliente creado con éxito!";
		clienteService.save(cliente);
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}
	
	
	
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id ,Map<String, Object> model,RedirectAttributes flash) {
		Cliente cliente = null;
		if(id > 0) {
			cliente = clienteService.findOne(id);
			if(cliente == null) {
				flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD!");
				return "redirect:/listar";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero!");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Clientes");
		return "form";
	}
	
	@RequestMapping(value="/eliminar/{id}")
	public  String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		if(id > 0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito!");
		}
		return "redirect:/listar";
	}
	
	
	
}
