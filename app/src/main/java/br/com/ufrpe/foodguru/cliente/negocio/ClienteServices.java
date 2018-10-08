package br.com.ufrpe.foodguru.cliente.negocio;

import br.com.ufrpe.foodguru.cliente.dominio.Cliente;
import br.com.ufrpe.foodguru.cliente.persistencia.ClienteDAO;

public class ClienteServices {
    private ClienteDAO clienteDAO = new ClienteDAO();
    public boolean adicionarCliente(Cliente cliente) {
        return clienteDAO.adicionarCliente(cliente);
    }
}
