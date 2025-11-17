package com.shera.framework.employment.employment.modules.company.repository;

import com.shera.framework.employment.employment.modules.company.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 企业数据访问接口
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    
    /**
     * 根据企业名称查询企业
     */
    Optional<Company> findByCompanyName(String companyName);
    
    /**
     * 根据营业执照号查询企业
     */
    Optional<Company> findByBusinessLicenseNo(String businessLicenseNo);
    
    /**
     * 根据状态查询企业列表
     */
    List<Company> findByStatus(Integer status);
    
    /**
     * 根据状态查询企业列表（分页）
     */
    Page<Company> findByStatus(Integer status, Pageable pageable);
    
    /**
     * 根据认证状态查询企业列表
     */
    List<Company> findByAuthStatus(Integer authStatus);
    
    /**
     * 根据行业查询企业列表
     */
    List<Company> findByIndustry(String industry);
    
    /**
     * 根据城市查询企业列表
     */
    List<Company> findByCity(String city);
    
    /**
     * 根据企业名称模糊查询
     */
    @Query("SELECT c FROM Company c WHERE c.companyName LIKE %:keyword% OR c.companyShortName LIKE %:keyword%")
    List<Company> findByCompanyNameContaining(@Param("keyword") String keyword);
    
    /**
     * 根据状态和认证状态查询企业
     */
    Page<Company> findByStatusAndAuthStatus(Integer status, Integer authStatus, Pageable pageable);
    
    /**
     * 统计企业数量
     */
    @Query("SELECT COUNT(c) FROM Company c WHERE c.status = :status")
    Long countByStatus(@Param("status") Integer status);
    
    /**
     * 统计认证企业数量
     */
    @Query("SELECT COUNT(c) FROM Company c WHERE c.authStatus = :authStatus")
    Long countByAuthStatus(@Param("authStatus") Integer authStatus);
    
    /**
     * 根据企业名称和状态查询
     */
    Optional<Company> findByCompanyNameAndStatus(String companyName, Integer status);
    
    /**
     * 根据企业名称和认证状态查询
     */
    Optional<Company> findByCompanyNameAndAuthStatus(String companyName, Integer authStatus);
    
    /**
     * 根据企业名称、状态和认证状态查询
     */
    Optional<Company> findByCompanyNameAndStatusAndAuthStatus(String companyName, Integer status, Integer authStatus);
}
