package com.boot.util.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel导入导出
 */
public class ExcelUtil {

    /**
     * 导出多个sheet的excel
     * @param name
     * @param mapList
     * @param response
     * @param <T>
     */
    public static <T> void exportMultisheetExcel(String name, List<Map> mapList, HttpServletResponse response) {
        BufferedOutputStream bos = null;
        try {
            String fileName = name + ".xlsx";
            bos = getBufferedOutputStream(fileName, response);
            doExport(mapList, bos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从excel中读内容
     * @param filePath
     * @param sheetIndex
     * @return
     */
    public static List<Map<String, String>> readExcel(String filePath, Integer sheetIndex) {
        List<Map<String, String>> dataList = new ArrayList<>();
        Workbook wb = ExcelUtil.createWorkBook(filePath);
        if (wb != null) {
            Sheet sheet = wb.getSheetAt(sheetIndex);
            int maxRownum = sheet.getPhysicalNumberOfRows();
            Row firstRow = sheet.getRow(0);
            int maxColnum = firstRow.getPhysicalNumberOfCells();
            String columns[] = new String[maxColnum];
            for (int i = 0; i < maxRownum; i++) {
                Map<String, String> map = null;
                if (i > 0) {
                    map = new LinkedHashMap<>();
                    firstRow = sheet.getRow(i);
                }
                if (firstRow != null) {
                    String cellData = null;
                    for (int j = 0; j < maxColnum; j++) {
                        cellData = (String) ExcelUtil.getCellFormatValue(firstRow.getCell(j));
                        /*Cell cell = firstRow.getCell(j,Row.CREATE_NULL_AS_BLANK);
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cellData = cell.getStringCellValue();*/
                        if (i == 0) {
                            columns[j] = cellData;
                        } else {
                            map.put(columns[j], cellData);
                        }
                    }
                } else {
                    break;
                }
                if (i > 0) {
                    dataList.add(map);
                }
            }
        }
        return dataList;
    }

    /**
     　　根据excel提取表单内容
     * @param wb 工作表,空的cell返回‘’
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     * @throws
     * @author yuez
     * @date 2021/4/7 14:53
     */
    private List<Map<String, String>> readExcel(Workbook wb){
        List<Map<String, String>> dataList = new ArrayList<>();
        Sheet sheet = wb.getSheetAt(0);
        int maxRows = sheet.getPhysicalNumberOfRows();
        Row firstRow = sheet.getRow(0);
        int maxColumns = firstRow.getPhysicalNumberOfCells();
        String[] columns = new String[maxColumns];
        for (int i = 0; i < maxRows; i++) {
            Map<String, String> map = null;
            if (i > 0) {
                map = new LinkedHashMap<>();
                firstRow = sheet.getRow(i);
            }
            if (firstRow != null){
                String cellData = null;
                for (int j = 0; j < maxColumns; j++) {
                    Cell cell = firstRow.getCell(j,Row.CREATE_NULL_AS_BLANK);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cellData = cell.getStringCellValue();
                    if(i == 0){
                        columns[j] = cellData;
                    }else{
                        map.put(columns[j], cellData);
                    }
                }
            }else{
                break;
            }
            if(i > 0){
                dataList.add(map);
            }
        }
        return dataList;
    }

    private static BufferedOutputStream getBufferedOutputStream(String fileName, HttpServletResponse response) throws Exception {
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment;filename="
                + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        return new BufferedOutputStream(response.getOutputStream());
    }

    private static <T> void doExport(List<Map> mapList, OutputStream outputStream) {
        int maxBuff = 100;
        // 创建excel工作文本，100表示默认允许保存在内存中的行数
        SXSSFWorkbook wb = new SXSSFWorkbook(maxBuff);
        try {
            for (int i = 0; i < mapList.size(); i++) {
                Map map = mapList.get(i);
                String[] headers = (String[]) map.get("headers");
                Collection<T> dataList = (Collection<T>) map.get("dataList");
                String fileName = (String) map.get("fileName");
                createSheet(wb, null, headers, dataList, fileName, maxBuff);
            }

            if (outputStream != null) {
                wb.write(outputStream);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static <T> void createSheet(SXSSFWorkbook wb, String[] exportFields, String[] headers, Collection<T> dataList, String fileName, int maxBuff) throws NoSuchFieldException, IllegalAccessException, IOException {

        Sheet sh = wb.createSheet(fileName);

        CellStyle style = wb.createCellStyle();
        CellStyle style2 = wb.createCellStyle();
        //创建表头
        Font font = wb.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 11);//设置字体大小
        style.setFont(font);//选择需要用到的字体格式

        style.setFillForegroundColor(HSSFColor.YELLOW.index);// 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        style2.setFont(font);//选择需要用到的字体格式

        style2.setFillForegroundColor(HSSFColor.WHITE.index);// 设置背景色
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //垂直居中
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平向下居中
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框

        Row headerRow = sh.createRow(0); //表头

        int headerSize = headers.length;
        for (int cellnum = 0; cellnum < headerSize; cellnum++) {
            Cell cell = headerRow.createCell(cellnum);
            cell.setCellStyle(style);
            sh.setColumnWidth(cellnum, 4000);
            cell.setCellValue(headers[cellnum]);
        }

        int rownum = 0;
        Iterator<T> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            T data = iterator.next();
            Row row = sh.createRow(rownum + 1);

            Field[] fields = getExportFields(data.getClass(), exportFields);
            for (int cellnum = 0; cellnum < headerSize; cellnum++) {
                Cell cell = row.createCell(cellnum);
                cell.setCellStyle(style2);
                Field field = fields[cellnum];

                setData(field, data, field.getName(), cell);
            }
            rownum = sh.getLastRowNum();
            // 大数据量时将之前的数据保存到硬盘
            if (rownum % maxBuff == 0) {
                ((SXSSFSheet) sh).flushRows(maxBuff); // 超过100行后将之前的数据刷新到硬盘

            }
        }
    }


    private static <T> void doExport(String[] headers, String[] exportFields, Collection<T> dataList,
                                     String fileName, OutputStream outputStream) {

        int maxBuff = 100;
        // 创建excel工作文本，100表示默认允许保存在内存中的行数
        SXSSFWorkbook wb = new SXSSFWorkbook(maxBuff);
        try {
            createSheet(wb, exportFields, headers, dataList, fileName, maxBuff);
            if (outputStream != null) {
                wb.write(outputStream);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取单条数据的属性
     *
     * @param object
     * @param property
     * @param <T>
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static <T> Field getDataField(T object, String property) throws NoSuchFieldException, IllegalAccessException {
        Field dataField;
        if (property.contains(".")) {
            String p = property.substring(0, property.indexOf("."));
            dataField = object.getClass().getDeclaredField(p);
            return dataField;
        } else {
            dataField = object.getClass().getDeclaredField(property);
        }
        return dataField;
    }

    private static Field[] getExportFields(Class<?> targetClass, String[] exportFieldNames) {
        Field[] fields = null;
        if (exportFieldNames == null || exportFieldNames.length < 1) {
            fields = targetClass.getDeclaredFields();
        } else {
            fields = new Field[exportFieldNames.length];
            for (int i = 0; i < exportFieldNames.length; i++) {
                try {
                    fields[i] = targetClass.getDeclaredField(exportFieldNames[i]);
                } catch (Exception e) {
                    try {
                        fields[i] = targetClass.getSuperclass().getDeclaredField(exportFieldNames[i]);
                    } catch (Exception e1) {
                        throw new IllegalArgumentException("无法获取导出字段", e);
                    }

                }
            }
        }
        return fields;
    }

    /**
     * 根据属性设置对应的属性值
     *
     * @param dataField 属性
     * @param object    数据对象
     * @param property  表头的属性映射
     * @param cell      单元格
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private static <T> void setData(Field dataField, T object, String property, Cell cell)
            throws IllegalAccessException, NoSuchFieldException {
        dataField.setAccessible(true); //允许访问private属性
        Object val = dataField.get(object); //获取属性值
        Sheet sh = cell.getSheet(); //获取excel工作区
        CellStyle style = cell.getCellStyle(); //获取单元格样式
        int cellnum = cell.getColumnIndex();
        if (val != null) {
            if (dataField.getType().toString().endsWith("String")) {
                cell.setCellValue((String) val);
            } else if (dataField.getType().toString().endsWith("Integer") || dataField.getType().toString().endsWith("int")) {
                cell.setCellValue((Integer) val);
            } else if (dataField.getType().toString().endsWith("Long") || dataField.getType().toString().endsWith("long")) {
                cell.setCellValue(val.toString());
            } else if (dataField.getType().toString().endsWith("Double") || dataField.getType().toString().endsWith("double")) {
                cell.setCellValue((Double) val);
            } else if (dataField.getType().toString().endsWith("Date")) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cell.setCellValue(format.format((Date) val));
            } else if (dataField.getType().toString().endsWith("List")) {
                List list1 = (List) val;
                int size = list1.size();
                for (int i = 0; i < size; i++) {
                    //加1是因为要去掉点号
                    int start = property.indexOf(dataField.getName()) + dataField.getName().length() + 1;
                    String tempProperty = property.substring(start, property.length());
                    Field field = getDataField(list1.get(i), tempProperty);
                    Cell tempCell = cell;
                    if (i > 0) {
                        int rowNum = cell.getRowIndex() + i;
                        Row row = sh.getRow(rowNum);
                        if (row == null) {//另起一行
                            row = sh.createRow(rowNum);
                            //合并之前的空白单元格（在这里需要在header中按照顺序把list类型的字段放到最后，方便显示和合并单元格）
                            for (int j = 0; j < cell.getColumnIndex(); j++) {
                                sh.addMergedRegion(new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex() + size - 1, j, j));
                                Cell c = row.createCell(j);
                                c.setCellStyle(style);
                            }
                        }
                        tempCell = row.createCell(cellnum);
                        tempCell.setCellStyle(style);
                    }
                    //递归传参到单元格并获取偏移量（这里获取到的偏移量都是第二层后list的偏移量）
                    setData(field, list1.get(i), tempProperty, tempCell);
                }
            } else {
                if (property.contains(".")) {
                    String p = property.substring(property.indexOf(".") + 1, property.length());
                    Field field = getDataField(val, p);
                    setData(field, val, p, cell);
                } else {
                    cell.setCellValue(val.toString());
                }
            }
        }
    }


    private static Workbook createWorkBook(String filePath) {
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                return wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return wb = new XSSFWorkbook(is);
            } else {
                return wb;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    /**
     * 将字段转为相应的格式
     * @param cell
     * @return
     */
    private static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        cellValue = cell.getDateCellValue();////转换为日期格式YYYY-mm-dd
                    } else {
                        cellValue = String.valueOf(cell.getNumericCellValue()); //数字
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }



}
class ExcelUtilSelf {
    /**
    　　<code>
         if("2003".equals(type)){
            fileName = fileName+".xls";//创建工作薄
         }else{
            fileName = fileName+".xlsx";
         }
         Workbook wb = ExcelUtil.getHSSFWorkbook(list, type);
         this.setResponseHeader(response, fileName);
         OutputStream os = response.getOutputStream();
         wb.write(os);
         os.flush();
         os.close();
     </code>
    */
    public static Workbook getHSSFWorkbook(List<Map<String, String>> list, String type){
        Workbook workbook= new HSSFWorkbook();
        if("2003".equals(type)){
            workbook = new HSSFWorkbook();//创建工作薄
        }else{
            workbook = new XSSFWorkbook();//2007
        }
        Sheet sheet = workbook.createSheet("接入实时监测");//名称
        setWidth(sheet);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);//水平
        cellStyle.setWrapText(true);//单元格显示不下时自动换行
        Font font = workbook.createFont();
        font.setFontName("Arial Unicode MS");
        font.setFontHeightInPoints((short) 11);//设置字体大小
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        cellStyle.setFont(font);//选择需要用到的字体格式

        CellStyle cellStyleIn = workbook.createCellStyle();
        cellStyleIn.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直
        cellStyleIn.setAlignment(CellStyle.ALIGN_CENTER);//水平
        cellStyleIn.setWrapText(true);//单元格显示不下时自动换行
        Font font2 = workbook.createFont();
        font2.setFontName("Arial Unicode MS");
        font2.setFontHeightInPoints((short) 10);//设置字体大小
        cellStyleIn.setFont(font2);//选择需要用到的字体格式

        Row rowtitle = sheet.createRow(0);
        rowtitle.setRowStyle(cellStyle);
        setValueAndStyle(rowtitle,cellStyle,0,"卡口编号");
        setValueAndStyle(rowtitle,cellStyle,1,"卡口名称");
        setValueAndStyle(rowtitle,cellStyle,2,"卡口类型");
        setValueAndStyle(rowtitle,cellStyle,3,"经度");
        setValueAndStyle(rowtitle,cellStyle,4,"纬度");
        setValueAndStyle(rowtitle,cellStyle,5,"卡口最新经过时间");
        setValueAndStyle(rowtitle,cellStyle,6,"卡口最新人脸时间");
        setValueAndStyle(rowtitle,cellStyle,7,"卡口最新违法");
        setValueAndStyle(rowtitle,cellStyle,8,"车道方向");
        setValueAndStyle(rowtitle,cellStyle,9,"车道编号");
        setValueAndStyle(rowtitle,cellStyle,10,"车道最新经过时间");
        setValueAndStyle(rowtitle,cellStyle,11,"车道最新违法时间");
        setValueAndStyle(rowtitle,cellStyle,12,"车道过车延时");
        int j=0;
        boolean bing=true;
        //设置行与合并单元格
        for(int i = 0;i<list.size(); i++)
        {
            Map<String, String> detailmap = list.get(i);
            if(detailmap.size()==0){
                Row row = sheet.createRow(j+1);
                /*setValueAndStyle(row,cellStyleIn,0,list.get(i).getJkdbh());
                setValueAndStyle(row,cellStyleIn,1,list.get(i).getJkdmc());
                setValueAndStyle(row,cellStyleIn,2,getLx(list.get(i).getJkdlx()));
                setValueAndStyle(row,cellStyleIn,3,list.get(i).getX());
                setValueAndStyle(row,cellStyleIn,4,list.get(i).getY());
                setValueAndStyle(row,cellStyleIn,5,list.get(i).getPassTime());
                setValueAndStyle(row,cellStyleIn,6,list.get(i).getFacePassTime());
                setValueAndStyle(row,cellStyleIn,7,list.get(i).getReportTime());*/
                j++;
                continue;
            }
            for (Map.Entry<String, ?> entry : detailmap.entrySet()) {
                if(bing){
                    //合并单元格
                    CellRangeAddress region0 = new CellRangeAddress(j+1, j+detailmap.size(), 0, 0);
                    CellRangeAddress region1 = new CellRangeAddress(j+1, j+detailmap.size(), 1, 1);
                    CellRangeAddress region2 = new CellRangeAddress(j+1, j+detailmap.size(), 2, 2);
                    CellRangeAddress region3 = new CellRangeAddress(j+1, j+detailmap.size(), 3, 3);
                    CellRangeAddress region4 = new CellRangeAddress(j+1, j+detailmap.size(), 4, 4);
                    CellRangeAddress region5 = new CellRangeAddress(j+1, j+detailmap.size(), 5, 5);
                    CellRangeAddress region6 = new CellRangeAddress(j+1, j+detailmap.size(), 6, 6);
                    CellRangeAddress region7 = new CellRangeAddress(j+1, j+detailmap.size(), 7, 7);
                    sheet.addMergedRegion(region0);
                    sheet.addMergedRegion(region1);
                    sheet.addMergedRegion(region2);
                    sheet.addMergedRegion(region3);
                    sheet.addMergedRegion(region4);
                    sheet.addMergedRegion(region5);
                    sheet.addMergedRegion(region6);
                    sheet.addMergedRegion(region7);
                }
                bing=false;
                Row row = sheet.createRow(j+1);
                /*setValueAndStyle(row,cellStyleIn,0,list.get(i).getJkdbh());
                setValueAndStyle(row,cellStyleIn,1,list.get(i).getJkdmc());
                setValueAndStyle(row,cellStyleIn,2,getLx(list.get(i).getJkdlx()));
                setValueAndStyle(row,cellStyleIn,3,list.get(i).getX());
                setValueAndStyle(row,cellStyleIn,4,list.get(i).getY());
                setValueAndStyle(row,cellStyleIn,5,dataFormat(list.get(i).getPassTime()));
                setValueAndStyle(row,cellStyleIn,6,dataFormat(list.get(i).getFacePassTime()));
                setValueAndStyle(row,cellStyleIn,7,dataFormat(list.get(i).getReportTime()));
                setValueAndStyle(row,cellStyleIn,8,entry.getValue().getCdfx());
                setValueAndStyle(row,cellStyleIn,9,entry.getValue().getCdbh());
                setValueAndStyle(row,cellStyleIn,10,dataFormat(entry.getValue().getPassTime()));
                setValueAndStyle(row,cellStyleIn,11,dataFormat(entry.getValue().getWfTime()));
                setValueAndStyle(row,cellStyleIn,12,entry.getValue().getDelay()+"");*/
                j++;
            }
            bing=true;

        }
        /**第一个参数表示要冻结的列数；
         第二个参数表示要冻结的行数；
         第三个参数表示右边区域可见的首列序号，从1开始计算；
         第四个参数表示下边区域可见的首行序号，也是从1开始计算；*/
        sheet.createFreezePane( 0, 1, 0, 1);
        return workbook;
    }

    public static void setValueAndStyle(Row rowtitle, CellStyle cellStyle, int i, String value){
        Cell cell = rowtitle.createCell(i);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);
    }
    public static String getLx(int i){
        Map<Integer,String> map = new HashMap<Integer,String>(){{
            put(78,"WIFI探针卡口");
            put(79,"人脸卡口");
            put(80,"治安卡口");
            put(81,"交通卡口");
            put(82,"其他");
            put(1,"电警");
            put(2,"卡口");
            put(3,"卡警");
        }};
        return map.get(i);
    }
    public static void setWidth(Sheet sheet){
        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(1, 9000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 5000);
        sheet.setColumnWidth(7, 5000);
        sheet.setColumnWidth(8, 3000);
        sheet.setColumnWidth(9, 3000);
        sheet.setColumnWidth(10, 5000);
        sheet.setColumnWidth(11, 5000);
        sheet.setColumnWidth(12, 5000);
    }

    public static String dataFormat(String passTime){
        if(StringUtils.isNotEmpty(passTime)&&passTime.length()>=14){
            return passTime.substring(0,4)+"/"+passTime.substring(4,6)+"/"+passTime.substring(6,8)+" "+
                    passTime.substring(8,10)+":"+passTime.substring(10,12)+":"+passTime.substring(12,14);
        }
        return passTime;
    }
}



