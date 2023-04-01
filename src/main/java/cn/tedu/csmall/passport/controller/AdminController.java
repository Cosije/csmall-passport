package cn.tedu.csmall.passport.controller;

import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.passport.security.LoginPrincipal;
import cn.tedu.csmall.passport.service.IAdminService;
import cn.tedu.csmall.passport.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@Api(tags = "01.管理员管理模块")
@RestController
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    IAdminService adminService;

    public AdminController() {
        log.info("创建控制器类：AdminController");
    }

    // http://localhost:9081/admins/add-new?username=aa&phone=bb&email=cc
    @ApiOperation("添加管理员")
    @ApiOperationSupport(order = 100)
    @PostMapping("/add-new")
    public JsonResult<Void> addNew(AdminAddNewDTO adminAddNewDTO) {
        log.debug("开始处理【添加管理员】的请求，参数：{}", adminAddNewDTO);
        adminService.addNew(adminAddNewDTO);
        return JsonResult.ok();
    }

    @ApiOperation("查询管理员列表")
    @ApiOperationSupport(order = 420)
    @GetMapping("")
    @PreAuthorize("hasAuthority('/ams/admin/read')")
    public JsonResult<List<AdminListItemVO>> list(){
        log.debug("开始处理【查询相册列表】的请求");
        List<AdminListItemVO> list = adminService.list();
        return JsonResult.ok(list);
    }

    @ApiOperation("删除管理员")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParam(name = "id", value = "管理员id", required = true,dataType = "long")
    @PostMapping("/{id:[0-9]+}/delete")
    public JsonResult<Void> delete(@PathVariable Long id,
              @ApiIgnore @AuthenticationPrincipal LoginPrincipal loginPrincipal){
        log.debug("开始处理【删除管理员】的请求，请求参数：{}",id);
        log.debug("当前登录的当事人：{}",loginPrincipal);
        adminService.delete(id);
        return JsonResult.ok();
    }

    @ApiOperation("启用管理员")
    @ApiOperationSupport(order = 310)
    @ApiImplicitParam(name = "id",value = "管理员id",required = true,dataType = "long")
    @PostMapping("/{id:[0-9]+}/enable")
    public JsonResult<Void> setEnable(@PathVariable Long id){
        log.debug("开始处理【启用管理员】的请求，参数：{}",id);
        adminService.setEnable(id);
        return JsonResult.ok();
    }

    @ApiOperation("禁用管理员")
    @ApiOperationSupport(order = 311)
    @ApiImplicitParam(name = "id",value = "管理员id",required = true,dataType = "long")
    @PostMapping("/{id:[0-9]+}/disable")
    public JsonResult<Void> setDisable(@PathVariable Long id){
        log.debug("开始处理【禁用管理员】的请求，参数：{}",id);
        adminService.setDisable(id);
        return JsonResult.ok();
    }

    // http://localhost:9081/admins/login
    @PostMapping("/login")
    public JsonResult<String> login(AdminLoginDTO adminLoginDTO){
        log.debug("开始处理【管理员登录】的请求，参数：{}", adminLoginDTO);
        //TODO调用service处理登录
        String jwt = adminService.login(adminLoginDTO);
        return JsonResult.ok(jwt);
    }
}
