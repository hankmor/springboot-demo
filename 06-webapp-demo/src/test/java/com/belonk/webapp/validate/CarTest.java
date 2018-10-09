package com.belonk.webapp.validate;

import com.belonk.webapp.domain.Car;
import com.belonk.webapp.domain.Person;
import com.belonk.webapp.domain.RentalCar;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
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
        assertEquals("不能为null", cvs.iterator().next().getMessage());
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
        assertEquals("个数必须在5和12之间", constraintViolations.iterator().next().getMessage());
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
        assertEquals("最小不能小于2", constraintViolations.iterator().next().getMessage());
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
    public void testDriverAndPassengers() {
        // 司机，年龄未满18
        Person person = new Person("司机", "siji@123.com", 10);
        // 车辆，生产厂商不能为空
        Car car = new Car(null, "川A﹒12345", 5);
        car.setDriver(person);

        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        System.out.println(constraintViolations.size());
        for (ConstraintViolation<Car> constraintViolation : constraintViolations) {
            System.out.println(constraintViolation.getMessage());
        }
        assertEquals(2, constraintViolations.size());
    }

    @Test
    public void testPassengers() {
        // 生产厂商不对
        Car car = new Car(null, "川A﹒12345", 5);
        List<Person> passengers = new ArrayList<>();
        // 年龄不对
        Person passenger1 = new Person("baby", "baby@123.com", 1);
        // 邮箱不对
        Person passenger2 = new Person("kid", "kid123.com", 20);
        // 邮箱为空，年龄不对
        Person passenger3 = new Person("ghost", null, 200);
        passengers.add(passenger1);
        passengers.add(passenger2);
        passengers.add(passenger3);
        // 载人数为2，超载了
        car.setPassengers(passengers);

        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        System.out.println(constraintViolations.size());
        for (ConstraintViolation<Car> constraintViolation : constraintViolations) {
            System.out.println(constraintViolation.getMessage());
        }
        assertEquals(6, constraintViolations.size());
    }

    @Test
    public void testPassengers1() {
        // 生产厂商不对
        Car car = new Car(null, "川A﹒12345", 5);
        List<Person> passengers = new ArrayList<>();
        // 年龄不对
        Person passenger1 = new Person("baby", "baby@123.com", 1);
        // 邮箱不对
        Person passenger2 = new Person("kid", "kid123.com", 20);
        passengers.add(passenger1);
        passengers.add(passenger2);
        // 载人数为2
        car.setPassengers(passengers);

        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        System.out.println(constraintViolations.size());
        for (ConstraintViolation<Car> constraintViolation : constraintViolations) {
            System.out.println(constraintViolation.getMessage());
        }
        assertEquals(3, constraintViolations.size());
    }

    @Test
    public void testRentalCar() {
        // 会校验子类，同时还会校验父类，这就是约束继承，这同样适用于接口。如果子类覆盖了父类的方法，那么子类和父类的约束都会被校验。
        RentalCar rentalCar = new RentalCar(null);
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(rentalCar);
        System.out.println(constraintViolations.size());
        for (ConstraintViolation<Car> constraintViolation : constraintViolations) {
            System.out.println(constraintViolation.getMessage());
        }
        assertEquals(4, constraintViolations.size());
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