package br.com.techfix.service;

import java.util.List;

import br.com.techfix.model.Cliente;
import br.com.techfix.repository.ClienteRepository;

/**
 * Service de regras de negócio para clientes.
 */
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("ClienteRepository não pode ser nulo");
        }
        this.repository = repository;
    }

    public Cliente cadastrarCliente(Cliente c) {
        if (c == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }

        validarObrigatorios(c);

        repository.buscarPorCpfCnpj(c.getCpfCnpj())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Já existe cliente cadastrado com o CPF/CNPJ informado");
                });

        // Se o status vier como false e a regra do MVP for de cadastro ativo,
        // ajustamos para ativo. Caso contrário, preservamos.
        if (!c.isStatus()) {
            c.setStatus(true);
        }

        return repository.salvar(c);
    }

    public Cliente atualizarCliente(Cliente c) {
        if (c == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        if (c.getId() <= 0) {
            throw new IllegalArgumentException("ID do cliente é obrigatório para atualizar");
        }

        validarObrigatorios(c);

        Cliente existente = repository.buscarPorId(c.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado para id=" + c.getId()));

        // Impede duplicidade: CPF/CNPJ deve ser único.
        repository.buscarPorCpfCnpj(c.getCpfCnpj())
                .ifPresent(found -> {
                    if (found.getId() != c.getId()) {
                        throw new IllegalArgumentException("Já existe cliente cadastrado com o CPF/CNPJ informado");
                    }
                });

        // Atualiza os campos permitidos.
        existente.setNome(c.getNome());
        existente.setCpfCnpj(c.getCpfCnpj());
        existente.setTelefone(c.getTelefone());
        existente.setEmail(c.getEmail());
        existente.setEndereco(c.getEndereco());
        existente.setStatus(c.isStatus());

        return repository.salvar(existente);
    }

    public void excluirCliente(int id) {
        Cliente c = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado para id=" + id));

        c.setStatus(false);
    }

    public Cliente buscarPorId(int id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado para id=" + id));
    }

    public List<Cliente> listarClientes() {
        return repository.listarTodos();
    }

    private static void validarObrigatorios(Cliente c) {
        if (isBlank(c.getNome())) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (isBlank(c.getCpfCnpj())) {
            throw new IllegalArgumentException("CPF/CNPJ é obrigatório");
        }
        if (isBlank(c.getEmail())) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

