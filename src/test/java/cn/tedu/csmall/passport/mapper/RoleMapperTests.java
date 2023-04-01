package cn.tedu.csmall.passport.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class RoleMapperTests {
    @Autowired
    RoleMapper mapper;

    @Test
    void testList(){
        List<?> list = mapper.list();
        System.out.println("查询列表完成，列表中的数量的数据="+list.size());
        for (Object item : list){
            System.out.println(item);
        }
    }
}
