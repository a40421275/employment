package com.shera.framework.employment.employment.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户与企业信息DTO（用于投影查询）
 */
@Data
public class UserWithCompanyDTO {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private Integer userType;
    private Integer status;
    private Integer authStatus;
    private Long companyId;
    private String companyName;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public UserWithCompanyDTO(Long id, String username, String phone, String email, 
                            Integer userType, Integer status, Integer authStatus, 
                            Long companyId, String companyName, LocalDateTime lastLoginTime,
                            LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.userType = userType;
        this.status = status;
        this.authStatus = authStatus;
        this.companyId = companyId;
        this.companyName = companyName;
        this.lastLoginTime = lastLoginTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
