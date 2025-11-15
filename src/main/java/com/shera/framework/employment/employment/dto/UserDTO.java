package com.shera.framework.employment.employment.dto;

import lombok.Data;

/**
 * 用户数据传输对象
 */
@Data
public class UserDTO {
    
    private Long id;
    
    private String username;
    
    private String phone;
    
    private String email;
    
    private String password;
    
    private String wxOpenid;
    
    private String wxUnionid;
    
    private Integer userType = 1; // 1-求职者，2-企业用户，3-管理员
    
    private Integer status = 1; // 0-禁用，1-正常，2-黑名单
    
    private Integer authStatus = 0; // 0-未认证，1-审核中，2-已认证
    
    private Long companyId; // 所属企业ID，仅企业用户使用
}
