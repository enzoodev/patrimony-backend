package com.dpmg.patrimonio.models.dtos.ITPatrimony;

import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaveITPatrimonyDTO extends BaseAuditDTO {
    @NotNull(message = "O parâmetro 'numPatrimonio' é obrigatório para definir qual o número de patrimônio do item")
    private String numPatrimonio;

    @NotNull(message = "O parâmetro 'nomeMaquina' é obrigatório para definir qual o nome da máquina")
    private String nomeMaquina;

    private String modelo;

    private String marca;

    private String versaoOffice;

    private String sistemaOperacional;

    @NotNull(message = "O parâmetro 'serialMaquina' é obrigatório para definir qual o serial da máquina")
    private String serialMaquina;

    private String serialWindows;

    private String serialOffice;

    private String processador;

    private String memoria;

    @NotNull(message = "O parâmetro 'ativo' é obrigatório para definir se o item está ativo ou não")
    private Boolean ativo;

    private String lotacao;

    private String hd;

    private String macAdress;

    private String nomeResponsavel;

    private String observacao;
}
