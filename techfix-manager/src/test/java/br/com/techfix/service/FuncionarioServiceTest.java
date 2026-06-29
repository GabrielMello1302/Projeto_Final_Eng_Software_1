package br.com.techfix.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.techfix.model.Funcionario;
import br.com.techfix.repository.FuncionarioRepository;

class FuncionarioServiceTest {

    private FuncionarioRepository repository;
    private FuncionarioService service;

    @BeforeEach
    void setUp() {
        repository = new FuncionarioRepository();
        service = new FuncionarioService(repository);
    }

    @Test
    void deveCadastrarFuncionarioComSucesso() {
        Funcionario f = new Funcionario(
                0,
                "João",
                "joao@email.com",
                "123",
                true,
                br.com.techfix.model.Perfil.TECNICO,
                "M-1",
                "99999-0000",
                "Rua A"
        );

        Funcionario salvo = service.cadastrarFuncionario(f);

        assertNotNull(salvo);
        assertTrue(salvo.getId() > 0, "ID deve ser gerado automaticamente");
        assertEquals(1, salvo.getId());
        assertEquals(1, service.listarFuncionarios().size());
    }

    @Test
    void naoDeveCadastrarFuncionarioComEmailDuplicado() {
        Funcionario f1 = new Funcionario(
                0,
                "João",
                "dup@email.com",
                "123",
                true,
                br.com.techfix.model.Perfil.TECNICO,
                "M-1",
                "99999-0000",
                "Rua A"
        );

        Funcionario f2 = new Funcionario(
                0,
                "Maria",
                "dup@email.com",
                "456",
                true,
                br.com.techfix.model.Perfil.TECNICO,
                "M-2",
                "88888-1111",
                "Rua B"
        );

        service.cadastrarFuncionario(f1);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrarFuncionario(f2));
        assertEquals(1, service.listarFuncionarios().size());
    }

    @Test
    void naoDeveCadastrarFuncionarioComCamposObrigatoriosVazios() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> service.cadastrarFuncionario(
                        new Funcionario(0, "", "a@email.com", "123", true, br.com.techfix.model.Perfil.TECNICO, "M-1", "999", "Rua A")
                )),
                () -> assertThrows(IllegalArgumentException.class, () -> service.cadastrarFuncionario(
                new Funcionario(0, "Nome", "  ", "123", true, br.com.techfix.model.Perfil.TECNICO, "M-1", "999", "Rua A")
                )),
                () -> assertThrows(IllegalArgumentException.class, () -> service.cadastrarFuncionario(
                        new Funcionario(0, "Nome", "a@email.com", null, true, br.com.techfix.model.Perfil.TECNICO, "M-1", "999", "Rua A")
                ))
        );

        assertEquals(0, service.listarFuncionarios().size());
    }

    @Test
    void deveRealizarExclusaoLogicaComSucesso() {
        Funcionario f = new Funcionario(
                0,
                "João",
                "joao2@email.com",
                "123",
                true,
                br.com.techfix.model.Perfil.TECNICO,
                "M-1",
                "99999-0000",
                "Rua A"
        );

        Funcionario salvo = service.cadastrarFuncionario(f);
        int id = salvo.getId();

        assertEquals(1, service.listarFuncionarios().size());

        service.excluirFuncionario(id);

        assertEquals(0, service.listarFuncionarios().size(), "Excluído logicamente deve deixar de aparecer na listagem");
        Funcionario encontrado = service.buscarPorId(id);
        assertFalse(encontrado.isStatus(), "status deve ser false após exclusão lógica");
    }

    @Test
    void deveLancarExcecaoAoBuscarOuExcluirIdInexistente() {
        int idInexistente = 999;

        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(idInexistente));
        assertThrows(IllegalArgumentException.class, () -> service.excluirFuncionario(idInexistente));
    }
}

