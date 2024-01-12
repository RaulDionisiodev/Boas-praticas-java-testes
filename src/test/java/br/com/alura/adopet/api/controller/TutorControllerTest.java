package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.service.TutorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TutorControllerTest {

    @MockBean
    private TutorService tutorService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CadastroTutorDto> tutorDtoJacksonTester;

    @Autowired
    private JacksonTester<AtualizacaoTutorDto> atualizacaoTutorDtoJacksonTester;

    @Test
    void deveriaCadastrarTutorCorretamente() throws Exception {
        CadastroTutorDto dto = new CadastroTutorDto("Tutor", "2144444444", "tutor@email.com");

        var result = mvc.perform(
            post("/tutores")
                .content(tutorDtoJacksonTester.write(dto).getJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, result.getStatus());
    }

    @Test
    void deveriaRetornarStatus400EmCasoDeCadastroDeTutorComParametrosErrados() throws Exception {

        //Telefone inválido
        CadastroTutorDto dto = new CadastroTutorDto("Tutor", "21 44444444", "tutor@email.com");

        var result = mvc.perform(
            post("/tutores")
                .content(tutorDtoJacksonTester.write(dto).getJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(400, result.getStatus());
    }

    @Test
    void deveriaAtualizarTutorCorretamente() throws Exception {
        AtualizacaoTutorDto dto = new AtualizacaoTutorDto(1L, "Tutor", "2144444444", "tutor@email.com");

        var result = mvc.perform(
            put("/tutores")
                .content(atualizacaoTutorDtoJacksonTester.write(dto).getJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, result.getStatus());
    }

    @Test
    void deveriaRetornarStatus400EmCasoDeAtualizacaoDeTutorComParametrosErrados() throws Exception {
        //Dados faltando no dto
        AtualizacaoTutorDto dto = new AtualizacaoTutorDto(1L, "Tutor", "2144444444", "");

        var result = mvc.perform(
            put("/tutores")
                .content(atualizacaoTutorDtoJacksonTester.write(dto).getJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(400, result.getStatus());
    }

}