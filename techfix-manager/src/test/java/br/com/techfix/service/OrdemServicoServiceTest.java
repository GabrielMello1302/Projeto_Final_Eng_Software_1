package br.com.techfix.service;

import br.com.techfix.model.Cliente;
import br.com.techfix.model.Funcionario;
import br.com.techfix.model.OrdemServico;
import br.com.techfix.model.Perfil;
import br.com.techfix.model.StatusOS;
import br.com.techfix.repository.OrdemServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrdemServicoServiceTest {

    private OrdemServicoRepository repository;
    private OrdemServicoService service;

    private Cliente cliente;
    private Funcionario tecnico;

    @BeforeEach
    void setUp() {
        repository = new OrdemServicoRepository();
        service = new OrdemServicoService(repository);

        cliente = new Cliente(0, "Cliente Teste", "12345678900", "1199999-0000", "cliente@email.com", "Rua X", true);
        tecnico = new Funcionario(
                0,
                "Tecnico 01",
                "tecnico@email.com",
                "123",
                true,
                Perfil.TECNICO,
                "M-1",
                "1199999-0001",
                "Rua Y"
        );
    }

    @Test
    void deveCriarOrdemServicoComSucesso() {
        OrdemServico os = service.criarOS(cliente, "Conserto do equipamento", "ALTA");

        assertNotNull(os);
        assertNotNull(os.getNumeroOS());
        assertTrue(os.getNumeroOS().matches("OS-\\d{4}-\\d{6}"), "Formato esperado: OS-YYYY-000001");

        assertEquals(StatusOS.ABERTA, os.getStatus());
        assertEquals(LocalDate.now(), os.getDataAbertura());

        int expectedYear = LocalDate.now().getYear();
        assertTrue(os.getNumeroOS().startsWith("OS-" + expectedYear + "-"));
    }

    @Test
    void deveAtribuirTecnicoComSucesso() {
        OrdemServico os = service.criarOS(cliente, "Conserto", "MEDIA");

        OrdemServico atualizada = service.atribuirTecnico(os.getId(), tecnico);

        assertEquals(StatusOS.EM_EXECUCAO, atualizada.getStatus());
        assertNotNull(atualizada.getTecnico());
        assertEquals(tecnico, atualizada.getTecnico());
    }

    @Test
    void naoDeveAtribuirUsuarioComPerfilInvalido() {
        OrdemServico os = service.criarOS(cliente, "Conserto", "MEDIA");

        Funcionario atendenteInvalido = new Funcionario(
                0,
                "Atendente 01",
                "atendente@email.com",
                "123",
                true,
                Perfil.ATENDENTE,
                "M-2",
                "1199999-0002",
                "Rua Z"
        );

        assertThrows(IllegalArgumentException.class, () -> service.atribuirTecnico(os.getId(), atendenteInvalido));
    }

    @Test
    void deveEmitirOrcamentoComSucesso() {
        OrdemServico os = service.criarOS(cliente, "Conserto", "BAIXA");

        OrdemServico atualizada = service.emitirOrcamento(os.getId(), 150.75);

        assertEquals(150.75, atualizada.getOrcamento());
        assertEquals(StatusOS.ABERTA, atualizada.getStatus());

        // impede se estiver finalizada
        OrdemServico finalizada = service.finalizarOS(os.getId());
        assertEquals(StatusOS.FINALIZADA, finalizada.getStatus());

        assertThrows(IllegalArgumentException.class, () -> service.emitirOrcamento(os.getId(), 999.0));
    }

    @Test
    void deveFinalizarOSComSucesso() {
        OrdemServico os = service.criarOS(cliente, "Conserto", "MEDIA");

        OrdemServico finalizada = service.finalizarOS(os.getId());

        assertEquals(StatusOS.FINALIZADA, finalizada.getStatus());
        assertEquals(LocalDate.now(), finalizada.getDataFechamento());
    }

    @Test
    void naoPermitirModificacoesEmOSFinalizada() {
        OrdemServico os = service.criarOS(cliente, "Conserto", "MEDIA");
        service.finalizarOS(os.getId());

        assertThrows(IllegalArgumentException.class, () -> service.atribuirTecnico(os.getId(), tecnico));
        assertThrows(IllegalArgumentException.class, () -> service.emitirOrcamento(os.getId(), 10.0));

        // finalizar novamente deve falhar
        assertThrows(IllegalArgumentException.class, () -> service.finalizarOS(os.getId()));
    }
}

