package com.dpmg.patrimonio.models.dtos.Patrimony;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PatrimonyDetailsDTO {
    private Long id;
    private Long numeroPatrimonio;
    private String descricao;
    private UnitDTO unidadeResponsavel;
    private String nomeResponsavel;
    private Long codigoUnidadeContabil;
    private UnitDTO unidadeGerencial;
    private String nomeOrgaoTerceiroResponsavel;
    private String orgaoTerceiroDestino;
    private UnitDTO unidadeDestino;
    private Date dataTombamento;
    private String tipoBemPatrimonial;
    private Long codItemMaterial;
    private Long codElementoItemDespesa;
    private String estadoConservacaoBem;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String destinacaoBem;
    private String convenio;
    private String documentoUltimaMovimentacao;
    private Double valorBemPatrimonial;
}
