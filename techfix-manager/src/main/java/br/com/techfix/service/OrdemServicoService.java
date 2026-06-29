package br.com.techfix.service;

import java.time.LocalDate;

import br.com.techfix.model.Cliente;
import br.com.techfix.model.Funcionario;
import br.com.techfix.model.OrdemServico;
import br.com.techfix.model.StatusOS;
import br.com.techfix.repository.OrdemServicoRepository;

/**
 * Regras de negócio para Ordens de Serviço.
 */
public class OrdemServicoService {

    private final OrdemServicoRepository repository;

    public OrdemServicoService(OrdemServicoRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("OrdemServicoRepository não pode ser nulo");
        }
        this.repository = repository;
    }

    /**
     * criarOS(Cliente cliente, String descricao, String prioridade)
     */
    public OrdemServico criarOS(Cliente cliente, String descricao, String prioridade) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }
        if (isBlank(descricao)) {
            throw new IllegalArgumentException("Descrição é obrigatória");
        }

        // Primeiro cria com ID sequencial para gerar numeroOS com base no ID.
        OrdemServico os = new OrdemServico();
        os.setCliente(cliente);
        os.setDescricao(descricao);
        os.setPrioridade(prioridade);
        os.setStatus(StatusOS.ABERTA);
        os.setDataAbertura(LocalDate.now());
        os.setOrcamento(0.0);

        repository.salvar(os);

        int ano = LocalDate.now().getYear();
        os.setNumeroOS(gerarNumeroOS(ano, os.getId()));

        return repository.salvar(os);
    }

    /**
     * atribuirTecnico(int idOS, Funcionario tecnico)
     */
    public OrdemServico atribuirTecnico(int idOS, Funcionario tecnico) {
        if (tecnico == null) {
            throw new IllegalArgumentException("Técnico é obrigatório");
        }

        OrdemServico os = repository.buscarPorId(idOS)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada para id=" + idOS));

        if (os.getStatus() == StatusOS.FINALIZADA) {
            throw new IllegalArgumentException("Não é possível atribuir técnico em OS finalizada");
        }

        if (tecnico.getPerfil() != br.com.techfix.model.Perfil.TECNICO) {
            throw new IllegalArgumentException("O funcionário atribuído deve possuir o perfil de TECNICO");
        }

        os.setTecnico(tecnico);


        os.setStatus(StatusOS.EM_EXECUCAO);

        return repository.salvar(os);
    }

    /**
     * emitirOrcamento(int idOS, double valor)
     */
    public OrdemServico emitirOrcamento(int idOS, double valor) {
        OrdemServico os = repository.buscarPorId(idOS)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada para id=" + idOS));

        if (os.getStatus() == StatusOS.FINALIZADA) {
            throw new IllegalArgumentException("Não é possível emitir orçamento em OS finalizada");
        }

        os.setOrcamento(valor);
        return repository.salvar(os);
    }

    /**
     * finalizarOS(int idOS)
     */
    public OrdemServico finalizarOS(int idOS) {
        OrdemServico os = repository.buscarPorId(idOS)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada para id=" + idOS));

        if (os.getStatus() == StatusOS.FINALIZADA) {
            throw new IllegalArgumentException("OS já está finalizada");
        }

        os.setStatus(StatusOS.FINALIZADA);
        os.setDataFechamento(LocalDate.now());
        return repository.salvar(os);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String gerarNumeroOS(int ano, int id) {
        // Formato: OS-YYYY-000001
        return String.format("OS-%d-%06d", ano, id);
    }
}

