package br.com.fiap.climby.repository;

import br.com.fiap.climby.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
}
