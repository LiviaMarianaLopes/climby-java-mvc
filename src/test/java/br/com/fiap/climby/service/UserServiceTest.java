package br.com.fiap.climby.service;

import br.com.fiap.climby.dto.UserRequest;
import br.com.fiap.climby.model.User;
import br.com.fiap.climby.repository.UserRepository;
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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void deveSalvarUsuarioComSucesso() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Usuário Teste");
        userRequest.setEmail("teste@example.com");
        userRequest.setCountry("Brasil");
        userRequest.setCity("São Paulo");

        User usuarioSalvo = new User();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setName(userRequest.getName());
        usuarioSalvo.setEmail(userRequest.getEmail());
        usuarioSalvo.setCountry(userRequest.getCountry());
        usuarioSalvo.setCity(userRequest.getCity());

        when(userRepository.save(any(User.class))).thenReturn(usuarioSalvo);

        User resultado = userService.salvarUser(userRequest);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Usuário Teste", resultado.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deveBuscarUsuarioPorIdComSucesso() {
        Long userId = 1L;
        User usuarioMock = new User();
        usuarioMock.setId(userId);
        usuarioMock.setName("Usuário Encontrado");

        when(userRepository.findById(userId)).thenReturn(Optional.of(usuarioMock));

        User resultado = userService.buscarUser(userId);

        assertNotNull(resultado);
        assertEquals(userId, resultado.getId());
        assertEquals("Usuário Encontrado", resultado.getName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void deveRetornarNuloAoBuscarUsuarioPorIdInexistente() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User resultado = userService.buscarUser(userId);

        assertNull(resultado);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void deveListarTodosUsuarios() {
        User usuario1 = new User();
        usuario1.setId(1L);
        usuario1.setName("Usuário Um");

        User usuario2 = new User();
        usuario2.setId(2L);
        usuario2.setName("Usuário Dois");

        when(userRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        List<User> resultados = userService.buscarUsers();

        assertNotNull(resultados);
        assertEquals(2, resultados.size());
        assertEquals("Usuário Um", resultados.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void deveDeletarUsuarioComSucesso() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        userService.deletarUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        Long userId = 1L;
        UserRequest userRequestAtualizado = new UserRequest();
        userRequestAtualizado.setName("Usuário Atualizado");
        userRequestAtualizado.setEmail("atualizado@example.com");
        userRequestAtualizado.setCountry("Portugal");
        userRequestAtualizado.setCity("Lisboa");

        User usuarioExistente = new User();
        usuarioExistente.setId(userId);
        usuarioExistente.setName("Usuário Antigo");
        usuarioExistente.setEmail("antigo@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(usuarioExistente));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.atualizarUser(userId, userRequestAtualizado);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(argThat(savedUser ->
                savedUser.getId().equals(userId) &&
                        savedUser.getName().equals("Usuário Atualizado") &&
                        savedUser.getEmail().equals("atualizado@example.com") &&
                        savedUser.getCountry().equals("Portugal") &&
                        savedUser.getCity().equals("Lisboa")
        ));
    }

    @Test
    void naoDeveAtualizarUsuarioSeNaoEncontrado() {
        Long userId = 99L;
        UserRequest userRequestAtualizado = new UserRequest();
        userRequestAtualizado.setName("Não Deve Atualizar");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        userService.atualizarUser(userId, userRequestAtualizado);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deveConverterUserParaUserRequestCorretamente() {
        User user = new User();
        user.setId(1L);
        user.setName("Nome User");
        user.setEmail("user@example.com");
        user.setCountry("País User");
        user.setCity("Cidade User");

        UserRequest userRequest = userService.userToRequest(user);

        assertEquals(user.getName(), userRequest.getName());
        assertEquals(user.getEmail(), userRequest.getEmail());
        assertEquals(user.getCountry(), userRequest.getCountry());
        assertEquals(user.getCity(), userRequest.getCity());
    }
}