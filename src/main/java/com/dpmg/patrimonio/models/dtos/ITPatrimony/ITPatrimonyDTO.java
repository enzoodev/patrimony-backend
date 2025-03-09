package com.dpmg.patrimonio.models.dtos.ITPatrimony;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ITPatrimonyDTO {
    private Long id;
    private String numPatrimonio;
    private String nomeMaquina;
    private String modelo;
    private String marca;
    private String versaoOffice;
    private String sistemaOperacional;
    private String serialMaquina;
    private String serialWindows;
    private String serialOffice;
    private String processador;
    private String memoria;
    private String hd;
    private String lotacao;
    private String nomeResponsavel;
    private Boolean ativo;
}
