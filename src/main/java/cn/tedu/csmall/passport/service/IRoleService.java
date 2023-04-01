package cn.tedu.csmall.passport.service;

import cn.tedu.csmall.passport.pojo.vo.RoleListItemVO;

import java.util.List;

/**
 * 处理角色数据的业务接口
 */
public interface IRoleService {

    /**
     * 查询角色列表
     */
    List<RoleListItemVO> list();
}
