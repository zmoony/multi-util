package com.example.text.java;

import lombok.Data;
import org.junit.Assert;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * JAVA对象转换
 *
 * @author yuez
 * @since 2022/9/14
 */
public class ConvertTest {

    @Test
    public void simpleBeanCopy(){
        //不使用转换器，直接对相同的属性名进行copy
        BeanCopier beanCopier = BeanCopier.create(UserDO.class, UserDTO.class, false);
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("aihe");
        userDO.setGmtCreate(new Date());
        userDO.setGender(0);
        userDO.setPassword("xxxxxx");
        UserDTO userDTO = new UserDTO();
        beanCopier.copy(userDO,userDTO,null);
        Assert.assertEquals("名称未成功拷贝",userDTO.getName(),"aihe");
        Assert.assertEquals("Id未成功拷贝", 1L, (long)userDTO.getId());
        Assert.assertEquals("性别未成功拷贝", Integer.valueOf(0),userDTO.getGender());
    }

    @Test
    public void complexBeanCopy(){
        //不使用转换器，直接对相同的属性名进行copy
        BeanCopier beanCopier = BeanCopier.create(UserDO.class, UserDTO.class, true);
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("aihe");
        userDO.setGmtCreate(new Date());
        userDO.setGender(0);
        userDO.setPassword("xxxxxx");
        UserDTO userDTO = new UserDTO();
        beanCopier.copy(userDO, userDTO, new Converter() {
            @Override
            public Object convert(Object o, Class aClass, Object o1) {
                return null;
            }
        });
        Assert.assertEquals("名称未成功拷贝",userDTO.getName(),"aihe");
        Assert.assertEquals("Id未成功拷贝", 1L, (long)userDTO.getId());
        Assert.assertEquals("性别未成功拷贝", Integer.valueOf(0),userDTO.getGender());
    }

}
@Data
class UserDO {

    private Long id;

    private String name;

    private Integer gender;

    private String password;

    private Date gmtCreate;

    private Date gmtModified;

}
@Data
class UserDTO {

    private Long id;

    private String name;

    private Integer gender;

    private Integer pwd;

}
