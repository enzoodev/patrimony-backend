package com.dpmg.patrimonio.models.entities;

import com.dpmg.patrimonio.models.entities.shared.BaseEntity;
import com.dpmg.patrimonio.models.enums.PatrimonySituationEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_patrimonio", indexes = {
        @Index(name = "idx_patrimonio_inventario", columnList = "co_controle_inventario, nu_patrimonio, co_unidade_responsavel")
})
public class PatrimonyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_seq_patrimonio")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_controle_inventario", nullable = false)
    private InventoryControlEntity inventario;

    @Column(name = "fl_outra_situacao", nullable = false)
    private Boolean isOutraSituacao;

    @Column(name = "tp_bem_patrimonial")
    private String tipoBemPatrimonial;

    @Column(name = "nu_patrimonio")
    private Long numeroPatrimonio;

    @Column(name = "ds_item_material", nullable = false)
    private String descricaoItemMaterial;

    @Column(name = "ds_estado_conservacao_bem")
    private String estadoConservacaoBem;

    @Column(name = "dt_tombamento")
    private Date dataTombamento;

    @Column(name = "vl_bem_patrimonial")
    private Double valorBemPatrimonial;

    @Column(name = "nu_elemento_item_despesa")
    private Long numeroElementoItemDespesa;

    @Column(name = "no_orgao_terceiro_responsavel")
    private String orgaoTerceiroResponsavel;

    @Column(name = "nu_item_material")
    private Long numeroItemMaterial;

    @Column(name = "no_marca")
    private String marca;

    @Column(name = "no_modelo")
    private String modelo;

    @Column(name = "nu_serie")
    private String serie;

    @Column(name = "ds_destinacao_bem")
    private String destinacaoBem;

    @Column(name = "no_orgao_terceiro_destino")
    private String orgaoTerceiroDestino;

    @Column(name = "no_convenio")
    private String convenio;

    @Column(name = "nu_docum_ultima_movimentacao")
    private String numeroDocumentoUltimaMovimentacao;

    @Column(name = "no_responsavel")
    private String nomeResponsavel;

    @Column(name = "no_corresponsavel")
    private String nomeCorresponsavel;

    @Column(name = "fl_patrimonio_fora_unidade")
    private Boolean isPatrimonioForaDaUnidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "tp_situacao")
    private PatrimonySituationEnum situacao;

    @Column(name = "nu_sala")
    private String sala;

    @Column(name = "ds_observacao")
    private String observacao;

    @Column(name = "fl_cadastro_manual", nullable = false)
    private Boolean isCadastroManual;

    @Column(name = "co_unidade_contabil")
    private Long codigoUnidadeContabil;

    @Column(name = "co_unidade_responsavel")
    private Long codigoUnidadeResponsavel;

    @Column(name = "no_unidade_responsavel")
    private String nomeUnidadeResponsavel;

    @Column(name = "co_unidade_gerencial")
    private Long codigoUnidadeGerencial;

    @Column(name = "no_unidade_gerencial")
    private String nomeUnidadeGerencial;

    @Column(name = "co_unidade_destino")
    private Long codigoUnidadeDestino;

    @Column(name = "no_unidade_destino")
    private String nomeUnidadeDestino;

    @Column(name = "co_unidade_encontrado")
    private Long codigoUnidadeEncontrado;

    @Column(name = "no_unidade_encontrado")
    private String nomeUnidadeEncontrado;

    @PrePersist
    @Override
    public void prePersist() {
        super.prePersist();
    }
}
