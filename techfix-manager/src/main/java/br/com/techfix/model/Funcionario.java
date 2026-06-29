package br.com.techfix.model;

/**
 * Funcionário do sistema.
 */
public class Funcionario extends Usuario {

    private String matricula;
    private String telefone;
    private String endereco;
    private boolean status;

    public Funcionario(int id,
                        String nome,
                        String email,
                        String senha,
                        boolean status,
                        Perfil perfil,
                        String matricula,
                        String telefone,
                        String endereco) {
        super(id, nome, email, senha, status, perfil);
        this.status = status;
        this.matricula = matricula;
        this.telefone = telefone;
        this.endereco = endereco;
    }


    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public boolean isStatus() {
        return status;
    }

    @Override
    public void setStatus(boolean status) {
        this.status = status;
        super.setStatus(status);
    }
}

