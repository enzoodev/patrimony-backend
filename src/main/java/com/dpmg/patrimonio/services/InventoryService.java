package com.dpmg.patrimonio.services;

import com.dpmg.patrimonio.exceptions.*;
import com.dpmg.patrimonio.models.dtos.Inventory.InventoryDTO;
import com.dpmg.patrimonio.models.dtos.Inventory.UpdateInventoryStatusDTO;
import com.dpmg.patrimonio.models.dtos.shared.ResponseDTO;
import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import com.dpmg.patrimonio.models.entities.InventoryEntity;
import com.dpmg.patrimonio.models.enums.InventorySituationEnum;
import com.dpmg.patrimonio.models.enums.PatrimonySituationEnum;
import com.dpmg.patrimonio.repositories.InventoryRepository;
import com.dpmg.patrimonio.utils.Messages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Data
@Service
public class InventoryService {
    private final HttpServletRequest request;
    private final ExcelService excelService;
    private final ImportInventoryExecutorService importInventoryExecutorService;
    private final InventoryRepository inventoryRepository;

    private InventoryEntity findById(Long id) {
        Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(id);

        if (optionalInventoryEntity.isEmpty() || Boolean.FALSE.equals(optionalInventoryEntity.get().getIsAtivo())) {
            throw new InventoryNotFoundException();
        }

        return optionalInventoryEntity.get();
    }

    public ResponseDTO<InventoryDTO> findDTOByYear(Integer year) {
        InventoryDTO inventoryDTO = inventoryRepository.findDTOByYear(year);

        if (inventoryDTO == null) {
            createInventoryForCurrentYear();
            inventoryRepository.flush();

            InventoryDTO newInventoryDTO = inventoryRepository.findDTOByYear(year);
            return new ResponseDTO<>(Messages.FOUND_INVENTORY, newInventoryDTO);
        }

        return new ResponseDTO<>(Messages.FOUND_INVENTORY, inventoryDTO);
    }

    public ResponseDTO<Map<String, String>> findPatrimonySituations() {
        return new ResponseDTO<>(Messages.SUCCESS_FETCH, PatrimonySituationEnum.getLabels());
    }

    public ResponseDTO<Map<String, String>> findPatrimonyOtherSituations() {
        return new ResponseDTO<>(Messages.SUCCESS_FETCH, PatrimonySituationEnum.getOtherSituationLabels());
    }

    public void verifyIfIsOpenById(Long id) {
        InventorySituationEnum status = inventoryRepository.findStatusById(id);

        if (status == null) {
            throw new InventoryNotFoundException();
        }

        if (status != InventorySituationEnum.ABERTO) {
            throw new CanNotUpdateItemIfInventoryIsNotOpenException();
        }
    }

    @Transactional
    public ResponseDTO<InventorySituationEnum> updateStatus(UpdateInventoryStatusDTO dto) {
        Long id = dto.getIdInventario();
        InventorySituationEnum status = dto.getStatus();

        if (status != InventorySituationEnum.ABERTO && status != InventorySituationEnum.FECHADO) {
            throw new InvalidStatusException();
        }

        InventoryEntity inventory = findById(id);

        if (inventory.getStatus() == InventorySituationEnum.ENCERRADO) {
            throw new CanNotUpdateFinishedInventoryException(inventory.getAno());
        }

        inventory.setStatus(status);
        inventory.setSgProjetoModificador(dto.getSgProjetoModificador());
        inventory.setSgAcaoModificadora(dto.getSgAcaoModificadora());
        inventory.setNoEndPointModificador(request.getRequestURL().toString());

        inventoryRepository.save(inventory);
        return new ResponseDTO<>(Messages.SUCCESS_UPDATE_STATUS, status);
    }

    private void closeLastYearInventory() {
        Integer lastYear = LocalDateTime.now().getYear() - 1;
        Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findByAnoAndIsAtivoTrue(lastYear);

        if (optionalInventoryEntity.isEmpty()) {
            return;
        }

        InventoryEntity inventoryEntity = optionalInventoryEntity.get();
        inventoryEntity.setStatus(InventorySituationEnum.ENCERRADO);

        inventoryEntity.setSgProjetoModificador("IVT");
        inventoryEntity.setSgAcaoModificadora("AUTO");
        inventoryEntity.setNoEndPointModificador("AUTO");

        inventoryRepository.save(inventoryEntity);
    }

    private void createInventoryForCurrentYear() {
        Integer currentYear = LocalDateTime.now().getYear();
        Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findByAnoAndIsAtivoTrue(currentYear);

        if (optionalInventoryEntity.isPresent()) {
            return;
        }

        InventoryEntity inventoryEntity = new InventoryEntity();

        // TODO: change it in the future
        inventoryEntity.setUuidUsuario("TESTE DO ENZO");
        inventoryEntity.setSgProjetoModificador("IVT");
        inventoryEntity.setSgAcaoModificadora("AUTO");
        inventoryEntity.setNoEndPointModificador("AUTO");

        inventoryRepository.save(inventoryEntity);
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

    public ResponseDTO<InventorySituationEnum> importInventory(
            MultipartFile file,
            String sgProjetoModificador,
            String sgAcaoModificadora
    ) {
        excelService.validateFile(file);

        BaseAuditDTO auditDTO = new BaseAuditDTO();
        auditDTO.setSgProjetoModificador(sgProjetoModificador);
        auditDTO.setSgAcaoModificadora(sgAcaoModificadora);

        try {
            byte[] fileBytes = file.getBytes();
            String requestURL = request.getRequestURL().toString();
            importInventoryExecutorService.execute(fileBytes, auditDTO, requestURL);

            return new ResponseDTO<>(Messages.IMPORT_STARTED, InventorySituationEnum.IMPORTACAO_EM_ANDAMENTO);
        } catch (IOException e) {
            throw new ImportAlreadyInProgressException();
        }
    }
}
