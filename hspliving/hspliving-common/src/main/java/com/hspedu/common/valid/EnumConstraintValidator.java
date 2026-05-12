package com.hspedu.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义数据校验器，自定义数据校验注解会调用该类，是真正实现业务需求的地方
 * <EnumValidate, Integer> 左边：自定义数据校验注解， 右边：自定义数据校验注解对哪种数据类型有效
 */
public class EnumConstraintValidator implements ConstraintValidator<EnumValidate, Integer> {
    //定义一个set集合，收集自定义数据校验注解指定的value值
    private Set<Integer> set = new HashSet<>();

    @Override
    public void initialize(EnumValidate constraintAnnotation) {
        int[] values = constraintAnnotation.value();
        for (int value : values) {
            set.add(value);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(value); //将用户输入的值与set集合中存放的值进行比较，如果存在相同元素就返回true,通过数据校验
    }
}