package cn.tedu.csmall.passport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class Jsd2206CsmallPassportApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    DataSource dataSource;

    @Test
    void testGetConnection() throws SQLException {
        dataSource.getConnection();
        System.out.println("数据库连接成功!");
    }

}
