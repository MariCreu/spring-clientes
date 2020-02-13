package com.bolsadeideas.springboot.backend.apirest.services;

import com.bolsadeideas.springboot.backend.apirest.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.repository.IClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDao iClienteDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return (List<Cliente>) iClienteDao.findAll();
    }

    @Override
    public Page<Cliente> findAll(org.springframework.data.domain.Pageable pageable) {
        return iClienteDao.findAll(pageable);
    }

//     @Override
//     @Transactional(readOnly = true)

//     public Page<Cliente> findAll(Pageable pageable) {
//         return iClienteDao.findAll(pageable);
//     }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        return iClienteDao.save(cliente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
    iClienteDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return iClienteDao.findById(id).orElse(null);
    }


}
