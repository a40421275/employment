package com.shera.framework.employment.employment.repository;

import com.shera.framework.employment.employment.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 系统配置数据访问接口
 */
@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
    
    /**
     * 根据配置键查询配置
     */
    Optional<SystemConfig> findByConfigKey(String configKey);
    
    /**
     * 根据配置键列表查询配置
     */
    List<SystemConfig> findByConfigKeyIn(List<String> configKeys);
    
    /**
     * 检查配置键是否存在
     */
    boolean existsByConfigKey(String configKey);
    
    /**
     * 根据配置键删除配置
     */
    void deleteByConfigKey(String configKey);
    
    /**
     * 根据配置键列表删除配置
     */
    void deleteByConfigKeyIn(List<String> configKeys);
    
    /**
     * 根据描述模糊查询配置
     */
    List<SystemConfig> findByDescriptionContaining(String description);
    
    /**
     * 根据创建时间范围查询配置
     */
    List<SystemConfig> findByCreateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * 根据更新时间范围查询配置
     */
    List<SystemConfig> findByUpdateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * 查询所有配置键
     */
    @Query("SELECT sc.configKey FROM SystemConfig sc")
    List<String> findAllConfigKeys();
    
    /**
     * 查询配置键和配置值
     */
    @Query("SELECT sc.configKey, sc.configValue FROM SystemConfig sc")
    List<Object[]> findAllKeyValues();
    
    /**
     * 根据配置键前缀查询配置
     */
    @Query("SELECT sc FROM SystemConfig sc WHERE sc.configKey LIKE :prefix%")
    List<SystemConfig> findByConfigKeyStartingWith(@Param("prefix") String prefix);
    
    /**
     * 根据配置键后缀查询配置
     */
    @Query("SELECT sc FROM SystemConfig sc WHERE sc.configKey LIKE %:suffix")
    List<SystemConfig> findByConfigKeyEndingWith(@Param("suffix") String suffix);
    
    /**
     * 根据配置键包含查询配置
     */
    @Query("SELECT sc FROM SystemConfig sc WHERE sc.configKey LIKE %:keyword%")
    List<SystemConfig> findByConfigKeyContaining(@Param("keyword") String keyword);
    
    /**
     * 查询配置键和描述
     */
    @Query("SELECT sc.configKey, sc.description FROM SystemConfig sc")
    List<Object[]> findAllKeyDescriptions();
    
    /**
     * 查询最近更新的配置
     */
    @Query("SELECT sc FROM SystemConfig sc ORDER BY sc.updateTime DESC")
    List<SystemConfig> findRecentlyUpdated();
    
    /**
     * 查询最近创建的配置
     */
    @Query("SELECT sc FROM SystemConfig sc ORDER BY sc.createTime DESC")
    List<SystemConfig> findRecentlyCreated();
    
    /**
     * 统计配置数量
     */
    @Query("SELECT COUNT(sc) FROM SystemConfig sc")
    Long countAllConfigs();
    
    /**
     * 统计配置键前缀的数量
     */
    @Query("SELECT COUNT(sc) FROM SystemConfig sc WHERE sc.configKey LIKE :prefix%")
    Long countByConfigKeyStartingWith(@Param("prefix") String prefix);
    
    /**
     * 批量更新配置值
     */
    @Query("UPDATE SystemConfig sc SET sc.configValue = :configValue, sc.updateTime = :updateTime WHERE sc.configKey = :configKey")
    int updateConfigValue(@Param("configKey") String configKey, @Param("configValue") String configValue, @Param("updateTime") LocalDateTime updateTime);
    
    /**
     * 批量更新配置描述
     */
    @Query("UPDATE SystemConfig sc SET sc.description = :description, sc.updateTime = :updateTime WHERE sc.configKey = :configKey")
    int updateConfigDescription(@Param("configKey") String configKey, @Param("description") String description, @Param("updateTime") LocalDateTime updateTime);
    
    /**
     * 批量插入配置（如果不存在）
     */
    @Query(value = "INSERT IGNORE INTO system_config (config_key, config_value, description, create_time, update_time) VALUES (:configKey, :configValue, :description, :createTime, :updateTime)", nativeQuery = true)
    int insertIfNotExists(@Param("configKey") String configKey, @Param("configValue") String configValue, @Param("description") String description, @Param("createTime") LocalDateTime createTime, @Param("updateTime") LocalDateTime updateTime);
    
    /**
     * 批量更新配置值（如果存在）
     */
    @Query(value = "UPDATE system_config SET config_value = :configValue, update_time = :updateTime WHERE config_key = :configKey", nativeQuery = true)
    int updateIfExists(@Param("configKey") String configKey, @Param("configValue") String configValue, @Param("updateTime") LocalDateTime updateTime);
    
    /**
     * 查询配置分组统计
     */
    @Query("SELECT SUBSTRING_INDEX(sc.configKey, '_', 1) as groupName, COUNT(sc) FROM SystemConfig sc GROUP BY SUBSTRING_INDEX(sc.configKey, '_', 1) ORDER BY COUNT(sc) DESC")
    List<Object[]> countByGroup();
    
    /**
     * 查询配置值类型统计
     */
    @Query(value = "SELECT " +
           "CASE " +
           "WHEN config_value REGEXP '^[0-9]+$' THEN 'INTEGER' " +
           "WHEN config_value REGEXP '^[0-9]+\\.[0-9]+$' THEN 'DECIMAL' " +
           "WHEN config_value IN ('true', 'false') THEN 'BOOLEAN' " +
           "WHEN config_value LIKE '[%]' OR config_value LIKE '{%' THEN 'JSON' " +
           "ELSE 'STRING' " +
           "END as valueType, " +
           "COUNT(*) " +
           "FROM system_config " +
           "GROUP BY valueType", nativeQuery = true)
    List<Object[]> countByValueType();
}
