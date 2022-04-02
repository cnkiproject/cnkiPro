package net.cnki.cnkispringboot.Utilitys;


import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelExportUtil {
    private static final Logger logger = LogManager.getLogger(ExcelExportUtil.class);

    // 导出Excel
    public Boolean exportExcel(HSSFWorkbook workbook, int sheetNum,
                               String sheetTitle, String[] headers, List<List<String>> result,
                               OutputStream out) {
        Boolean res = false;
        try {
            HSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(sheetNum, sheetTitle);
            sheet.setDefaultColumnWidth((short) 20);
            CellStyle style = workbook.createCellStyle();
            style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
            style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());


            Font headerFont = workbook.createFont();
            headerFont.setFontName("Arial");
            headerFont.setFontHeightInPoints((short) 10);
            headerFont.setBoldweight((short) 200);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            style.setFont(headerFont);

            style.setWrapText(true);

            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell((short) i);

                cell.setCellStyle(style);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text.toString());
            }
            if (result != null) {
                int index = 1;
                for (List<String> m : result) {
                    row = sheet.createRow(index);
                    int cellIndex = 0;
                    for (String str : m) {
                        HSSFCell cell = row.createCell((short) cellIndex);
                        if(!StringUtils.isEmpty(str)){
                            cell.setCellValue(str.toString());
                        }else{
                            cell.setCellValue("");
                        }

                        cellIndex++;
                    }
                    index++;
                }
            }
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtil.getStackTrace(e));
        }
        return res;
    }
}
