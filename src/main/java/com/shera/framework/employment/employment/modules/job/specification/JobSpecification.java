package com.shera.framework.employment.employment.modules.job.specification;

import com.shera.framework.employment.employment.modules.job.dto.JobQueryDTO;
import com.shera.framework.employment.employment.modules.job.entity.Job;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 岗位查询规格构建器
 */
public class JobSpecification {

    /**
     * 构建动态查询条件
     */
    public static Specification<Job> buildQuerySpecification(JobQueryDTO queryDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 关键词搜索条件
            if (queryDTO.getKeyword() != null && !queryDTO.getKeyword().trim().isEmpty()) {
                String keyword = "%" + queryDTO.getKeyword().trim() + "%";
                Predicate titlePredicate = criteriaBuilder.like(root.get("title"), keyword);
                Predicate descriptionPredicate = criteriaBuilder.like(root.get("description"), keyword);
                Predicate requirementsPredicate = criteriaBuilder.like(root.get("requirements"), keyword);
                predicates.add(criteriaBuilder.or(titlePredicate, descriptionPredicate, requirementsPredicate));
            }

            // 分类ID条件
            if (queryDTO.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), queryDTO.getCategoryId()));
            }

            // 工作城市条件
            if (queryDTO.getWorkCity() != null && !queryDTO.getWorkCity().trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("workCity"), queryDTO.getWorkCity().trim()));
            }

            // 岗位类型条件
            if (queryDTO.getJobType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("jobType"), queryDTO.getJobType()));
            }

            // 最低薪资条件
            if (queryDTO.getMinSalary() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("salaryMin"), queryDTO.getMinSalary()));
            }

            // 最高薪资条件
            if (queryDTO.getMaxSalary() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("salaryMax"), queryDTO.getMaxSalary()));
            }

            // 状态条件（如果未指定状态，默认查询已发布的岗位）
            Integer status = queryDTO.getStatus() != null ? queryDTO.getStatus() : 1;
            predicates.add(criteriaBuilder.equal(root.get("status"), status));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
