package com.jiajia.baselibrary.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Numen_fan on 2022/3/13
 * Desc: 方法上的网络check
 */


@Target(ElementType.METHOD) // 作用在方法之上
@Retention(RetentionPolicy.RUNTIME) // 运行时生效
public @interface CheckNet {


}
