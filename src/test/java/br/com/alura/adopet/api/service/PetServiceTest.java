package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @InjectMocks
    PetService service;

    @Mock
    PetRepository repository;

    @Captor
    ArgumentCaptor<Pet> captor;

    @Test
    void deveriaCadastrarPet() {
        Abrigo abrigo = new Abrigo(new CadastroAbrigoDto("Abrigo 1", "21333333333", "email@mail.com"));
        CadastroPetDto dto = new CadastroPetDto(TipoPet.GATO, "Gatuno", "raça", 5, "azul", 7.5f);

        service.cadastrarPet(abrigo, dto);

        then(repository).should().save(captor.capture());
        Pet pet = captor.getValue();

        assertEquals(pet.getNome(), dto.nome());
        assertEquals(TipoPet.GATO, pet.getTipo());
        assertEquals(pet.getIdade(), dto.idade());
        assertEquals(pet.getPeso(), dto.peso());
        assertEquals(pet.getRaca(), dto.raca());
        assertEquals(pet.getAbrigo().getNome(), abrigo.getNome());

    }

}