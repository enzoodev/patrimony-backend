package com.dpmg.patrimonio.models.entities;

import com.dpmg.patrimonio.models.entities.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_patrimonio_ti")
public class ITPatrimonyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_seq_patrimonio_ti")
    private Long id;

    @Column(name = "ds_hd")
    private String hd;

    @Column(name = "ds_lotacao")
    private String lotacao;

    @Column(name = "nu_mac_adress")
    private String macAddress;

    @Column(name = "ds_marca")
    private String marca;

    @Column(name = "ds_memoria")
    private String memoria;

    @Column(name = "ds_modelo")
    private String modelo;

    @Column(name = "no_maquina")
    private String maquina;

    @Column(name = "no_responsavel")
    private String responsavel;

    @Column(name = "nu_patrimonio")
    private String numeroPatrimonio;

    @Column(name = "ds_observacao")
    private String observacao;

    @Column(name = "ds_processador")
    private String processador;

    @Column(name = "nu_serial_maquina")
    private String serialMaquina;

    @Column(name = "nu_serial_office")
    private String serialOffice;

    @Column(name = "nu_serial_windows")
    private String serialWindows;

    @Column(name = "ds_sistema_operacional")
    private String sistemaOperacional;

    @Column(name = "ds_versao_office")
    private String versaoOffice;

    @PrePersist
    @Override
    public void prePersist() {
        super.prePersist();
    }
}
