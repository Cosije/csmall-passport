package cn.tedu.csmall.passport.service;

import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.csmall.passport.pojo.entity.Admin;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;

import java.util.List;

public interface IAdminService {

    /**
     * 插入管理员数据
     * @param adminAddNewDTO
     */
    void addNew(AdminAddNewDTO adminAddNewDTO);

    /**
     * 删除管理员
     */
    void delete(Long id);

    /**
     * 查询管理员列表
     */
    List<AdminListItemVO> list();

    /**
     * 根据管理员id修改管理员的数据
     */
    void updateEnableById(Long id,Integer enable);

    /**
     * 启用管理员
     */
    void setEnable(Long id);

    /**
     * 禁用管理员
     */
    void setDisable(Long id);

    /**
     * 管理员登陆
     * @return 成功登录的JWT数据
     */
    String login(AdminLoginDTO adminLoginDTO);
}
