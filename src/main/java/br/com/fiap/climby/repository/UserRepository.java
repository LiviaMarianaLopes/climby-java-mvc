package br.com.fiap.climby.repository;

import br.com.fiap.climby.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
