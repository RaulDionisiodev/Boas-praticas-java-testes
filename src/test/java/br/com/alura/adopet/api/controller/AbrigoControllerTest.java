package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AbrigoDto;
import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.dto.PetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.PetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
class AbrigoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AbrigoService abrigoService;

    @MockBean
    private PetService petService;

    @Autowired
    private JacksonTester<CadastroAbrigoDto> json;

    @Autowired
    private JacksonTester<CadastroPetDto> jsonPet;

    @Test
    void deveriaListarAbrigos () throws Exception {
        AbrigoDto abrigoDto = new AbrigoDto(1L, "Abrigo");
        given(abrigoService.listar()).willReturn(List.of(abrigoDto));

        String expected = "[{\"id\":1,\"nome\":\"Abrigo\"}]";

        var response = mvc.perform(
            get("/abrigos")
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals(expected, response.getContentAsString());
    }

    @Test
    void deveriaDispararExceptionEmCasoDeCadastroComBodyInvalido() throws Exception {
        String body = "{}";

        var response = mvc.perform(
            post("/abrigos")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();


        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaPermitirCadastro() throws Exception {
        CadastroAbrigoDto dto = new CadastroAbrigoDto("Abrigo teste", "2111111111", "abrigo@email.com");

        var response = mvc.perform(
            post("/abrigos")
                .content(json.write(dto).getJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaListarPets() throws Exception {
        PetDto petDto = new PetDto(1L, TipoPet.GATO, "Gatuno", "Gato da Gabby", 5);
        given(abrigoService.listarPetsDoAbrigo("1")).willReturn(List.of(petDto));

        String expectedString = "[{\"id\":1,\"tipo\":\"GATO\",\"nome\":\"Gatuno\",\"raca\":\"Gato da Gabby\",\"idade\":5}]";

        var response = mvc.perform(
            get("/abrigos/1/pets")
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals(expectedString, response.getContentAsString());
    }

    @Test
    void deveriaCadastrarPet() throws Exception {
        Abrigo abrigo = new Abrigo(
            new CadastroAbrigoDto("Abrigo teste", "2111111111", "abrigo@email.com"));
        CadastroPetDto cadastroPetDto = new CadastroPetDto(TipoPet.GATO, "Pandy", "Gato da Gabby",
            4, "Branco", 5f);

        given(abrigoService.carregarAbrigo("Abrigo Teste")).willReturn(abrigo);

        var response = mvc.perform(
            post("/abrigos/Abrigo Teste/pets")
                .content(jsonPet.write(cadastroPetDto).getJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
    }
}