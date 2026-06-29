package br.com.techfix.service;

import java.util.List;

import br.com.techfix.model.Funcionario;
import br.com.techfix.repository.FuncionarioRepository;

/**
 * Service de regras de negócio para funcionários.
 */
public class FuncionarioService {

    private final FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("FuncionarioRepository não pode ser nulo");
        }
        this.repository = repository;
    }

    /**
     * Cadastra um funcionário.
     *
     * Regras:
     * - E-mail deve ser único (não pode já existir).
     * - Campos obrigatórios: nome, email, senha não podem ser vazios/nulos.
     */
    public Funcionario cadastrarFuncionario(Funcionario f) {
        if (f == null) {
            throw new IllegalArgumentException("Funcionario não pode ser nulo");
        }

        validarObrigatorios(f);

        repository.buscarPorEmail(f.getEmail()).ifPresent(existing -> {
            throw new IllegalArgumentException("Já existe funcionário cadastrado com o e-mail informado");
        });

        return repository.salvar(f);
    }

    /**
     * Exclusão lógica: altera status para false.
     */
    public void excluirFuncionario(int id) {
        Funcionario f = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado para id=" + id));

        f.setStatus(false);
    }

    public Funcionario buscarPorId(int id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado para id=" + id));
    }

    public List<Funcionario> listarFuncionarios() {
        return repository.listarTodos();
    }

    private static void validarObrigatorios(Funcionario f) {
        if (isBlank(f.getNome())) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (isBlank(f.getEmail())) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (isBlank(f.getSenha())) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

