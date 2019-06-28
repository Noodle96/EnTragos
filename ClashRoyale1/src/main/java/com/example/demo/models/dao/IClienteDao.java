package com.example.demo.models.dao;

//import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long> {
	/*
	public List<Cliente>  findAll();
	public void save(Cliente cliente);
	public Cliente findOne(Long id);
	public void delete(Long id);
	*/
}
