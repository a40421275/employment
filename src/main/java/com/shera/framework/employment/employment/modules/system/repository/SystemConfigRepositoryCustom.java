package com.shera.framework.employment.employment.modules.system.repository;

import com.shera.framework.employment.employment.modules.system.dto.SystemConfigQueryDTO;
import com.shera.framework.employment.employment.modules.system.entity.SystemConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 系统配置自定义数据访问接口
 */
public interface SystemConfigRepositoryCustom {
    
    /**
     * 统一查询系统配置
     * @param queryDTO 查询条件
     * @return 配置列表
     */
    List<SystemConfig> findConfigs(SystemConfigQueryDTO queryDTO);
    
    /**
     * 统一查询系统配置（分页）
     * @param queryDTO 查询条件
     * @param pageable 分页信息
     * @return 分页结果
     */
    Page<SystemConfig> findConfigs(SystemConfigQueryDTO queryDTO, Pageable pageable);
    
    /**
     * 统计查询结果数量
     * @param queryDTO 查询条件
     * @return 数量
     */
    Long countConfigs(SystemConfigQueryDTO queryDTO);
}
