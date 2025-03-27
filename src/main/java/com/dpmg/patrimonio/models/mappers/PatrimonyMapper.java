package com.dpmg.patrimonio.models.mappers;

import com.dpmg.patrimonio.models.dtos.Patrimony.UnitDTO;
import com.dpmg.patrimonio.models.dtos.PatrimonyOtherSituation.SavePatrimonyOtherSituationDTO;
import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import com.dpmg.patrimonio.models.entities.InventoryControlEntity;
import com.dpmg.patrimonio.models.entities.PatrimonyEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PatrimonyMapper {
    public PatrimonyEntity toEntity(InventoryControlEntity inventory, BaseAuditDTO auditData, String requestURL) {
        PatrimonyEntity patrimonyEntity = new PatrimonyEntity();

        patrimonyEntity.setInventario(inventory);
        patrimonyEntity.setIsOutraSituacao(false);
        patrimonyEntity.setIsPatrimonioForaDaUnidade(false);
        patrimonyEntity.setIsCadastroManual(false);
        patrimonyEntity.setDescricaoItemMaterial("teste do enzo");

        patrimonyEntity.setSgProjetoModificador(auditData.getSgProjetoModificador());
        patrimonyEntity.setSgAcaoModificadora(auditData.getSgAcaoModificadora());
        patrimonyEntity.setNoEndPointModificador(requestURL);
        patrimonyEntity.setUuidUsuario("TESTE DO ENZO");

        return patrimonyEntity;
    }

    public PatrimonyEntity toEntityFromOtherSituation(SavePatrimonyOtherSituationDTO dto, UnitDTO unit, String requestURL) {
        PatrimonyEntity patrimonyEntity = new PatrimonyEntity();

        patrimonyEntity.setIsOutraSituacao(true);
        patrimonyEntity.setIsCadastroManual(true);
        patrimonyEntity.setSituacao(dto.getSituacao());
        patrimonyEntity.setNumeroPatrimonio(dto.getNumeroPatrimonio());
        patrimonyEntity.setDescricaoItemMaterial(dto.getDescricaoItemMaterial());
        patrimonyEntity.setCodigoUnidadeResponsavel(unit.getCodigo());
        patrimonyEntity.setNomeUnidadeResponsavel(unit.getNome());
        patrimonyEntity.setCodigoUnidadeEncontrado(unit.getCodigo());
        patrimonyEntity.setNomeUnidadeEncontrado(unit.getNome());

        patrimonyEntity.setSgProjetoModificador(dto.getSgProjetoModificador());
        patrimonyEntity.setSgAcaoModificadora(dto.getSgAcaoModificadora());
        patrimonyEntity.setNoEndPointModificador(requestURL);

        patrimonyEntity.setUuidUsuario("TESTE DO ENZO");

//        patrimonyEntity.setSituacao(dto.getSituacao());
//        patrimonyEntity.setNumeroPatrimonio(dto.getNumeroPatrimonio());
//        patrimonyEntity.setDescricaoItemMaterial(dto.getDescricaoItemMaterial());
//        patrimonyEntity.setCodigoUnidadeResponsavel(unit.getCodigo());
//        patrimonyEntity.setNomeUnidadeResponsavel(unit.getNome());
//        patrimonyEntity.setCodigoUnidadeEncontrado(unit.getCodigo());
//        patrimonyEntity.setNomeUnidadeEncontrado(unit.getNome());
//
//        patrimonyEntity.setSgProjetoModificador(dto.getSgProjetoModificador());
//        patrimonyEntity.setSgAcaoModificadora(dto.getSgAcaoModificadora());
//        patrimonyEntity.setNoEndPointModificador(request.getRequestURL().toString());

        return patrimonyEntity;
    }
}
