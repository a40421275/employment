package com.shera.framework.employment.employment.modules.system.service;

import com.shera.framework.employment.employment.modules.system.dto.SystemConfigDTO;
import com.shera.framework.employment.employment.modules.system.dto.SystemConfigQueryDTO;
import com.shera.framework.employment.employment.modules.system.entity.SystemConfig;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 系统配置服务接口
 */
public interface SystemConfigService {
    
    /**
     * 创建系统配置
     */
    SystemConfig createConfig(SystemConfigDTO configDTO);
    
    /**
     * 更新系统配置
     */
    SystemConfig updateConfig(Long id, SystemConfigDTO configDTO);
    
    /**
     * 根据配置键更新配置值
     */
    SystemConfig updateConfigByKey(String configKey, String configValue);
    
    /**
     * 删除系统配置
     */
    void deleteConfig(Long id);
    
    /**
     * 根据配置键删除配置
     */
    void deleteConfigByKey(String configKey);
    
    /**
     * 根据ID获取系统配置
     */
    SystemConfig getConfigById(Long id);
    
    /**
     * 根据配置键获取系统配置
     */
    Optional<SystemConfig> getConfigByKey(String configKey);
    
    /**
     * 获取所有系统配置
     */
    List<SystemConfig> getAllConfigs();
    
    /**
     * 根据配置键列表获取系统配置
     */
    List<SystemConfig> getConfigsByKeys(List<String> configKeys);
    
    /**
     * 获取所有配置键值对
     */
    Map<String, String> getAllKeyValues();
    
    /**
     * 根据配置键前缀获取配置
     */
    List<SystemConfig> getConfigsByKeyPrefix(String prefix);
    
    /**
     * 根据配置键后缀获取配置
     */
    List<SystemConfig> getConfigsByKeySuffix(String suffix);
    
    /**
     * 根据配置键包含查询配置
     */
    List<SystemConfig> getConfigsByKeyContaining(String keyword);
    
    /**
     * 根据描述模糊查询配置
     */
    List<SystemConfig> getConfigsByDescription(String description);
    
    /**
     * 批量创建或更新配置
     */
    List<SystemConfig> batchCreateOrUpdate(List<SystemConfigDTO> configDTOs);
    
    /**
     * 批量删除配置
     */
    void batchDelete(List<Long> ids);
    
    /**
     * 批量删除配置（根据配置键）
     */
    void batchDeleteByKeys(List<String> configKeys);
    
    /**
     * 检查配置键是否存在
     */
    boolean existsByConfigKey(String configKey);
    
    /**
     * 获取配置数量
     */
    Long countConfigs();
    
    /**
     * 获取配置分组统计
     */
    Map<String, Long> countByGroup();
    
    /**
     * 获取配置值类型统计
     */
    Map<String, Long> countByValueType();
    
    /**
     * 获取最近更新的配置
     */
    List<SystemConfig> getRecentlyUpdatedConfigs(int limit);
    
    /**
     * 获取最近创建的配置
     */
    List<SystemConfig> getRecentlyCreatedConfigs(int limit);
    
    
    /**
     * 获取配置值（字符串类型）
     */
    String getStringValue(String configKey, String defaultValue);
    
    /**
     * 获取配置值（整数类型）
     */
    Integer getIntegerValue(String configKey, Integer defaultValue);
    
    /**
     * 获取配置值（布尔类型）
     */
    Boolean getBooleanValue(String configKey, Boolean defaultValue);
    
    /**
     * 获取配置值（小数类型）
     */
    Double getDoubleValue(String configKey, Double defaultValue);
    
    /**
     * 获取配置值（JSON类型）
     */
    String getJsonValue(String configKey, String defaultValue);
    
    /**
     * 获取网站配置
     */
    Map<String, String> getSiteConfigs();
    
    /**
     * 获取安全配置
     */
    Map<String, String> getSecurityConfigs();
    
    /**
     * 获取邮件配置
     */
    Map<String, String> getEmailConfigs();
    
    /**
     * 获取短信配置
     */
    Map<String, String> getSmsConfigs();
    
    /**
     * 获取文件配置
     */
    Map<String, String> getFileConfigs();
    
    /**
     * 获取通知配置
     */
    Map<String, String> getNotificationConfigs();
    
    /**
     * 获取分析配置
     */
    Map<String, String> getAnalyticsConfigs();
    
    /**
     * 获取缓存配置
     */
    Map<String, String> getCacheConfigs();
    
    /**
     * 获取备份配置
     */
    Map<String, String> getBackupConfigs();
    
    /**
     * 获取业务配置
     */
    Map<String, String> getBusinessConfigs();
    
    /**
     * 统一查询系统配置
     * @param queryDTO 查询条件
     * @return 配置列表
     */
    List<SystemConfig> queryConfigs(SystemConfigQueryDTO queryDTO);
    
    /**
     * 统一查询系统配置（分页）
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<SystemConfig> queryConfigsPage(SystemConfigQueryDTO queryDTO);
}
