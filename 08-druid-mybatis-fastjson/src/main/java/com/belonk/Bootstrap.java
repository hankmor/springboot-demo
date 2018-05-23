package com.belonk;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"config-spring-mybatis.xml"})
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

    /**
     * 自定义Http转换器。
     * <p>
     * Spring MVC使用HttpMessageConverter接口来转换HTTP请求和响应。已经包含了默认的转换器，例如对象可以自动转换为JSON(使用
     * Jackson库)或XML(如果可以使用Jackson XML扩展，则使用JAXB)。默认情况下，字符串使用UTF-8编码。
     * <p>
     * 如果需要自定义转换器，则使用如下方法定义，返回<code>HttpMessageConverters</code>.
     *
     * @return
     */
    @Bean
    public HttpMessageConverters customConverters() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        // JSON包含null字段
        // converter.setFeatures(SerializerFeature.WriteMapNullValue);
        // JSON格式化输出
        // converter.setFeatures(SerializerFeature.PrettyFormat);
        return new HttpMessageConverters(converter);
    }
}