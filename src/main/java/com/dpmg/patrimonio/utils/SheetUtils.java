package com.dpmg.patrimonio.utils;

import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class SheetUtils {
    public final String mainName = "Material Permanente";

    public String getStringCellValue(Cell cell) {
        if (cell == null)
            return null;

        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }

        String value = cell.getStringCellValue().trim();

        if (value.isEmpty() || value.equalsIgnoreCase("NAO SE APLICA")) {
            return null;
        }

        return value;
    }

    public Long getNumericCellValue(Cell cell) {
        if (cell == null) return null;

        if (cell.getCellType() == CellType.STRING) {
            String stringValue = cell.getStringCellValue().trim();

            if (stringValue.isEmpty() || stringValue.equalsIgnoreCase("NAO SE APLICA")) {
                return null;
            }

            return Long.parseLong(cell.getStringCellValue());
        }

        double doubleValue = getDoubleCellValue(cell);

        if (doubleValue == 0) {
            return null;
        }

        long numericValue = (long) doubleValue;
        return (numericValue == 0) ? null : (long) numericValue;
    }

    public Double getDoubleCellValue(Cell cell) {
        if (cell == null) return null;

        if (cell.getCellType() == CellType.STRING) {
            return Double.parseDouble(cell.getStringCellValue());
        }

        double numericValue = cell.getNumericCellValue();
        return (numericValue == 0) ? null : numericValue;
    }

    public Date getDateCellValue(Cell cell) {
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            }

            if (cell.getCellType() == CellType.STRING) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                return dateFormat.parse(cell.getStringCellValue().trim());
            }
        } catch (ParseException e) {
            return null;
        }

        return null;
    }

    public boolean isRowEmpty(Row row) {
        for (Cell cell : row) {
            if (cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
