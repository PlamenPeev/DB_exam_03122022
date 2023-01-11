package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Mechanic;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {


    Optional<Mechanic> findMechanicByEmail(String email);

    Optional<Mechanic> findMechanicalByFirstName(String firstName);
}
