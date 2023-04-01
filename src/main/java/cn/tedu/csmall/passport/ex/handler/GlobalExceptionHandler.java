package cn.tedu.csmall.passport.ex.handler;

import cn.tedu.csmall.passport.ex.ServiceException;
import cn.tedu.csmall.passport.web.JsonResult;

import cn.tedu.csmall.passport.web.ServiceCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理器
     */

    public GlobalExceptionHandler(){
        System.out.println("创建全局异常处理器对象：GlobalExceptionHandler");
    }

    @ExceptionHandler
    public JsonResult handleServiceException(ServiceException e) {
        log.debug("捕获到ServiceException：{}", e.getMessage());
        return JsonResult.fail(e.getServiceCode(),e.getMessage());
    }

    @ExceptionHandler
    public String handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.debug("捕获到HttpRequestMethodNotSupportedException：{}", e.getMessage());
        return "非法访问";
    }

    @ExceptionHandler
    public String handleThrowable(Throwable e) {
        log.debug("捕获到Throwable：{}", e.getMessage());
        e.printStackTrace(); // 强烈建议
        return "服务器运行过程中出现未知错误，请联系系统管理员！";
    }

    @ExceptionHandler({
            InternalAuthenticationServiceException.class,
            BadCredentialsException.class
    })

    public JsonResult handleAuthenticationException(AuthenticationException e){
        log.debug("捕获到AuthenticationException");
        log.debug("异常类型：{}", e.getClass().getName());
        log.debug("异常信息：{}", e.getMessage());
        String message = "登陆失败，用户名或密码错误！";
        return JsonResult.fail(ServiceCode.ERR_UNAUTHORIZED,message);
    }

    @ExceptionHandler
    public JsonResult handleDisabledException(DisabledException e){
        log.debug("捕获到DisabledException：{}", e.getMessage());
        String message = "登录失败，此管理员账号已经被禁用！";
        return JsonResult.fail(ServiceCode.ERR_UNAUTHORIZED_DISABLED,message);
    }


}
