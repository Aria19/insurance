package com.maghrebia.data_extract.Utils;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelCellUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelCellUtil.class);

    public static <T> T getCellValue(Row row, int cellIndex, Class<T> type) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return null;
        }

        try {
            switch (type.getSimpleName()) {
                case "String":
                    return type.cast(getStringValue(cell));

                case "Integer":
                    return type.cast(getIntegerValue(cell));

                case "Float":
                    return type.cast(getFloatValue(cell));

                case "Double":
                    return type.cast(getDoubleValue(cell));

                case "Date":
                    return type.cast(getDateValue(cell));

                default:
                    throw new IllegalArgumentException("Unsupported type: " + type.getSimpleName());
            }
        } catch (Exception e) {
            logger.warn("Error converting cell at index {}: {}", cellIndex, e.getMessage());
            return null;
        }
    }

    private static String getStringValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        } else {
            return cell.getStringCellValue().trim();
        }
    }

    private static Integer getIntegerValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Integer.parseInt(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Float getFloatValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return (float) cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Float.parseFloat(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Double getDoubleValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }
        return null;
    }

    private static Date getDateValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue(); // Correctly formatted date
                } else {
                    return null; // Numeric but not a date
                }

            case STRING:
                String dateStr = cell.getStringCellValue().trim();
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    return formatter.parse(dateStr);
                } catch (ParseException e) {
                    return null; // Could not parse text as date
                }

            default:
                return null;
        }
    }
}
