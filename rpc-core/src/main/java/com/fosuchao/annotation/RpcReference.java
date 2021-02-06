package com.fosuchao.annotation;

import java.lang.annotation.*;

/**
 * Created by Chao Ye on 2021/2/6
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcReference {

    /**
     * 服务版本，默认为空串
     */
    String version() default "";

    /**
     * 服务的组，默认为空串
     */
    String group() default "";
}
