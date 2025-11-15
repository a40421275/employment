package com.shera.framework.employment.employment.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

/**
 * 企业查询参数DTO
 */
@Data
public class CompanyQueryDTO {
    
    @Parameter(description = "搜索关键词（企业名称、简称）")
    private String keyword;
    
    @Parameter(description = "城市")
    private String city;
    
    @Parameter(description = "行业")
    private String industry;
    
    @Parameter(description = "企业类型")
    private Integer companyType;
    
    @Parameter(description = "企业规模")
    private Integer scale;
    
    @Parameter(description = "状态")
    private Integer status;
    
    @Parameter(description = "认证状态")
    private Integer authStatus;
    
    @Parameter(description = "省份")
    private String province;
    
    @Parameter(description = "区县")
    private String district;
    
    @Parameter(description = "页码")
    private Integer page = 0;
    
    @Parameter(description = "每页大小")
    private Integer size = 10;
    
    @Parameter(description = "排序字段")
    private String sort = "createTime";
    
    @Parameter(description = "排序方向")
    private String direction = "desc";
    
    /**
     * 检查是否有查询条件
     */
    public boolean hasQueryConditions() {
        return keyword != null || city != null || industry != null || 
               companyType != null || scale != null || status != null || 
               authStatus != null || province != null || district != null;
    }
    
    /**
     * 检查是否有分页参数
     */
    public boolean hasPagination() {
        return page != null && size != null;
    }
}
