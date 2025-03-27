package com.dpmg.patrimonio.services;

import com.dpmg.patrimonio.exceptions.CanNotImportInventoryException;
import com.dpmg.patrimonio.exceptions.ImportAlreadyInProgressException;
import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import com.dpmg.patrimonio.models.entities.InventoryEntity;
import com.dpmg.patrimonio.models.entities.PatrimonyEntity;
import com.dpmg.patrimonio.models.enums.InventorySituationEnum;
import com.dpmg.patrimonio.repositories.InventoryRepository;
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
    private final InventoryRepository inventoryRepository;
    private final EmailService emailService;

    private InventoryEntity createInventory(BaseAuditDTO auditDTO, String requestURL) {
        InventoryEntity inventoryEntity = new InventoryEntity();

        inventoryEntity.setUuidUsuario("TESTE DO ENZO");
        inventoryEntity.setSgProjetoModificador(auditDTO.getSgProjetoModificador());
        inventoryEntity.setSgAcaoModificadora(auditDTO.getSgAcaoModificadora());
        inventoryEntity.setNoEndPointModificador(requestURL);

        return inventoryRepository.save(inventoryEntity);
    }

    private InventoryEntity getInventoryEntity(BaseAuditDTO auditDTO, String requestURL) {
        Optional<InventoryEntity> inventoryEntityOptional = inventoryRepository.findByAnoAndIsAtivoTrue(LocalDateTime.now().getYear());

        // this condition usually will not be true because the inventory is created in the rolloverInventory method, but it is a good practice to check
        if (inventoryEntityOptional.isEmpty()) {
            return createInventory(auditDTO, requestURL);
        }

        InventoryEntity inventoryEntity = inventoryEntityOptional.get();
        InventorySituationEnum status = inventoryEntity.getStatus();

        if (status == InventorySituationEnum.INICIADO) {
            return inventoryEntity;
        }

        if (status == InventorySituationEnum.IMPORTADO) {
            inventoryRepository.delete(inventoryEntity);
            inventoryRepository.flush();
            return createInventory(auditDTO, requestURL);
        }

        if (status == InventorySituationEnum.IMPORTACAO_EM_ANDAMENTO) {
            throw new ImportAlreadyInProgressException();
        }

        throw new CanNotImportInventoryException(status);
    }

    private InventoryEntity start(BaseAuditDTO auditDTO, String requestURL) {
        InventoryEntity inventoryEntity = getInventoryEntity(auditDTO, requestURL);

        inventoryEntity.setStatus(InventorySituationEnum.IMPORTACAO_EM_ANDAMENTO);
        inventoryEntity.setSgProjetoModificador(auditDTO.getSgProjetoModificador());
        inventoryEntity.setSgAcaoModificadora(auditDTO.getSgAcaoModificadora());
        inventoryEntity.setNoEndPointModificador(requestURL);

        return inventoryRepository.save(inventoryEntity);
    }

    private void rollback(String errorMessage) {
        Optional<InventoryEntity> inventoryEntityOptional = inventoryRepository.findByAnoAndIsAtivoTrue(LocalDateTime.now().getYear());

        if (inventoryEntityOptional.isEmpty()) {
            emailService.sendImportFailureEmail(errorMessage);
            return;
        }

        InventoryEntity inventoryEntity = inventoryEntityOptional.get();
        inventoryEntity.setListaPatrimonio(null);
        inventoryEntity.setStatus(InventorySituationEnum.INICIADO);
        inventoryRepository.save(inventoryEntity);

        emailService.sendImportFailureEmail(errorMessage);
    }
    
    private void finish(InventoryEntity inventoryEntity, List<PatrimonyEntity> patrimonyList) {
        inventoryEntity.setListaPatrimonio(patrimonyList);
        inventoryEntity.setStatus(InventorySituationEnum.IMPORTADO);
        inventoryRepository.save(inventoryEntity);

        emailService.sendImportCompletionEmail(inventoryEntity);
    }

    @Async("importInventoryExecutor")
    public void execute(
            byte[] fileBytes,
            BaseAuditDTO auditDTO,
            String requestURL
    ) {
        try {
            InventoryEntity inventoryEntity = start(auditDTO, requestURL);
            List<PatrimonyEntity> patrimonyList = excelService.getPatrimonyListFromExcel(
                    fileBytes,
                    inventoryEntity,
                    auditDTO,
                    requestURL
            );

            finish(inventoryEntity, patrimonyList);
        } catch (Exception exception) {
            rollback(exception.getMessage());
        }
    }
}
