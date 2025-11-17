package com.shera.framework.employment.employment.modules.system.repository;

import com.shera.framework.employment.employment.modules.system.dto.SystemConfigQueryDTO;
import com.shera.framework.employment.employment.modules.system.entity.SystemConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统配置自定义数据访问实现类
 */
@Repository
public class SystemConfigRepositoryCustomImpl implements SystemConfigRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SystemConfig> findConfigs(SystemConfigQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SystemConfig> query = cb.createQuery(SystemConfig.class);
        Root<SystemConfig> root = query.from(SystemConfig.class);

        List<Predicate> predicates = buildPredicates(queryDTO, cb, root);
        query.where(predicates.toArray(new Predicate[0]));

        // 添加排序
        if (StringUtils.hasText(queryDTO.getSortField())) {
            Path<Object> sortPath = root.get(queryDTO.getSortField());
            if ("desc".equalsIgnoreCase(queryDTO.getSortDirection())) {
                query.orderBy(cb.desc(sortPath));
            } else {
                query.orderBy(cb.asc(sortPath));
            }
        }

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Page<SystemConfig> findConfigs(SystemConfigQueryDTO queryDTO, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        // 查询数据
        CriteriaQuery<SystemConfig> dataQuery = cb.createQuery(SystemConfig.class);
        Root<SystemConfig> root = dataQuery.from(SystemConfig.class);
        
        List<Predicate> predicates = buildPredicates(queryDTO, cb, root);
        dataQuery.where(predicates.toArray(new Predicate[0]));
        
        // 添加排序
        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                Path<Object> path = root.get(order.getProperty());
                if (order.isAscending()) {
                    orders.add(cb.asc(path));
                } else {
                    orders.add(cb.desc(path));
                }
            });
            dataQuery.orderBy(orders);
        }

        TypedQuery<SystemConfig> typedQuery = entityManager.createQuery(dataQuery);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        
        List<SystemConfig> content = typedQuery.getResultList();

        // 查询总数
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<SystemConfig> countRoot = countQuery.from(SystemConfig.class);
        countQuery.select(cb.count(countRoot));
        
        List<Predicate> countPredicates = buildPredicates(queryDTO, cb, countRoot);
        countQuery.where(countPredicates.toArray(new Predicate[0]));
        
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Long countConfigs(SystemConfigQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<SystemConfig> root = query.from(SystemConfig.class);
        
        query.select(cb.count(root));
        
        List<Predicate> predicates = buildPredicates(queryDTO, cb, root);
        query.where(predicates.toArray(new Predicate[0]));
        
        return entityManager.createQuery(query).getSingleResult();
    }

    /**
     * 构建查询条件
     */
    private List<Predicate> buildPredicates(SystemConfigQueryDTO queryDTO, CriteriaBuilder cb, Root<SystemConfig> root) {
        List<Predicate> predicates = new ArrayList<>();

        // 配置键精确匹配
        if (StringUtils.hasText(queryDTO.getConfigKey())) {
            predicates.add(cb.equal(root.get("configKey"), queryDTO.getConfigKey()));
        }

        // 配置键前缀
        if (StringUtils.hasText(queryDTO.getConfigKeyPrefix())) {
            predicates.add(cb.like(root.get("configKey"), queryDTO.getConfigKeyPrefix() + "%"));
        }

        // 配置键后缀
        if (StringUtils.hasText(queryDTO.getConfigKeySuffix())) {
            predicates.add(cb.like(root.get("configKey"), "%" + queryDTO.getConfigKeySuffix()));
        }

        // 配置键包含关键词
        if (StringUtils.hasText(queryDTO.getConfigKeyKeyword())) {
            predicates.add(cb.like(root.get("configKey"), "%" + queryDTO.getConfigKeyKeyword() + "%"));
        }

        // 配置值包含关键词
        if (StringUtils.hasText(queryDTO.getConfigValueKeyword())) {
            predicates.add(cb.like(root.get("configValue"), "%" + queryDTO.getConfigValueKeyword() + "%"));
        }

        // 描述包含关键词
        if (StringUtils.hasText(queryDTO.getDescriptionKeyword())) {
            predicates.add(cb.like(root.get("description"), "%" + queryDTO.getDescriptionKeyword() + "%"));
        }

        // 配置分组
        if (StringUtils.hasText(queryDTO.getConfigGroup())) {
            predicates.add(cb.equal(root.get("configGroup"), queryDTO.getConfigGroup()));
        }

        // 配置值类型
        if (StringUtils.hasText(queryDTO.getValueType())) {
            predicates.add(cb.equal(root.get("valueType"), queryDTO.getValueType()));
        }

        // 是否可编辑
        if (queryDTO.getEditable() != null) {
            predicates.add(cb.equal(root.get("editable"), queryDTO.getEditable()));
        }

        // 是否为系统配置
        if (queryDTO.getSystemConfig() != null) {
            predicates.add(cb.equal(root.get("systemConfig"), queryDTO.getSystemConfig()));
        }

        // 创建时间范围
        if (StringUtils.hasText(queryDTO.getCreateTimeStart())) {
            LocalDateTime startTime = parseDateTime(queryDTO.getCreateTimeStart());
            predicates.add(cb.greaterThanOrEqualTo(root.get("createTime"), startTime));
        }
        if (StringUtils.hasText(queryDTO.getCreateTimeEnd())) {
            LocalDateTime endTime = parseDateTime(queryDTO.getCreateTimeEnd());
            predicates.add(cb.lessThanOrEqualTo(root.get("createTime"), endTime));
        }

        // 更新时间范围
        if (StringUtils.hasText(queryDTO.getUpdateTimeStart())) {
            LocalDateTime startTime = parseDateTime(queryDTO.getUpdateTimeStart());
            predicates.add(cb.greaterThanOrEqualTo(root.get("updateTime"), startTime));
        }
        if (StringUtils.hasText(queryDTO.getUpdateTimeEnd())) {
            LocalDateTime endTime = parseDateTime(queryDTO.getUpdateTimeEnd());
            predicates.add(cb.lessThanOrEqualTo(root.get("updateTime"), endTime));
        }

        return predicates;
    }

    /**
     * 解析日期时间字符串
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            if (dateTimeStr.length() == 10) { // yyyy-MM-dd
                return LocalDateTime.parse(dateTimeStr + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } else if (dateTimeStr.length() == 16) { // yyyy-MM-dd HH:mm
                return LocalDateTime.parse(dateTimeStr.replace(" ", "T") + ":00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } else { // yyyy-MM-dd HH:mm:ss
                return LocalDateTime.parse(dateTimeStr.replace(" ", "T"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date time format: " + dateTimeStr, e);
        }
    }
}
