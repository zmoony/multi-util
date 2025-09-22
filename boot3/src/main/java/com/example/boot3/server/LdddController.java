package com.example.boot3.server;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import feign.Param;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * LdddController
 *
 * @author yuez
 * @since 2024/1/30
 */
@RestController
@CrossOrigin
public class LdddController {

    @RequestMapping("/lddd/vehicle/lddd/query/vehicle")
    public ResponseCommonBean queryVehicle(String picUrl) {
        ResponseCommonBean responseCommonBean = null;
        try {
            responseCommonBean = new ResponseCommonBean();
            List<LdddVehicleResultExcelBean> ldddVehicleResultExcelBeans = new ArrayList<>();
            ldddVehicleResultExcelBeans.addAll(Arrays.asList(new LdddVehicleResultExcelBean("西雅特 LEON 2012(CUPRA)", "123456", "123456", "西雅特 LEON 2012(CUPRA)", "123456", "123456","")));
            responseCommonBean.setFlag(true);
            responseCommonBean.setStdOuts(ldddVehicleResultExcelBeans);
        } catch (Exception e) {
            e.printStackTrace();
            responseCommonBean.setDescribe("流动赌档运人车辆判别请求异常");
            responseCommonBean.setFlag(false);
        }
        return responseCommonBean;
    }

    @PostMapping({"/lddd/vehicle/lddd/upload","/lddd/person/lddd/upload"})
    public void upload(@Param("file") MultipartFile file, HttpServletResponse response) throws IOException {
        System.out.println(file.getOriginalFilename());
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel");
        String fileName = URLEncoder.encode("xxxxxxxx", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        response.setStatus(HttpServletResponse.SC_OK);
        List<LdddVehicleResultExcelBean> ldddVehicleResultExcelBeans = new ArrayList<>();
        ldddVehicleResultExcelBeans.addAll(Arrays.asList(new LdddVehicleResultExcelBean("123456", "123456", "123456", "123456", "123456", "123456","")));
        EasyExcel.write(response.getOutputStream(), LdddVehicleResultExcelBean.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("人员分析结果").doWrite(ldddVehicleResultExcelBeans);
    }

}

@Data
class ResponseCommonBean<T> {
    private boolean flag;
    private String describe;
    private List<T> stdOuts;
    private Map<String, Object> content;
    private long total;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY, getterVisibility= JsonAutoDetect.Visibility.NONE)
class LdddVehicleResultExcelBean {
    @ExcelProperty(value = "车牌号", index = 0)
    private String motorVehicleLicensePlate;
    @ExcelProperty(value = "车牌颜色", index = 1)
    private String motorVehiclePlateColor;
    @ExcelProperty(value = "车辆颜色", index = 2)
    private String motorVehicleColor;
    @ExcelProperty(value = "车辆品牌", index = 3)
    private String motorVehicleBrandName;
    @ExcelProperty(value = "车辆子品牌", index = 4)
    private String motorVehicleSubbrand;
    @ExcelProperty(value = "车辆年款", index = 5)
    private String motorVehicleYearBrand;
    @ExcelProperty(value = "车辆类型", index = 6)
    private String motorVehicleCategory;

}
