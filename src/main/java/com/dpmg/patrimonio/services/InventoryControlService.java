package com.dpmg.patrimonio.services;

import com.dpmg.patrimonio.exceptions.*;
import com.dpmg.patrimonio.models.dtos.InventoryControl.InventoryControlDTO;
import com.dpmg.patrimonio.models.dtos.InventoryControl.UpdateInventoryStatusDTO;
import com.dpmg.patrimonio.models.dtos.shared.ResponseDTO;
import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import com.dpmg.patrimonio.models.entities.InventoryControlEntity;
import com.dpmg.patrimonio.models.enums.InventoryControlSituationEnum;
import com.dpmg.patrimonio.models.enums.PatrimonySituationEnum;
import com.dpmg.patrimonio.repositories.InventoryControlRepository;
import com.dpmg.patrimonio.utils.Messages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Data
@Service
public class InventoryControlService {
    private final ExcelService excelService;
    private final InventoryControlRepository inventoryControlRepository;
    private final HttpServletRequest request;

    private InventoryControlEntity findById(Long id) {
        Optional<InventoryControlEntity> inventoryControl = inventoryControlRepository.findById(id);

        if (inventoryControl.isEmpty() || Boolean.FALSE.equals(inventoryControl.get().getIsAtivo())) {
            throw new InventoryNotFoundException();
        }

        return inventoryControl.get();
    }

    public ResponseDTO<InventoryControlDTO> findDTOByYear(Integer year) {
        InventoryControlDTO inventoryControlDTO = inventoryControlRepository.findDTOByYear(year);
        String message = inventoryControlDTO == null ? Messages.INVENTORY_NOT_FOUND : Messages.FOUND_INVENTORY;
        return new ResponseDTO<>(message, inventoryControlDTO);
    }

    public ResponseDTO<Map<String, String>> findPatrimonySituations() {
        return new ResponseDTO<>(Messages.SUCCESS_FETCH, PatrimonySituationEnum.getLabels());
    }

    public ResponseDTO<Map<String, String>> findPatrimonyOtherSituations() {
        return new ResponseDTO<>(Messages.SUCCESS_FETCH, PatrimonySituationEnum.getOtherSituationLabels());
    }

    public void verifyIfIsOpenById(Long id) {
        InventoryControlSituationEnum status = inventoryControlRepository.findStatusById(id);

        if (status == null) {
            throw new InventoryNotFoundException();
        }

        if (status != InventoryControlSituationEnum.ABERTO) {
            throw new CanNotUpdateItemIfInventoryIsNotOpenException();
        }
    }

    @Transactional
    public ResponseDTO<InventoryControlSituationEnum> updateStatus(UpdateInventoryStatusDTO dto) {
        Long id = dto.getIdInventario();
        InventoryControlSituationEnum status = dto.getStatus();

        if (status != InventoryControlSituationEnum.ABERTO && status != InventoryControlSituationEnum.FECHADO) {
            throw new InvalidStatusException();
        }

        InventoryControlEntity inventory = findById(id);

        if (inventory.getStatus() == InventoryControlSituationEnum.ENCERRADO) {
            throw new CanNotUpdateFinishedInventoryException(inventory.getAno());
        }

        inventory.setStatus(status);
        inventory.setSgProjetoModificador(dto.getSgProjetoModificador());
        inventory.setSgAcaoModificadora(dto.getSgAcaoModificadora());
        inventory.setNoEndPointModificador(request.getRequestURL().toString());

        inventoryControlRepository.save(inventory);
        return new ResponseDTO<>(Messages.SUCCESS_UPDATE_STATUS, status);
    }

    private void closeLastYearInventory() {
        Integer lastYear = LocalDateTime.now().getYear() - 1;
        Optional<InventoryControlEntity> inventoryControlOptional = inventoryControlRepository.findByAnoAndIsAtivoTrue(lastYear);

        if (inventoryControlOptional.isEmpty()) {
            return;
        }

        InventoryControlEntity inventoryControl = inventoryControlOptional.get();
        inventoryControl.setStatus(InventoryControlSituationEnum.ENCERRADO);

        inventoryControl.setSgProjetoModificador("IVT");
        inventoryControl.setSgAcaoModificadora("AUTO");
        inventoryControl.setNoEndPointModificador("AUTO");

        inventoryControlRepository.save(inventoryControl);
    }

    private void createInventoryForCurrentYear() {
        Integer currentYear = LocalDateTime.now().getYear();
        Optional<InventoryControlEntity> inventoryControlOptional = inventoryControlRepository.findByAnoAndIsAtivoTrue(currentYear);

        if (inventoryControlOptional.isPresent()) {
            return;
        }

        InventoryControlEntity inventoryControl = new InventoryControlEntity();

        // TODO: change it in the future
        inventoryControl.setUuidUsuario("TESTE DO ENZO");
        inventoryControl.setSgProjetoModificador("IVT");
        inventoryControl.setSgAcaoModificadora("AUTO");
        inventoryControl.setNoEndPointModificador("AUTO");

        inventoryControlRepository.save(inventoryControl);
    }

    /**
     * every year, close the last year inventory and create the inventory for the new year.
     */
    @Scheduled(cron = "0 0 0 1 1 *", zone = "America/Sao_Paulo")
    @Transactional
    public void rolloverInventory() {
        closeLastYearInventory();
        createInventoryForCurrentYear();
    }

    private InventoryControlEntity createInventoryToImport(BaseAuditDTO auditDTO) {
        InventoryControlEntity inventoryControl = new InventoryControlEntity();
        inventoryControl.setStatus(InventoryControlSituationEnum.IMPORTACAO_EM_ANDAMENTO);

        inventoryControl.setSgProjetoModificador(auditDTO.getSgProjetoModificador());
        inventoryControl.setSgAcaoModificadora(auditDTO.getSgAcaoModificadora());
        inventoryControl.setNoEndPointModificador(request.getRequestURL().toString());

        return inventoryControlRepository.save(inventoryControl);
    }

    private InventoryControlEntity getInventoryControlEntityToImport(BaseAuditDTO auditDTO) {
        Optional<InventoryControlEntity> inventoryControlEntityOptional = inventoryControlRepository.findByAnoAndIsAtivoTrue(LocalDateTime.now().getYear());

        // this condition usually will not be true because the inventory is created in the rolloverInventory method, but it is a good practice to check
        if (inventoryControlEntityOptional.isEmpty()) {
            return createInventoryToImport(auditDTO);
        }

        InventoryControlEntity inventoryControlEntity = inventoryControlEntityOptional.get();
        InventoryControlSituationEnum status = inventoryControlEntity.getStatus();

        if (status == InventoryControlSituationEnum.INICIADO) {
            return inventoryControlEntity;
        }

        if (status == InventoryControlSituationEnum.IMPORTADO) {
            inventoryControlRepository.delete(inventoryControlEntity);
            return createInventoryToImport(auditDTO);
        }

        if (status == InventoryControlSituationEnum.IMPORTACAO_EM_ANDAMENTO) {
            throw new ImportAlreadyInProgressException();
        }

        throw new CanNotImportInventoryException(status);
    }

    private ResponseDTO<InventoryControlSituationEnum> startImport(InventoryControlEntity inventoryControlEntity, BaseAuditDTO auditData) {
        inventoryControlEntity.setStatus(InventoryControlSituationEnum.IMPORTACAO_EM_ANDAMENTO);
        inventoryControlEntity.setSgProjetoModificador(auditData.getSgProjetoModificador());
        inventoryControlEntity.setSgAcaoModificadora(auditData.getSgAcaoModificadora());
        inventoryControlEntity.setNoEndPointModificador(request.getRequestURL().toString());

        inventoryControlRepository.save(inventoryControlEntity);

        return new ResponseDTO<>(Messages.IMPORT_STARTED, inventoryControlEntity.getStatus());
    }

    private void finishImport(InventoryControlEntity inventoryControlEntity) {
        inventoryControlEntity.setStatus(InventoryControlSituationEnum.IMPORTADO);
        inventoryControlRepository.save(inventoryControlEntity);
    }

    @Transactional
    public ResponseDTO<InventoryControlSituationEnum> importInventory(
            MultipartFile file,
            String sgProjetoModificador,
            String sgAcaoModificadora
    ) {
        excelService.validateFile(file);

        BaseAuditDTO auditDTO = new BaseAuditDTO();
        auditDTO.setSgProjetoModificador(sgProjetoModificador);
        auditDTO.setSgAcaoModificadora(sgAcaoModificadora);

        InventoryControlEntity inventoryControlEntity = getInventoryControlEntityToImport(auditDTO);

        return startImport(inventoryControlEntity, auditDTO);
//
//        InventoryControlEntity inventoryControlEntityAfterUpdate = excelService.setInventoryDataFromExcel(
//                file,
//                inventoryControlEntity,
//                auditDTO,
//                request.getRequestURL().toString()
//        );
//
//        inventoryControlRepository.save(inventoryControlEntityAfterUpdate);
//        finishImport(inventoryControlEntityAfterUpdate);
    }
}
