package com.shera.framework.employment.employment.service;

import com.shera.framework.employment.employment.dto.CompanyDTO;
import com.shera.framework.employment.employment.dto.CompanyOptionDTO;
import com.shera.framework.employment.employment.dto.CompanyQueryDTO;
import com.shera.framework.employment.employment.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 企业服务接口
 */
public interface CompanyService {
    
    /**
     * 创建企业
     */
    CompanyDTO createCompany(CompanyDTO companyDTO);
    
    /**
     * 更新企业信息
     */
    CompanyDTO updateCompany(Long id, CompanyDTO companyDTO);
    
    /**
     * 根据ID获取企业
     */
    CompanyDTO getCompanyById(Long id);
    
    /**
     * 根据企业名称获取企业
     */
    CompanyDTO getCompanyByName(String companyName);
    
    /**
     * 删除企业
     */
    void deleteCompany(Long id);
    
    /**
     * 分页查询企业列表
     */
    Page<CompanyDTO> getCompanies(Pageable pageable);
    
    /**
     * 根据状态查询企业列表
     */
    List<CompanyDTO> getCompaniesByStatus(Integer status);
    
    /**
     * 根据认证状态查询企业列表
     */
    List<CompanyDTO> getCompaniesByAuthStatus(Integer authStatus);
    
    /**
     * 根据行业查询企业列表
     */
    List<CompanyDTO> getCompaniesByIndustry(String industry);
    
    /**
     * 根据城市查询企业列表
     */
    List<CompanyDTO> getCompaniesByCity(String city);
    
    /**
     * 搜索企业
     */
    List<CompanyDTO> searchCompanies(String keyword);
    
    /**
     * 统一查询企业列表（支持各种筛选和分页查询）
     */
    Page<CompanyDTO> queryCompanies(CompanyQueryDTO queryDTO);
    
    /**
     * 审核企业认证
     */
    CompanyDTO auditCompany(Long id, Integer authStatus, String authFailReason, Long operatorId);
    
    /**
     * 更新企业状态
     */
    CompanyDTO updateCompanyStatus(Long id, Integer status);
    
    /**
     * 增加企业查看次数
     */
    void incrementViewCount(Long id);
    
    /**
     * 增加企业收藏次数
     */
    void incrementFavoriteCount(Long id);
    
    /**
     * 增加企业申请次数
     */
    void incrementApplyCount(Long id);
    
    /**
     * 更新企业岗位数量
     */
    void updateJobCount(Long id, Integer jobCount);
    
    /**
     * 统计企业数量
     */
    Long countCompanies(Integer status);
    
    /**
     * 统计认证企业数量
     */
    Long countAuthCompanies(Integer authStatus);
    
    /**
     * 验证企业信息
     */
    boolean validateCompany(CompanyDTO companyDTO);
    
    /**
     * 检查企业名称是否已存在
     */
    boolean isCompanyNameExists(String companyName);
    
    /**
     * 检查营业执照号是否已存在
     */
    boolean isBusinessLicenseNoExists(String businessLicenseNo);
    
    /**
     * 获取企业选项列表
     * 用于用户关联公司时查询公司列表，支持名称搜索
     * @param keyword 搜索关键词
     * @param pageable 分页参数
     * @return 企业选项列表
     */
    Page<CompanyOptionDTO> getCompanyOptions(String keyword, Pageable pageable);
}
