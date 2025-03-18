package com.dpmg.patrimonio.services;

import com.dpmg.patrimonio.exceptions.CanNotImportInventoryException;
import com.dpmg.patrimonio.exceptions.ImportAlreadyInProgressException;
import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import com.dpmg.patrimonio.models.entities.InventoryControlEntity;
import com.dpmg.patrimonio.models.entities.PatrimonyEntity;
import com.dpmg.patrimonio.models.enums.InventoryControlSituationEnum;
import com.dpmg.patrimonio.repositories.InventoryControlRepository;
import lombok.Data;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class ImportProcessingService {
    private final ExcelService excelService;
    private final InventoryControlRepository inventoryControlRepository;
    private final EmailService emailService;

    private InventoryControlEntity createInventoryToImport(BaseAuditDTO auditDTO, String requestURL) {
        InventoryControlEntity inventoryControl = new InventoryControlEntity();

        inventoryControl.setUuidUsuario("TESTE DO ENZO");
        inventoryControl.setSgProjetoModificador(auditDTO.getSgProjetoModificador());
        inventoryControl.setSgAcaoModificadora(auditDTO.getSgAcaoModificadora());
        inventoryControl.setNoEndPointModificador(requestURL);

        return inventoryControlRepository.save(inventoryControl);
    }

    private InventoryControlEntity getInventoryControlEntityToImport(BaseAuditDTO auditDTO, String requestURL) {
        Optional<InventoryControlEntity> inventoryControlEntityOptional = inventoryControlRepository.findByAnoAndIsAtivoTrue(LocalDateTime.now().getYear());

        // this condition usually will not be true because the inventory is created in the rolloverInventory method, but it is a good practice to check
        if (inventoryControlEntityOptional.isEmpty()) {
            return createInventoryToImport(auditDTO, requestURL);
        }

        InventoryControlEntity inventoryControlEntity = inventoryControlEntityOptional.get();
        InventoryControlSituationEnum status = inventoryControlEntity.getStatus();

        if (status == InventoryControlSituationEnum.INICIADO) {
            return inventoryControlEntity;
        }

        if (status == InventoryControlSituationEnum.IMPORTADO) {
            inventoryControlRepository.delete(inventoryControlEntity);
            inventoryControlRepository.flush();
            return createInventoryToImport(auditDTO, requestURL);
        }

        if (status == InventoryControlSituationEnum.IMPORTACAO_EM_ANDAMENTO) {
            throw new ImportAlreadyInProgressException();
        }

        throw new CanNotImportInventoryException(status);
    }

    private InventoryControlEntity startImport(BaseAuditDTO auditDTO, String requestURL) {
        InventoryControlEntity inventoryControlEntity = getInventoryControlEntityToImport(auditDTO, requestURL);

        inventoryControlEntity.setStatus(InventoryControlSituationEnum.IMPORTACAO_EM_ANDAMENTO);
        inventoryControlEntity.setSgProjetoModificador(auditDTO.getSgProjetoModificador());
        inventoryControlEntity.setSgAcaoModificadora(auditDTO.getSgAcaoModificadora());
        inventoryControlEntity.setNoEndPointModificador(requestURL);

        inventoryControlRepository.save(inventoryControlEntity);

        return inventoryControlEntity;
    }

    private void rollbackImport(InventoryControlEntity inventoryControlEntity, String errorMessage) {
        inventoryControlEntity.setListaPatrimonio(null);
        inventoryControlEntity.setStatus(InventoryControlSituationEnum.INICIADO);
        inventoryControlRepository.save(inventoryControlEntity);

        emailService.sendImportFailureEmail(errorMessage);
    }
    
    private void finishImport(InventoryControlEntity inventoryControlEntity, List<PatrimonyEntity> patrimonyList) {
        inventoryControlEntity.setListaPatrimonio(patrimonyList);
        inventoryControlEntity.setStatus(InventoryControlSituationEnum.IMPORTADO);
        inventoryControlRepository.save(inventoryControlEntity);

        emailService.sendImportCompletionEmail(inventoryControlEntity);
    }

    @Async
    public void handleInventoryImport(
            MultipartFile file,
            BaseAuditDTO auditDTO,
            String requestURL
    ) {
        InventoryControlEntity inventoryControlEntity = startImport(auditDTO, requestURL);

        try {
            List<PatrimonyEntity> patrimonyList = excelService.getPatrimonyListFromExcel(
                    file,
                    inventoryControlEntity,
                    auditDTO,
                    requestURL
            );

            finishImport(inventoryControlEntity, patrimonyList);
        } catch (Exception e) {
            rollbackImport(inventoryControlEntity, e.getMessage());
        }
    }
}
