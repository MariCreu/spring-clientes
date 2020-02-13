package com.bolsadeideas.springboot.backend.apirest.repository;

import com.bolsadeideas.springboot.backend.apirest.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClienteDao extends JpaRepository<Cliente, Long> {
}
