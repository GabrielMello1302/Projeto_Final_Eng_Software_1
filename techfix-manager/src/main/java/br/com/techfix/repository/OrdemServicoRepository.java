package br.com.techfix.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import br.com.techfix.model.OrdemServico;

/**
 * Repository em memória para {@link OrdemServico}.
 */
public class OrdemServicoRepository {

    private final List<OrdemServico> ordens = new ArrayList<>();
    private int nextId = 1;

    /**
     * Salva ou atualiza uma ordem.
     */
    public OrdemServico salvar(OrdemServico os) {
        if (os == null) {
            throw new IllegalArgumentException("OrdemServico não pode ser nula");
        }

        if (os.getId() <= 0) {
            os.setId(nextId++);
            ordens.add(os);
            return os;
        }

        for (int i = 0; i < ordens.size(); i++) {
            if (ordens.get(i).getId() == os.getId()) {
                ordens.set(i, os);
                return os;
            }
        }

        ordens.add(os);
        if (os.getId() >= nextId) {
            nextId = os.getId() + 1;
        }
        return os;
    }

    public Optional<OrdemServico> buscarPorId(int id) {
        return ordens.stream().filter(os -> os.getId() == id).findFirst();
    }

    public List<OrdemServico> listarTodas() {
        return ordens.stream()
                .sorted(Comparator.comparingInt(OrdemServico::getId))
                .toList();
    }
}

