package com.example.text.demoOnLine.参数校验;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * 传递校验
 *
 * @author yuez
 * @since 2023/2/21
 */
@Data
public class ClassInfo {
  @NotBlank(message = "班主任姓名不能为空")
  private String teacher;
  @NotNull(message = "教师不能为空")
  private Integer classroom;
  @NotNull(message = "年级不能为空")
  @Min(value = 1, message = "年级只能是 1-6")
  @Max(value = 6, message = "年级只能是 1-6")
  private Integer grade;
}

