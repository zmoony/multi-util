package com.example.text.java.function;

/**
 * VUtils
 *
 * @author yuez
 * @since 2023/4/12
 */
public class VUtils {

    /**
     *
     * @param b true 抛出异常
     * @return
     */
    public static ThrowExceptionFunction isTrue(boolean b){
        return (errormessage)->{
            if(b){
                throw new RuntimeException(errormessage);
            }
        };
    }

    public static BranchHandle isTrueOrFalse(boolean b){
        return ((trueHandle, falseHandle) -> {
            if(b){
                trueHandle.run();
            }else{
                falseHandle.run();
            }
        });
    }


    public static com.example.text.java.function.PresentOrElseHandle<?> isBlankOrNotBlank(String str){
        return ((consumer,runable)->{
            if(str == null || str.length() == 0){
                runable.run();
            }else{
                consumer.accept(str);
            }
        });
    }
}
