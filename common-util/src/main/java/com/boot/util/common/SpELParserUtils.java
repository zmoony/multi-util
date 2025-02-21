package com.boot.util.common;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * LogRecordParser
 * AOP+SpEL
 *
 * @author yuez
 * @since 2024/1/11
 */
@Log4j2
@NoArgsConstructor
public class SpELParserUtils {

    private static final String EXPRESSION_PREFIX = "#{";
    private static final String EXPRESSION_SUFFIX = "}";
    /**
     * 表达式解析器
     */
    private static ExpressionParser parser = new SpelExpressionParser();

    /**
     * 参数名称发现器
     */
    private static DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 一次只运行一次，下面直接获取
     * @param method 方法
     * @param args 参数
     * @return 上下下文
     */
    public static EvaluationContext initContext(Method method,Object[] args){
        String[] params = parameterNameDiscoverer.getParameterNames(method);
        StandardEvaluationContext context = new StandardEvaluationContext();
        //设置上下文变量
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        return context;
    }

    /**
     * 解析表达式
     * @param method 方法
     * @param args 参数
     * @param spelExpression 表达式
     * @param clazz 返回结果的类型
     * @param defaultResult 默认结果
     * @return 执行spel表达式后的结果
     */
    public static <T> T parse(EvaluationContext context,String spelExpression,Class<T> clazz,T defaultResult){
        T result = getResult(context,spelExpression,clazz);
        return Optional.ofNullable(result).orElse(defaultResult);
    }

    /**
     * 解析spel表达式
     *
     * @param method  方法
     * @param args 参数值
     * @param spelExpression  表达式
     * @param clz  返回结果的类型
     * @return 执行spel表达式后的结果
     */
    public static <T> T parse(EvaluationContext context ,String spelExpression, Class<T> clz) {
        return getResult(context,spelExpression,clz);
    }


    /**
     * 解析spel表达式
     *
     * @param param  参数名
     * @param paramValue 参数值
     * @param spelExpression  表达式
     * @param clz  返回结果的类型
     * @return 执行spel表达式后的结果
     */
    public static <T> T parse(String param, Object paramValue, String spelExpression, Class<T> clz) {
        EvaluationContext context = new StandardEvaluationContext();
        //设置上下文变量
        context.setVariable(param, paramValue);
        return getResult(context,spelExpression,clz);
    }

    /**
     * 解析spel表达式
     *
     * @param param 参数名
     * @param paramValue 参数值
     * @param spelExpression  表达式
     * @param clz  返回结果的类型
     * @param defaultResult 默认结果
     * @return 执行spel表达式后的结果
     */
    public static <T> T parse(String param, Object paramValue,String spelExpression, Class<T> clz, T defaultResult) {
        EvaluationContext context = new StandardEvaluationContext();
        //设置上下文变量
        context.setVariable(param, paramValue);
        T result = getResult(context,spelExpression,clz);
        if(Objects.isNull(result)){
            return defaultResult;
        }
        return result;

    }

    /**
     * 获取spel表达式后的结果
     *
     * @param context 解析器上下文接口
     * @param spelExpression  表达式
     * @param clz  返回结果的类型
     * @return 执行spel表达式后的结果
     */
    private static <T> T getResult(EvaluationContext  context, String spelExpression, Class<T> clazz) {
        try {
            Expression expression =parseExpression(spelExpression);
            return expression.getValue(context, clazz);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 解析表达式
     * @param spelExpression spel表达式
     * @return
     */
    private static Expression  parseExpression(String spelExpression) {
        // 如果表达式是一个#{}表达式，需要为解析传入模板解析器上下文
        if (spelExpression.startsWith(EXPRESSION_PREFIX) && spelExpression.endsWith(EXPRESSION_SUFFIX)) {
            return parser.parseExpression(spelExpression, new TemplateParserContext());
        } else {
            return parser.parseExpression(spelExpression);
        }
    }


    public static void main(String[] args) {
        ExpressionParser parser = new SpelExpressionParser();
//        Expression expression = parser.parseExpression("('Hello' + ' World').concat(#end)");
        Expression expression = parser.parseExpression("'success'.concat(#end)");
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("end", "!");
        System.out.println(expression.getValue(context));
    }

}
