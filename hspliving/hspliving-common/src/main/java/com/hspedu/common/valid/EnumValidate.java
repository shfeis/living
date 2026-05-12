package com.hspedu.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 自定义数据校验注解
 * @Target 指定该注解可以应用在哪些程序元素上
 * @Retention 指定该注解的保留策略为运行时保留
 * @Documented 指定该注解应该被 javadoc 等文档工具记录
 * @Constraint 指定该注解与哪个自定义数据校验器关联
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EnumConstraintValidator.class})
public @interface EnumValidate {
    /*
     校验错误时的默认提示信息,url就是该属性的url,提示信息存放在resources目录下的ValidationMessages.properties，
     如果javaBean在使用自定义数据校验注解时指定了message提示信息，就会覆盖properties配置文件中的默认提示信息
    */
    String message() default "{com.hspedu.common.valid.EnumValidate.message}";
    //对哪些组有效
    Class<?>[] groups() default {};
    //接收用户传入的数据
    int[] value() default {};
    //默认保留的注解参数
    Class<? extends Payload>[] payload() default {};
}
