package cn.tedu.csmall.passport.mapper;

import cn.tedu.csmall.passport.pojo.entity.AdminRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理管理员和角色的关联数据的mapper接口
 */
@Repository
public interface AdminRoleMapper {
    /**
     * 批量插入管理员与角色的关联数据
     */
    int insertBatch(List<AdminRole> adminRoleList);

    /**
     * 根据管理员id删除管理员与角色关联数据
     */
    int deleteByAdminId(Long adminId);
}
