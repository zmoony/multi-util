package com.example.text.demoOnLine.参数校验;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;

/**
 * 需要校验的实体类
 *
 * @author yuez
 * @since 2023/2/21
 */
@Data
public class Student {
    @NotBlank(message = "主键不能为空",groups = {Student.Update.class})
    private String id;
    @NotBlank(message = "名字不能为空")
    @Size(min=2, max = 4, message = "名字字符长度必须为 2~4个")
    private String name;
    @Pattern(regexp = "^1(3\\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "手机号格式错误")
    private String phone;
    @Email(message = "邮箱格式错误")
    private String email;
    @Past(message = "生日必须早于当前时间")
    private Date birth;
    @Min(value = 0, message = "年龄必须为 0~100")
    @Max(value = 100, message = "年龄必须为 0~100")
    private Integer age;
    @PositiveOrZero
    private Double score;
    @Valid
    private ClassInfo classInfo;

    //分组校验，不同接口不同校验
    public interface Update{}
    public interface Create{}

}
