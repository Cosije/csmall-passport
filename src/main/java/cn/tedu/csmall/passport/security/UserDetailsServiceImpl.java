package cn.tedu.csmall.passport.security;

import cn.tedu.csmall.passport.mapper.AdminMapper;
import cn.tedu.csmall.passport.pojo.vo.AdminLoginInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("Spring Security调用了loadUserByUsername()方法，参数：{}", s);
        AdminLoginInfoVO loginInfo = adminMapper.getLoginInfoByUsername(s);
        log.debug("从数据库查询与用户名【{}】匹配的管理员信息：{}", s, loginInfo);

        if (loginInfo == null) {
            log.debug("此用户名【{}】不存在，即将抛出异常");
            String message = "登录失败，用户名不存在！";
            throw new BadCredentialsException(message);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String permission : loginInfo.getPermissions()) {
            GrantedAuthority authority = new SimpleGrantedAuthority(permission);
            authorities.add(authority);
        }


        AdminDetails adminDetails = new AdminDetails(
                loginInfo.getUsername(),  loginInfo.getPassword(),
                loginInfo.getEnable()==1,authorities);
        adminDetails.setId(loginInfo.getId());


        log.debug("即将向Spring Security返回UserDetails对象：{}", adminDetails);
        return adminDetails;
    }
}
