package com.boot.util.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.boot.util.common.CollectionUtils;
import com.boot.util.common.StringUtils;
import com.sun.deploy.net.URLEncoder;
import lombok.extern.log4j.Log4j2;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于alibaba封装的easyexcel
 * https://developer.aliyun.com/article/1266482
 * <p>
 * 调用1
 * <p>
 * //                EasyExcel.read(file.getInputStream(), LdddVehicleExcelBean.class, new AnalysisEventListener<LdddVehicleExcelBean>() {
 * //                    @Override
 * //                    public void invoke(LdddVehicleExcelBean data, AnalysisContext context) {
 * //                        try {
 * //                            TimeUnit.SECONDS.sleep(20);
 * //                        }catch (Exception ignore){}
 * //                        list.add(data);
 * //                    }
 * //                    @Override
 * //                    public void doAfterAllAnalysed(AnalysisContext context) {
 * //                        log.info("解析完成，解析到的数据量为：" + list.size());
 * //                    }
 * //                }).sheet().doRead();
 * <p>
 * 调用2
 * List<LdddVehicleExcelBean> list = EasyExcel.read(file.getInputStream()).head(LdddVehicleExcelBean.class).sheet().doReadSync();
 * <p>
 * 下载
 * response.setCharacterEncoding("utf-8");
 * response.setContentType("application/vnd.ms-excel");
 * String fileName = URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
 * response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
 * response.setStatus(HttpServletResponse.SC_OK);
 *
 * @author yuez
 * @see <a href="https://easyexcel.opensource.alibaba.com/docs/current/api/">参考文档API</a>
 * @since 2022/7/5
 */
public class EasyExcelUtil {


    /**
     * 写出一个 excel 文件到本地
     * <br />
     * 将类型所有加了 @ExcelProperty 注解的属性全部写出
     *
     * @param fileName  文件名 不要后缀
     * @param sheetName sheet名
     * @param data      写出的数据
     * @param clazz     要写出数据类的Class类型对象
     * @param <T>       写出的数据类型
     */
    public static <T> void writeExcel(String fileName, String sheetName, List<T> data, Class<T> clazz) {
        writeExcel(null, fileName, sheetName, data, clazz);
    }


    /**
     * 按照指定的属性名进行写出 一个 excel
     *
     * @param attrName  指定的属性名 必须与数据类型的属性名一致
     * @param fileName  文件名 不要后缀
     * @param sheetName sheet名
     * @param data      要写出的数据
     * @param clazz     要写出数据类的Class类型对象
     * @param <T>       要写出的数据类型
     */
    public static <T> void writeExcel(Set<String> attrName, String fileName, String sheetName, List<T> data, Class<T> clazz) {
        fileName = StringUtils.isEmpty(fileName) ? "基础表格" : fileName;
        sheetName = StringUtils.isEmpty(sheetName) ? "sheet0" : sheetName;

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            write(fos, attrName, sheetName, data, clazz);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 读取 指定格式的 excel文档
     *
     * @param fileName 文件名
     * @param clazz    数据类型的class对象
     * @param <T>      数据类型
     * @return
     */
    public static <T> List<T> readExcel(String fileName, Class<T> clazz) {
        return readExcel(fileName, clazz, null);
    }

    /**
     * 取 指定格式的 excel文档
     * 注意一旦传入自定义监听器，则返回的list为空，数据需要在自定义监听器里面获取
     *
     * @param fileName     文件名
     * @param clazz        数据类型的class对象
     * @param readListener 自定义监听器
     * @param <T>          数据类型
     * @return
     */
    public static <T> List<T> readExcel(String fileName, Class<T> clazz, ReadListener<T> readListener) {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            return read(fis, clazz, readListener);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }


    /**
     * 导出  一个 excel
     * 导出excel所有数据
     *
     * @param response
     * @param fileName  件名 最好为英文，不要后缀名
     * @param sheetName sheet名
     * @param data      要写出的数据
     * @param clazz     要写出数据类的Class类型对象
     * @param <T>       要写出的数据类型
     */
    public static <T> void export(HttpServletResponse response, String fileName, String sheetName, List<T> data, Class<T> clazz) {
        export(response, null, fileName, sheetName, data, clazz);
    }

    /**
     * 按照指定的属性名进行写出 一个 excel
     *
     * @param response
     * @param attrName  指定的属性名 必须与数据类型的属性名一致
     * @param fileName  文件名 最好为英文，不要后缀名，中文进行URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
     * @param sheetName sheet名
     * @param data      要写出的数据
     * @param clazz     要写出数据类的Class类型对象
     * @param <T>       要写出的数据类型
     */
    public static <T> void export(HttpServletResponse response, Set<String> attrName, String fileName, String sheetName, List<T> data, Class<T> clazz) {

        fileName = StringUtils.isEmpty(fileName) ? "base-system-manager" : fileName;
        sheetName = StringUtils.isEmpty(sheetName) ? "sheet0" : sheetName;

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-disposition", "attachment;filename=" + fileName + ExcelTypeEnum.XLSX.getValue());

        try (OutputStream os = response.getOutputStream()) {
            write(os, attrName, sheetName, data, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 接收一个excel文件，并且进行解析
     * 注意一旦传入自定义监听器，则返回的list为空，数据需要在自定义监听器里面获取
     *
     * @param multipartFile excel文件
     * @param clazz         数据类型的class对象
     * @param readListener  监听器
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile multipartFile, Class<T> clazz, ReadListener<T> readListener) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            return read(inputStream, clazz, readListener);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static <T> void write(OutputStream os, Set<String> attrName, String sheetName, List<T> data, Class<T> clazz) {
        ExcelWriterBuilder write = EasyExcel.write(os, clazz);
        // 如果没有指定要写出那些属性数据，则写出全部
        if (!CollectionUtils.isEmpty(attrName)) {
            write.includeColumnFiledNames(attrName);
        }
        write.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet(sheetName).doWrite(data);
    }


    private static <T> List<T> read(InputStream in, Class<T> clazz, ReadListener<T> readListener) {
        List<T> list = new ArrayList<>();

        Optional<ReadListener> optional = Optional.ofNullable(readListener);

        EasyExcel.read(in, clazz, optional.orElse(new AnalysisEventListener<T>() {

            @Override
            public void invoke(T data, AnalysisContext context) {
                list.add(data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                System.out.println("解析完成");
            }
        })).sheet().doRead();

        return list;
    }

    /************************ <a href="https://juejin.cn/post/7328242736027762739?utm_source=gold_browser_extension">模板2</a>**************************/


    /**
     * 导入excel
     *
     * @param clazz
     * @param file
     * @param <T>
     * @throws IOException
     */
    public <T> List<T> read(Class<T> clazz, MultipartFile file) throws IOException {
        return EasyExcel.read(file.getInputStream()).head(clazz).sheet().doReadSync();
    }

    /**
     * 读取 Excel(多个 sheet)
     *
     * @param excel    文件
     * @param rowModel 实体类映射
     * @return Excel 数据 list
     */
    public static <T> List<T> readExcelData(MultipartFile excel, Class<T> rowModel, Integer headRowNumber) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReaderBuilder readerBuilder = getReader(excel, excelListener);
        if (readerBuilder == null) {
            return null;
        }
        if (headRowNumber == null) {
            headRowNumber = 1;
        }
        readerBuilder.head(rowModel).headRowNumber(headRowNumber).doReadAll();
        return excelListener.getData();
    }

    /**
     * 读取某个 sheet 的 Excel
     *
     * @param excel       文件
     * @param rowModel    实体类映射
     * @param sheetNo     sheet 的序号 从1开始
     * @param headLineNum 表头行数，默认为1
     * @return Excel 数据 list
     */
    public static <T> List<T> excelImport(MultipartFile excel, Class rowModel, int sheetNo,
                                          Integer headLineNum) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReaderBuilder readerBuilder = getReader(excel, excelListener);
        if (readerBuilder == null) {
            return null;
        }
        ExcelReader reader = readerBuilder.headRowNumber(headLineNum).build();
        ReadSheet readSheet = EasyExcel.readSheet(sheetNo).head(rowModel).build();
        reader.read(readSheet);
        return excelListener.getData();
    }


    /**
     * 返回 ExcelReader
     *
     * @param excel         需要解析的 Excel 文件
     * @param excelListener 监听器
     */
    private static ExcelReaderBuilder getReader(MultipartFile excel, ExcelListener excelListener)  {
        String filename = excel.getOriginalFilename();
        if (filename == null || (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx"))) {
           return null;
        }
        InputStream inputStream;
        try {
            inputStream = new BufferedInputStream(excel.getInputStream());
            return EasyExcel.read(inputStream, excelListener);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public <T, R> void importExcel(MultipartFile file, Class<T> tClass, Integer headRowNumber, Function<T, R> map, Consumer<List<R>> consumer) {
        List<T> excelData = readExcelData(file, tClass, headRowNumber);
        List<R> result = excelData.stream().map(map).collect(Collectors.toList());
        consumer.accept(result);
    }

    public <T> void importExcel(MultipartFile file, Class<T> tClass, Integer headRowNumber, Consumer<List<T>> consumer) {
        List<T> excelData = readExcelData(file, tClass, headRowNumber);
        consumer.accept(excelData);
    }


    /**
     * 导出excel
     *
     * @param data
     * @param response
     * @param clazz
     * @param <T>
     */
    public <T> void exportExcel(List<T> data, Class<T> clazz, HttpServletResponse response) {
        try {
            this.setExcelResponseProp(response, "用户列表");
            EasyExcel.write(response.getOutputStream())
                    .head(clazz)
                    .excelType(ExcelTypeEnum.XLSX)
                    .registerConverter(new LocalDateTimeConverter())
                    .sheet("工作表1")
                    .doWrite(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T, R> void exportExcel(List<T> list, Function<T, R> map, Class<R> tClass, HttpServletResponse response) throws IOException {
        setExcelResponseProp(response, "用户列表");
        List<R> result = list.stream().map(map).collect(Collectors.toList());
        exportExcel(result, tClass, response);
    }

    public <T> void excelExport(HttpServletResponse response, String fileName, String sheetName,
                                Class<T> head, List<T> data, Set<String> excludeColumnFiledNames) throws IOException {
        setExcelResponseProp(response, fileName);
        EasyExcel.write(response.getOutputStream(), head)
//                .autoCloseStream(Boolean.FALSE)
                .excludeColumnFiledNames(excludeColumnFiledNames).sheet(sheetName)
                .doWrite(data);
    }

    /**
     * <a href="https://blog.csdn.net/qq_43750656/article/details/111379498">模板的数据excel填充</a>
     * 使用模板的数据excel填充
     * 表头 。。。
     * 列 {param}占位使用，如果{}不需要转义，则需要用 \ 转义。
     * 多组数据填充,表示list {.param}   FillConfig build = FillConfig.builder().forceNewRow(true).build(); workBook.fill(listdata,build,sheet); workBook.fill(singledata,build,sheet);
     * 水平填充 FillConfig build = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
     *
     * @param list
     * @param tClass
     * @param template
     * @param response
     * @param <T>
     * @throws IOException
     */
    public <T> void exportExcel(List<T> list, Class<T> tClass, String template, HttpServletResponse response) throws IOException {
        setExcelResponseProp(response, "用户列表");
        EasyExcel.write(response.getOutputStream())
                .withTemplate(template) //文件路劲
                .excelType(ExcelTypeEnum.XLSX)
                .useDefaultStyle(false)
                .registerConverter(new LocalDateTimeConverter())
                .sheet(0)
                .doFill(list);

    }


    /**
     * 设置响应结果
     *
     * @param response    响应结果对象
     * @param rawFileName 文件名
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    private void setExcelResponseProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }
}

@Log4j2
class ExcelListener<T> extends AnalysisEventListener<T> {
    public ExcelListener() {
    }

    /**
     * 每隔1000条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 1000;
    List<T> list = new ArrayList<>();

    @Override
    public void invoke(T data, AnalysisContext context) {
        list.add(data);
        log.info("解析到一条数据:{}", data.toString());

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("所有数据解析完成！");
    }


    /**
     * 返回list
     */
    public List<T> getData() {
        return this.list;
    }

}


/**
 * 解决 EasyExcel 日期类型 LocalDateTime 转换的问题
 * 读取
 * @ExcelProperty(value = "创建时间",converter = LocalDateTimeConverter.class)
 * private Date bornDate;
 *
 * 导出
 * .registerConverter(new LocalDateTimeConverter())
 */
class LocalDateTimeConverter implements Converter<LocalDateTime> {

    @Override
    public Class<LocalDateTime> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration) {
        return LocalDateTime.parse(cellData.getStringValue(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public WriteCellData<String> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        return new WriteCellData(value.toString("yyyy-MM-dd HH:mm:ss"));
    }

}


