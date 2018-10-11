package com.belonk.webapp.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

/**
 * 多个约束的组合。
 * <p>
 * Created by sun on 2018/10/11.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NotEmpty
@Size(min = 5, max = 16)
@Pattern(regexp = "^GL.*$")
// 不需要单独的验证器
@Constraint(validatedBy = {})
// 添加了这个注解后，表示：不会验证所有约束，当有一个约束验证失败，则会立即返回组合约束所定义的错误信息，而不是单个约束本身的错误信息
// 有约束校验失败立即返回，信息为组合约束定义的信息
@ReportAsSingleViolation
public @interface ValidNumber {
    String message() default "{com.belonk.car.no}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
