package com.dpmg.patrimonio.controllers;

import com.dpmg.patrimonio.models.dtos.shared.FindByInventoryIdDTO;
import com.dpmg.patrimonio.models.dtos.Patrimony.UnitDTO;
import com.dpmg.patrimonio.models.dtos.shared.ResponseDTO;
import com.dpmg.patrimonio.services.InventoryService;
import com.dpmg.patrimonio.services.PatrimonyService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Data
@RestController
@RequestMapping("/utils")
public class UtilsController {
    private final InventoryService inventoryService;
    private final PatrimonyService patrimonyService;

    @GetMapping("/situacoes")
    public ResponseEntity<ResponseDTO<Map<String, String>>> getPatrimonySituations() {
        return ResponseEntity.ok(inventoryService.findPatrimonySituations());
    }

    @GetMapping("/outras-situacoes")
    public ResponseEntity<ResponseDTO<Map<String, String>>> getPatrimonyOtherSituations() {
        return ResponseEntity.ok(inventoryService.findPatrimonyOtherSituations());
    }

    @GetMapping("/descricao")
    public ResponseEntity<ResponseDTO<List<String>>> findDescriptionsByInventoryId(@Valid @ModelAttribute FindByInventoryIdDTO dto) {
        return ResponseEntity.ok(patrimonyService.findDescriptionsByInventoryId(dto.getIdInventario()));
    }

    @GetMapping("/unidade")
    public ResponseEntity<ResponseDTO<List<UnitDTO>>> findUnitsByInventoryId(@Valid @ModelAttribute FindByInventoryIdDTO dto) {
        return ResponseEntity.ok(patrimonyService.findResponsibleUnitListByInventoryId(dto.getIdInventario()));
    }
}
