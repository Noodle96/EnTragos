package com.example.demo.controllers;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.entity.Cliente;
import com.example.demo.models.service.IClienteService;
import com.example.demo.models.service.IUploadFileService;
import com.example.demo.util.paginator.PageRender;

@Controller // marcar la clase como controlador
@SessionAttributes("cliente")
public class ClienteController {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired // para selecciona el bean correcto
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadFileService;

	
	
	
	
	@Secured("ROLE_USER")
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "atachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
	
	
	
	
	
	@Secured("ROLE_USER")
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = clienteService.fetchByIdWithFacturas(id);  //findOne(id);
		if (cliente == null) {
			flash.addFlashAttribute("error", "El Cliente no existe en la BBDD");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Detalle Cliente " + cliente.getNombre());
		return "ver";
	}
	
	

	// mostrar el listado de los clientes
	@RequestMapping(value = {"/listar","/"}, method = RequestMethod.GET) // ir a esa vista listar.jsp
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, 
			HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			log.info("Bienvenido Usuario con SecurityContextHolder.getContext().getAuthentication(); : ".concat(auth.getName()));
		}
		
		//De la forma programatica
		if(hasRole("ROLE_ADMIN")) {
			log.info("hola Usuario ".concat(auth.getName()).concat(" tienes acceso"));
		}else {
			log.info("hola Usuario ".concat(auth.getName()).concat(" No tienes acceso"));
		}
		
		//De la forma con SecurityContextHolderAwareRequestWrapper
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		if(securityContext.isUserInRole("ADMIN")) {
			log.info("hola Usuario forma SecurityContextHolderAwareRequestWrapper ".concat(auth.getName()).concat(" tienes acceso"));
		}
		else {
			log.info("hola Usuario forma SecurityContextHolderAwareRequestWrapper ".concat(auth.getName()).concat(" No tienes acceso"));
		}
		
		//De la forma con request
		if(request.isUserInRole("ROLE_ADMIN")) {
			log.info("hola Usuario forma HttpServletRequest ".concat(auth.getName()).concat(" tienes acceso"));
		}
		else {
			log.info("hola Usuario forma HttpServletRequest ".concat(auth.getName()).concat(" No tienes acceso"));
		}
		
		
		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("Clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";

	}

	
	
	// manda el objecto cliente al form.jsp
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.GET) // get
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "formulario Cliente");
		return "form";
	}

	
	
	// guardar a un cliente a la base de datos
	// @valid habilita la validacion en el objeto mapeado al form
	// SessionStatus status
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST) // post
	public String guardar(@Valid Cliente cliente, BindingResult result, Model mode,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		
		if (result.hasErrors()) {
			mode.addAttribute("titulo", "formulario Clientes errors");
			return "form";
		}
		// log.info("INIT GUARDAR: " + cliente.toString());
		if (!foto.isEmpty()) {
			// para edicion podemos editar la foto , entonces eliminamos la que ya no sirv
			/*
			 * cliente.getId() > 0 && cliente.getFoto() != null &&
			 * cliente.getFoto().length() > 0
			 */
			if (cliente.getId() != null && cliente.getFoto() != null && cliente.getId() > 0
					&& cliente.getFoto().length() > 0) {
				uploadFileService.delete(cliente.getFoto());
			}

			// Path directorioRecursos = Paths.get("src//main//resources//static/uploads");
			// String rootPath = directorioRecursos.toFile().getAbsolutePath();
			// String rootPath = "//home//russel//uploads";

			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			flash.addFlashAttribute("info",
					"Has subido correctamente ' " + uniqueFilename + "'" + " para " + cliente.getApellido());
			log.info("ANTES SET FOTO: " + cliente.toString());
			cliente.setFoto(uniqueFilename);
			log.info("DESPUES SET FOTO: " + cliente.toString());

		}

		String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito!" + cliente.getNombre()
				: "Cliente creado con éxito!" + cliente.getNombre();
		log.info("ANTES DE SAVE: "+cliente.toString());
		clienteService.save(cliente);
		status.setComplete();
		log.info("DESPUES DE SAVE: " +cliente.toString());
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	
	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD!");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero!");
			return "redirect:/listar";
		}
		log.info("FINDONE: " + cliente.toString());
		model.put("cliente", cliente);
		model.put("titulo", "Editar Clientes");
		return "form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito!");

			if(cliente.getFoto() != null && cliente.getFoto().length() > 0) {
				if (uploadFileService.delete(cliente.getFoto())) {
					flash.addFlashAttribute("info", "foto " + cliente.getFoto() + " delete succesfully");
				}
			}
			

		}
		return "redirect:/listar";
	}
	
	private boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();
		if(context ==null) {
			return false;
		}
		Authentication auth = context.getAuthentication();
		if(auth==null) {
			return false;
		}
		Collection<? extends GrantedAuthority> autorities = auth.getAuthorities();
		return autorities.contains(new SimpleGrantedAuthority(role));
		/*
		for(GrantedAuthority autority: autorities  ) {
			if(role.equals(autority.getAuthority())) {
				log.info("hola Usuario ".concat(auth.getName()).concat(" tu role es: ").concat(autority.getAuthority()));
				return true;
			}
		}
		return false;
		*/
	}
	

}
