package com.dpmg.patrimonio.controllers;

import com.dpmg.patrimonio.models.dtos.InventoryControl.FindInventoryByYearDTO;
import com.dpmg.patrimonio.models.dtos.InventoryControl.InventoryControlDTO;
import com.dpmg.patrimonio.models.dtos.InventoryControl.UpdateInventoryStatusDTO;
import com.dpmg.patrimonio.models.dtos.shared.ResponseDTO;
import com.dpmg.patrimonio.models.enums.InventoryControlSituationEnum;
import com.dpmg.patrimonio.services.InventoryControlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/controle-inventario")
@RequiredArgsConstructor
public class InventoryControlController {
    private final InventoryControlService inventoryControlService;

    @GetMapping
    public ResponseEntity<ResponseDTO<InventoryControlDTO>> findByYear(@Valid @ModelAttribute FindInventoryByYearDTO dto) {
        return ResponseEntity.ok(inventoryControlService.findDTOByYear(dto.getAno()));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<InventoryControlSituationEnum>> importInventory(
            @RequestParam("file") MultipartFile file,
            @RequestParam("sgProjetoModificador") String sgProjetoModificador,
            @RequestParam("sgAcaoModificadora") String sgAcaoModificadora
    ) {
        return ResponseEntity.ok(inventoryControlService.importInventory(file, sgProjetoModificador, sgAcaoModificadora));
    }

    @PatchMapping("/status")
    public ResponseEntity<ResponseDTO<InventoryControlSituationEnum>> updateStatus(@Valid @RequestBody UpdateInventoryStatusDTO dto) {
        return ResponseEntity.ok(inventoryControlService.updateStatus(dto));
    }
}
