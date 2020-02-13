package com.bolsadeideas.springboot.backend.apirest.services;

import com.bolsadeideas.springboot.backend.apirest.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {
    public Cliente save(Cliente cliente);
    public List<Cliente> findAll();
    public Page<Cliente> findAll(Pageable pageable);
    public void delete(Long id);
    public Cliente findById(Long id);

}
