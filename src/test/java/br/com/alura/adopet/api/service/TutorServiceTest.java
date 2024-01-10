package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @InjectMocks
    private TutorService service;

    @Mock
    private TutorRepository repository;

    @Captor
    ArgumentCaptor<Tutor> captor;

    @Test
    void deveriaCasatrarTutor() {
        CadastroTutorDto dto = new CadastroTutorDto("Tutor", "21 22222222", "tutor@email.com");
        Tutor tutor = new Tutor(dto);

        given(repository.existsByTelefoneOrEmail(dto.telefone(), dto.email())).willReturn(false);

        service.cadastrar(dto);

        then(repository).should().save(captor.capture());
        Tutor expected = captor.getValue();

        assertEquals(tutor.getNome(), expected.getNome());
        assertEquals(tutor.getEmail(), expected.getEmail());
        assertEquals(tutor.getTelefone(), expected.getTelefone());
    }

    @Test
    void deveriaDispararExceptionSeOTutorJaEstiverCadastrado() {
        CadastroTutorDto dto = new CadastroTutorDto("Tutor", "21 22222222", "tutor@email.com");
        Tutor tutor = new Tutor(dto);

        given(repository.existsByTelefoneOrEmail(dto.telefone(), dto.email())).willReturn(true);

        assertThrows(ValidacaoException.class, () -> service.cadastrar(dto));
    }

    @Test
    void deveriaAtualizarTutor() {
        CadastroTutorDto dto = new CadastroTutorDto("Tutor", "21 22222222", "tutor@email.com");
        Tutor tutor = new Tutor(dto);

        AtualizacaoTutorDto dtoToUpdate = new AtualizacaoTutorDto(1L,"Novo Tutor", "21 33333333", "novoTutor@email.com");

        given(repository.getReferenceById(1L)).willReturn(tutor);

        service.atualizar(dtoToUpdate);

        then(repository).should().save(captor.capture());

        Tutor expected = captor.getValue();

        assertEquals(expected.getNome(), dtoToUpdate.nome());
        assertEquals(expected.getTelefone(), dtoToUpdate.telefone());
        assertEquals(expected.getEmail(), dtoToUpdate.email());
    }

}