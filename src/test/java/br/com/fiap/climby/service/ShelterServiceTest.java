package br.com.fiap.climby.service;

import br.com.fiap.climby.dto.ShelterRequest;
import br.com.fiap.climby.model.Shelter;
import br.com.fiap.climby.repository.ShelterRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ShelterServiceTest {

    @Mock
    private ShelterRepository shelterRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ShelterService shelterService;

    @Test
    void deveSalvarAbrigoComSucesso() {
        ShelterRequest shelterRequest = new ShelterRequest();
        shelterRequest.setName("Abrigo Esperança");
        shelterRequest.setEmail("esperanca@example.com");
        shelterRequest.setPhone(123456789L);
        shelterRequest.setCountry("Brasil");
        shelterRequest.setCity("Rio de Janeiro");
        shelterRequest.setAdress("Rua da Paz, 123");
        shelterRequest.setAdressNumber(123);
        shelterRequest.setCep("20000000");
        shelterRequest.setDistrict("Centro");
        shelterRequest.setFull(false);

        Shelter abrigoSalvo = new Shelter();
        abrigoSalvo.setId(1L);
        abrigoSalvo.setName(shelterRequest.getName());
        abrigoSalvo.setEmail(shelterRequest.getEmail());
        abrigoSalvo.setCep(shelterRequest.getCep());


        when(shelterRepository.save(any(Shelter.class))).thenReturn(abrigoSalvo);
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyLong());


        Shelter resultado = shelterService.salvarShelter(shelterRequest);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Abrigo Esperança", resultado.getName());
        assertEquals("20000000", resultado.getCep());
        verify(shelterRepository, times(1)).save(any(Shelter.class));
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), eq(1L));
    }

    @Test
    void deveBuscarAbrigoPorIdComSucesso() {
        Long shelterId = 1L;
        Shelter abrigoMock = new Shelter();
        abrigoMock.setId(shelterId);
        abrigoMock.setName("Abrigo Encontrado");
        abrigoMock.setCep("10000001");

        when(shelterRepository.findById(shelterId)).thenReturn(Optional.of(abrigoMock));

        Shelter resultado = shelterService.buscarShelter(shelterId);

        assertNotNull(resultado);
        assertEquals(shelterId, resultado.getId());
        assertEquals("Abrigo Encontrado", resultado.getName());
        assertEquals("10000001", resultado.getCep());
        verify(shelterRepository, times(1)).findById(shelterId);
    }

    @Test
    void deveRetornarNuloAoBuscarAbrigoPorIdInexistente() {
        Long shelterId = 99L;
        when(shelterRepository.findById(shelterId)).thenReturn(Optional.empty());

        Shelter resultado = shelterService.buscarShelter(shelterId);

        assertNull(resultado);
        verify(shelterRepository, times(1)).findById(shelterId);
    }

    @Test
    void deveListarTodosAbrigos() {
        Shelter abrigo1 = new Shelter();
        abrigo1.setId(1L);
        abrigo1.setName("Abrigo Um");

        Shelter abrigo2 = new Shelter();
        abrigo2.setId(2L);
        abrigo2.setName("Abrigo Dois");

        when(shelterRepository.findAll()).thenReturn(Arrays.asList(abrigo1, abrigo2));

        List<Shelter> resultados = shelterService.buscarShelters();

        assertNotNull(resultados);
        assertEquals(2, resultados.size());
        assertEquals("Abrigo Um", resultados.get(0).getName());
        verify(shelterRepository, times(1)).findAll();
    }

    @Test
    void deveDeletarAbrigoComSucesso() {
        Long shelterId = 1L;
        doNothing().when(shelterRepository).deleteById(shelterId);

        shelterService.deletarShelter(shelterId);

        verify(shelterRepository, times(1)).deleteById(shelterId);
    }


    @Test
    void deveAtualizarAbrigoComSucesso() {
        Long shelterId = 1L;
        ShelterRequest shelterRequestAtualizado = new ShelterRequest();
        shelterRequestAtualizado.setName("Abrigo Renovado");
        shelterRequestAtualizado.setEmail("renovado@example.com");
        shelterRequestAtualizado.setCep("01001000");


        Shelter abrigoExistente = new Shelter();
        abrigoExistente.setId(shelterId);
        abrigoExistente.setName("Abrigo Antigo");
        abrigoExistente.setCep("00000000");

        when(shelterRepository.findById(shelterId)).thenReturn(Optional.of(abrigoExistente));
        when(shelterRepository.save(any(Shelter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        shelterService.atualizarShelter(shelterId, shelterRequestAtualizado);

        verify(shelterRepository, times(1)).findById(shelterId);
        verify(shelterRepository, times(1)).save(argThat(savedShelter ->
                savedShelter.getId().equals(shelterId) &&
                        savedShelter.getName().equals("Abrigo Renovado") &&
                        savedShelter.getCep().equals("01001000")
        ));
    }

    @Test
    void naoDeveAtualizarAbrigoSeNaoEncontrado() {
        Long shelterId = 99L;
        ShelterRequest shelterRequestAtualizado = new ShelterRequest();
        shelterRequestAtualizado.setName("Não Deve Atualizar");

        when(shelterRepository.findById(shelterId)).thenReturn(Optional.empty());

        shelterService.atualizarShelter(shelterId, shelterRequestAtualizado);

        verify(shelterRepository, times(1)).findById(shelterId);
        verify(shelterRepository, never()).save(any(Shelter.class));
    }

    @Test
    void deveConverterShelterParaShelterRequestCorretamente() {
        Shelter shelter = new Shelter();
        shelter.setId(1L);
        shelter.setName("Nome Abrigo");
        shelter.setEmail("abrigo@example.com");
        shelter.setPhone(987654321L);
        shelter.setCountry("País Abrigo");
        shelter.setCity("Cidade Abrigo");
        shelter.setAdress("Endereço Abrigo");
        shelter.setAdressNumber(456);
        shelter.setCep("05858000");
        shelter.setDistrict("Bairro Abrigo");
        shelter.setFull(true);


        ShelterRequest shelterRequest = shelterService.shelterToRequest(shelter);

        assertEquals(shelter.getName(), shelterRequest.getName());
        assertEquals(shelter.getEmail(), shelterRequest.getEmail());
        assertEquals(shelter.getPhone(), shelterRequest.getPhone());
        assertEquals(shelter.getCountry(), shelterRequest.getCountry());
        assertEquals(shelter.getCity(), shelterRequest.getCity());
        assertEquals(shelter.getAdress(), shelterRequest.getAdress());
        assertEquals(shelter.getAdressNumber(), shelterRequest.getAdressNumber());
        assertEquals(shelter.getCep(), shelterRequest.getCep());
        assertEquals(shelter.getDistrict(), shelterRequest.getDistrict());
        assertEquals(shelter.isFull(), shelterRequest.isFull());
    }
}