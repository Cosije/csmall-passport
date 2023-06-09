package cn.tedu.csmall.passport.mapper;


import cn.tedu.csmall.passport.pojo.entity.Admin;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class AdminMapperTests {

@Autowired
AdminMapper mapper;

    @Test
    void testInsert(){
        Admin admin = new Admin();
        admin.setUsername("测试管理员名称001");
        admin.setPassword("测试管理员密码001");
        admin.setNickname("测试管理员昵称001");

        System.out.println("插入数据之前，参数=" + admin);
        int rows = mapper.insert(admin);
        System.out.println("插入数据完成，受印象的行数="+ rows);
        System.out.println("插入数据之后，参数=" + admin);
    }

    @Test
    void testCountByUsername(){
        String username = "wangkejing";
        int count = mapper.countByUsername(username);
        log.debug("根据用户名【{}】统计管理员账号的数量：{}", username, count);
    }

    @Test
    void testCountByPhone(){
        String phone = "13900139004";
        int count = mapper.countByPhone(phone);
        log.debug("根据手机号【{}】统计管理员账号的数量：{}", phone, count);
    }

    @Test
    void testCountByEmail(){
        String email = "chengheng@qq.com";
        int count = mapper.countByEmail(email);
        log.debug("根据电子邮箱【{}】统计管理员账号的数量：{}", email, count);
    }

    @Test
    void testDeleteById(){
        Long id = 6L;
        int rows = mapper.deleteById(id);
        System.out.println("删除数据完成，受影响的行数="+rows);
    }

    @Test
    void testGetStandardById(){
        Long id = 1L;
        Object result = mapper.getStandardById(id);
        System.out.println("根据id="+id+"查询标准信息完成，结果="+result);
    }

    @Test
    void testList() {
        List<?> list = mapper.list();
        System.out.println("查询列表完成，列表中的数据的数量=" + list.size());
        for (Object item : list) {
            System.out.println(item);
        }
    }


    @Test
    void testUpdateById(){
        Admin data = new Admin();
        data.setId(9L);
        data.setUsername("新的测试数据名称");

        int rows = mapper.updateById(data);
        System.out.println("修改数据完成，受影响的行数=" + rows);
    }

    @Test
    void testGetLoginInfoByUsername(){
        String username = "fanchuanqi";
        Object result = mapper.getLoginInfoByUsername(username);
        System.out.println("根据username=" + username + "查询登录信息完成，结果=" + result);
    }

}
