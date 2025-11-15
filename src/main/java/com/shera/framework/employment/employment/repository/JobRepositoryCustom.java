package com.shera.framework.employment.employment.repository;

import com.shera.framework.employment.employment.dto.JobQueryDTO;
import com.shera.framework.employment.employment.dto.JobWithCompanyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 岗位自定义Repository接口
 */
public interface JobRepositoryCustom {
    
    /**
     * 动态查询岗位与企业信息
     */
    Page<JobWithCompanyDTO> findJobsWithCompanyByDynamicQuery(JobQueryDTO queryDTO, Pageable pageable);
}
