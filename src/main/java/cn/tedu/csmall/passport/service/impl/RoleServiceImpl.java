package cn.tedu.csmall.passport.service.impl;

import cn.tedu.csmall.passport.mapper.RoleMapper;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.passport.pojo.vo.RoleListItemVO;
import cn.tedu.csmall.passport.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    RoleMapper roleMapper;

    @Override
    public List<RoleListItemVO> list(){
        log.debug("开始处理【查询角色列表】的业务");
        //调用Mapper查询角色列表
        List<RoleListItemVO> list = roleMapper.list();
        //将角色列表中id=1的角色数据移除
        Iterator<RoleListItemVO> iterator = list.iterator();
        while (iterator.hasNext()){
            RoleListItemVO item = iterator.next();
            if (item.getId()==1){
                iterator.remove();
                break;
            }
        }
        //返回
        return list;
    }
}
