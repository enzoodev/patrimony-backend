package com.dpmg.patrimonio.repositories;

import com.dpmg.patrimonio.models.dtos.Patrimony.*;
import com.dpmg.patrimonio.models.dtos.PatrimonyOtherSituation.PatrimonyOtherSituationDTO;
import com.dpmg.patrimonio.models.entities.PatrimonyEntity;
import com.dpmg.patrimonio.models.enums.PatrimonySituationEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatrimonyRepository extends JpaRepository<PatrimonyEntity, Long> {

    @Query("""
        SELECT new com.dpmg.patrimonio.models.dtos.Patrimony.PatrimonyDTO(
            patrimonio.id,
            patrimonio.situacao,
            patrimonio.numeroPatrimonio,
            patrimonio.descricaoItemMaterial,
            patrimonio.dataCriacao,
            patrimonio.isPatrimonioForaDaUnidade,
            new com.dpmg.patrimonio.models.dtos.Patrimony.UnitDTO(
                patrimonio.codigoUnidadeResponsavel,
                patrimonio.nomeUnidadeResponsavel
            ),
            patrimonio.valorBemPatrimonial
        )
        FROM PatrimonyEntity patrimonio
        WHERE patrimonio.inventario.id = :inventoryId
        AND patrimonio.isAtivo = true
        AND patrimonio.isOutraSituacao = false
        AND (:numeroPatrimonio IS NULL OR patrimonio.numeroPatrimonio = :numeroPatrimonio)
        AND (:situacao IS NULL OR patrimonio.situacao = :situacao)
        AND (:codUnidadeResponsavel IS NULL OR patrimonio.codigoUnidadeResponsavel = :codUnidadeResponsavel)
        AND (:descricaoItemMaterial IS NULL OR patrimonio.descricaoItemMaterial LIKE CONCAT('%', CAST(:descricaoItemMaterial AS string), '%'))
    """)
    Page<PatrimonyDTO> findByInventoryId(
            @Param("numeroPatrimonio") Long numeroPatrimonio,
            @Param("situacao") PatrimonySituationEnum situacao,
            @Param("codUnidadeResponsavel") Long codUnidadeResponsavel,
            @Param("descricaoItemMaterial") String descricaoItemMaterial,
            @Param("inventoryId") Long inventoryId,
            Pageable pageable
    );

    @Query("""
        SELECT DISTINCT patrimonio.descricaoItemMaterial
        FROM PatrimonyEntity patrimonio
        WHERE patrimonio.inventario.id = :inventoryId
          AND patrimonio.isAtivo = true
          AND patrimonio.isOutraSituacao = false
    """)
    List<String> findItemDescriptionsByInventoryId(@Param("inventoryId") Long inventoryId);

    @Query("""
        SELECT new com.dpmg.patrimonio.models.dtos.Patrimony.PatrimonyDetailsDTO(
            patrimonio.id,
            patrimonio.numeroPatrimonio,
            patrimonio.descricaoItemMaterial,
            new com.dpmg.patrimonio.models.dtos.Patrimony.UnitDTO(
                patrimonio.codigoUnidadeResponsavel,
                patrimonio.nomeUnidadeResponsavel
            ),
            patrimonio.nomeResponsavel,
            patrimonio.codigoUnidadeContabil,
            new com.dpmg.patrimonio.models.dtos.Patrimony.UnitDTO(
                patrimonio.codigoUnidadeGerencial,
                patrimonio.nomeUnidadeGerencial
            ),
            patrimonio.orgaoTerceiroResponsavel,
            patrimonio.orgaoTerceiroDestino,
            new com.dpmg.patrimonio.models.dtos.Patrimony.UnitDTO(
                patrimonio.codigoUnidadeDestino,
                patrimonio.nomeUnidadeDestino
            ),
            patrimonio.dataTombamento,
            patrimonio.tipoBemPatrimonial,
            patrimonio.numeroItemMaterial,
            patrimonio.numeroElementoItemDespesa,
            patrimonio.estadoConservacaoBem,
            patrimonio.marca,
            patrimonio.modelo,
            patrimonio.serie,
            patrimonio.destinacaoBem,
            patrimonio.convenio,
            patrimonio.numeroDocumentoUltimaMovimentacao,
            patrimonio.valorBemPatrimonial
        )
        FROM PatrimonyEntity patrimonio
        WHERE patrimonio.id = :id AND patrimonio.isAtivo = true AND patrimonio.isOutraSituacao = false
    """)
    PatrimonyDetailsDTO findItemDetailsById(@Param("id") Long id);

    @Query("""
        SELECT new com.dpmg.patrimonio.models.dtos.Patrimony.PatrimonyObservationDTO(
            patrimonio.sala,
            patrimonio.observacao
        )
        FROM PatrimonyEntity patrimonio
        WHERE patrimonio.id = :id AND patrimonio.isAtivo = true AND patrimonio.isOutraSituacao = false
    """)
    PatrimonyObservationDTO findItemObservationById(@Param("id") Long id);

    @Query("""
        SELECT new com.dpmg.patrimonio.models.dtos.PatrimonyOtherSituation.PatrimonyOtherSituationDTO(
            patrimonio.id,
            patrimonio.situacao,
            patrimonio.numeroPatrimonio,
            patrimonio.descricaoItemMaterial,
            patrimonio.dataCriacao,
            patrimonio.isCadastroManual,
            new com.dpmg.patrimonio.models.dtos.Patrimony.UnitDTO(
                patrimonio.codigoUnidadeResponsavel,
                patrimonio.nomeUnidadeResponsavel
            ),
            new com.dpmg.patrimonio.models.dtos.Patrimony.UnitDTO(
                patrimonio.codigoUnidadeEncontrado,
                patrimonio.nomeUnidadeEncontrado
            )
        )
        FROM PatrimonyEntity patrimonio
        WHERE patrimonio.inventario.id = :inventoryId
        AND patrimonio.isAtivo = true
        AND patrimonio.isOutraSituacao = true
        AND (:situacao IS NULL OR patrimonio.situacao = :situacao)
        AND (:numeroPatrimonio IS NULL OR patrimonio.numeroPatrimonio = :numeroPatrimonio)
        AND (:codUnidadeResponsavel IS NULL OR patrimonio.codigoUnidadeResponsavel = :codUnidadeResponsavel)
        AND (:descricaoItemMaterial IS NULL OR patrimonio.descricaoItemMaterial LIKE CONCAT('%', CAST(:descricaoItemMaterial AS string), '%'))
    """)
    Page<PatrimonyOtherSituationDTO> findOtherSituations(
            @Param("situacao") PatrimonySituationEnum situacao,
            @Param("numeroPatrimonio") Long numeroPatrimonio,
            @Param("codUnidadeResponsavel") Long codUnidadeResponsavel,
            @Param("descricaoItemMaterial") String descricaoItemMaterial,
            @Param("inventoryId") Long inventoryId,
            Pageable pageable
    );

    @Query("""
        SELECT new com.dpmg.patrimonio.models.dtos.PatrimonyOtherSituation.PatrimonyOtherSituationDTO(
            patrimonio.id,
            patrimonio.situacao,
            patrimonio.numeroPatrimonio,
            patrimonio.descricaoItemMaterial,
            patrimonio.dataCriacao,
            patrimonio.isCadastroManual,
            new com.dpmg.patrimonio.models.dtos.Patrimony.UnitDTO(
                patrimonio.codigoUnidadeResponsavel,
                patrimonio.nomeUnidadeResponsavel
            ),
            new com.dpmg.patrimonio.models.dtos.Patrimony.UnitDTO(
                patrimonio.codigoUnidadeEncontrado,
                patrimonio.nomeUnidadeEncontrado
            )
        )
        FROM PatrimonyEntity patrimonio
        WHERE patrimonio.id = :id
        AND patrimonio.isAtivo = true
        AND patrimonio.isOutraSituacao = true
    """)
    PatrimonyOtherSituationDTO findOtherSituationById(@Param("id") Long id);

    @Query("""
        SELECT DISTINCT
            new com.dpmg.patrimonio.models.dtos.Patrimony.UnitDTO(
                patrimonio.codigoUnidadeResponsavel,
                patrimonio.nomeUnidadeResponsavel
            )
        FROM PatrimonyEntity patrimonio
        WHERE patrimonio.inventario.id = :inventoryId
          AND patrimonio.isAtivo = true
          AND patrimonio.codigoUnidadeResponsavel IS NOT NULL
    """)
    List<UnitDTO> findResponsibleUnitListByInventoryId(@Param("inventoryId") Long inventoryId);

    @Query("""
        SELECT patrimonio.codigoUnidadeResponsavel, patrimonio.nomeUnidadeResponsavel
        FROM PatrimonyEntity patrimonio
        WHERE patrimonio.inventario.id = :inventoryId
          AND patrimonio.codigoUnidadeResponsavel = :unitNumber
          AND patrimonio.isAtivo = true
    """)
    UnitDTO findResponsibleUnitByInventoryIdAndUnitNumber(@Param("inventoryId") Long inventoryId,  @Param("unitNumber") Long unitNumber);

    @Query("""
        SELECT COUNT(patrimonio) > 0
        FROM PatrimonyEntity patrimonio
        WHERE patrimonio.inventario.id = :inventoryId
          AND patrimonio.codigoUnidadeResponsavel = :unitNumber
          AND patrimonio.isAtivo = true
    """)
    boolean existsUnitByInventoryIdAndUnitNumber(@Param("inventoryId") Long inventoryId, @Param("unitNumber") Long unitNumber);

    @Query("""
        SELECT new com.dpmg.patrimonio.models.dtos.Patrimony.PatrimonyToBeLocalizedDTO(
            patrimonio.id,
            patrimonio.descricaoItemMaterial,
            null,
            patrimonio.situacao,
            null
        )
        FROM PatrimonyEntity patrimonio
        WHERE patrimonio.inventario.id = :inventoryId
          AND patrimonio.codigoUnidadeResponsavel = :unitNumber
          AND patrimonio.numeroPatrimonio = :patrimonyNumber
          AND patrimonio.isOutraSituacao = false
          AND patrimonio.isAtivo = true
    """)
    PatrimonyToBeLocalizedDTO findByInventoryIdAndUnitNumberAndPatrimonyNumber(
            @Param("inventoryId") Long inventoryId,
            @Param("unitNumber") Long unitNumber,
            @Param("patrimonyNumber") Long patrimonyNumber
    );

    @Query("""
        SELECT new com.dpmg.patrimonio.models.dtos.Patrimony.PatrimonyToBeLocalizedDTO(
            patrimonio.id,
            patrimonio.descricaoItemMaterial,
            null,
            patrimonio.situacao,
            null
        )
        FROM PatrimonyEntity patrimonio
        WHERE patrimonio.inventario.id = :inventoryId
          AND patrimonio.numeroPatrimonio = :patrimonyNumber
          AND patrimonio.isOutraSituacao = false
          AND patrimonio.isAtivo = true
    """)
    PatrimonyToBeLocalizedDTO findByInventoryIdAndPatrimonyNumber(
            @Param("inventoryId") Long inventoryId,
            @Param("patrimonyNumber") Long patrimonyNumber
    );
}
