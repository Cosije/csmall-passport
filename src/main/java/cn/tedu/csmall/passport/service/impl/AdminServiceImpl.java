package cn.tedu.csmall.passport.service.impl;

import cn.tedu.csmall.passport.ex.ServiceException;
import cn.tedu.csmall.passport.mapper.AdminMapper;
import cn.tedu.csmall.passport.mapper.AdminRoleMapper;
import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.csmall.passport.pojo.entity.Admin;
import cn.tedu.csmall.passport.pojo.entity.AdminRole;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.passport.pojo.vo.AdminStandardVO;
import cn.tedu.csmall.passport.security.AdminDetails;
import cn.tedu.csmall.passport.service.IAdminService;
import cn.tedu.csmall.passport.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    AdminRoleMapper adminRoleMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Value("${csmall.jwt.secret-key}")
    String secretKey;
    @Value("${csmall.jwt.duration-in-minute}")
    long durationInMinute;

    public AdminServiceImpl(){
        System.out.println("创建业务实现类：AdminServiceImpl");
    }

    @Override
    public String login(AdminLoginDTO adminLoginDTO){
        log.debug("开始处理【管理员登录】的业务，参数：{}", adminLoginDTO);
        // TODO 调用AuthenticationManager对象的authenticate()方法处理认证
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                adminLoginDTO.getUsername(),adminLoginDTO.getPassword());
        authenticationManager.authenticate(authentication);

        Authentication authenticateResult =
                authenticationManager.authenticate(authentication);
        log.debug("执行认证成功，authenticationManager返回：{}",authenticateResult);
        Object principal = authenticateResult.getPrincipal();
        log.debug("认证结果中的Principal数据类型：{}", principal.getClass().getName());
        log.debug("认证结果中的Principal数据：{}", principal);
        AdminDetails adminDetails = (AdminDetails) principal;

        log.debug("准备生成JWT数据");
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",adminDetails.getId());
        claims.put("username",adminDetails.getUsername());
        claims.put("authorities", JSON.toJSONString(adminDetails.getAuthorities()));//向JWT封装权限


        Date expirationDate = new Date(System.currentTimeMillis() + durationInMinute * 60 * 1000);
        String jwt = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        log.debug("返回JWT数据：{}", jwt);
        return jwt;

    }

    @Override
    public void addNew(AdminAddNewDTO adminAddNewDTO){
        log.debug("开始处理【添加管理员】的业务，参数：{}", adminAddNewDTO);

        log.debug("即将检查选择的角色是否合法");
        Long[] roleIds = adminAddNewDTO.getRoleIds();
        for (int i = 0; i < roleIds.length; i++) {
            if (roleIds[i]==1){
                String message = "非法访问！";
                log.debug(message);
                throw new ServiceException(ServiceCode.ERR_INSERT,message);
            }
        }

        log.debug("即将检查用户名是否被占用……");
        {
            // 从参数对象中获取username
            String username = adminAddNewDTO.getUsername();
            // 调用adminMapper的countByUsername()方法执行统计查询
            int count = adminMapper.countByUsername(username);
            // 判断统计结果是否不等于0
            if (count != 0) {
                // 是：抛出异常
                String message = "添加管理员失败，用户名【" + username + "】已经被占用！";
                log.debug(message);
                throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
            }
        }

        log.debug("即将检查手机号码是否被占用……");
        {
            // 从参数对象中获取手机号码
            String phone = adminAddNewDTO.getPhone();
            // 调用adminMapper的countByPhone()方法执行统计查询
            int count = adminMapper.countByPhone(phone);
            // 判断统计结果是否不等于0
            if (count != 0) {
                // 是：抛出异常
                String message = "添加管理员失败，手机号码【" + phone + "】已经被占用！";
                log.debug(message);
                throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
            }
        }

        log.debug("即将检查电子邮箱是否被占用……");
        {
            // 从参数对象中获取电子邮箱
            String email = adminAddNewDTO.getEmail();
            // 调用adminMapper的countByEmail()方法执行统计查询
            int count = adminMapper.countByEmail(email);
            // 判断统计结果是否不等于0
            if (count != 0) {
                // 是：抛出异常
                String message = "添加管理员失败，电子邮箱【" + email + "】已经被占用！";
                log.debug(message);
                throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
            }
        }

        // 创建Admin对象
        Admin admin = new Admin();
        // 通过BeanUtils.copyProperties()方法将参数对象的各属性值复制到Admin对象中
        BeanUtils.copyProperties(adminAddNewDTO, admin);
        // TODO 从Admin对象中取出密码，进行加密处理，并将密文封装回Admin对象中
        String rawPassword = admin.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        admin.setPassword(encodedPassword);
        // 补全Admin对象中的属性值：loginCount >>> 0
        admin.setLoginCount(0);
        // 调用adminMapper的insert()方法插入数据
        log.debug("即将插入管理员数据，参数：{}", admin);
        adminMapper.insert(admin);

        //调用adminRoleMapper的insertBatch()方法插入数据

        List<AdminRole> adminRoleList = new ArrayList<>();
        for (int i = 0;i <roleIds.length;i++){
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(admin.getId());
            adminRole.setRoleId(roleIds[i]);
            adminRoleList.add(adminRole);
        }
        adminRoleMapper.insertBatch(adminRoleList);
    }

    @Override
    public List<AdminListItemVO> list(){
        log.debug("开始处理【查询管理员列表】的业务");
        //调用Mapper查询管理员列表
        List<AdminListItemVO> list = adminMapper.list();
        //将管理员列表中id=1的管理员数据移除
        Iterator<AdminListItemVO> iterator = list.iterator();
        while (iterator.hasNext()){
            AdminListItemVO item = iterator.next();
            if (item.getId()==1){
                iterator.remove();
                break;
            }
        }
        //返回
        return list;
    }

    @Override
    public void delete(Long id){
        log.debug("开始处理【删除管理员】的业务，参数：{}",id);
        AdminStandardVO queryResult = adminMapper.getStandardById(id);
        if (queryResult == null) {
            String message = "删除管理员失败，尝试访问数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }
        adminMapper.deleteById(id);

        //执行删除此管理员与角色的关联数据
        int rows = adminRoleMapper.deleteByAdminId(id);
        if (rows < 1){
            String message = "删除管理员失败，服务器忙，请稍后再次尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_CONFLICT,message);
        }
    }

    @Override
    public void updateEnableById(Long id,Integer enable){
        String[] tips = {"禁用","启用"};
        log.debug("开始处理【{}管理员】，参数:{}",tips[enable],id);
        //检查要访问的数据是否存在
        AdminStandardVO queryResult = adminMapper.getStandardById(id);
        if (queryResult == null){
            String message = tips[enable]+"管理员失败，尝试访问数据不存在!";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }

        //判断目标状态
        if (queryResult.getEnable() == enable){
            String message = tips[enable]+"管理员失败，管理员账号已处于"+tips[enable]+"状态！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_CONFLICT,message);
        }

        //执行更新
        Admin admin = new Admin();
        admin.setId(id);
        admin.setEnable(enable);
        int rows = adminMapper.updateById(admin);
        if (rows != 1){
            String message = tips[enable]+"管理员失败，服务器忙，请稍后再试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_UPDATE,message);
        }
    }

    @Override
    public void setEnable(Long id){
        updateEnableById(id,1);
    }

    @Override
    public void setDisable(Long id){
        updateEnableById(id,0);
    }



}
