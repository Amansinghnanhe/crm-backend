package com.crm.crmbackend.modules.lead.util;

import com.crm.crmbackend.modules.lead.entity.Lead;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<Lead> excelToLeads(InputStream is) {
        try{
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<Lead> leads = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();


                if (rowNumber ==0) {
                    rowNumber++;
                    continue;
                }
                Lead lead = new Lead();
                lead.setName(currentRow.getCell(0).getStringCellValue());
                lead.setEmail(currentRow.getCell(1).getStringCellValue());
                lead.setPhone(String.valueOf((long) currentRow.getCell(2).getNumericCellValue()));

                if (currentRow.getCell(3) !=null) {
                    lead.setStatus(currentRow.getCell(3).getStringCellValue());
                } else{
                    lead.setStatus("NEW");
                }
                leads.add(lead);
            }
            workbook.close();
            return leads;
        } catch (Exception e) {
            throw new RuntimeException("Excel  file parse karne mein dikkat aayi: "+ e.getMessage());
        }
    }
}
