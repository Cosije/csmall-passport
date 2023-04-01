package cn.tedu.csmall.passport.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminAddNewDTO implements Serializable {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（原文）
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否启用，1=启用，0=未启用
     */
    private Integer enable;
    /**
     * 尝试添加的管理员的角色id列表
     */
    private Long[] roleIds;


}
