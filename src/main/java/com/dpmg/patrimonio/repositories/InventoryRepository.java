package com.dpmg.patrimonio.repositories;

import com.dpmg.patrimonio.models.dtos.Inventory.InventoryDTO;
import com.dpmg.patrimonio.models.entities.InventoryEntity;
import com.dpmg.patrimonio.models.enums.InventorySituationEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

    @Query("SELECT new com.dpmg.patrimonio.models.dtos.Inventory.InventoryDTO(inventario.id, inventario.ano, inventario.status, inventario.observacao) " +
            "FROM InventoryEntity inventario " +
            "WHERE inventario.ano = :year AND inventario.isAtivo = true")
    InventoryDTO findDTOByYear(@Param("year") Integer year);

    @Query("SELECT inventario.status FROM InventoryEntity inventario WHERE inventario.id = :id AND inventario.isAtivo = true")
    InventorySituationEnum findStatusById(@Param("id") Long id);

    Optional<InventoryEntity> findByAnoAndIsAtivoTrue(Integer ano);
}
