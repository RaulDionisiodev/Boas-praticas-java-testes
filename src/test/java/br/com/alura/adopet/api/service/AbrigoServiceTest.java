package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

    @Mock
    private AbrigoRepository abrigoRepository;

    @InjectMocks
    private AbrigoService service;

    @Captor
    ArgumentCaptor<Abrigo> abrigoArgumentCaptor;

    @Test
    void deveriaCadastrarAbrigoCasoAindaNaoSejaCadastrado () {

        CadastroAbrigoDto dto = new CadastroAbrigoDto("Abrigo 1", "21333333333", "email@mail.com");

        given(abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email())).willReturn(false);

        service.cadatrar(dto);

        then(abrigoRepository).should().save(abrigoArgumentCaptor.capture());
        Abrigo abrigo = abrigoArgumentCaptor.getValue();
        assertEquals(abrigo.getNome(), dto.nome());
        assertEquals(abrigo.getEmail(), dto.email());
        assertEquals(abrigo.getTelefone(), dto.telefone());
    }

    @Test
    void deveriaDispararExceptionAoCadastrarAbrigoJaCadastrado() {
        CadastroAbrigoDto dto = new CadastroAbrigoDto("Abrigo 1", "21333333333", "email@mail.com");

        given(abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email())).willReturn(true);

        assertThrows(ValidacaoException.class, () -> service.cadatrar(dto));
    }

    @Test
    void deveriaCarregarAbrigoPorId() {
        Optional<Abrigo> abrigo = Optional.of(
            new Abrigo(new CadastroAbrigoDto("Abrigo 1", "21333333333", "email@mail.com")));

        given(abrigoRepository.findById(1L)).willReturn(abrigo);

        Abrigo abrigoResult = service.carregarAbrigo("1");

        Abrigo expected = abrigo.get();
        assertEquals(abrigoResult.getNome(), expected.getNome());
        assertEquals(abrigoResult.getTelefone(), expected.getTelefone());
        assertEquals(abrigoResult.getEmail(), expected.getEmail());
    }

    @Test
    void deveriaCarregarAbrigoPorNome() {
        Optional<Abrigo> abrigo = Optional.of(
            new Abrigo(new CadastroAbrigoDto("Abrigo 1", "21333333333", "email@mail.com")));

        given(abrigoRepository.findByNome("Abrigo 1")).willReturn(abrigo);

        Abrigo abrigoResult = service.carregarAbrigo("Abrigo 1");

        Abrigo expected = abrigo.get();
        assertEquals(abrigoResult.getNome(), expected.getNome());
        assertEquals(abrigoResult.getTelefone(), expected.getTelefone());
        assertEquals(abrigoResult.getEmail(), expected.getEmail());
    }

    @Test
    void deveriaDispararExceptionAoNaoEncontrarAbrigo() {
        Optional<Abrigo> abrigo = Optional.empty();

        given(abrigoRepository.findById(1L)).willReturn(abrigo);

        assertThrows(ValidacaoException.class, () -> service.carregarAbrigo("1"));

    }

}