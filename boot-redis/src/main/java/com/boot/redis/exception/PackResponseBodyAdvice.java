/*
package com.boot.redis.exception;

@ControllerAdvice
public class PackResponseBodyAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    Class<?> clazz = returnType.getDeclaringClass() ;
    // 在以下三种情况下不进行包装处理
    return
        // 1.类上有@NoWrapper注解
        !clazz.isAnnotationPresent(NoWrapper.class)
        // 2.方法上有@NoWrapper注解
        && !returnType.hasMethodAnnotation(NoWrapper.class)
        // 3.返回值已经是R类型
        && !R.class.isAssignableFrom(returnType.getParameterType());
  }
  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
      ServerHttpResponse response) {
    if (body instanceof String str) {
      // 最终保证返回的数据结构还是一致的
      return this.objectMapper.writeValueAsString(R.success(str)) ;
    }
    return R.success(body) ;
  }
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface NoWrapper {
}

public class R<T> {
  */
/**0-成功, 1-失败*//*

  private Integer code = 0 ;
  private String msg = "成功" ;
  private T data ;

  public static <T> R<T> success(T data) {
    R<T> r = new R<T>() ;
    r.setData(data) ;
    return r ;
  }
  public static <T> R<T> success(String msg) {
    R<T> r = new R<T>() ;
    r.setMsg(msg) ;
    return r ;
  }
  // getters, setters
}
*/
