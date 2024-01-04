package ma.ensate.comptecqrsevensourcing.query.repositories;

import ma.ensate.comptecqrsevensourcing.query.entites.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
