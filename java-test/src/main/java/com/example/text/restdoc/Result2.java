package com.example.text.restdoc;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "返回类",name = "Result")
@Data
public class Result2 {
    @Schema(name = "code",title = "状态码")
    int code;
    String msg;

    public Result2(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}