package com.shera.framework.employment.employment.dto;

import lombok.Data;

/**
 * 企业选项DTO
 * 用于用户关联公司时查询公司列表，只包含企业id和企业名称
 */
@Data
public class CompanyOptionDTO {
    
    /**
     * 企业ID
     */
    private Long id;
    
    /**
     * 企业名称
     */
    private String companyName;
    
    /**
     * 默认构造函数
     */
    public CompanyOptionDTO() {}
    
    /**
     * 带参构造函数
     * @param id 企业ID
     * @param companyName 企业名称
     */
    public CompanyOptionDTO(Long id, String companyName) {
        this.id = id;
        this.companyName = companyName;
    }
}
