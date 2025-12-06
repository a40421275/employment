package com.shera.framework.employment.employment.modules.file.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储策略工厂
 * 负责管理和提供不同的存储策略实现
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StorageStrategyFactory {
    
    private final LocalStorageStrategy localStorageStrategy;
    private final H3CObjectStorageStrategy h3cObjectStorageStrategy;
    
    private final Map<String, StorageStrategy> strategyMap = new HashMap<>();
    
    /**
     * 初始化存储策略映射
     */
    private void initStrategyMap() {
        if (strategyMap.isEmpty()) {
            strategyMap.put("local", localStorageStrategy);
            strategyMap.put("h3c-object-storage", h3cObjectStorageStrategy);
            log.info("存储策略工厂初始化完成 - 可用策略: {}", strategyMap.keySet());
        }
    }
    
    /**
     * 根据存储类型获取存储策略
     * @param storageType 存储类型
     * @return 存储策略实例
     */
    public StorageStrategy getStorageStrategy(String storageType) {
        initStrategyMap();
        
        if (storageType == null || storageType.trim().isEmpty()) {
            log.warn("存储类型为空，使用默认本地存储策略");
            return localStorageStrategy;
        }
        
        StorageStrategy strategy = strategyMap.get(storageType);
        if (strategy == null) {
            log.warn("未找到存储类型 '{}' 的策略，使用默认本地存储策略", storageType);
            return localStorageStrategy;
        }
        
        log.debug("获取存储策略成功 - 存储类型: {}", storageType);
        return strategy;
    }
    
    /**
     * 获取默认存储策略（本地存储）
     * @return 默认存储策略
     */
    public StorageStrategy getDefaultStorageStrategy() {
        return localStorageStrategy;
    }
    
    /**
     * 获取H3C对象存储策略
     * @return H3C对象存储策略
     */
    public StorageStrategy getH3CObjectStorageStrategy() {
        return h3cObjectStorageStrategy;
    }
    
    /**
     * 获取所有可用的存储策略信息
     * @return 存储策略信息映射
     */
    public Map<String, Map<String, Object>> getAllStorageStrategiesInfo() {
        initStrategyMap();
        
        Map<String, Map<String, Object>> strategiesInfo = new HashMap<>();
        
        strategyMap.forEach((type, strategy) -> {
            Map<String, Object> info = new HashMap<>();
            info.put("storageType", strategy.getStorageType());
            info.put("connectionTest", strategy.testConnection());
            
            // 添加特定策略的额外信息
            if (strategy instanceof H3CObjectStorageStrategy) {
                H3CObjectStorageStrategy h3cStrategy = (H3CObjectStorageStrategy) strategy;
                info.put("config", h3cStrategy.getConfigInfo());
            } else if (strategy instanceof LocalStorageStrategy) {
                LocalStorageStrategy localStrategy = (LocalStorageStrategy) strategy;
                info.put("statistics", localStrategy.getStorageStatistics());
            }
            
            strategiesInfo.put(type, info);
        });
        
        return strategiesInfo;
    }
    
    /**
     * 测试所有存储策略的连接
     * @return 连接测试结果
     */
    public Map<String, Boolean> testAllStorageConnections() {
        initStrategyMap();
        
        Map<String, Boolean> testResults = new HashMap<>();
        
        strategyMap.forEach((type, strategy) -> {
            boolean connected = strategy.testConnection();
            testResults.put(type, connected);
            log.info("存储策略连接测试 - 类型: {}, 结果: {}", type, connected ? "成功" : "失败");
        });
        
        return testResults;
    }
    
    /**
     * 根据文件路径或业务逻辑自动选择存储策略
     * @param filePath 文件路径
     * @param businessType 业务类型
     * @return 合适的存储策略
     */
    public StorageStrategy selectStorageStrategy(String filePath, String businessType) {
        // 这里可以根据业务规则自动选择存储策略
        // 例如：某些业务类型使用H3C对象存储，其他使用本地存储
        
        if (businessType != null && businessType.startsWith("h3c_")) {
            log.debug("根据业务类型选择H3C对象存储策略 - 业务类型: {}", businessType);
            return h3cObjectStorageStrategy;
        }
        
        // 默认使用本地存储
        log.debug("使用默认本地存储策略");
        return localStorageStrategy;
    }
}
