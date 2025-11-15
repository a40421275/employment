package com.shera.framework.employment.employment.repository;

import com.shera.framework.employment.employment.dto.JobQueryDTO;
import com.shera.framework.employment.employment.dto.JobWithCompanyDTO;
import com.shera.framework.employment.employment.entity.Company;
import com.shera.framework.employment.employment.entity.Job;
import com.shera.framework.employment.employment.entity.JobCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 岗位自定义Repository实现类
 */
@Repository
public class JobRepositoryCustomImpl implements JobRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<JobWithCompanyDTO> findJobsWithCompanyByDynamicQuery(JobQueryDTO queryDTO, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        // 创建查询
        CriteriaQuery<JobWithCompanyDTO> query = cb.createQuery(JobWithCompanyDTO.class);
        Root<Job> jobRoot = query.from(Job.class);
        
        // 关联表
        Join<Job, Company> companyJoin = jobRoot.join("company", JoinType.LEFT);
        Join<Job, JobCategory> categoryJoin = jobRoot.join("category", JoinType.LEFT);
        
        // 构建查询条件
        List<Predicate> predicates = buildPredicates(queryDTO, cb, jobRoot, companyJoin, categoryJoin);
        
        // 构建查询
        query.where(predicates.toArray(new Predicate[0]));
        
        // 构建投影（JobWithCompanyDTO构造函数）
        query.multiselect(
            jobRoot.get("id"),
            jobRoot.get("title"),
            jobRoot.get("categoryId"),
            categoryJoin.get("name"),
            jobRoot.get("companyId"),
            companyJoin.get("companyName"),
            jobRoot.get("department"),
            jobRoot.get("jobType"),
            jobRoot.get("salaryMin"),
            jobRoot.get("salaryMax"),
            jobRoot.get("salaryUnit"),
            jobRoot.get("workCity"),
            jobRoot.get("workAddress"),
            jobRoot.get("workDistrict"),
            jobRoot.get("workLatitude"),
            jobRoot.get("workLongitude"),
            jobRoot.get("description"),
            jobRoot.get("requirements"),
            jobRoot.get("benefits"),
            jobRoot.get("contactInfo"),
            jobRoot.get("educationRequirement"),
            jobRoot.get("experienceRequirement"),
            jobRoot.get("recruitNumber"),
            jobRoot.get("urgentLevel"),
            jobRoot.get("priorityLevel"),
            jobRoot.get("isRecommended"),
            jobRoot.get("recommendReason"),
            jobRoot.get("keywords"),
            jobRoot.get("status"),
            jobRoot.get("viewCount"),
            jobRoot.get("applyCount"),
            jobRoot.get("favoriteCount"),
            jobRoot.get("publishTime"),
            jobRoot.get("expireTime"),
            jobRoot.get("createTime"),
            jobRoot.get("updateTime")
        );
        
        // 排序
        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                if (order.isAscending()) {
                    orders.add(cb.asc(jobRoot.get(order.getProperty())));
                } else {
                    orders.add(cb.desc(jobRoot.get(order.getProperty())));
                }
            });
            query.orderBy(orders);
        }
        
        // 执行查询
        TypedQuery<JobWithCompanyDTO> typedQuery = entityManager.createQuery(query);
        
        // 分页
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        
        List<JobWithCompanyDTO> resultList = typedQuery.getResultList();
        
        // 获取总数
        Long total = getTotalCount(queryDTO, cb);
        
        return new PageImpl<>(resultList, pageable, total);
    }
    
    /**
     * 构建查询条件
     */
    private List<Predicate> buildPredicates(JobQueryDTO queryDTO, CriteriaBuilder cb, 
                                          Root<Job> jobRoot, Join<Job, Company> companyJoin, 
                                          Join<Job, JobCategory> categoryJoin) {
        List<Predicate> predicates = new ArrayList<>();
        
        // 关键词搜索条件
        if (queryDTO.getKeyword() != null && !queryDTO.getKeyword().trim().isEmpty()) {
            String keyword = "%" + queryDTO.getKeyword().trim() + "%";
            Predicate titlePredicate = cb.like(jobRoot.get("title"), keyword);
            Predicate descriptionPredicate = cb.like(jobRoot.get("description"), keyword);
            Predicate requirementsPredicate = cb.like(jobRoot.get("requirements"), keyword);
            predicates.add(cb.or(titlePredicate, descriptionPredicate, requirementsPredicate));
        }
        
        // 分类ID条件
        if (queryDTO.getCategoryId() != null) {
            predicates.add(cb.equal(jobRoot.get("categoryId"), queryDTO.getCategoryId()));
        }
        
        // 工作城市条件
        if (queryDTO.getWorkCity() != null && !queryDTO.getWorkCity().trim().isEmpty()) {
            predicates.add(cb.equal(jobRoot.get("workCity"), queryDTO.getWorkCity().trim()));
        }
        
        // 岗位类型条件
        if (queryDTO.getJobType() != null) {
            predicates.add(cb.equal(jobRoot.get("jobType"), queryDTO.getJobType()));
        }
        
        // 最低薪资条件
        if (queryDTO.getMinSalary() != null) {
            predicates.add(cb.greaterThanOrEqualTo(jobRoot.get("salaryMin"), queryDTO.getMinSalary()));
        }
        
        // 最高薪资条件
        if (queryDTO.getMaxSalary() != null) {
            predicates.add(cb.lessThanOrEqualTo(jobRoot.get("salaryMax"), queryDTO.getMaxSalary()));
        }
        
        // 状态条件（只有明确指定状态时才添加条件）
        if (queryDTO.getStatus() != null) {
            predicates.add(cb.equal(jobRoot.get("status"), queryDTO.getStatus()));
        }
        
        return predicates;
    }
    
    /**
     * 获取总数
     */
    private Long getTotalCount(JobQueryDTO queryDTO, CriteriaBuilder cb) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Job> jobRoot = countQuery.from(Job.class);
        
        // 关联表
        Join<Job, Company> companyJoin = jobRoot.join("company", JoinType.LEFT);
        Join<Job, JobCategory> categoryJoin = jobRoot.join("category", JoinType.LEFT);
        
        // 构建查询条件
        List<Predicate> predicates = buildPredicates(queryDTO, cb, jobRoot, companyJoin, categoryJoin);
        
        countQuery.select(cb.count(jobRoot));
        countQuery.where(predicates.toArray(new Predicate[0]));
        
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
