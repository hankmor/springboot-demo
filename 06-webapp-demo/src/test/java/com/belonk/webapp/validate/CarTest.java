package com.belonk.webapp.validate;

import com.belonk.webapp.domain.*;
import com.belonk.webapp.validator.CarChecks;
import com.belonk.webapp.validator.DriverChecks;
import com.belonk.webapp.validator.OrderedChecks;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by sun on 2018/10/9.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class CarTest {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Validator validator;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Public Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Before
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * manufacturer违反NotNull约束
     */
    @Test
    public void testManufacturerIsNull() {
        Car car = new Car(null, "川A﹒1234", 5);
        Set<ConstraintViolation<Car>> cvs = validator.validate(car);
        // 有一条错误信息
        assertEquals(1, cvs.size());
        // hibernate-validator：ValidationMessages_zh_CN.properties
        assertEquals("生产厂家不能为空", cvs.iterator().next().getMessage());
    }

    /**
     * 牌照不合法
     */
    @Test
    public void testLicensePlateIsInvalid() {
        Car car = new Car("ford", "川A﹒1", 5);
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(1, constraintViolations.size());
        // hibernate-validator：ValidationMessages_zh_CN.properties
        assertEquals("牌照长度为5到12", constraintViolations.iterator().next().getMessage());
    }

    /**
     * 座位数太小
     */
    @Test
    public void testSeatCountIsTooLow() {
        Car car = new Car("ford", "川A﹒1234", 1);
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(1, constraintViolations.size());
        // hibernate-validator：ValidationMessages_zh_CN.properties
        assertEquals("座位数大于等于2", constraintViolations.iterator().next().getMessage());
    }

    /**
     * 验证通过
     */
    @Test
    public void testCarIsValid() {
        Car car = new Car("ford", "川A﹒1234", 5);
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void testDriver() {
        // 司机，年龄未满18, 无驾照
        Driver driver = new Driver("司机", "siji@123.com", 10, false);
        // 车辆，生产厂商不能为空
        Car car = new Car(null, "川A﹒12345", 5);
        car.setDriver(driver);

        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(3, constraintViolations.size());
    }

    @Test
    public void testPassengers() {
        // 生产厂商不对
        Car car = new Car(null, "川A﹒12345", 5);
        List<User> passengers = new ArrayList<>();
        // 正确
        User passenger1 = new User("baby", "baby@123.com");
        // 邮箱不对
        User passenger2 = new User("kid", "kid123.com");
        // 邮箱为空
        User passenger3 = new User("ghost", null);
        passengers.add(passenger1);
        passengers.add(passenger2);
        passengers.add(passenger3);
        // 载人数为2，超载了
        car.setPassengers(passengers);

        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        for (ConstraintViolation<Car> constraintViolation : constraintViolations) {
            System.out.println(constraintViolation.getMessage());
        }
        assertEquals(4, constraintViolations.size());
    }

    @Test
    public void testPassengers1() {
        // 生产厂商不对
        Car car = new Car(null, "川A﹒12345", 5);
        List<User> passengers = new ArrayList<>();
        // ok
        User passenger1 = new User("baby", "baby@123.com");
        // 邮箱不对
        User passenger2 = new User("kid", "kid123.com");
        passengers.add(passenger1);
        passengers.add(passenger2);
        // 载人数为2，ok
        car.setPassengers(passengers);

        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(2, constraintViolations.size());
    }

    @Test
    public void testRentalCar() {
        // 会校验子类，同时还会校验父类，这就是约束继承，这同样适用于接口。如果子类覆盖了父类的方法，那么子类和父类的约束都会被校验。
        RentalCar rentalCar = new RentalCar(null);
        Set<ConstraintViolation<RentalCar>> constraintViolations =
                validator.validate(rentalCar);
        assertEquals(4, constraintViolations.size());
    }

    @Test
    public void testPropertyValidate() {
        RentalCar rentalCar = new RentalCar(null);
        Set<ConstraintViolation<RentalCar>> constraintViolations =
                validator.validateProperty(rentalCar, "rentalStation");
        assertEquals(1, constraintViolations.size());
    }

    @Test
    public void testPropertyValueValidate() {
        Set<ConstraintViolation<RentalCar>> constraintViolations =
                validator.validateValue(RentalCar.class, "rentalStation", null);
        assertEquals(1, constraintViolations.size());
    }

    @Test
    public void testConstraintViolation() {
        RentalCar rentalCar = new RentalCar(null);
        Set<ConstraintViolation<RentalCar>> constraintViolations =
                validator.validate(rentalCar);
        for (ConstraintViolation<RentalCar> constraintViolation : constraintViolations) {
            System.out.println("message         : " + constraintViolation.getMessage());
            System.out.println("message template: " + constraintViolation.getMessageTemplate());
            System.out.println("property        : " + constraintViolation.getPropertyPath().toString());
            System.out.println("invalid value   : " + constraintViolation.getInvalidValue());
            System.out.println("root bean class : " + constraintViolation.getRootBeanClass());
            System.out.println("leaf bean       : " + constraintViolation.getLeafBean());
            System.out.println("---------------------");
        }
        assertEquals(4, constraintViolations.size());
    }

    // 校验组测试

    @Test
    public void driveAway() {
        // 通过校验，默认组
        Car car = new Car("ford", "川A12345", 2);
        Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);
        assertEquals(0, constraintViolations.size());

        // 未通过上路检测
        constraintViolations = validator.validate(car, CarChecks.class);
        assertEquals(1, constraintViolations.size());
        assertEquals("必须先通过上路检测", constraintViolations.iterator().next().getMessage());

        // 设置通过上路检测
        car.setPassedVehicleInspection(true);
        assertEquals(0, validator.validate(car).size());

        // 设置一个司机
        Driver john = new Driver("john", "john@123.com", 20);
        car.setDriver(john);
        constraintViolations = validator.validate(car, DriverChecks.class);
        assertEquals(1, constraintViolations.size());
        assertEquals("你想无证驾驶？", constraintViolations.iterator().next().getMessage());

        // 司机通过了驾照考试
        john.setHasDrivingLicense(true);
        assertEquals(0, validator.validate(car, DriverChecks.class).size());

        // 全部通过
        assertEquals(0, validator.validate(car, Default.class, CarChecks.class, DriverChecks.class).size());
    }

    // 校验组顺序

    @Test
    public void testOrderedChecks() {
        Car car = new Car("ford", "川A12345", 2);
        car.setPassedVehicleInspection(true);

        Driver john = new Driver("john", "john@123.com", 20);
        john.setHasDrivingLicense(true);
        car.setDriver(john);

        assertEquals(0, validator.validate(car, OrderedChecks.class).size());
    }

    // 重新定义默认校验组测试

    @Test
    public void testOrderedChecksWithRedefinedDefault() {
        GreatWallCar greatWallCar = new GreatWallCar();
        greatWallCar.setManufacturer("great wall");
        greatWallCar.setPassedVehicleInspection(true);
        greatWallCar.setLicensePlate("川A123456");
        greatWallCar.setSeatCount(5);
        greatWallCar.setNo("dassa");

        Driver john = new Driver("john", "john@123.com", 2);
        john.setHasDrivingLicense(true);
        greatWallCar.setDriver(john);

        Set<ConstraintViolation<GreatWallCar>> constraintViolations = validator.validate(greatWallCar);
        for (ConstraintViolation<GreatWallCar> constraintViolation : constraintViolations) {
            System.out.println(constraintViolation.getMessage());
        }
        assertEquals(0, constraintViolations.size());
    }

    // 多个校验组合约束测试

    @Test
    public void testConstraintComposing() {
        GeelyCar geelyCar = new GeelyCar();
        geelyCar.setManufacturer("geely wall");
        geelyCar.setPassedVehicleInspection(true);
        geelyCar.setLicensePlate("川A123456");
        geelyCar.setSeatCount(5);
        geelyCar.setProductionNo("123");

        Set<ConstraintViolation<GeelyCar>> constraintViolations = validator.validate(geelyCar);
        assertEquals(1, constraintViolations.size());
        assertEquals("汽车生产编号不合法", constraintViolations.iterator().next().getMessage());

        Driver john = new Driver("john", "john@123.com", 2);
        john.setHasDrivingLicense(true);
        geelyCar.setDriver(john);
        geelyCar.setProductionNo("GL12345");

        constraintViolations = validator.validate(geelyCar);
        assertEquals(0, constraintViolations.size());
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Protected Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
    
    
    
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Property accessors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
     
     
     
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Inner classes
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */


}