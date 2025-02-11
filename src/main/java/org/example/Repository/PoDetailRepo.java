package org.example.Repository;

import org.example.Entity.PoDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoDetailRepo extends JpaRepository<PoDetailEntity, Long> {
    Boolean existsByPoHeaderIdAndItemId(Long poHeaderId, Long itemId);
}
