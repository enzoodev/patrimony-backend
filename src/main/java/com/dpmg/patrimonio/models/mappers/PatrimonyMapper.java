package com.dpmg.patrimonio.models.mappers;

import com.dpmg.patrimonio.models.dtos.Patrimony.UnitDTO;
import com.dpmg.patrimonio.models.dtos.PatrimonyOtherSituation.SavePatrimonyOtherSituationDTO;
import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import com.dpmg.patrimonio.models.entities.InventoryEntity;
import com.dpmg.patrimonio.models.entities.PatrimonyEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PatrimonyMapper {
    public PatrimonyEntity toEntity(InventoryEntity inventory, BaseAuditDTO auditData, String requestURL) {
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

    public PatrimonyEntity toEntityFromCreateOtherSituation(SavePatrimonyOtherSituationDTO dto, UnitDTO unit, String requestURL) {
        PatrimonyEntity patrimonyEntity = new PatrimonyEntity();

        patrimonyEntity.setIsOutraSituacao(true);
        patrimonyEntity.setIsCadastroManual(true);

        return setCommonPatrimonyData(patrimonyEntity, dto, unit, requestURL);
    }

    public PatrimonyEntity toEntityFromUpdateOtherSituation(
            PatrimonyEntity patrimonyEntity,
            SavePatrimonyOtherSituationDTO dto,
            UnitDTO unit,
            String requestURL
    ) {
        return setCommonPatrimonyData(patrimonyEntity, dto, unit, requestURL);
    }

    private PatrimonyEntity setCommonPatrimonyData(PatrimonyEntity patrimonyEntity, SavePatrimonyOtherSituationDTO dto, UnitDTO unit, String requestURL) {
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

        return patrimonyEntity;
    }
}
