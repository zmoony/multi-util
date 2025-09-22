package com.example.text.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.beust.jcommander.internal.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * easyExcel的初步学习
 * <UL>
 * <li>EasyExcel 是阿里巴巴开源的一个 excel 处理框架，以使用简单、节省内存著称。
 * EasyExcel 能大大减少占用内存的主要原因是在解析 Excel 时没有将文件数据一次性全部加载到内存中，而是从磁盘上一行行读取数据，逐个解析。</li>
 * <li>EasyExcel 采用一行一行的解析模式，并将一行的解析结果以观察者的模式通知处理（AnalysisEventListener）</li>
 * </UL>
 * 常用注解
 * <ol>
 *   <li>@ExcelProperty：指定当前字段对应 excel 中的那一列。可以根据名字或者Index去匹配。当然也可以不写，默认第一个字段就是index=0，以此类推。千万注意，要么全部不写，要么全部用index，要么全部用名字去匹配。千万别三个混着用，除非你非常了解源代码中三个混着用怎么去排序的。</li>
 *
 *   <li>@ExcelIgnore：默认所有字段都会和excel去匹配，加了这个注解会忽略该字段</li>
 *
 *   <li> @DateTimeFormat：日期转换，用 String 去接收 excel 日期格式的数据会调用这个注解。里面的 value 参照 java.text.SimpleDateFormat</li>
 *
 *   <li>@NumberFormat：数字转换，用 String 去接收 excel 数字格式的数据会调用这个注解。里面的 value 参照 java.text.DecimalFormat</li>
 *
 *   <li>@ExcelIgnoreUnannotated：默认不加 ExcelProperty 的注解的都会参与读写,加了不会参与</li>
 * </ol>
 *
 * @author yuez
 * @since 2022/7/5
 */

class First {
    public static void main(String[] args) {
        read();
    }

    /**
     * {@link #UserVo}
     */
    @Test
    private static void write() {
        List<UserVo> userVos = Lists.newArrayList(24);
        for (int i = 0; i < 11; i++) {
            UserVo userVo = new UserVo(UUID.randomUUID().toString(),
                    (i % 2 == 0 ? "张" : "王") + "某某",
                    (i % 2 == 0 ? "男" : "女"),
                    "南京市第" + i + "街区",
                    "025-" + String.valueOf(i * 31 * 31));
            userVos.add(userVo);
        }
        //部分输出
        Set<String> set = Sets.newHashSet();
        set.add("id");
        set.add("name");
        EasyExcel.write(new File("C:\\Users\\yuez\\Desktop\\用户.xlsx"), UserVo.class)
                //指定列输出
                .includeColumnFieldNames(set)
                //自适应表头
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("20220705")
                .doWrite(userVos);
    }
    @Test
    private static void read(){
        //类必须是public修饰
        List<UserVo> userVos = Lists.newArrayList(24);
        String fileName = "C:\\Users\\yuez\\Desktop\\用户.xlsx";
        EasyExcel.read(fileName, UserVo.class, new AnalysisEventListener<UserVo>() {
            //每读取一行触发
            @Override
            public void invoke(UserVo userVo, AnalysisContext analysisContext) {
                userVos.add(userVo);
            }
            //全部读完触发
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                System.out.println("全部读完");
            }
        }).sheet().doRead();
        System.out.println(userVos);
    }
}

@HeadRowHeight(30) //标题高度
@ContentRowHeight(20) //内容高度
@ColumnWidth(16) //行宽
@ExcelIgnoreUnannotated
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserVo {
    @ExcelProperty(value = {"用户信息","编号"},order = 1)
    @ColumnWidth(40)
    private String id;
    @ExcelProperty(value = {"用户信息","名称"},order = 2)
    private String name;
    @ExcelProperty(value = {"用户信息","性别"},order = 4)
    private String sex;
    @ExcelProperty(value = {"用户信息","地址"},order = 5)
    @ColumnWidth(25)
    private String address;
    @ExcelProperty(value = {"用户信息","电话"},order = 4)
    @ColumnWidth(25)
    private String phone;
    /*@ExcelProperty("生日")
    @DateTimeFormat("yyyyMMdd")
    private Date birthday;*/
}
