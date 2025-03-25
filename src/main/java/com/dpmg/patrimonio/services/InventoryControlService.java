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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Data
@Service
public class InventoryControlService {
    private final HttpServletRequest request;
    private final ExcelService excelService;
    private final ImportInventoryExecutorService importInventoryExecutorService;
    private final InventoryControlRepository inventoryControlRepository;

    private InventoryControlEntity findById(Long id) {
        Optional<InventoryControlEntity> inventoryControl = inventoryControlRepository.findById(id);

        if (inventoryControl.isEmpty() || Boolean.FALSE.equals(inventoryControl.get().getIsAtivo())) {
            throw new InventoryNotFoundException();
        }

        return inventoryControl.get();
    }

    public ResponseDTO<InventoryControlDTO> findDTOByYear(Integer year) {
        InventoryControlDTO inventoryControlDTO = inventoryControlRepository.findDTOByYear(year);

        if (inventoryControlDTO == null) {
            createInventoryForCurrentYear();
            inventoryControlRepository.flush();

            InventoryControlDTO newInventoryControlDTO = inventoryControlRepository.findDTOByYear(year);
            return new ResponseDTO<>(Messages.FOUND_INVENTORY, newInventoryControlDTO);
        }

        return new ResponseDTO<>(Messages.FOUND_INVENTORY, inventoryControlDTO);
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

    public ResponseDTO<InventoryControlSituationEnum> importInventory(
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

            return new ResponseDTO<>(Messages.IMPORT_STARTED, InventoryControlSituationEnum.IMPORTACAO_EM_ANDAMENTO);
        } catch (IOException e) {
            throw new ImportAlreadyInProgressException();
        }
    }
}
