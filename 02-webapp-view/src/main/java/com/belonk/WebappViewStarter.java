package com.belonk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebappViewStarter {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(WebappViewStarter.class, args);

        Logger logger = LoggerFactory.getLogger(WebappViewStarter.class);
        while(true) {
            logger.info("Spring Validator\n" +
                    "其实，Spring很早就有了自己的Bean验证机制，其核心为Validator接口，表示校验器：\n" +
                    "\n" +
                    "public interface Validator {\n" +
                    "    // 检测Validator是否支持校验提供的Class\n" +
                    "    boolean supports(Class<?> clazz);\n" +
                    "\n" +
                    "    // 校验逻辑，校验的结果信息通过errors获取\n" +
                    "    void validate(@Nullable Object target, Errors errors);\n" +
                    "}\n" +
                    "1.\n" +
                    "2.\n" +
                    "3.\n" +
                    "4.\n" +
                    "5.\n" +
                    "6.\n" +
                    "7.\n" +
                    "Errors接口，用以表示校验失败的错误信息：\n" +
                    "\n" +
                    "public interface Errors {\n" +
                    "    // 获取被校验的根对象    \n" +
                    "    String getObjectName();\n" +
                    "\n" +
                    "    // 校验结果是否有错\n" +
                    "    boolean hasErrors();\n" +
                    "\n" +
                    "    // 获取校验错误数量\n" +
                    "    int getErrorCount();\n" +
                    "    \n" +
                    "    // 获取所有错误信息，包括全局错误和字段错误\n" +
                    "    List<ObjectError> getAllErrors();\n" +
                    "\n" +
                    "    // 获取所有字段错误\n" +
                    "\tList<FieldError> getFieldErrors();\n" +
                    "\n" +
                    "    ……\n" +
                    "}\n" +
                    "1.\n" +
                    "2.\n" +
                    "3.\n" +
                    "4.\n" +
                    "5.\n" +
                    "6.\n" +
                    "7.\n" +
                    "8.\n" +
                    "9.\n" +
                    "10.\n" +
                    "11.\n" +
                    "12.\n" +
                    "13.\n" +
                    "14.\n" +
                    "15.\n" +
                    "16.\n" +
                    "17.\n" +
                    "18.\n" +
                    "当Bean Validation被标准化过后，从Spring3.X开始，已经完全支持JSR 303(1.0)规范，通过Spring的LocalValidatorFactoryBean实现，它对Spring的Validator接口和javax.validation.Validator接口进行了适配。\n" +
                    "\n" +
                    "全局Validator\n" +
                    "全局Validator通过上述的LocalValidatorFactoryBean类来提供，只要使用@EnableWebMvc即可（Xml配置开启<mvc:annotation-driven>），也可以进行自定义：\n" +
                    "\n" +
                    "@Configuration\n" +
                    "@EnableWebMvc\n" +
                    "public class WebConfig extends WebMvcConfigurerAdapter {\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public Validator getValidator(); {\n" +
                    "        // return \"global\" validator\n" +
                    "    }\n" +
                    "}\n" +
                    "1.\n" +
                    "2.\n" +
                    "3.\n" +
                    "4.\n" +
                    "5.\n" +
                    "6.\n" +
                    "7.\n" +
                    "8.\n" +
                    "9.\n" +
                    "私有validator\n" +
                    "Spring也支持特定Controller私有的验证器，需要使用@InitBinder将验证器与Controller进行绑定，一个典型的应用场景是：一个Bean的几个属性的校验逻辑在同一个验证器完成。例如：定义如下的Bean，并未使用JSR303，而是使用自定义验证器来校验它的几个属性，示例代码如下：\n" +
                    "\n" +
                    "1、定义Bean：\n" +
                    "\n" +
                    "@Data\n" +
                    "public class Employee {\n" +
                    "    private int id;\n" +
                    "    private String name;\n" +
                    "    private String role;\n" +
                    "}\n" +
                    "1.\n" +
                    "2.\n" +
                    "3.\n" +
                    "4.\n" +
                    "5.\n" +
                    "6.\n" +
                    "2、自定义验证器：\n" +
                    "\n" +
                    "@Component\n" +
                    "public class EmployeeFormValidator implements Validator {\n" +
                    "    @Override\n" +
                    "    public boolean supports(Class<?> clazz) {\n" +
                    "        return Employee.class.equals(clazz);\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void validate(@Nullable Object target, Errors errors) {\n" +
                    "        // id不能为空\n" +
                    "        ValidationUtils.rejectIfEmptyOrWhitespace(errors, \"id\", \"id.required\");\n" +
                    "        Employee emp = (Employee) target;\n" +
                    "        if (emp.getId() <= 0) {\n" +
                    "            errors.rejectValue(\"id\", \"negativeValue\", new Object[]{\"'id'\"}, \"id can't be negative\");\n" +
                    "        }\n" +
                    "        ValidationUtils.rejectIfEmptyOrWhitespace(errors, \"name\", \"name.required\", \"name cant't be null\");\n" +
                    "        ValidationUtils.rejectIfEmptyOrWhitespace(errors, \"role\", \"role.required\", \"role cant't be null\");\n" +
                    "    }\n" +
                    "}\n" +
                    "1.\n" +
                    "2.\n" +
                    "3.\n" +
                    "4.\n" +
                    "5.\n" +
                    "6.\n" +
                    "7.\n" +
                    "8.\n" +
                    "9.\n" +
                    "10.\n" +
                    "11.\n" +
                    "12.\n" +
                    "13.\n" +
                    "14.\n" +
                    "15.\n" +
                    "16.\n" +
                    "17.\n" +
                    "18.\n" +
                    "19.\n" +
                    "需要实现Spring的Validator接口，这里使用了Spring提供的ValidationUtils工具类，该验证器将Employee的三个属性都进行了校验。\n" +
                    "\n" +
                    "3、绑定到Controller：\n" +
                    "\n" +
                    "@RestController\n" +
                    "@RequestMapping(\"/emp\")\n" +
                    "public class EmployeeController {\n" +
                    "    @Autowired\n" +
                    "    @Qualifier(\"employeeFormValidator\")\n" +
                    "    private Validator validator;\n" +
                    "\n" +
                    "    @InitBinder\n" +
                    "    private void initBinder(WebDataBinder binder) {\n" +
                    "        // 绑定验证器\n" +
                    "        binder.setValidator(validator);\n" +
                    "    }\n" +
                    "\n" +
                    "    @PostMapping(produces = \"application/json;charset=utf-8\")\n" +
                    "    public ResultMsg save(@RequestBody @Validated Employee employee,\n" +
                    "                          BindingResult bindingResult, Model model) {\n" +
                    "        if (bindingResult.hasErrors()) {\n" +
                    "            // 校验失败，获取校验错误信息\n" +
                    "            List<FieldError> errors = bindingResult.getFieldErrors();\n" +
                    "            StringBuilder sb = new StringBuilder();\n" +
                    "            for (FieldError error : errors) {\n" +
                    "                sb.append(String.format(\"错误字段：%s，错误值：%s，原因：%s\",\n" +
                    "                        error.getField(),\n" +
                    "                        error.getRejectedValue(),\n" +
                    "                        error.getDefaultMessage())\n" +
                    "                ).append(\"\\r\\n\");\n" +
                    "            }\n" +
                    "            return ResultMsg.error(MsgDefinition.ILLEGAL_ARGUMENTS.codeOf(), sb.toString());\n" +
                    "        } else {\n" +
                    "            return ResultMsg.success(employee);\n" +
                    "        }\n" +
                    "    }\n" +
                    "}\n" +
                    "1.\n" +
                    "2.\n" +
                    "3.\n" +
                    "4.\n" +
                    "5.\n" +
                    "6.\n" +
                    "7.\n" +
                    "8.\n" +
                    "9.\n" +
                    "10.\n" +
                    "11.\n" +
                    "12.\n" +
                    "13.\n" +
                    "14.\n" +
                    "15.\n" +
                    "16.\n" +
                    "17.\n" +
                    "18.\n" +
                    "19.\n" +
                    "20.\n" +
                    "21.\n" +
                    "22.\n" +
                    "23.\n" +
                    "24.\n" +
                    "25.\n" +
                    "26.\n" +
                    "27.\n" +
                    "28.\n" +
                    "29.\n" +
                    "30.\n" +
                    "31.\n" +
                    "32.\n" +
                    "33.\n" +
                    "要开启自动校验功能，需要在Controller校验的Bean上添加Spring的@Validated注解或者Bean Validation的@Valid注解(二者的区别请看文末的特别说明)，然后在被校验的Bean参数后加上BindingResult接口，用以接收校验失败的错误信息，该接口扩展了Errors接口。\n" +
                    "\n" +
                    "4、测试\n" +
                    "\n" +
                    "编写单元测试代码，测试Controller：\n" +
                    "\n" +
                    "@RunWith(SpringRunner.class)\n" +
                    "@SpringBootTest\n" +
                    "public class EmployeeControllerTest {\n" +
                    "    private MockMvc mockMvc;\n" +
                    "    @Autowired\n" +
                    "    protected WebApplicationContext wac;\n" +
                    "\n" +
                    "    @Before\n" +
                    "    public void setUp() {\n" +
                    "        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)\n" +
                    "                .alwaysExpect(MockMvcResultMatchers.status().isOk())\n" +
                    "                .build();\n" +
                    "    }\n" +
                    "\n" +
                    "    @Test\n" +
                    "    public void testAdd() throws Exception {\n" +
                    "        Employee employee = new Employee();\n" +
                    "        employee.setId(-1);\n" +
                    "        employee.setName(\"张三\");\n" +
                    "        // employee.setRole(\"哈哈\");\n" +
                    "        MvcResult mvcResult = mockMvc.perform(\n" +
                    "                MockMvcRequestBuilders\n" +
                    "                        .post(\"/emp\")\n" +
                    "                        .accept(\"application/json;charset=utf-8\")\n" +
                    "                        .characterEncoding(\"utf-8\")\n" +
                    "                        // 设置请求的content-type\n" +
                    "                        .contentType(\"application/json;charset=utf-8\")\n" +
                    "                        // 设置json格式请求参数\n" +
                    "                        .content(JsonUtil.toJson(employee))\n" +
                    "        ).andReturn();\n" +
                    "        MockHttpServletResponse resultResponse = mvcResult.getResponse();\n" +
                    "        String result = resultResponse.getContentAsString();\n" +
                    "        System.out.println(result);\n" +
                    "        // {\"rtnCode\":\"4002\",\"rtnMsg\":\"错误字段：id，错误值：-1，原因：id can't be negative\\r\\n错误字段：role，错误值：null，原因：role cant't be null\\r\\n\",\"data\":null,\"type\":\"error\"}\n" +
                    "    }\n" +
                    "}\n" +
                    "1.\n" +
                    "2.\n" +
                    "3.\n" +
                    "4.\n" +
                    "5.\n" +
                    "6.\n" +
                    "7.\n" +
                    "8.\n" +
                    "9.\n" +
                    "10.\n" +
                    "11.\n" +
                    "12.\n" +
                    "13.\n" +
                    "14.\n" +
                    "15.\n" +
                    "16.\n" +
                    "17.\n" +
                    "18.\n" +
                    "19.\n" +
                    "20.\n" +
                    "21.\n" +
                    "22.\n" +
                    "23.\n" +
                    "24.\n" +
                    "25.\n" +
                    "26.\n" +
                    "27.\n" +
                    "28.\n" +
                    "29.\n" +
                    "30.\n" +
                    "31.\n" +
                    "32.\n" +
                    "33.\n" +
                    "34.\n" +
                    "35.\n" +
                    "36.\n" +
                    "可以看到，校验功能已经启动，Spring进行了参数校验，成功输出校验的错误信息。\n" +
                    "\n" +
                    "上边的内容仅仅简单介绍了Spring的校验机制，更多Spring Validator的详细信息可以看这里。\n" +
                    "\n" +
                    "Web中集成Bean Validation\n" +
                    "前边说过，Spring从3.0已经全面支持Bean Validation 1.0，在Spring Boot工程中，可以直接使用它来作为Bean校验框架，我们来看看如何使用。\n" +
                    "\n" +
                    "编码处理校验结果\n" +
                    "前边已经说过，可以在被校验的Bean参数前加上@Valid或者@Validated注意来开启Bean校验，后加上BindingResult接口来获取校验失败信息（见Spring Boot参数验证（上）——Bean Validation及其Hibernate实现一篇）：\n" +
                    "\n" +
                    "@Valid：标准JSR-303规范的标记型注解，用来标记验证属性和方法返回值，进行级联和递归校验\n" +
                    "@Validated：Spring的注解，Spring's JSR-303规范，是标准JSR-303的一个变种，提供了一个分组功能，可以在入参验证时，根据不同的分组采用不同的验证机制\n" +
                    "@BindingResult：扩展自Errors接口，表示校验失败的结果\n" +
                    "在校验方法参数时，使用@Valid和@Validated并无特殊差异，但@Validated注解可以用于类级别，而且支持分组，而@Valid可以用在属性级别约束，用来表示级联校验。关于@Valid和@Validated的区别，请查阅相关资料，这里不再赘述。\n" +
                    "\n" +
                    "需要注意的是，校验的Bean和BindingResult作为方法的参数，需要对应。示例代码见上文绑定到Controller章节。\n" +
                    "\n" +
                    "编写全局异常处理校验结果\n" +
                    "多数情况下，异常处理逻辑基本上是相同的，可以将编码校验工作抽取出来，让Controller层只需要使用注解来标记验证约束，而不需要关注校验结果，只需要校验失败时，自动返回校验失败的信息。\n" +
                    "\n" +
                    "一种方式时，使用Spring Boot的全局异常处理机制。基本思路是：Spring在参数校验失败时，会抛出MethodArgumentNotValidException，只需要编写异常处理器来处理该异常即可。关于如何定义全局异常，可以看Spring boot全局异常处理和自定义异常页面一文。\n" +
                    "\n" +
                    "我们看看如何实现：\n" +
                    "\n");
            Thread.sleep(100);
        }
    }
}