package com.shera.framework.employment.employment.repository;

import com.shera.framework.employment.employment.dto.JobWithCompanyDTO;
import com.shera.framework.employment.employment.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 岗位Repository
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job>, JobRepositoryCustom {
    
    /**
     * 根据分类ID查找岗位列表
     */
    List<Job> findByCategoryId(Long categoryId);
    
    /**
     * 根据公司ID查找岗位列表
     */
    List<Job> findByCompanyId(Long companyId);
    
    /**
     * 根据工作城市查找岗位列表
     */
    List<Job> findByWorkCity(String workCity);
    
    /**
     * 根据岗位类型查找岗位列表
     */
    List<Job> findByJobType(Integer jobType);
    
    /**
     * 根据状态查找岗位列表
     */
    List<Job> findByStatus(Integer status);
    
    /**
     * 根据状态分页查找岗位列表
     */
    Page<Job> findByStatus(Integer status, Pageable pageable);
    
    /**
     * 根据分类ID和状态查找岗位列表
     */
    List<Job> findByCategoryIdAndStatus(Long categoryId, Integer status);
    
    /**
     * 根据工作城市和状态查找岗位列表
     */
    List<Job> findByWorkCityAndStatus(String workCity, Integer status);
    
    /**
     * 根据岗位类型和状态查找岗位列表
     */
    List<Job> findByJobTypeAndStatus(Integer jobType, Integer status);
    
    /**
     * 根据薪资范围查找岗位列表
     */
    @Query("SELECT j FROM Job j WHERE j.salaryMin >= :minSalary AND j.salaryMax <= :maxSalary AND j.status = :status")
    List<Job> findBySalaryRangeAndStatus(@Param("minSalary") Double minSalary, @Param("maxSalary") Double maxSalary, @Param("status") Integer status);
    
    /**
     * 根据关键词搜索岗位
     */
    @Query("SELECT j FROM Job j WHERE (j.title LIKE %:keyword% OR j.description LIKE %:keyword% OR j.requirements LIKE %:keyword%) AND j.status = :status")
    Page<Job> searchByKeyword(@Param("keyword") String keyword, @Param("status") Integer status, Pageable pageable);
    
    /**
     * 查找已发布且未过期的岗位
     */
    @Query("SELECT j FROM Job j WHERE j.status = 1 AND (j.expireTime IS NULL OR j.expireTime > :now)")
    List<Job> findActiveJobs(@Param("now") LocalDateTime now);
    
    /**
     * 查找即将过期的岗位
     */
    @Query("SELECT j FROM Job j WHERE j.status = 1 AND j.expireTime IS NOT NULL AND j.expireTime BETWEEN :start AND :end")
    List<Job> findExpiringJobs(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    /**
     * 根据多个条件筛选岗位
     */
    @Query("SELECT j FROM Job j WHERE " +
           "(:categoryId IS NULL OR j.categoryId = :categoryId) AND " +
           "(:workCity IS NULL OR j.workCity = :workCity) AND " +
           "(:jobType IS NULL OR j.jobType = :jobType) AND " +
           "(:minSalary IS NULL OR j.salaryMin >= :minSalary) AND " +
           "(:maxSalary IS NULL OR j.salaryMax <= :maxSalary) AND " +
           "j.status = :status")
    Page<Job> findByMultipleConditions(@Param("categoryId") Long categoryId,
                                      @Param("workCity") String workCity,
                                      @Param("jobType") Integer jobType,
                                      @Param("minSalary") Double minSalary,
                                      @Param("maxSalary") Double maxSalary,
                                      @Param("status") Integer status,
                                      Pageable pageable);
    
    /**
     * 统计各状态的岗位数量
     */
    @Query("SELECT j.status, COUNT(j) FROM Job j GROUP BY j.status")
    List<Object[]> countByStatus();
    
    /**
     * 统计各城市的岗位数量
     */
    @Query("SELECT j.workCity, COUNT(j) FROM Job j WHERE j.status = 1 GROUP BY j.workCity")
    List<Object[]> countByCity();

    // ==================== 投影查询方法 ====================

    /**
     * 根据ID查询岗位与企业信息（投影查询）
     */
    @Query("SELECT new com.shera.framework.employment.employment.dto.JobWithCompanyDTO(" +
           "j.id, j.title, j.categoryId, c.name, j.companyId, co.companyName, j.department, " +
           "j.jobType, j.salaryMin, j.salaryMax, j.salaryUnit, j.workCity, j.workAddress, " +
           "j.workDistrict, j.workLatitude, j.workLongitude, j.description, j.requirements, " +
           "j.benefits, j.contactInfo, j.educationRequirement, j.experienceRequirement, " +
           "j.recruitNumber, j.urgentLevel, j.priorityLevel, j.isRecommended, j.recommendReason, " +
           "j.keywords, j.status, j.viewCount, j.applyCount, j.favoriteCount, j.publishTime, " +
           "j.expireTime, j.createTime, j.updateTime) " +
           "FROM Job j " +
           "LEFT JOIN Company co ON j.companyId = co.id " +
           "LEFT JOIN JobCategory c ON j.categoryId = c.id " +
           "WHERE j.id = :jobId")
    Optional<JobWithCompanyDTO> findJobWithCompanyById(@Param("jobId") Long jobId);

    /**
     * 分页查询岗位列表与企业信息（投影查询）
     */
    @Query("SELECT new com.shera.framework.employment.employment.dto.JobWithCompanyDTO(" +
           "j.id, j.title, j.categoryId, c.name, j.companyId, co.companyName, j.department, " +
           "j.jobType, j.salaryMin, j.salaryMax, j.salaryUnit, j.workCity, j.workAddress, " +
           "j.workDistrict, j.workLatitude, j.workLongitude, j.description, j.requirements, " +
           "j.benefits, j.contactInfo, j.educationRequirement, j.experienceRequirement, " +
           "j.recruitNumber, j.urgentLevel, j.priorityLevel, j.isRecommended, j.recommendReason, " +
           "j.keywords, j.status, j.viewCount, j.applyCount, j.favoriteCount, j.publishTime, " +
           "j.expireTime, j.createTime, j.updateTime) " +
           "FROM Job j " +
           "LEFT JOIN Company co ON j.companyId = co.id " +
           "LEFT JOIN JobCategory c ON j.categoryId = c.id " +
           "WHERE j.status = :status")
    Page<JobWithCompanyDTO> findJobsWithCompanyByStatus(@Param("status") Integer status, Pageable pageable);

    /**
     * 根据企业ID查询岗位列表与企业信息（投影查询）
     */
    @Query("SELECT new com.shera.framework.employment.employment.dto.JobWithCompanyDTO(" +
           "j.id, j.title, j.categoryId, c.name, j.companyId, co.companyName, j.department, " +
           "j.jobType, j.salaryMin, j.salaryMax, j.salaryUnit, j.workCity, j.workAddress, " +
           "j.workDistrict, j.workLatitude, j.workLongitude, j.description, j.requirements, " +
           "j.benefits, j.contactInfo, j.educationRequirement, j.experienceRequirement, " +
           "j.recruitNumber, j.urgentLevel, j.priorityLevel, j.isRecommended, j.recommendReason, " +
           "j.keywords, j.status, j.viewCount, j.applyCount, j.favoriteCount, j.publishTime, " +
           "j.expireTime, j.createTime, j.updateTime) " +
           "FROM Job j " +
           "LEFT JOIN Company co ON j.companyId = co.id " +
           "LEFT JOIN JobCategory c ON j.categoryId = c.id " +
           "WHERE j.companyId = :companyId AND j.status = :status")
    Page<JobWithCompanyDTO> findJobsWithCompanyByCompanyIdAndStatus(@Param("companyId") Long companyId, @Param("status") Integer status, Pageable pageable);

    /**
     * 根据关键词搜索岗位与企业信息（投影查询）
     */
    @Query("SELECT new com.shera.framework.employment.employment.dto.JobWithCompanyDTO(" +
           "j.id, j.title, j.categoryId, c.name, j.companyId, co.companyName, j.department, " +
           "j.jobType, j.salaryMin, j.salaryMax, j.salaryUnit, j.workCity, j.workAddress, " +
           "j.workDistrict, j.workLatitude, j.workLongitude, j.description, j.requirements, " +
           "j.benefits, j.contactInfo, j.educationRequirement, j.experienceRequirement, " +
           "j.recruitNumber, j.urgentLevel, j.priorityLevel, j.isRecommended, j.recommendReason, " +
           "j.keywords, j.status, j.viewCount, j.applyCount, j.favoriteCount, j.publishTime, " +
           "j.expireTime, j.createTime, j.updateTime) " +
           "FROM Job j " +
           "LEFT JOIN Company co ON j.companyId = co.id " +
           "LEFT JOIN JobCategory c ON j.categoryId = c.id " +
           "WHERE (j.title LIKE %:keyword% OR j.description LIKE %:keyword% OR j.requirements LIKE %:keyword%) " +
           "AND j.status = :status")
    Page<JobWithCompanyDTO> searchJobsWithCompanyByKeyword(@Param("keyword") String keyword, @Param("status") Integer status, Pageable pageable);

    /**
     * 多条件筛选岗位与企业信息（投影查询）
     */
    @Query("SELECT new com.shera.framework.employment.employment.dto.JobWithCompanyDTO(" +
           "j.id, j.title, j.categoryId, c.name, j.companyId, co.companyName, j.department, " +
           "j.jobType, j.salaryMin, j.salaryMax, j.salaryUnit, j.workCity, j.workAddress, " +
           "j.workDistrict, j.workLatitude, j.workLongitude, j.description, j.requirements, " +
           "j.benefits, j.contactInfo, j.educationRequirement, j.experienceRequirement, " +
           "j.recruitNumber, j.urgentLevel, j.priorityLevel, j.isRecommended, j.recommendReason, " +
           "j.keywords, j.status, j.viewCount, j.applyCount, j.favoriteCount, j.publishTime, " +
           "j.expireTime, j.createTime, j.updateTime) " +
           "FROM Job j " +
           "LEFT JOIN Company co ON j.companyId = co.id " +
           "LEFT JOIN JobCategory c ON j.categoryId = c.id " +
           "WHERE (:categoryId IS NULL OR j.categoryId = :categoryId) AND " +
           "(:workCity IS NULL OR j.workCity = :workCity) AND " +
           "(:jobType IS NULL OR j.jobType = :jobType) AND " +
           "(:minSalary IS NULL OR j.salaryMin >= :minSalary) AND " +
           "(:maxSalary IS NULL OR j.salaryMax <= :maxSalary) AND " +
           "j.status = :status")
    Page<JobWithCompanyDTO> findJobsWithCompanyByMultipleConditions(@Param("categoryId") Long categoryId,
                                                                   @Param("workCity") String workCity,
                                                                   @Param("jobType") Integer jobType,
                                                                   @Param("minSalary") Double minSalary,
                                                                   @Param("maxSalary") Double maxSalary,
                                                                   @Param("status") Integer status,
                                                                   Pageable pageable);

    /**
     * 根据分类ID查询岗位与企业信息（投影查询）
     */
    @Query("SELECT new com.shera.framework.employment.employment.dto.JobWithCompanyDTO(" +
           "j.id, j.title, j.categoryId, c.name, j.companyId, co.companyName, j.department, " +
           "j.jobType, j.salaryMin, j.salaryMax, j.salaryUnit, j.workCity, j.workAddress, " +
           "j.workDistrict, j.workLatitude, j.workLongitude, j.description, j.requirements, " +
           "j.benefits, j.contactInfo, j.educationRequirement, j.experienceRequirement, " +
           "j.recruitNumber, j.urgentLevel, j.priorityLevel, j.isRecommended, j.recommendReason, " +
           "j.keywords, j.status, j.viewCount, j.applyCount, j.favoriteCount, j.publishTime, " +
           "j.expireTime, j.createTime, j.updateTime) " +
           "FROM Job j " +
           "LEFT JOIN Company co ON j.companyId = co.id " +
           "LEFT JOIN JobCategory c ON j.categoryId = c.id " +
           "WHERE j.categoryId = :categoryId AND j.status = :status")
    Page<JobWithCompanyDTO> findJobsWithCompanyByCategoryId(@Param("categoryId") Long categoryId, 
                                                           @Param("status") Integer status, 
                                                           Pageable pageable);

    /**
     * 根据工作城市查询岗位与企业信息（投影查询）
     */
    @Query("SELECT new com.shera.framework.employment.employment.dto.JobWithCompanyDTO(" +
           "j.id, j.title, j.categoryId, c.name, j.companyId, co.companyName, j.department, " +
           "j.jobType, j.salaryMin, j.salaryMax, j.salaryUnit, j.workCity, j.workAddress, " +
           "j.workDistrict, j.workLatitude, j.workLongitude, j.description, j.requirements, " +
           "j.benefits, j.contactInfo, j.educationRequirement, j.experienceRequirement, " +
           "j.recruitNumber, j.urgentLevel, j.priorityLevel, j.isRecommended, j.recommendReason, " +
           "j.keywords, j.status, j.viewCount, j.applyCount, j.favoriteCount, j.publishTime, " +
           "j.expireTime, j.createTime, j.updateTime) " +
           "FROM Job j " +
           "LEFT JOIN Company co ON j.companyId = co.id " +
           "LEFT JOIN JobCategory c ON j.categoryId = c.id " +
           "WHERE j.workCity = :workCity AND j.status = :status")
    Page<JobWithCompanyDTO> findJobsWithCompanyByWorkCity(@Param("workCity") String workCity, 
                                                         @Param("status") Integer status, 
                                                         Pageable pageable);

    /**
     * 根据岗位类型查询岗位与企业信息（投影查询）
     */
    @Query("SELECT new com.shera.framework.employment.employment.dto.JobWithCompanyDTO(" +
           "j.id, j.title, j.categoryId, c.name, j.companyId, co.companyName, j.department, " +
           "j.jobType, j.salaryMin, j.salaryMax, j.salaryUnit, j.workCity, j.workAddress, " +
           "j.workDistrict, j.workLatitude, j.workLongitude, j.description, j.requirements, " +
           "j.benefits, j.contactInfo, j.educationRequirement, j.experienceRequirement, " +
           "j.recruitNumber, j.urgentLevel, j.priorityLevel, j.isRecommended, j.recommendReason, " +
           "j.keywords, j.status, j.viewCount, j.applyCount, j.favoriteCount, j.publishTime, " +
           "j.expireTime, j.createTime, j.updateTime) " +
           "FROM Job j " +
           "LEFT JOIN Company co ON j.companyId = co.id " +
           "LEFT JOIN JobCategory c ON j.categoryId = c.id " +
           "WHERE j.jobType = :jobType AND j.status = :status")
    Page<JobWithCompanyDTO> findJobsWithCompanyByJobType(@Param("jobType") Integer jobType, 
                                                        @Param("status") Integer status, 
                                                        Pageable pageable);
}
