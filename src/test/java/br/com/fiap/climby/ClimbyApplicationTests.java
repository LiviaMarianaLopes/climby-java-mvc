package br.com.fiap.climby;

import br.com.fiap.climby.model.Shelter;
import br.com.fiap.climby.model.User;
import br.com.fiap.climby.repository.ShelterRepository;
import br.com.fiap.climby.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ClimbyApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ShelterRepository shelterRepository;


	private User userParaTestes;
	private Shelter shelterParaTestes;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
		shelterRepository.deleteAll();

		userParaTestes = new User();
		userParaTestes.setName("Usuário Padrão");
		userParaTestes.setEmail("padrao@example.com");
		userParaTestes.setCountry("Brasil");
		userParaTestes.setCity("Teste");
		userParaTestes = userRepository.save(userParaTestes);

		shelterParaTestes = new Shelter();
		shelterParaTestes.setName("Abrigo Padrão");
		shelterParaTestes.setEmail("shelter@example.com");
		shelterParaTestes.setPhone(11999998888L);
		shelterParaTestes.setCountry("Brasil");
		shelterParaTestes.setCity("Cidade Teste");
		shelterParaTestes.setAdress("Rua Teste, 100");
		shelterParaTestes.setAdressNumber(100);
		shelterParaTestes.setCep("01001000");
		shelterParaTestes.setDistrict("Bairro Teste");
		shelterParaTestes.setFull(false);
		shelterParaTestes = shelterRepository.save(shelterParaTestes);
	}

	@Test
	void contextLoads() {
	}

	@Test
	@WithMockUser
	void deveCriarUsuarioERedirecionarParaLista() throws Exception {
		long countAntes = userRepository.count();

		mockMvc.perform(post("/user/cadastrar")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("name", "Novo Usuário Integração")
						.param("email", "novointegracao@example.com")
						.param("country", "Portugal")
						.param("city", "Lisboa")
						.with(csrf())
				)
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/lista"));

		long countDepois = userRepository.count();
		assertThat(countDepois).isEqualTo(countAntes + 1);
	}

	@Test
	@WithMockUser
	void deveListarUsuarios() throws Exception {
		mockMvc.perform(get("/user/lista"))
				.andExpect(status().isOk())
				.andExpect(view().name("userList"))
				.andExpect(model().attributeExists("listaUsers"))
				.andExpect(model().attribute("listaUsers", hasItem(
						allOf(
								hasProperty("id", is(userParaTestes.getId())),
								hasProperty("name", is(userParaTestes.getName()))
						)
				)));
	}

	@Test
	@WithMockUser
	void deveCarregarPaginaDeEdicaoDeUsuario() throws Exception {
		mockMvc.perform(get("/user/edicao/" + userParaTestes.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name("userEdit"))
				.andExpect(model().attributeExists("user"))
				.andExpect(model().attribute("user", hasProperty("name", is(userParaTestes.getName()))));
	}

	@Test
	@WithMockUser
	void deveEditarUsuarioComSucessoERedirecionar() throws Exception {
		mockMvc.perform(post("/user/editar/" + userParaTestes.getId())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("name", "Usuário Padrão Editado")
						.param("email", userParaTestes.getEmail())
						.param("country", userParaTestes.getCountry())
						.param("city", "Cidade Editada")
						.with(csrf())
				)
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/lista"));

		User usuarioEditado = userRepository.findById(userParaTestes.getId()).orElseThrow();
		assertThat(usuarioEditado.getName()).isEqualTo("Usuário Padrão Editado");
		assertThat(usuarioEditado.getCity()).isEqualTo("Cidade Editada");
	}

	@Test
	@WithMockUser
	void deveDeletarUsuarioComSucessoERedirecionar() throws Exception {
		mockMvc.perform(get("/user/deletar/" + userParaTestes.getId())
						.with(csrf())
				)
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/lista"));

		assertThat(userRepository.findById(userParaTestes.getId())).isEmpty();
	}


	@Test
	@WithMockUser
	void deveCriarAbrigoERedirecionarParaLista() throws Exception {
		long countAntes = shelterRepository.count();

		mockMvc.perform(post("/shelter/cadastrar")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("name", "Novo Abrigo Integração")
						.param("email", "novoabrigo@example.com")
						.param("phone", "11987654321")
						.param("country", "Chile")
						.param("city", "Santiago")
						.param("adress", "Calle Falsa, 123")
						.param("adressNumber", "123")
						.param("cep", "83200000")
						.param("district", "Providencia")
						.with(csrf())
				)
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/shelter/lista"));

		long countDepois = shelterRepository.count();
		assertThat(countDepois).isEqualTo(countAntes + 1);
	}

	@Test
	@WithMockUser
	void deveListarAbrigos() throws Exception {
		mockMvc.perform(get("/shelter/lista"))
				.andExpect(status().isOk())
				.andExpect(view().name("shelterList"))
				.andExpect(model().attributeExists("listaShelters"))
				.andExpect(model().attribute("listaShelters", hasItem(
						allOf(
								hasProperty("id", is(shelterParaTestes.getId())),
								hasProperty("name", is(shelterParaTestes.getName()))
						)
				)));
	}


	@Test
	@WithMockUser
	void deveCarregarPaginaDeEdicaoDeAbrigo() throws Exception {
		mockMvc.perform(get("/shelter/edicao/" + shelterParaTestes.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name("shelterEdit"))
				.andExpect(model().attributeExists("shelterRequest"))
				.andExpect(model().attribute("shelterRequest", hasProperty("name", is(shelterParaTestes.getName()))));
	}

	@Test
	@WithMockUser
	void deveEditarAbrigoComSucessoERedirecionar() throws Exception {
		mockMvc.perform(post("/shelter/editar/" + shelterParaTestes.getId())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("name", "Abrigo Padrão Editado")
						.param("email", shelterParaTestes.getEmail())
						.param("phone", String.valueOf(shelterParaTestes.getPhone()))
						.param("country", shelterParaTestes.getCountry())
						.param("city", "Cidade Teste Editada")
						.param("adress", shelterParaTestes.getAdress())
						.param("adressNumber", String.valueOf(shelterParaTestes.getAdressNumber()))
						.param("cep", shelterParaTestes.getCep())
						.param("district", shelterParaTestes.getDistrict())
						.with(csrf())
				)
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/shelter/lista"));

		Shelter abrigoEditado = shelterRepository.findById(shelterParaTestes.getId()).orElseThrow();
		assertThat(abrigoEditado.getName()).isEqualTo("Abrigo Padrão Editado");
		assertThat(abrigoEditado.getCity()).isEqualTo("Cidade Teste Editada");
	}

	@Test
	@WithMockUser
	void deveDeletarAbrigoComSucessoERedirecionar() throws Exception {
		mockMvc.perform(get("/shelter/deletar/" + shelterParaTestes.getId())
						.with(csrf())
				)
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/shelter/lista"));

		assertThat(shelterRepository.findById(shelterParaTestes.getId())).isEmpty();
	}
}