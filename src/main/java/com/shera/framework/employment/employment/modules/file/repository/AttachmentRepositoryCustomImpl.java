package com.shera.framework.employment.employment.modules.file.repository;

import com.shera.framework.employment.employment.modules.file.dto.FileQueryDTO;
import com.shera.framework.employment.employment.modules.file.dto.AttachmentWithFileDTO;
import com.shera.framework.employment.employment.modules.file.entity.Attachment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 附件自定义数据访问实现类
 */
@Repository
@RequiredArgsConstructor
public class AttachmentRepositoryCustomImpl implements AttachmentRepositoryCustom {
    
    private final EntityManager entityManager;
    
    @Override
    public List<Attachment> findByQuery(FileQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Attachment> query = cb.createQuery(Attachment.class);
        Root<Attachment> root = query.from(Attachment.class);
        
        List<Predicate> predicates = buildPredicates(queryDTO, cb, root);
        query.where(predicates.toArray(new Predicate[0]));
        
        // 排序
        if (queryDTO.getSortField() != null && queryDTO.getSortDirection() != null) {
            String sortField = queryDTO.getSortField();
            if ("DESC".equalsIgnoreCase(queryDTO.getSortDirection())) {
                query.orderBy(cb.desc(root.get(sortField)));
            } else {
                query.orderBy(cb.asc(root.get(sortField)));
            }
        } else {
            query.orderBy(cb.desc(root.get("createTime")));
        }
        
        return entityManager.createQuery(query).getResultList();
    }
    
    @Override
    public Page<Attachment> findByQuery(FileQueryDTO queryDTO, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        // 查询数据
        CriteriaQuery<Attachment> query = cb.createQuery(Attachment.class);
        Root<Attachment> root = query.from(Attachment.class);
        
        List<Predicate> predicates = buildPredicates(queryDTO, cb, root);
        query.where(predicates.toArray(new Predicate[0]));
        
        // 排序
        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                if (order.isAscending()) {
                    orders.add(cb.asc(root.get(order.getProperty())));
                } else {
                    orders.add(cb.desc(root.get(order.getProperty())));
                }
            });
            query.orderBy(orders);
        } else {
            query.orderBy(cb.desc(root.get("createTime")));
        }
        
        TypedQuery<Attachment> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        
        List<Attachment> result = typedQuery.getResultList();
        
        // 查询总数
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Attachment> countRoot = countQuery.from(Attachment.class);
        countQuery.select(cb.count(countRoot));
        
        List<Predicate> countPredicates = buildPredicates(queryDTO, cb, countRoot);
        countQuery.where(countPredicates.toArray(new Predicate[0]));
        
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        
        return new PageImpl<>(result, pageable, total);
    }
    
    @Override
    public Page<AttachmentWithFileDTO> findAttachmentWithFileByQuery(FileQueryDTO queryDTO, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        // 查询数据 - 使用原生SQL查询直接返回DTO
        String sql = buildAttachmentWithFileQuery(queryDTO, pageable);
        String countSql = buildAttachmentWithFileCountQuery(queryDTO);
        
        // 执行查询
        List<Object[]> resultList = entityManager.createNativeQuery(sql)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        
        // 转换为DTO
        List<AttachmentWithFileDTO> content = resultList.stream()
                .map(this::mapToAttachmentWithFileDTO)
                .toList();
        
        // 查询总数
        Long total = ((Number) entityManager.createNativeQuery(countSql).getSingleResult()).longValue();
        
        return new PageImpl<>(content, pageable, total);
    }
    
    @Override
    public long countByQuery(FileQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Attachment> root = query.from(Attachment.class);
        
        query.select(cb.count(root));
        
        List<Predicate> predicates = buildPredicates(queryDTO, cb, root);
        query.where(predicates.toArray(new Predicate[0]));
        
        return entityManager.createQuery(query).getSingleResult();
    }
    
    private List<Predicate> buildPredicates(FileQueryDTO queryDTO, CriteriaBuilder cb, Root<Attachment> root) {
        List<Predicate> predicates = new ArrayList<>();
        
        // 用户ID筛选
        if (queryDTO.getUserId() != null) {
            predicates.add(cb.equal(root.get("userId"), queryDTO.getUserId()));
        }
        
        // 业务类型筛选
        if (queryDTO.getBusinessType() != null && !queryDTO.getBusinessType().trim().isEmpty()) {
            predicates.add(cb.equal(root.get("businessType"), queryDTO.getBusinessType()));
        }
        
        // 业务ID筛选
        if (queryDTO.getBusinessId() != null) {
            predicates.add(cb.equal(root.get("businessId"), queryDTO.getBusinessId()));
        }
        
        // 文件类型筛选（需要关联File表）
        if (queryDTO.getFileType() != null && !queryDTO.getFileType().trim().isEmpty()) {
            Join<Object, Object> fileJoin = root.join("file", JoinType.INNER);
            predicates.add(cb.equal(fileJoin.get("fileType"), queryDTO.getFileType()));
        }
        
        // 文件扩展名筛选（需要关联File表）
        if (queryDTO.getFileExtension() != null && !queryDTO.getFileExtension().trim().isEmpty()) {
            Join<Object, Object> fileJoin = root.join("file", JoinType.INNER);
            predicates.add(cb.equal(fileJoin.get("fileExtension"), queryDTO.getFileExtension()));
        }
        
        // 关键词搜索（文件名、描述、标签）
        if (queryDTO.getKeyword() != null && !queryDTO.getKeyword().trim().isEmpty()) {
            String keyword = "%" + queryDTO.getKeyword().trim() + "%";
            Join<Object, Object> fileJoin = root.join("file", JoinType.INNER);
            
            Predicate fileNamePredicate = cb.like(fileJoin.get("originalName"), keyword);
            Predicate descriptionPredicate = cb.like(root.get("description"), keyword);
            Predicate tagsPredicate = cb.like(root.get("tags"), keyword);
            
            predicates.add(cb.or(fileNamePredicate, descriptionPredicate, tagsPredicate));
        }
        
        // 状态筛选
        if (queryDTO.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), queryDTO.getStatus()));
        }
        
        // 是否公开筛选
        if (queryDTO.getIsPublic() != null) {
            predicates.add(cb.equal(root.get("isPublic"), queryDTO.getIsPublic()));
        }
        
        // 是否临时文件筛选
        if (queryDTO.getIsTemporary() != null) {
            predicates.add(cb.equal(root.get("isTemporary"), queryDTO.getIsTemporary()));
        }
        
        // 标签筛选
        if (queryDTO.getTags() != null && !queryDTO.getTags().trim().isEmpty()) {
            String tag = "%" + queryDTO.getTags().trim() + "%";
            predicates.add(cb.like(root.get("tags"), tag));
        }
        
        // 时间范围筛选
        if (queryDTO.getStartTime() != null && !queryDTO.getStartTime().trim().isEmpty()) {
            LocalDateTime startTime = LocalDateTime.parse(queryDTO.getStartTime(), 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            predicates.add(cb.greaterThanOrEqualTo(root.get("createTime"), startTime));
        }
        
        if (queryDTO.getEndTime() != null && !queryDTO.getEndTime().trim().isEmpty()) {
            LocalDateTime endTime = LocalDateTime.parse(queryDTO.getEndTime(), 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            predicates.add(cb.lessThanOrEqualTo(root.get("createTime"), endTime));
        }
        
        // 文件大小范围筛选（需要关联File表）
        if (queryDTO.getMinFileSize() != null || queryDTO.getMaxFileSize() != null) {
            Join<Object, Object> fileJoin = root.join("file", JoinType.INNER);
            
            if (queryDTO.getMinFileSize() != null) {
                predicates.add(cb.greaterThanOrEqualTo(fileJoin.get("fileSize"), queryDTO.getMinFileSize()));
            }
            
            if (queryDTO.getMaxFileSize() != null) {
                predicates.add(cb.lessThanOrEqualTo(fileJoin.get("fileSize"), queryDTO.getMaxFileSize()));
            }
        }
        
        return predicates;
    }
    
    /**
     * 构建附件和文件联合查询SQL
     */
    private String buildAttachmentWithFileQuery(FileQueryDTO queryDTO, Pageable pageable) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
           .append("a.id as attachmentId, a.user_id, a.business_type, a.business_id, a.description, a.tags, ")
           .append("a.is_public, a.is_temporary, a.expire_time, a.status, a.download_count, a.view_count, ")
           .append("a.create_time, a.update_time, ")
           .append("f.id as fileId, f.file_name, f.original_name, f.file_path, f.file_size, f.file_type, ")
           .append("f.mime_type, f.file_extension, f.file_hash, f.reference_count ")
           .append("FROM attachment a ")
           .append("INNER JOIN file f ON a.file_id = f.id ")
           .append("WHERE 1=1");
        
        // 添加查询条件
        appendQueryConditions(sql, queryDTO);
        
        // 添加排序
        appendOrderBy(sql, pageable);
        
        return sql.toString();
    }
    
    /**
     * 构建附件和文件联合查询计数SQL
     */
    private String buildAttachmentWithFileCountQuery(FileQueryDTO queryDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ")
           .append("FROM attachment a ")
           .append("INNER JOIN file f ON a.file_id = f.id ")
           .append("WHERE 1=1");
        
        // 添加查询条件
        appendQueryConditions(sql, queryDTO);
        
        return sql.toString();
    }
    
    /**
     * 添加查询条件
     */
    private void appendQueryConditions(StringBuilder sql, FileQueryDTO queryDTO) {
        // 用户ID筛选
        if (queryDTO.getUserId() != null) {
            sql.append(" AND a.user_id = ").append(queryDTO.getUserId());
        }
        
        // 业务类型筛选
        if (queryDTO.getBusinessType() != null && !queryDTO.getBusinessType().trim().isEmpty()) {
            sql.append(" AND a.business_type = '").append(queryDTO.getBusinessType()).append("'");
        }
        
        // 业务ID筛选
        if (queryDTO.getBusinessId() != null) {
            sql.append(" AND a.business_id = ").append(queryDTO.getBusinessId());
        }
        
        // 文件类型筛选
        if (queryDTO.getFileType() != null && !queryDTO.getFileType().trim().isEmpty()) {
            sql.append(" AND f.file_type = '").append(queryDTO.getFileType()).append("'");
        }
        
        // 文件扩展名筛选
        if (queryDTO.getFileExtension() != null && !queryDTO.getFileExtension().trim().isEmpty()) {
            sql.append(" AND f.file_extension = '").append(queryDTO.getFileExtension()).append("'");
        }
        
        // 关键词搜索（文件名、描述、标签）
        if (queryDTO.getKeyword() != null && !queryDTO.getKeyword().trim().isEmpty()) {
            String keyword = queryDTO.getKeyword().trim();
            sql.append(" AND (f.original_name LIKE '%").append(keyword).append("%'")
               .append(" OR a.description LIKE '%").append(keyword).append("%'")
               .append(" OR a.tags LIKE '%").append(keyword).append("%')");
        }
        
        // 状态筛选
        if (queryDTO.getStatus() != null) {
            sql.append(" AND a.status = ").append(queryDTO.getStatus());
        }
        
        // 是否公开筛选
        if (queryDTO.getIsPublic() != null) {
            sql.append(" AND a.is_public = ").append(queryDTO.getIsPublic() ? 1 : 0);
        }
        
        // 是否临时文件筛选
        if (queryDTO.getIsTemporary() != null) {
            sql.append(" AND a.is_temporary = ").append(queryDTO.getIsTemporary() ? 1 : 0);
        }
        
        // 标签筛选
        if (queryDTO.getTags() != null && !queryDTO.getTags().trim().isEmpty()) {
            String tag = queryDTO.getTags().trim();
            sql.append(" AND a.tags LIKE '%").append(tag).append("%'");
        }
        
        // 时间范围筛选
        if (queryDTO.getStartTime() != null && !queryDTO.getStartTime().trim().isEmpty()) {
            sql.append(" AND a.create_time >= '").append(queryDTO.getStartTime()).append("'");
        }
        
        if (queryDTO.getEndTime() != null && !queryDTO.getEndTime().trim().isEmpty()) {
            sql.append(" AND a.create_time <= '").append(queryDTO.getEndTime()).append("'");
        }
        
        // 文件大小范围筛选
        if (queryDTO.getMinFileSize() != null) {
            sql.append(" AND f.file_size >= ").append(queryDTO.getMinFileSize());
        }
        
        if (queryDTO.getMaxFileSize() != null) {
            sql.append(" AND f.file_size <= ").append(queryDTO.getMaxFileSize());
        }
    }
    
    /**
     * 添加排序
     */
    private void appendOrderBy(StringBuilder sql, Pageable pageable) {
        if (pageable.getSort().isSorted()) {
            sql.append(" ORDER BY ");
            List<String> orders = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                String field = order.getProperty();
                // 映射字段名
                if ("fileName".equals(field)) {
                    field = "f.original_name";
                } else if ("fileSize".equals(field)) {
                    field = "f.file_size";
                } else if ("downloadCount".equals(field)) {
                    field = "a.download_count";
                } else if ("viewCount".equals(field)) {
                    field = "a.view_count";
                } else if ("createTime".equals(field)) {
                    field = "a.create_time";
                } else if ("updateTime".equals(field)) {
                    field = "a.update_time";
                } else {
                    field = "a." + field;
                }
                orders.add(field + " " + (order.isAscending() ? "ASC" : "DESC"));
            });
            sql.append(String.join(", ", orders));
        } else {
            sql.append(" ORDER BY a.create_time DESC");
        }
    }
    
    /**
     * 将查询结果映射到AttachmentWithFileDTO
     */
    private AttachmentWithFileDTO mapToAttachmentWithFileDTO(Object[] row) {
        AttachmentWithFileDTO dto = new AttachmentWithFileDTO();
        
        // 附件信息
        dto.setAttachmentId(convertToLong(row[0]));
        dto.setUserId(convertToLong(row[1]));
        dto.setBusinessType(convertToString(row[2]));
        dto.setBusinessId(convertToLong(row[3]));
        dto.setDescription(convertToString(row[4]));
        dto.setTags(convertToString(row[5]));
        dto.setIsPublic(convertToBoolean(row[6]));
        dto.setIsTemporary(convertToBoolean(row[7]));
        dto.setExpireTime(convertToLocalDateTime(row[8]));
        dto.setStatus(convertToInteger(row[9]));
        dto.setDownloadCount(convertToInteger(row[10]));
        dto.setViewCount(convertToInteger(row[11]));
        dto.setCreateTime(convertToLocalDateTime(row[12]));
        dto.setUpdateTime(convertToLocalDateTime(row[13]));
        
        // 文件信息
        dto.setFileId(convertToLong(row[14]));
        dto.setFileName(convertToString(row[15]));
        dto.setOriginalName(convertToString(row[16]));
        dto.setFilePath(convertToString(row[17]));
        dto.setFileSize(convertToLong(row[18]));
        dto.setFileType(convertToString(row[19]));
        dto.setMimeType(convertToString(row[20]));
        dto.setFileExtension(convertToString(row[21]));
        dto.setFileHash(convertToString(row[22]));
        dto.setReferenceCount(convertToInteger(row[23]));
        
        // 计算属性
        dto.setDownloadable(dto.getStatus() == 1 && !dto.getIsTemporary());
        dto.setExpired(dto.getExpireTime() != null && dto.getExpireTime().isBefore(java.time.LocalDateTime.now()));
        
        return dto;
    }
    
    /**
     * 将对象转换为布尔值
     * 支持Number类型（1为true，0为false）和Boolean类型
     */
    private boolean convertToBoolean(Object value) {
        if (value == null) {
            return false;
        }
        
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        
        if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        }
        
        if (value instanceof String) {
            String strValue = ((String) value).toLowerCase();
            return "true".equals(strValue) || "1".equals(strValue) || "yes".equals(strValue);
        }
        
        return false;
    }
    
    /**
     * 将对象转换为Long类型
     */
    private Long convertToLong(Object value) {
        if (value == null) {
            return null;
        }
        
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        
        return null;
    }
    
    /**
     * 将对象转换为Integer类型
     */
    private Integer convertToInteger(Object value) {
        if (value == null) {
            return null;
        }
        
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        
        return null;
    }
    
    /**
     * 将对象转换为String类型
     */
    private String convertToString(Object value) {
        if (value == null) {
            return null;
        }
        
        return value.toString();
    }
    
    /**
     * 将对象转换为LocalDateTime类型
     */
    private LocalDateTime convertToLocalDateTime(Object value) {
        if (value == null) {
            return null;
        }
        
        if (value instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) value).toLocalDateTime();
        }
        
        if (value instanceof java.sql.Date) {
            return ((java.sql.Date) value).toLocalDate().atStartOfDay();
        }
        
        if (value instanceof java.util.Date) {
            return ((java.util.Date) value).toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();
        }
        
        if (value instanceof String) {
            try {
                return LocalDateTime.parse((String) value, 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception e) {
                return null;
            }
        }
        
        return null;
    }
}
