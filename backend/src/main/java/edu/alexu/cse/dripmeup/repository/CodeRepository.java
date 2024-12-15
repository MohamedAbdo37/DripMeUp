package edu.alexu.cse.dripmeup.repository;

import edu.alexu.cse.dripmeup.entity.CodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<CodeEntity, Long> {
    CodeEntity findByEmail(String email);
}
