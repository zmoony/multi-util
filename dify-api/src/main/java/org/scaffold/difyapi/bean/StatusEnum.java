package org.scaffold.difyapi.bean;

/**
 * 状态枚举
 * 不建议与http状态码合并
 * 成功code统一为0，否则就是具体的错误码，至少用4到5位数字
 */
@SuppressWarnings({"all"})
public enum StatusEnum {
    SUCCESS(0, "请求处理成功"),
    UNAUTHORIZED(56401, "用户认证失败"),
    FORBIDDEN(56403, "权限不足"),
    SERVICE_ERROR(56500, "服务器去旅行了，请稍后重试"),
    PARAM_INVALID(561000, "无效的参数"),
    HTTP_METHOD_NOT_SUPPORT(56800,"请求方式不支持" );
    public final Integer code;

    public final String message;

    StatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
