package br.com.techfix.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import br.com.techfix.model.Cliente;

/**
 * Repository em memória para {@link Cliente}.
 */
public class ClienteRepository {

    private final List<Cliente> clientes = new ArrayList<>();
    private int nextId = 1;

    /**
     * Salva o cliente.
     * - Se o ID for <= 0, atribui ID sequencial.
     * - Se já existir um cliente com mesmo ID, substitui.
     */
    public Cliente salvar(Cliente c) {
        if (c == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }

        if (c.getId() <= 0) {
            c.setId(nextId++);
            clientes.add(c);
            return c;
        }

        // Atualiza se já existe.
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == c.getId()) {
                clientes.set(i, c);
                return c;
            }
        }

        // Se não existe, insere.
        clientes.add(c);
        if (c.getId() >= nextId) {
            nextId = c.getId() + 1;
        }
        return c;
    }

    /**
     * Lista apenas clientes ativos (status == true), ordenados por ID.
     */
    public List<Cliente> listarTodos() {
        return clientes.stream()
                .filter(Cliente::isStatus)
                .sorted(Comparator.comparingInt(Cliente::getId))
                .toList();
    }

    public Optional<Cliente> buscarPorId(int id) {
        return clientes.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    /**
     * Busca por CPF/CNPJ (case-insensitive).
     */
    public Optional<Cliente> buscarPorCpfCnpj(String cpfCnpj) {
        if (cpfCnpj == null) {
            return Optional.empty();
        }

        String normalized = cpfCnpj.trim().toLowerCase();
        return clientes.stream()
                .filter(c -> c.getCpfCnpj() != null && c.getCpfCnpj().trim().toLowerCase().equals(normalized))
                .findFirst();
    }
}

