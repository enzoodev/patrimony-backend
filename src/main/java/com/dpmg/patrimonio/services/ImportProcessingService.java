package com.dpmg.patrimonio.services;

import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import com.dpmg.patrimonio.models.entities.InventoryControlEntity;
import com.dpmg.patrimonio.models.entities.PatrimonyEntity;
import com.dpmg.patrimonio.models.enums.InventoryControlSituationEnum;
import com.dpmg.patrimonio.repositories.InventoryControlRepository;
import lombok.Data;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Service
public class ImportProcessingService {
    private final ExcelService excelService;
    private final InventoryControlRepository inventoryControlRepository;
    private final EmailService emailService;

    @Async
    public void processImportAsync(MultipartFile file, InventoryControlEntity inventoryControlEntity, BaseAuditDTO auditDTO, String requestURL) {
        try {
            List<PatrimonyEntity> patrimonyList = excelService.getPatrimonyListFromExcel(
                    file,
                    inventoryControlEntity,
                    auditDTO,
                    requestURL
            );

            inventoryControlEntity.setListaPatrimonio(patrimonyList);
            inventoryControlEntity.setStatus(InventoryControlSituationEnum.IMPORTADO);
            inventoryControlRepository.save(inventoryControlEntity);

            emailService.sendImportCompletionEmail(inventoryControlEntity);
        } catch (Exception e) {
            inventoryControlEntity.setStatus(InventoryControlSituationEnum.INICIADO);
            inventoryControlEntity.setListaPatrimonio(null);
            inventoryControlRepository.save(inventoryControlEntity);
            emailService.sendImportFailureEmail(e.getMessage());
        }
    }
}
