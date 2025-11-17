package com.shera.framework.employment.employment.modules.system.service.impl;

import com.shera.framework.employment.employment.modules.system.dto.SystemConfigDTO;
import com.shera.framework.employment.employment.modules.system.dto.SystemConfigQueryDTO;
import com.shera.framework.employment.employment.modules.system.entity.SystemConfig;
import com.shera.framework.employment.employment.modules.system.repository.SystemConfigRepository;
import com.shera.framework.employment.employment.modules.system.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统配置服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {
    
    private final SystemConfigRepository systemConfigRepository;
    
    @Override
    @Transactional
    public SystemConfig createConfig(SystemConfigDTO configDTO) {
        // 检查配置键是否已存在
        if (systemConfigRepository.existsByConfigKey(configDTO.getConfigKey())) {
            throw new RuntimeException("配置键已存在: " + configDTO.getConfigKey());
        }
        
        SystemConfig config = new SystemConfig();
        copyConfigProperties(config, configDTO);
        
        return systemConfigRepository.save(config);
    }
    
    @Override
    @Transactional
    public SystemConfig updateConfig(Long id, SystemConfigDTO configDTO) {
        SystemConfig config = getConfigById(id);
        copyConfigProperties(config, configDTO);
        
        return systemConfigRepository.save(config);
    }
    
    @Override
    @Transactional
    public SystemConfig updateConfigByKey(String configKey, String configValue) {
        Optional<SystemConfig> configOpt = systemConfigRepository.findByConfigKey(configKey);
        if (configOpt.isPresent()) {
            SystemConfig config = configOpt.get();
            config.setConfigValue(configValue);
            return systemConfigRepository.save(config);
        } else {
            throw new RuntimeException("配置不存在: " + configKey);
        }
    }
    
    @Override
    @Transactional
    public void deleteConfig(Long id) {
        SystemConfig config = getConfigById(id);
        systemConfigRepository.delete(config);
    }
    
    @Override
    @Transactional
    public void deleteConfigByKey(String configKey) {
        systemConfigRepository.deleteByConfigKey(configKey);
    }
    
    @Override
    public SystemConfig getConfigById(Long id) {
        return systemConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("配置不存在: " + id));
    }
    
    @Override
    public Optional<SystemConfig> getConfigByKey(String configKey) {
        return systemConfigRepository.findByConfigKey(configKey);
    }
    
    @Override
    public List<SystemConfig> getAllConfigs() {
        return systemConfigRepository.findAll();
    }
    
    @Override
    public List<SystemConfig> getConfigsByKeys(List<String> configKeys) {
        return systemConfigRepository.findByConfigKeyIn(configKeys);
    }
    
    @Override
    public Map<String, String> getAllKeyValues() {
        List<Object[]> keyValues = systemConfigRepository.findAllKeyValues();
        return keyValues.stream()
                .collect(Collectors.toMap(
                    obj -> (String) obj[0],
                    obj -> (String) obj[1]
                ));
    }
    
    @Override
    public List<SystemConfig> getConfigsByKeyPrefix(String prefix) {
        return systemConfigRepository.findByConfigKeyStartingWith(prefix);
    }
    
    @Override
    public List<SystemConfig> getConfigsByKeySuffix(String suffix) {
        return systemConfigRepository.findByConfigKeyEndingWith(suffix);
    }
    
    @Override
    public List<SystemConfig> getConfigsByKeyContaining(String keyword) {
        return systemConfigRepository.findByConfigKeyContaining(keyword);
    }
    
    @Override
    public List<SystemConfig> getConfigsByDescription(String description) {
        return systemConfigRepository.findByDescriptionContaining(description);
    }
    
    @Override
    @Transactional
    public List<SystemConfig> batchCreateOrUpdate(List<SystemConfigDTO> configDTOs) {
        List<SystemConfig> results = new ArrayList<>();
        for (SystemConfigDTO configDTO : configDTOs) {
            try {
                Optional<SystemConfig> existingConfig = systemConfigRepository.findByConfigKey(configDTO.getConfigKey());
                if (existingConfig.isPresent()) {
                    // 更新现有配置
                    SystemConfig config = existingConfig.get();
                    copyConfigProperties(config, configDTO);
                    results.add(systemConfigRepository.save(config));
                } else {
                    // 创建新配置
                    SystemConfig config = new SystemConfig();
                    copyConfigProperties(config, configDTO);
                    results.add(systemConfigRepository.save(config));
                }
            } catch (Exception e) {
                log.error("批量处理配置失败: {}", configDTO.getConfigKey(), e);
            }
        }
        return results;
    }
    
    @Override
    @Transactional
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            try {
                systemConfigRepository.deleteById(id);
            } catch (Exception e) {
                log.error("删除配置失败: {}", id, e);
            }
        }
    }
    
    @Override
    @Transactional
    public void batchDeleteByKeys(List<String> configKeys) {
        systemConfigRepository.deleteByConfigKeyIn(configKeys);
    }
    
    @Override
    public boolean existsByConfigKey(String configKey) {
        return systemConfigRepository.existsByConfigKey(configKey);
    }
    
    @Override
    public Long countConfigs() {
        return systemConfigRepository.countAllConfigs();
    }
    
    @Override
    public Map<String, Long> countByGroup() {
        List<Object[]> groupCounts = systemConfigRepository.countByGroup();
        return groupCounts.stream()
                .collect(Collectors.toMap(
                    obj -> (String) obj[0],
                    obj -> (Long) obj[1]
                ));
    }
    
    @Override
    public Map<String, Long> countByValueType() {
        List<Object[]> valueTypeCounts = systemConfigRepository.countByValueType();
        return valueTypeCounts.stream()
                .collect(Collectors.toMap(
                    obj -> (String) obj[0],
                    obj -> (Long) obj[1]
                ));
    }
    
    @Override
    public List<SystemConfig> getRecentlyUpdatedConfigs(int limit) {
        List<SystemConfig> allConfigs = systemConfigRepository.findRecentlyUpdated();
        return allConfigs.stream().limit(limit).collect(Collectors.toList());
    }
    
    @Override
    public List<SystemConfig> getRecentlyCreatedConfigs(int limit) {
        List<SystemConfig> allConfigs = systemConfigRepository.findRecentlyCreated();
        return allConfigs.stream().limit(limit).collect(Collectors.toList());
    }
    
    
    @Override
    public String getStringValue(String configKey, String defaultValue) {
        return getConfigByKey(configKey)
                .map(SystemConfig::getConfigValue)
                .orElse(defaultValue);
    }
    
    @Override
    public Integer getIntegerValue(String configKey, Integer defaultValue) {
        try {
            return getConfigByKey(configKey)
                    .map(config -> Integer.parseInt(config.getConfigValue()))
                    .orElse(defaultValue);
        } catch (NumberFormatException e) {
            log.warn("配置值不是有效的整数: {}", configKey);
            return defaultValue;
        }
    }
    
    @Override
    public Boolean getBooleanValue(String configKey, Boolean defaultValue) {
        return getConfigByKey(configKey)
                .map(config -> Boolean.parseBoolean(config.getConfigValue()))
                .orElse(defaultValue);
    }
    
    @Override
    public Double getDoubleValue(String configKey, Double defaultValue) {
        try {
            return getConfigByKey(configKey)
                    .map(config -> Double.parseDouble(config.getConfigValue()))
                    .orElse(defaultValue);
        } catch (NumberFormatException e) {
            log.warn("配置值不是有效的小数: {}", configKey);
            return defaultValue;
        }
    }
    
    @Override
    public String getJsonValue(String configKey, String defaultValue) {
        return getConfigByKey(configKey)
                .map(SystemConfig::getConfigValue)
                .orElse(defaultValue);
    }
    
    @Override
    public Map<String, String> getSiteConfigs() {
        return getConfigsByKeyPrefix("site_").stream()
                .collect(Collectors.toMap(
                    SystemConfig::getConfigKey,
                    SystemConfig::getConfigValue
                ));
    }
    
    @Override
    public Map<String, String> getSecurityConfigs() {
        return getConfigsByKeyPrefix("security_").stream()
                .collect(Collectors.toMap(
                    SystemConfig::getConfigKey,
                    SystemConfig::getConfigValue
                ));
    }
    
    @Override
    public Map<String, String> getEmailConfigs() {
        return getConfigsByKeyPrefix("email_").stream()
                .collect(Collectors.toMap(
                    SystemConfig::getConfigKey,
                    SystemConfig::getConfigValue
                ));
    }
    
    @Override
    public Map<String, String> getSmsConfigs() {
        return getConfigsByKeyPrefix("sms_").stream()
                .collect(Collectors.toMap(
                    SystemConfig::getConfigKey,
                    SystemConfig::getConfigValue
                ));
    }
    
    @Override
    public Map<String, String> getFileConfigs() {
        return getConfigsByKeyPrefix("file_").stream()
                .collect(Collectors.toMap(
                    SystemConfig::getConfigKey,
                    SystemConfig::getConfigValue
                ));
    }
    
    @Override
    public Map<String, String> getNotificationConfigs() {
        return getConfigsByKeyPrefix("notification_").stream()
                .collect(Collectors.toMap(
                    SystemConfig::getConfigKey,
                    SystemConfig::getConfigValue
                ));
    }
    
    @Override
    public Map<String, String> getAnalyticsConfigs() {
        return getConfigsByKeyPrefix("analytics_").stream()
                .collect(Collectors.toMap(
                    SystemConfig::getConfigKey,
                    SystemConfig::getConfigValue
                ));
    }
    
    @Override
    public Map<String, String> getCacheConfigs() {
        return getConfigsByKeyPrefix("cache_").stream()
                .collect(Collectors.toMap(
                    SystemConfig::getConfigKey,
                    SystemConfig::getConfigValue
                ));
    }
    
    @Override
    public Map<String, String> getBackupConfigs() {
        return getConfigsByKeyPrefix("backup_").stream()
                .collect(Collectors.toMap(
                    SystemConfig::getConfigKey,
                    SystemConfig::getConfigValue
                ));
    }
    
    @Override
    public Map<String, String> getBusinessConfigs() {
        List<String> businessKeys = Arrays.asList(
            "resume_privacy_default", "job_expire_days", "match_threshold",
            "max_resume_count", "max_job_apply_count"
        );
        return getConfigsByKeys(businessKeys).stream()
                .collect(Collectors.toMap(
                    SystemConfig::getConfigKey,
                    SystemConfig::getConfigValue
                ));
    }
    
    /**
     * 复制配置属性
     */
    private void copyConfigProperties(SystemConfig config, SystemConfigDTO configDTO) {
        config.setConfigKey(configDTO.getConfigKey());
        config.setConfigValue(configDTO.getConfigValue());
        config.setDescription(configDTO.getDescription());
        config.setConfigGroup(configDTO.getConfigGroup());
        config.setValueType(configDTO.getValueType());
        config.setEditable(configDTO.getEditable());
        config.setSystemConfig(configDTO.getSystemConfig());
        config.setSortOrder(configDTO.getSortOrder());
        config.setValidationRule(configDTO.getValidationRule());
        config.setDefaultValue(configDTO.getDefaultValue());
        config.setOptions(configDTO.getOptions());
    }
    
    
    @Override
    public List<SystemConfig> queryConfigs(SystemConfigQueryDTO queryDTO) {
        // 如果没有查询条件，返回所有配置
        if (!queryDTO.hasQueryConditions()) {
            return getAllConfigs();
        }
        
        // 使用自定义查询方法
        return systemConfigRepository.findConfigs(queryDTO);
    }
    
    @Override
    public Page<SystemConfig> queryConfigsPage(SystemConfigQueryDTO queryDTO) {
        // 如果没有查询条件，返回所有配置的分页结果
        if (!queryDTO.hasQueryConditions()) {
            return systemConfigRepository.findAll(queryDTO.getPageable());
        }
        
        // 使用自定义查询方法
        return systemConfigRepository.findConfigs(queryDTO, queryDTO.getPageable());
    }
}
