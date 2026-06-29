package br.com.techfix.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import br.com.techfix.model.Funcionario;

/**
 * Repository em memória para {@link Funcionario}.
 */
public class FuncionarioRepository {

    private final List<Funcionario> funcionarios = new ArrayList<>();
    private int nextId = 1;

    /**
     * Salva um funcionário (atribuindo ID sequencial se necessário).
     */
    public Funcionario salvar(Funcionario f) {
        if (f == null) {
            throw new IllegalArgumentException("Funcionario não pode ser nulo");
        }

        // Se ainda não possui ID válido, atribui.
        if (f.getId() <= 0) {
            f.setId(nextId++);
        }

        funcionarios.add(f);
        return f;
    }

    /**
     * Lista apenas funcionários ativos (status == true).
     */
    public List<Funcionario> listarTodos() {
        return funcionarios.stream()
                .filter(Funcionario::isStatus)
                .sorted(Comparator.comparingInt(Funcionario::getId))
                .toList();
    }

    /**
     * Busca por id (inclui inclusive os inativos; validação de status fica a cargo do Service).
     */
    public Optional<Funcionario> buscarPorId(int id) {
        return funcionarios.stream()
                .filter(f -> f.getId() == id)
                .findFirst();
    }

    /**
     * Busca por e-mail (case-insensitive).
     */
    public Optional<Funcionario> buscarPorEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }

        String normalized = email.trim().toLowerCase();
        return funcionarios.stream()
                .filter(f -> f.getEmail() != null && f.getEmail().trim().toLowerCase().equals(normalized))
                .findFirst();
    }
}

