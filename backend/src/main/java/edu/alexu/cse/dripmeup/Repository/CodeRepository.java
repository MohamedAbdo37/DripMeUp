package edu.alexu.cse.dripmeup.Repository;

import edu.alexu.cse.dripmeup.Entity.CodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<CodeEntity, Long> {
    CodeEntity findByEmail(String email);
}
