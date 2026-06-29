package br.com.techfix.model;

import java.time.LocalDate;

/**
 * Ordem de Serviço.
 */
public class OrdemServico {

    private int id;
    private String numeroOS;
    private String descricao;
    private LocalDate dataAbertura;
    private LocalDate dataFechamento;
    private StatusOS status;
    private String prioridade;
    private double orcamento;
    private Cliente cliente;
    private Funcionario tecnico;

    public OrdemServico() {
    }

    public OrdemServico(int id,
                         String numeroOS,
                         String descricao,
                         LocalDate dataAbertura,
                         LocalDate dataFechamento,
                         StatusOS status,
                         String prioridade,
                         double orcamento,
                         Cliente cliente,
                         Funcionario tecnico) {
        this.id = id;
        this.numeroOS = numeroOS;
        this.descricao = descricao;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.status = status;
        this.prioridade = prioridade;
        this.orcamento = orcamento;
        this.cliente = cliente;
        this.tecnico = tecnico;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroOS() {
        return numeroOS;
    }

    public void setNumeroOS(String numeroOS) {
        this.numeroOS = numeroOS;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDate getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDate dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public StatusOS getStatus() {
        return status;
    }

    public void setStatus(StatusOS status) {
        this.status = status;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public double getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(double orcamento) {
        this.orcamento = orcamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Funcionario getTecnico() {
        return tecnico;
    }

    public void setTecnico(Funcionario tecnico) {
        this.tecnico = tecnico;
    }
}

