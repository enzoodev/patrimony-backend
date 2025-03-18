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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class ImportInventoryExecutorService {
    private final ExcelService excelService;
    private final InventoryControlRepository inventoryControlRepository;
    private final EmailService emailService;

    private InventoryControlEntity createInventory(BaseAuditDTO auditDTO, String requestURL) {
        InventoryControlEntity inventoryControl = new InventoryControlEntity();

        inventoryControl.setUuidUsuario("TESTE DO ENZO");
        inventoryControl.setSgProjetoModificador(auditDTO.getSgProjetoModificador());
        inventoryControl.setSgAcaoModificadora(auditDTO.getSgAcaoModificadora());
        inventoryControl.setNoEndPointModificador(requestURL);

        return inventoryControlRepository.save(inventoryControl);
    }

    private InventoryControlEntity getInventoryControlEntity(BaseAuditDTO auditDTO, String requestURL) {
        Optional<InventoryControlEntity> inventoryControlEntityOptional = inventoryControlRepository.findByAnoAndIsAtivoTrue(LocalDateTime.now().getYear());

        // this condition usually will not be true because the inventory is created in the rolloverInventory method, but it is a good practice to check
        if (inventoryControlEntityOptional.isEmpty()) {
            return createInventory(auditDTO, requestURL);
        }

        InventoryControlEntity inventoryControlEntity = inventoryControlEntityOptional.get();
        InventoryControlSituationEnum status = inventoryControlEntity.getStatus();

        if (status == InventoryControlSituationEnum.INICIADO) {
            return inventoryControlEntity;
        }

        if (status == InventoryControlSituationEnum.IMPORTADO) {
            inventoryControlRepository.delete(inventoryControlEntity);
            inventoryControlRepository.flush();
            return createInventory(auditDTO, requestURL);
        }

        if (status == InventoryControlSituationEnum.IMPORTACAO_EM_ANDAMENTO) {
            throw new ImportAlreadyInProgressException();
        }

        throw new CanNotImportInventoryException(status);
    }

    private InventoryControlEntity start(BaseAuditDTO auditDTO, String requestURL) {
        InventoryControlEntity inventoryControlEntity = getInventoryControlEntity(auditDTO, requestURL);

        inventoryControlEntity.setStatus(InventoryControlSituationEnum.IMPORTACAO_EM_ANDAMENTO);
        inventoryControlEntity.setSgProjetoModificador(auditDTO.getSgProjetoModificador());
        inventoryControlEntity.setSgAcaoModificadora(auditDTO.getSgAcaoModificadora());
        inventoryControlEntity.setNoEndPointModificador(requestURL);

        return inventoryControlRepository.save(inventoryControlEntity);
    }

    private void rollback(String errorMessage) {
        Optional<InventoryControlEntity> inventoryControlEntityOptional = inventoryControlRepository.findByAnoAndIsAtivoTrue(LocalDateTime.now().getYear());

        if (inventoryControlEntityOptional.isEmpty()) {
            emailService.sendImportFailureEmail(errorMessage);
            return;
        }

        InventoryControlEntity inventoryControlEntity = inventoryControlEntityOptional.get();
        inventoryControlEntity.setListaPatrimonio(null);
        inventoryControlEntity.setStatus(InventoryControlSituationEnum.INICIADO);
        inventoryControlRepository.save(inventoryControlEntity);

        emailService.sendImportFailureEmail(errorMessage);
    }
    
    private void finish(InventoryControlEntity inventoryControlEntity, List<PatrimonyEntity> patrimonyList) {
        inventoryControlEntity.setListaPatrimonio(patrimonyList);
        inventoryControlEntity.setStatus(InventoryControlSituationEnum.IMPORTADO);
        inventoryControlRepository.save(inventoryControlEntity);

        emailService.sendImportCompletionEmail(inventoryControlEntity);
    }

    @Async("importInventoryExecutor")
    public void execute(
            byte[] fileBytes,
            BaseAuditDTO auditDTO,
            String requestURL
    ) {
        try {
            InventoryControlEntity inventoryControlEntity = start(auditDTO, requestURL);
            List<PatrimonyEntity> patrimonyList = excelService.getPatrimonyListFromExcel(
                    fileBytes,
                    inventoryControlEntity,
                    auditDTO,
                    requestURL
            );

            finish(inventoryControlEntity, patrimonyList);
        } catch (Exception exception) {
            rollback(exception.getMessage());
        }
    }
}
