package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class ValidacaoPetComAdocaoEmAndamentoTest {

    @InjectMocks
    private ValidacaoPetComAdocaoEmAndamento validacao;
    @Mock
    private AdocaoRepository adocaoRepository;

    @Test
    void deveriaDispararExceptionCasoOPetEstejaAguardandoAvaliacao() {

        //Arrange
        BDDMockito.given(adocaoRepository.existsByPetIdAndStatus(1l, StatusAdocao.AGUARDANDO_AVALIACAO)).willReturn(true);
        SolicitacaoAdocaoDto dto = getDto();

        //Act + Assert
        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    void deveriaPermitirAdocaoCasoOPetNaoEstejaAguardandoAvaliacao() {

        //Arrange
        BDDMockito.given(adocaoRepository.existsByPetIdAndStatus(1l, StatusAdocao.AGUARDANDO_AVALIACAO)).willReturn(false);

        //Act + Assert
        Assertions.assertDoesNotThrow(() -> validacao.validar(getDto()));
    }


    public SolicitacaoAdocaoDto getDto() {
        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(1l, 123l, "Motivo Qualquer");
        return dto;
    }

}