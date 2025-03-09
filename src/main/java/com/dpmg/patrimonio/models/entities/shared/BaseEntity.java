package com.dpmg.patrimonio.models.entities.shared;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "st_ativo", nullable = false)
    private Boolean isAtivo;

    @Column(name = "dh_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "dh_alteracao")
    private LocalDateTime dataAlteracao;

    @Column(name = "tp_operacao", nullable = false)
    private String operacao;

    @Column(name = "nu_versao", nullable = false)
    private Integer versao;

    @Column(name = "co_uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "co_uuid_1", nullable = false)
    private String uuidUsuario;

    @Column(name = "sg_projeto_modificador", nullable = false)
    private String sgProjetoModificador;

    @Column(name = "sg_acao_modificadora", nullable = false)
    private String sgAcaoModificadora;

    @Column(name = "no_end_point_modificador", nullable = false)
    private String noEndPointModificador;

    public void prePersist() {
        this.uuid = UUID.randomUUID().toString();
        this.dataCriacao = LocalDateTime.now();
        this.isAtivo = true;
        this.operacao = "CREATE";
        this.versao = 1;
    }
}
