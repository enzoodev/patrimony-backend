package com.dpmg.patrimonio.models.dtos.InventoryControl;

import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import com.dpmg.patrimonio.models.entities.InventoryControlEntity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImportInventoryDTO {
    private MultipartFile file;
    private InventoryControlEntity inventory;
    private BaseAuditDTO auditData;
    private String requestURL;
}
