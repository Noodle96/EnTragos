package com.example.demo.models.dao;

//import java.util.List;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.models.entity.Cliente;

// PagingAndSortingRepository entends CrudRepository
//sprig data contiene un componente para la paginacion,
//tanto para la persitencia(database)
public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {
	/*
	public List<Cliente>  findAll();
	public void save(Cliente cliente);
	public Cliente findOne(Long id);
	public void delete(Long id);
	*/
}
