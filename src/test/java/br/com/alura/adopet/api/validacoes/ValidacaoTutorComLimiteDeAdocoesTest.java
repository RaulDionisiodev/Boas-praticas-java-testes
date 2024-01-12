package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComLimiteDeAdocoesTest {

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private TutorRepository tutorRepository;

    @InjectMocks
    private ValidacaoTutorComLimiteDeAdocoes validacao;

    @Test
    void deveriaDispararExceptionQuantoOTutorTiverCincoOuMaisAdocoesAprovads() {
        SolicitacaoAdocaoDto dto = getDto();
        List<Adocao> adocaoList = getAdocoes();
        Tutor tutor = adocaoList.get(0).getTutor();

        adocaoList.get(4).marcarComoAprovada();

        BDDMockito.given(adocaoRepository.findAll()).willReturn(adocaoList);
        BDDMockito.given(tutorRepository.getReferenceById(123L)).willReturn(tutor);

        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    void deveriaPermitirAdocaoQuantoOTutorTiverMenosDeCincoAdocoesAprovads() {
        SolicitacaoAdocaoDto dto = getDto();
        List<Adocao> adocaoList = getAdocoes();
        Tutor tutor = adocaoList.get(0).getTutor();

        BDDMockito.given(adocaoRepository.findAll()).willReturn(adocaoList);
        BDDMockito.given(tutorRepository.getReferenceById(123L)).willReturn(tutor);

        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

    private List<Adocao> getAdocoes() {
        Tutor tutor = getTutor();

        Adocao adocao1 = new Adocao(tutor, new Pet(), "Motivo 1");
        adocao1.marcarComoAprovada();
        Adocao adocao2 = new Adocao(tutor, new Pet(), "Motivo 2");
        adocao2.marcarComoAprovada();
        Adocao adocao3 = new Adocao(tutor, new Pet(), "Motivo 3");
        adocao3.marcarComoAprovada();
        Adocao adocao4 = new Adocao(tutor, new Pet(), "Motivo 4");
        adocao4.marcarComoAprovada();
        Adocao adocao5 = new Adocao(tutor, new Pet(), "Motivo 5");

        return new ArrayList<>(Arrays.asList(adocao1, adocao2, adocao3, adocao4, adocao5));
    }

    private Tutor getTutor() {
        return new Tutor();
    }

    public SolicitacaoAdocaoDto getDto() {
        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(1L, 123L, "Motivo Qualquer");
        return dto;
    }

}