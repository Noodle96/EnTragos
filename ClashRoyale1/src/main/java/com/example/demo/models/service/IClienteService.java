package com.example.demo.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.models.entity.Cliente;
import com.example.demo.models.entity.Factura;
import com.example.demo.models.entity.Producto;


//service
public interface IClienteService {
	public List<Cliente>  findAll();
	//page similar a list ambas heredan de iterator
	public Page<Cliente>  findAll(Pageable pageable);
	public void save(Cliente cliente);
	public Cliente findOne(Long id);
	public Cliente fetchByIdWithFacturas(Long id);
	public void delete(Long id);
	public List<Producto> findByNombre(String term);
	public void saveFactura(Factura factura);
	public Producto findProductoById(Long id);
	public Factura findFacturaById(Long id);
	public void deleteFactura(Long id);
	public Factura fetchByIdWithClienteWhitItemFacturaWithProducto(Long id);
}
