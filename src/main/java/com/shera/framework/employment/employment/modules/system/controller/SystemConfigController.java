package com.shera.framework.employment.employment.modules.system.controller;

import com.shera.framework.employment.employment.modules.system.dto.SystemConfigDTO;
import com.shera.framework.employment.employment.modules.system.dto.SystemConfigQueryDTO;
import com.shera.framework.employment.employment.modules.system.entity.SystemConfig;
import com.shera.framework.employment.employment.modules.system.service.SystemConfigService;
import com.shera.framework.employment.employment.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 系统配置控制器
 */
@RestController
@RequestMapping("/api/system-configs")
@RequiredArgsConstructor
public class SystemConfigController {
    
    private final SystemConfigService systemConfigService;
    
    /**
     * 创建系统配置
     */
    @PostMapping
    public ResponseEntity<?> createConfig(@RequestBody SystemConfigDTO configDTO) {
        try {
            SystemConfig config = systemConfigService.createConfig(configDTO);
            return ResponseUtil.success("创建成功", config);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 更新系统配置
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateConfig(@PathVariable Long id, @RequestBody SystemConfigDTO configDTO) {
        try {
            SystemConfig config = systemConfigService.updateConfig(id, configDTO);
            return ResponseUtil.success("更新成功", config);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 根据配置键更新配置值
     */
    @PutMapping("/key/{configKey}")
    public ResponseEntity<?> updateConfigByKey(@PathVariable String configKey, @RequestBody Map<String, String> request) {
        try {
            String configValue = request.get("configValue");
            SystemConfig config = systemConfigService.updateConfigByKey(configKey, configValue);
            return ResponseUtil.success("更新成功", config);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 删除系统配置
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConfig(@PathVariable Long id) {
        try {
            systemConfigService.deleteConfig(id);
            return ResponseUtil.success("删除成功");
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 根据配置键删除配置
     */
    @DeleteMapping("/key/{configKey}")
    public ResponseEntity<?> deleteConfigByKey(@PathVariable String configKey) {
        try {
            systemConfigService.deleteConfigByKey(configKey);
            return ResponseUtil.success("删除成功");
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 根据ID获取系统配置
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getConfigById(@PathVariable Long id) {
        try {
            SystemConfig config = systemConfigService.getConfigById(id);
            return ResponseUtil.success("获取成功", config);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 根据配置键获取系统配置
     */
    @GetMapping("/key/{configKey}")
    public ResponseEntity<?> getConfigByKey(@PathVariable String configKey) {
        try {
            Optional<SystemConfig> config = systemConfigService.getConfigByKey(configKey);
            if (config.isPresent()) {
                return ResponseUtil.success("获取成功", config.get());
            } else {
                return ResponseUtil.error("配置不存在");
            }
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取所有系统配置
     */
    @GetMapping
    public ResponseEntity<?> getAllConfigs() {
        try {
            List<SystemConfig> configs = systemConfigService.getAllConfigs();
            return ResponseUtil.success("获取成功", configs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 根据配置键列表获取系统配置
     */
    @PostMapping("/batch")
    public ResponseEntity<?> getConfigsByKeys(@RequestBody List<String> configKeys) {
        try {
            List<SystemConfig> configs = systemConfigService.getConfigsByKeys(configKeys);
            return ResponseUtil.success("获取成功", configs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取所有配置键值对
     */
    @GetMapping("/key-values")
    public ResponseEntity<?> getAllKeyValues() {
        try {
            Map<String, String> keyValues = systemConfigService.getAllKeyValues();
            return ResponseUtil.success("获取成功", keyValues);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 根据配置键前缀获取配置
     */
    @GetMapping("/prefix/{prefix}")
    public ResponseEntity<?> getConfigsByKeyPrefix(@PathVariable String prefix) {
        try {
            List<SystemConfig> configs = systemConfigService.getConfigsByKeyPrefix(prefix);
            return ResponseUtil.success("获取成功", configs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 根据配置键后缀获取配置
     */
    @GetMapping("/suffix/{suffix}")
    public ResponseEntity<?> getConfigsByKeySuffix(@PathVariable String suffix) {
        try {
            List<SystemConfig> configs = systemConfigService.getConfigsByKeySuffix(suffix);
            return ResponseUtil.success("获取成功", configs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 根据配置键包含查询配置
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> getConfigsByKeyContaining(@PathVariable String keyword) {
        try {
            List<SystemConfig> configs = systemConfigService.getConfigsByKeyContaining(keyword);
            return ResponseUtil.success("获取成功", configs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 根据描述模糊查询配置
     */
    @GetMapping("/description/{description}")
    public ResponseEntity<?> getConfigsByDescription(@PathVariable String description) {
        try {
            List<SystemConfig> configs = systemConfigService.getConfigsByDescription(description);
            return ResponseUtil.success("获取成功", configs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 批量创建或更新配置
     */
    @PostMapping("/batch-create-update")
    public ResponseEntity<?> batchCreateOrUpdate(@RequestBody List<SystemConfigDTO> configDTOs) {
        try {
            List<SystemConfig> configs = systemConfigService.batchCreateOrUpdate(configDTOs);
            return ResponseUtil.success("批量操作成功", configs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 批量删除配置
     */
    @DeleteMapping("/batch")
    public ResponseEntity<?> batchDelete(@RequestBody List<Long> ids) {
        try {
            systemConfigService.batchDelete(ids);
            return ResponseUtil.success("批量删除成功");
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 批量删除配置（根据配置键）
     */
    @DeleteMapping("/batch-keys")
    public ResponseEntity<?> batchDeleteByKeys(@RequestBody List<String> configKeys) {
        try {
            systemConfigService.batchDeleteByKeys(configKeys);
            return ResponseUtil.success("批量删除成功");
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 检查配置键是否存在
     */
    @GetMapping("/exists/{configKey}")
    public ResponseEntity<?> existsByConfigKey(@PathVariable String configKey) {
        try {
            boolean exists = systemConfigService.existsByConfigKey(configKey);
            return ResponseUtil.success("检查成功", exists);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取配置数量
     */
    @GetMapping("/count")
    public ResponseEntity<?> countConfigs() {
        try {
            Long count = systemConfigService.countConfigs();
            return ResponseUtil.success("获取成功", count);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取配置分组统计
     */
    @GetMapping("/stats/group")
    public ResponseEntity<?> countByGroup() {
        try {
            Map<String, Long> groupStats = systemConfigService.countByGroup();
            return ResponseUtil.success("获取成功", groupStats);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取配置值类型统计
     */
    @GetMapping("/stats/value-type")
    public ResponseEntity<?> countByValueType() {
        try {
            Map<String, Long> valueTypeStats = systemConfigService.countByValueType();
            return ResponseUtil.success("获取成功", valueTypeStats);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取最近更新的配置
     */
    @GetMapping("/recently-updated")
    public ResponseEntity<?> getRecentlyUpdatedConfigs(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<SystemConfig> configs = systemConfigService.getRecentlyUpdatedConfigs(limit);
            return ResponseUtil.success("获取成功", configs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取最近创建的配置
     */
    @GetMapping("/recently-created")
    public ResponseEntity<?> getRecentlyCreatedConfigs(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<SystemConfig> configs = systemConfigService.getRecentlyCreatedConfigs(limit);
            return ResponseUtil.success("获取成功", configs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    
    /**
     * 获取网站配置
     */
    @GetMapping("/site")
    public ResponseEntity<?> getSiteConfigs() {
        try {
            Map<String, String> siteConfigs = systemConfigService.getSiteConfigs();
            return ResponseUtil.success("获取成功", siteConfigs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取安全配置
     */
    @GetMapping("/security")
    public ResponseEntity<?> getSecurityConfigs() {
        try {
            Map<String, String> securityConfigs = systemConfigService.getSecurityConfigs();
            return ResponseUtil.success("获取成功", securityConfigs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取邮件配置
     */
    @GetMapping("/email")
    public ResponseEntity<?> getEmailConfigs() {
        try {
            Map<String, String> emailConfigs = systemConfigService.getEmailConfigs();
            return ResponseUtil.success("获取成功", emailConfigs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取短信配置
     */
    @GetMapping("/sms")
    public ResponseEntity<?> getSmsConfigs() {
        try {
            Map<String, String> smsConfigs = systemConfigService.getSmsConfigs();
            return ResponseUtil.success("获取成功", smsConfigs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取文件配置
     */
    @GetMapping("/file")
    public ResponseEntity<?> getFileConfigs() {
        try {
            Map<String, String> fileConfigs = systemConfigService.getFileConfigs();
            return ResponseUtil.success("获取成功", fileConfigs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取通知配置
     */
    @GetMapping("/notification")
    public ResponseEntity<?> getNotificationConfigs() {
        try {
            Map<String, String> notificationConfigs = systemConfigService.getNotificationConfigs();
            return ResponseUtil.success("获取成功", notificationConfigs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取分析配置
     */
    @GetMapping("/analytics")
    public ResponseEntity<?> getAnalyticsConfigs() {
        try {
            Map<String, String> analyticsConfigs = systemConfigService.getAnalyticsConfigs();
            return ResponseUtil.success("获取成功", analyticsConfigs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取缓存配置
     */
    @GetMapping("/cache")
    public ResponseEntity<?> getCacheConfigs() {
        try {
            Map<String, String> cacheConfigs = systemConfigService.getCacheConfigs();
            return ResponseUtil.success("获取成功", cacheConfigs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取备份配置
     */
    @GetMapping("/backup")
    public ResponseEntity<?> getBackupConfigs() {
        try {
            Map<String, String> backupConfigs = systemConfigService.getBackupConfigs();
            return ResponseUtil.success("获取成功", backupConfigs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取业务配置
     */
    @GetMapping("/business")
    public ResponseEntity<?> getBusinessConfigs() {
        try {
            Map<String, String> businessConfigs = systemConfigService.getBusinessConfigs();
            return ResponseUtil.success("获取成功", businessConfigs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取配置值（字符串类型）
     */
    @GetMapping("/value/string/{configKey}")
    public ResponseEntity<?> getStringValue(@PathVariable String configKey, @RequestParam(defaultValue = "") String defaultValue) {
        try {
            String value = systemConfigService.getStringValue(configKey, defaultValue);
            return ResponseUtil.success("获取成功", value);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取配置值（整数类型）
     */
    @GetMapping("/value/integer/{configKey}")
    public ResponseEntity<?> getIntegerValue(@PathVariable String configKey, @RequestParam(defaultValue = "0") Integer defaultValue) {
        try {
            Integer value = systemConfigService.getIntegerValue(configKey, defaultValue);
            return ResponseUtil.success("获取成功", value);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取配置值（布尔类型）
     */
    @GetMapping("/value/boolean/{configKey}")
    public ResponseEntity<?> getBooleanValue(@PathVariable String configKey, @RequestParam(defaultValue = "false") Boolean defaultValue) {
        try {
            Boolean value = systemConfigService.getBooleanValue(configKey, defaultValue);
            return ResponseUtil.success("获取成功", value);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取配置值（小数类型）
     */
    @GetMapping("/value/double/{configKey}")
    public ResponseEntity<?> getDoubleValue(@PathVariable String configKey, @RequestParam(defaultValue = "0.0") Double defaultValue) {
        try {
            Double value = systemConfigService.getDoubleValue(configKey, defaultValue);
            return ResponseUtil.success("获取成功", value);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取配置值（JSON类型）
     */
    @GetMapping("/value/json/{configKey}")
    public ResponseEntity<?> getJsonValue(@PathVariable String configKey, @RequestParam(defaultValue = "{}") String defaultValue) {
        try {
            String value = systemConfigService.getJsonValue(configKey, defaultValue);
            return ResponseUtil.success("获取成功", value);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 统一查询系统配置（不分页）
     */
    @PostMapping("/query")
    public ResponseEntity<?> queryConfigs(@RequestBody SystemConfigQueryDTO queryDTO) {
        try {
            // 强制关闭分页，确保返回所有数据
            queryDTO.setEnablePagination(false);
            List<SystemConfig> configs = systemConfigService.queryConfigs(queryDTO);
            return ResponseUtil.success("查询成功", configs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 统一查询系统配置（分页）
     */
    @PostMapping("/query/page")
    public ResponseEntity<?> queryConfigsPage(@RequestBody SystemConfigQueryDTO queryDTO) {
        try {
            // 强制启用分页
            queryDTO.setEnablePagination(true);
            Page<SystemConfig> configs = systemConfigService.queryConfigsPage(queryDTO);
            return ResponseUtil.success("查询成功", configs);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 统一查询系统配置（GET方式，支持查询参数）
     */
    @GetMapping("/query")
    public ResponseEntity<?> queryConfigsByParams(
            @RequestParam(required = false) String configKey,
            @RequestParam(required = false) String configKeyPrefix,
            @RequestParam(required = false) String configKeySuffix,
            @RequestParam(required = false) String configKeyKeyword,
            @RequestParam(required = false) String configValueKeyword,
            @RequestParam(required = false) String descriptionKeyword,
            @RequestParam(required = false) String createTimeStart,
            @RequestParam(required = false) String createTimeEnd,
            @RequestParam(required = false) String updateTimeStart,
            @RequestParam(required = false) String updateTimeEnd,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "false") Boolean enablePagination,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        
        try {
            SystemConfigQueryDTO queryDTO = new SystemConfigQueryDTO();
            queryDTO.setConfigKey(configKey);
            queryDTO.setConfigKeyPrefix(configKeyPrefix);
            queryDTO.setConfigKeySuffix(configKeySuffix);
            queryDTO.setConfigKeyKeyword(configKeyKeyword);
            queryDTO.setConfigValueKeyword(configValueKeyword);
            queryDTO.setDescriptionKeyword(descriptionKeyword);
            queryDTO.setCreateTimeStart(createTimeStart);
            queryDTO.setCreateTimeEnd(createTimeEnd);
            queryDTO.setUpdateTimeStart(updateTimeStart);
            queryDTO.setUpdateTimeEnd(updateTimeEnd);
            queryDTO.setSortField(sortField);
            queryDTO.setSortDirection(sortDirection);
            queryDTO.setEnablePagination(enablePagination);
            queryDTO.setPage(page);
            queryDTO.setSize(size);
            
            if (enablePagination) {
                Page<SystemConfig> configs = systemConfigService.queryConfigsPage(queryDTO);
                return ResponseUtil.success("查询成功", configs);
            } else {
                List<SystemConfig> configs = systemConfigService.queryConfigs(queryDTO);
                return ResponseUtil.success("查询成功", configs);
            }
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
}
