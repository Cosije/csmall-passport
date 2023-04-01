package cn.tedu.csmall.passport.mapper;

import cn.tedu.csmall.passport.pojo.vo.RoleListItemVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理角色数据的mapper接口
 */
@Repository
public interface RoleMapper {
    /**
     * 查询角色列表
     */
    List<RoleListItemVO> list();
}
