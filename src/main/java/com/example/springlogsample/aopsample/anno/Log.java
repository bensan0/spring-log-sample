package com.example.springlogsample.aopsample.anno;
import com.example.springlogsample.aopsample.MethodType;

import java.lang.annotation.*;



@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 描述
     */
    String description() default "";

    /**
     * 方法型別 INSERT DELETE UPDATE OTHER
     */
    MethodType methodType() default MethodType.OTHER;
}
