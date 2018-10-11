package com.belonk.webapp.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by sun on 2018/10/9.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Documented
// 定义该约束的校验器
@Constraint(validatedBy = PassengerCountValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassengerCount {
    /**
     * 校验失败的消息模板，可以通过该属性覆盖模板来自定义消息
     *
     * @return 消息模板
     */
    String message() default "{com.belonk.car.passengerCount}";

    /**
     * 校验组。
     *
     * @return 组class
     */
    Class<?>[] groups() default {};

    /**
     * 定义约束条件的严重级别
     *
     * @return 级别class
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 核载人数
     *
     * @return 人数
     */
    int value();
}
