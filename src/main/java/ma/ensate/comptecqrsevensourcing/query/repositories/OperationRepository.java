package ma.ensate.comptecqrsevensourcing.query.repositories;


import ma.ensate.comptecqrsevensourcing.query.entites.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}
