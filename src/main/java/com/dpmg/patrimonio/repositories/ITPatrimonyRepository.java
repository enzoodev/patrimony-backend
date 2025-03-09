package com.dpmg.patrimonio.repositories;

import com.dpmg.patrimonio.models.dtos.ITPatrimony.ITPatrimonyDTO;
import com.dpmg.patrimonio.models.entities.ITPatrimonyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ITPatrimonyRepository extends JpaRepository<ITPatrimonyEntity, Long> {
    @Query("""
        SELECT new com.dpmg.patrimonio.models.dtos.ITPatrimony.ITPatrimonyDTO(
            patrimonio.id,
            patrimonio.numeroPatrimonio,
            patrimonio.maquina,
            patrimonio.modelo,
            patrimonio.marca,
            patrimonio.versaoOffice,
            patrimonio.sistemaOperacional,
            patrimonio.serialMaquina,
            patrimonio.serialWindows,
            patrimonio.serialOffice,
            patrimonio.processador,
            patrimonio.memoria,
            patrimonio.hd,
            patrimonio.lotacao,
            patrimonio.responsavel,
            patrimonio.isAtivo
        )
        FROM ITPatrimonyEntity patrimonio
        WHERE (:patrimonio IS NULL OR patrimonio.numeroPatrimonio LIKE CONCAT('%', CAST(:patrimonio AS string), '%'))
        AND (:nomeMaquina IS NULL OR patrimonio.maquina LIKE CONCAT('%', CAST(:nomeMaquina AS string), '%'))
        AND (:serialMaquina IS NULL OR patrimonio.serialMaquina LIKE CONCAT('%', CAST(:serialMaquina AS string), '%'))
        AND (:serialOffice IS NULL OR patrimonio.serialOffice LIKE CONCAT('%', CAST(:serialOffice AS string), '%'))
        AND (:modelo IS NULL OR patrimonio.modelo LIKE CONCAT('%', CAST(:modelo AS string), '%'))
        AND (:versaoOffice IS NULL OR patrimonio.versaoOffice LIKE CONCAT('%', CAST(:versaoOffice AS string), '%'))
        AND (:sistemaOperacional IS NULL OR patrimonio.sistemaOperacional LIKE CONCAT('%', CAST(:sistemaOperacional AS string), '%'))
        AND (:status IS NULL OR patrimonio.isAtivo = :status)
    """)
    Page<ITPatrimonyDTO> findAll(
            @Param("patrimonio") String patrimonio,
            @Param("nomeMaquina") String nomeMaquina,
            @Param("serialMaquina") String serialMaquina,
            @Param("serialOffice") String serialOffice,
            @Param("modelo") String modelo,
            @Param("versaoOffice") String versaoOffice,
            @Param("sistemaOperacional") String sistemaOperacional,
            @Param("status") Boolean status,
            Pageable pageable
    );

    @Query("""
        SELECT new com.dpmg.patrimonio.models.dtos.ITPatrimony.ITPatrimonyDTO(
            patrimonio.id,
            patrimonio.numeroPatrimonio,
            patrimonio.maquina,
            patrimonio.modelo,
            patrimonio.marca,
            patrimonio.versaoOffice,
            patrimonio.sistemaOperacional,
            patrimonio.serialMaquina,
            patrimonio.serialWindows,
            patrimonio.serialOffice,
            patrimonio.processador,
            patrimonio.memoria,
            patrimonio.hd,
            patrimonio.lotacao,
            patrimonio.responsavel,
            patrimonio.isAtivo
        )
        FROM ITPatrimonyEntity patrimonio
        WHERE patrimonio.id = :id
    """)
    ITPatrimonyDTO findItemDTOById(@Param("id") Long id);
}
