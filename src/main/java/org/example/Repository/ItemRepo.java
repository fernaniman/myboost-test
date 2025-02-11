package org.example.Repository;

import org.example.Entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepo extends JpaRepository<ItemEntity, Long> {

    Boolean existsByName(String name);
    Boolean existsByIdNotAndName(Long id, String name);
}
