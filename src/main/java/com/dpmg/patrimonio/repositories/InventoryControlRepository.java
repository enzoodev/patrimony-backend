package com.dpmg.patrimonio.repositories;

import com.dpmg.patrimonio.models.dtos.InventoryControl.InventoryControlDTO;
import com.dpmg.patrimonio.models.entities.InventoryControlEntity;
import com.dpmg.patrimonio.models.enums.InventoryControlSituationEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryControlRepository extends JpaRepository<InventoryControlEntity, Long> {

    @Query("SELECT new com.dpmg.patrimonio.models.dtos.InventoryControl.InventoryControlDTO(inventario.id, inventario.ano, inventario.status, inventario.observacao) " +
            "FROM InventoryControlEntity inventario " +
            "WHERE inventario.ano = :year AND inventario.isAtivo = true")
    InventoryControlDTO findDTOByYear(@Param("year") Integer year);

    @Query("SELECT inventario.status FROM InventoryControlEntity inventario WHERE inventario.id = :id AND inventario.isAtivo = true")
    InventoryControlSituationEnum findStatusById(@Param("id") Long id);

    Optional<InventoryControlEntity> findByAnoAndIsAtivoTrue(Integer ano);
}
