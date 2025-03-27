package com.dpmg.patrimonio.controllers;

import com.dpmg.patrimonio.models.dtos.Patrimony.*;
import com.dpmg.patrimonio.models.dtos.PatrimonyOtherSituation.FindAllPatrimonyOtherSituationDTO;
import com.dpmg.patrimonio.models.dtos.PatrimonyOtherSituation.PatrimonyOtherSituationDTO;
import com.dpmg.patrimonio.models.dtos.PatrimonyOtherSituation.SavePatrimonyOtherSituationDTO;
import com.dpmg.patrimonio.models.dtos.shared.PaginatedResponseDTO;
import com.dpmg.patrimonio.models.dtos.shared.ResponseDTO;
import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import com.dpmg.patrimonio.services.PatrimonyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patrimonios")
@RequiredArgsConstructor
public class PatrimonyController {
    private final PatrimonyService patrimonyService;

    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<PatrimonyDTO>> findAll(
            @Valid @ModelAttribute FindAllPatrimonyListDTO dto
    ) {
        return ResponseEntity.ok(patrimonyService.findAll(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<PatrimonyDetailsDTO>> findItemDetailsById(@PathVariable Long id) {
        return ResponseEntity.ok(patrimonyService.findItemDetailsById(id));
    }

    @GetMapping("/{id}/observacao")
    public ResponseEntity<ResponseDTO<PatrimonyObservationDTO>> findItemObservationById(@PathVariable Long id) {
        return ResponseEntity.ok(patrimonyService.findItemObservationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> localizePatrimony(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePatrimonyObservationDTO dto
    ) {
        return ResponseEntity.ok(patrimonyService.localizePatrimony(id, dto));
    }

    @PutMapping("/{id}/observacao")
    public ResponseEntity<ResponseDTO<Void>> updateItemObservation(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePatrimonyObservationDTO dto
    ) {
        return ResponseEntity.ok(patrimonyService.updateItemObservation(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateItemSituation(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePatrimonySituationDTO dto
    ) {
        patrimonyService.updateItemSituation(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verificacao")
    public ResponseEntity<ResponseDTO<PatrimonyToBeLocalizedDTO>> verifyIfPatrimonyIsReadToBeLocalized(
            @Valid @RequestBody VerifyIfPatrimonyIsReadToBeLocalizedDTO dto
    ) {
        return ResponseEntity.ok(patrimonyService.verifyIfPatrimonyIsReadToBeLocalized(dto));
    }

    @GetMapping("/outras-situacoes")
    public ResponseEntity<PaginatedResponseDTO<PatrimonyOtherSituationDTO>> findOtherSituations(
            @Valid @ModelAttribute FindAllPatrimonyOtherSituationDTO dto
    ) {
        return ResponseEntity.ok(patrimonyService.findOtherSituations(dto));
    }

    @GetMapping("/outras-situacoes/{id}")
    public ResponseEntity<ResponseDTO<PatrimonyOtherSituationDTO>> findOtherSituationById(@PathVariable Long id) {
        return ResponseEntity.ok(patrimonyService.findOtherSituationById(id));
    }

    @PostMapping("/outras-situacoes")
    public ResponseEntity<ResponseDTO<PatrimonyOtherSituationDTO>> createOtherSituation(@Valid @RequestBody SavePatrimonyOtherSituationDTO dto) {
        return ResponseEntity.ok(patrimonyService.createOtherSituation(dto));
    }

    @PutMapping("/outras-situacoes/{id}")
    public ResponseEntity<ResponseDTO<PatrimonyOtherSituationDTO>> updateOtherSituation(@PathVariable Long id, @Valid @RequestBody SavePatrimonyOtherSituationDTO dto) {
        return ResponseEntity.ok(patrimonyService.updateOtherSituation(id, dto));
    }

    @DeleteMapping("/outras-situacoes/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteOtherSituation(
            @PathVariable Long id,
            @Valid @ModelAttribute BaseAuditDTO dto
    ) {
        return ResponseEntity.ok(patrimonyService.deactivatePatrimonyOtherSituation(id, dto));
    }
}
