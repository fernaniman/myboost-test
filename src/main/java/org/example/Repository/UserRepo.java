package org.example.Repository;

import org.example.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UsersEntity, Long> {

    Boolean existsByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email);
    Boolean existsByIdNotAndFirstNameAndLastNameAndEmail(Long id, String firstName, String lastName, String email);
}
