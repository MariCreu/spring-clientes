package com.bolsadeideas.springboot.backend.apirest.controllers;

import com.bolsadeideas.springboot.backend.apirest.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")

public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    public List<Cliente> index(){
        return clienteService.findAll();
    }

    @GetMapping("/clientes/page/{page}")
    public Page<Cliente> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page,4);
        return clienteService.findAll(pageable);
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Cliente cliente=null;
        Map<String,Object> response= new HashMap<>();
        try{
            cliente= clienteService.findById(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(cliente==null){
            response.put("mensaje", "el cliente ID: ".concat(id.toString().concat(" no existe en la bbdd")));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(cliente, HttpStatus.OK);
    }
    @PostMapping("/clientes")
    public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result){
        Cliente clienteNuevo=null;
        Map<String,Object> response= new HashMap<>();
        if (result.hasErrors()){
            List<String> errors=  result.getFieldErrors()
                    .stream()
                    .map(err-> {
                        return  " el campo "+ err.getField()+ ": "+ err.getDefaultMessage();
                    })
                    .collect(Collectors.toList());
            response.put("mensaje","Error en los datos recibidos");
            response.put("error", errors);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            clienteNuevo= clienteService.save(cliente);
        }catch (DataAccessException e){
            response.put("mensaje","Error al realizar el insert en la bbdd");
            response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El cliente ha sido creado con éxito");
        response.put("cliente",clienteNuevo);
        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente,BindingResult result, @PathVariable Long id){
        Cliente clienteActual=clienteService.findById(id);
        Map<String,Object> response = new HashMap<>();
        if(result.hasErrors()){
            List<String> errors=  result.getFieldErrors()
                    .stream()
                    .map(err-> {
                        return  " el campo "+ err.getField()+ ": "+ err.getDefaultMessage();
                    })
                    .collect(Collectors.toList());
            response.put("mensaje","Error en los datos recibidos");
            response.put("error", errors);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
        if(clienteActual==null){
            response.put("mensaje", "No se ha encontrado ese cliente en la bbdd");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
        }
        try{
            clienteActual.setApellido(cliente.getApellido());
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setEmail(cliente.getEmail());
            Cliente clienteActualizado = clienteService.save(clienteActual);
            response.put("mensaje","El cliente se ha actualizado correctamente");
            response.put("cliente", clienteActualizado);
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
        }catch (DataAccessException e){
            response.put("mensaje", "error al actualizar el cliente");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Map<String,Object> response = new HashMap<>();
        try{
            clienteService.delete(id);
        }catch(DataAccessException e){
            response.put("mensaje", "No se ha podido borrar el cliente");
            response.put("error", e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "Cliente borrado");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
