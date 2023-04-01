package cn.tedu.csmall.passport.mapper;

import cn.tedu.csmall.passport.pojo.entity.Admin;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.passport.pojo.vo.AdminLoginInfoVO;
import cn.tedu.csmall.passport.pojo.vo.AdminStandardVO;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AdminMapper {

    /**
     * 插入管理员数据
     *
     * @param admin 管理员数据
     * @return 受影响的行数
     */
    int insert(Admin admin);

    /**
     * 根据id山粗管理员数据
     */
    int deleteById(Long id);

    /**
     * 根据id查询管理员数据
     */
    AdminStandardVO getStandardById(Long id);

    /**
     * 根据用户名统计管理员的数量
     */
    int countByUsername(String username);

    /**
     * 根据手机号统计管理员的数量
     */
    int countByPhone(String phone);

    /**
     * 根据电子邮箱统计管理员的数量
     */
    int countByEmail(String email);

    /**
     * 查询管理员列表信息
     */
    List<AdminListItemVO> list();

    /**
     * 根据管理员id修改管理员的数据
     */
    int updateById(Admin admin);

    /**
     * 根据用户名查询管理员登陆信息
     */
    AdminLoginInfoVO getLoginInfoByUsername(String name);

}
