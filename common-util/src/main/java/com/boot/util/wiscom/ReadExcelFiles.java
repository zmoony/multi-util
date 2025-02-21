package com.boot.util.wiscom;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadExcelFiles {

    /**
     * 递归获取目录下的所有文件名
     *
     * @param folder    目录
     * @param fileNames 文件名列表
     */
    public static void getFileNames(File folder, List<String> fileNames) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getFileNames(file, fileNames);
                } else {
                    fileNames.add(file.getPath());
                }
            }
        }
    }

    /**
     * 读取excel文件内容
     *
     * @param filePath 文件路径
     * @throws Exception
     */
    public static List<Map<String, Object>> readExcel(String filePath) throws Exception {
        List<Map<String, Object>> listReturn = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个sheet页
            int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum() + 1; // 获取行数
            List<String> keys = new ArrayList<>();
            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                int j = 0;
                Map<String, Object> mapRow = new HashMap<>();
                for (Cell cell : row) {
                    //System.out.print(cell.toString() + "\t"); // 输出单元格内容
                    if (i == 0) {
                        keys.add(cell.toString());
                    } else {
                        mapRow.put(keys.get(j), cell.toString());
                    }
                    j++;
                }
                if (i > 0) {
                    listReturn.add(mapRow);
                }
                //System.out.println();
            }
            workbook.close();
            inputStream.close();
        } catch (Exception e) {
            return listReturn;
        }

        return listReturn;
    }
}
