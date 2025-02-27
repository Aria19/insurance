package com.maghrebia.data_extract.Utils;

public class ExcelRowUtil {
    public static boolean isRowEmpty(Object... fields) {
        for (Object field : fields) {
            if (field != null && !field.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}

