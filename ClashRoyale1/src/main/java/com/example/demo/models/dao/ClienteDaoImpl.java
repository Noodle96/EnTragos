/*
package com.example.demo.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.Cliente;

@Repository //marcar la clase como componente de persistencia "jpa"
public class ClienteDaoImpl implements IClienteDao {
	@PersistenceContext //inyecta el EntityManager segun la config de la unidad de persistencia ->h2,mysql
	private EntityManager em;
	
	
	
	
	//retorna todos los clientes en una lista
	@SuppressWarnings("unchecked")
	@Override
	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return em.createQuery("from Cliente").getResultList();
	}
	
	
	//guarda un cliente o un la actualiza - trought param
	@Override
	public void save(Cliente cliente) {
		//primero se ejecuta el prepersist OJO
		if(cliente.getId() != null && cliente.getId() > 0) {
			//actualizar datos existentes, atacha al contexto de persistencia y lo actualiza 
			em.merge(cliente);
		}
		else {
			//insert new client , insert, atacha al contexto de persistencia 
			em.persist(cliente);
		}
	}
	
	
	//buscar Cliente con id
	@Override
	public Cliente findOne(Long id) {
		return em.find(Cliente.class,id); //jpa va a LA BASE DE DATOS  y retorna el objeto cliente(id)
	}
	
	
	
	//eliminar un cliente con id
	@Override
	public void delete(Long id) {
		em.remove(findOne(id));
	}

}
*/
