package com.dpmg.patrimonio.models.dtos.ITPatrimony;

import com.dpmg.patrimonio.models.dtos.shared.FindPaginatedDataDTO;
import com.dpmg.patrimonio.validation.ExclusiveSearch;
import lombok.Data;

@Data
@ExclusiveSearch(
        mainField = "patrimonio",
        otherFields = { "nomeMaquina", "serialMaquina", "serialOffice", "modelo", "versaoOffice", "sistemaOperacional", "status" }
)
public class FindITPatrimonyListDTO extends FindPaginatedDataDTO {
    private String patrimonio;
    private String nomeMaquina;
    private String serialMaquina;
    private String serialOffice;
    private String modelo;
    private String versaoOffice;
    private String sistemaOperacional;
    private Boolean status;
}
