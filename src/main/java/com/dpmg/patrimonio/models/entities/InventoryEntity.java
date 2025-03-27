package com.dpmg.patrimonio.models.entities;

import com.dpmg.patrimonio.models.entities.shared.BaseEntity;
import com.dpmg.patrimonio.models.enums.InventorySituationEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_controle_inventario")
public class InventoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_seq_controle_inventario")
    private Long id;

    @Column(name = "nu_ano_inventario", nullable = false, unique = true)
    private Integer ano;

    @Enumerated(EnumType.STRING)
    @Column(name = "st_inventario")
    private InventorySituationEnum status;

    @Column(name = "ds_observacao")
    private String observacao;

    @OneToMany(mappedBy = "inventario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PatrimonyEntity> listaPatrimonio;

    @PrePersist
    @Override
    public void prePersist() {
        super.prePersist();
        this.status = InventorySituationEnum.INICIADO;
        this.ano = LocalDateTime.now().getYear();
    }
}
