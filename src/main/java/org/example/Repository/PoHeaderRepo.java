package org.example.Repository;

import org.example.Entity.PoHeaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoHeaderRepo extends JpaRepository<PoHeaderEntity, Long> {
}
