package com.dpmg.patrimonio.services;

import com.dpmg.patrimonio.exceptions.FailedImportException;
import com.dpmg.patrimonio.exceptions.InvalidImportFileException;
import com.dpmg.patrimonio.exceptions.WrongSheetNameException;
import com.dpmg.patrimonio.models.dtos.shared.SaveDataDTO;
import com.dpmg.patrimonio.models.entities.InventoryControlEntity;
import com.dpmg.patrimonio.models.entities.PatrimonyEntity;
import com.dpmg.patrimonio.utils.SheetUtils;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Data
@Component
public class ExcelService {
    public void validateFile(MultipartFile file) {
        boolean isValid = Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        if (!isValid) {
            throw new InvalidImportFileException();
        }
    }

    private void mapCellData(Cell cell, int cellIndex, PatrimonyEntity patrimonyEntity) {
        switch (cellIndex) {
            case 0 -> patrimonyEntity.setCodigoUnidadeContabil(SheetUtils.getNumericCellValue(cell));
            case 1 -> patrimonyEntity.setCodigoUnidadeResponsavel(SheetUtils.getNumericCellValue(cell));
            case 2 -> patrimonyEntity.setNomeUnidadeResponsavel(SheetUtils.getStringCellValue(cell));
            case 3 -> patrimonyEntity.setTipoBemPatrimonial(SheetUtils.getStringCellValue(cell));
            case 4 -> patrimonyEntity.setNumeroPatrimonio(SheetUtils.getNumericCellValue(cell));
            case 5 -> patrimonyEntity.setDescricaoItemMaterial(SheetUtils.getStringCellValue(cell));
            case 6 -> patrimonyEntity.setEstadoConservacaoBem(SheetUtils.getStringCellValue(cell));
            case 7 -> patrimonyEntity.setDataTombamento(SheetUtils.getDateCellValue(cell));
            case 8 -> patrimonyEntity.setValorBemPatrimonial(SheetUtils.getDoubleCellValue(cell));
            case 9 -> patrimonyEntity.setNumeroElementoItemDespesa(SheetUtils.getNumericCellValue(cell));
            case 10 -> patrimonyEntity.setCodigoUnidadeGerencial(SheetUtils.getNumericCellValue(cell));
            case 11 -> patrimonyEntity.setNomeUnidadeGerencial(SheetUtils.getStringCellValue(cell));
            case 12 -> patrimonyEntity.setOrgaoTerceiroResponsavel(SheetUtils.getStringCellValue(cell));
            case 13 -> patrimonyEntity.setNumeroItemMaterial(SheetUtils.getNumericCellValue(cell));
            case 14 -> patrimonyEntity.setMarca(SheetUtils.getStringCellValue(cell));
            case 15 -> patrimonyEntity.setModelo(SheetUtils.getStringCellValue(cell));
            case 16 -> patrimonyEntity.setSerie(SheetUtils.getStringCellValue(cell));
            case 17 -> patrimonyEntity.setDestinacaoBem(SheetUtils.getStringCellValue(cell));
            case 18 -> patrimonyEntity.setOrgaoTerceiroDestino(SheetUtils.getStringCellValue(cell));
            case 19 -> patrimonyEntity.setCodigoUnidadeDestino(SheetUtils.getNumericCellValue(cell));
            case 20 -> patrimonyEntity.setNomeUnidadeDestino(SheetUtils.getStringCellValue(cell));
            case 21 -> patrimonyEntity.setConvenio(SheetUtils.getStringCellValue(cell));
            case 22 -> patrimonyEntity.setNumeroDocumentoUltimaMovimentacao(SheetUtils.getStringCellValue(cell));
            case 23 -> patrimonyEntity.setNomeResponsavel(SheetUtils.getStringCellValue(cell));
            case 24 -> patrimonyEntity.setNomeCorresponsavel(SheetUtils.getStringCellValue(cell));
            default -> {}
        }
    }

    private PatrimonyEntity createPatrimonyEntity(InventoryControlEntity inventory, SaveDataDTO auditData, String requestURL) {
        PatrimonyEntity patrimonyEntity = new PatrimonyEntity();

        patrimonyEntity.setInventario(inventory);
        patrimonyEntity.setIsOutraSituacao(false);
        patrimonyEntity.setIsPatrimonioForaDaUnidade(false);
        patrimonyEntity.setIsCadastroManual(false);
        patrimonyEntity.setDescricaoItemMaterial("teste do enzo");

        patrimonyEntity.setSgProjetoModificador(auditData.getSgProjetoModificador());
        patrimonyEntity.setSgAcaoModificadora(auditData.getSgAcaoModificadora());
        patrimonyEntity.setNoEndPointModificador(requestURL);
        patrimonyEntity.setUuidUsuario("TESTE DO ENZO");

        return patrimonyEntity;
    }

    private XSSFSheet getSheetFromExcel(MultipartFile file) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheet(SheetUtils.mainName);

            if (sheet == null) {
                throw new WrongSheetNameException();
            }

            return sheet;
        } catch (IOException e) {
            throw new FailedImportException();
        }
    }

    public InventoryControlEntity setInventoryDataFromExcel(
            MultipartFile file,
            InventoryControlEntity inventory,
            SaveDataDTO auditData,
            String requestURL
    ) {
        XSSFSheet sheet = getSheetFromExcel(file);

        int rowIndex = 0;

        for (Row row : sheet) {
            if (rowIndex == 0) {
                rowIndex++;
                continue;
            }

            if (SheetUtils.isRowEmpty(row)) {
                break;
            }

            Iterator<Cell> cellIterator = row.iterator();
            int cellIndex = 0;

            PatrimonyEntity patrimonyEntity = createPatrimonyEntity(inventory, auditData, requestURL);

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                mapCellData(cell, cellIndex, patrimonyEntity);
                cellIndex++;
            }

            inventory.getListaPatrimonio().add(patrimonyEntity);
            rowIndex++;
        }

        return inventory;
    }
}
