package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.entity.Cliente;
import com.example.demo.models.entity.Factura;
import com.example.demo.models.entity.Producto;
import com.example.demo.models.service.IClienteService;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
	
	@Autowired
	IClienteService clienteService;
	
	@GetMapping("/form/{clienteId}")
	public String crear( @PathVariable(value="clienteId") Long clienteId,
			Map<String, Object> model,
			RedirectAttributes flash){
		
		Cliente cliente = clienteService.findOne(clienteId);
		if(cliente ==null) {
			flash.addFlashAttribute("error", "El cliente no existe en DDBB");
			return "redirect:/listar";
		}
		Factura factura = new Factura();
		factura.setCliente(cliente);
		model.put("factura", factura);
		model.put("titulo", "Crear Factura");
		return "factura/form";
	}
	
	
	//ResponseBody suprime una vista al thymeleaf
	// y trasforma la salida a json
	@GetMapping(value="/cargar-productos/{term}", produces= {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term){
		return clienteService.findByNombre(term);
	}
}

