package com.shera.framework.employment.employment.modules.job.service;

import com.shera.framework.employment.employment.modules.job.dto.JobTagDTO;
import com.shera.framework.employment.employment.modules.job.dto.JobTagDetailDTO;
import com.shera.framework.employment.employment.modules.job.dto.JobTagQueryDTO;
import com.shera.framework.employment.employment.modules.job.entity.JobTag;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * 岗位标签服务接口
 */
public interface JobTagService {
    
    /**
     * 新建标签
     */
    JobTag createTag(String tagName);
    
    /**
     * 为岗位添加标签
     */
    JobTag addTagToJob(Long jobId, String tagName);
    
    /**
     * 为岗位批量添加标签
     */
    List<JobTag> addTagsToJob(Long jobId, List<String> tagNames);
    
    /**
     * 从岗位移除标签
     */
    void removeTagFromJob(Long jobId, String tagName);
    
    /**
     * 从岗位批量移除标签
     */
    void removeTagsFromJob(Long jobId, List<String> tagNames);
    
    /**
     * 获取岗位的所有标签
     */
    List<JobTag> getTagsByJobId(Long jobId);
    
    /**
     * 获取岗位的标签名称列表
     */
    List<String> getTagNamesByJobId(Long jobId);
    
    /**
     * 根据标签名称获取岗位列表
     */
    List<Long> getJobIdsByTagName(String tagName);
    
    /**
     * 根据多个标签名称获取岗位列表
     */
    List<Long> getJobIdsByTagNames(List<String> tagNames);
    
    /**
     * 根据标签名称搜索岗位
     */
    List<Long> searchJobIdsByTagName(String tagName);
    
    /**
     * 获取热门标签
     */
    List<Object[]> getHotTags(int limit);
    
    /**
     * 统计标签使用次数
     */
    Map<String, Long> countTagsUsage();
    
    /**
     * 获取所有标签
     */
    List<String> getAllTags();
    
    /**
     * 获取系统常用标签
     */
    List<String> getSystemTags();
    
    /**
     * 检查标签是否已存在
     */
    boolean isTagExists(Long jobId, String tagName);
    
    /**
     * 更新标签
     */
    JobTag updateTag(Long tagId, JobTagDTO jobTagDTO);
    
    /**
     * 删除标签
     */
    void deleteTag(Long tagId);
    
    /**
     * 批量删除标签
     */
    void batchDeleteTags(List<Long> tagIds);
    
    /**
     * 根据标签类型获取标签
     */
    List<JobTag> getTagsByType(Integer tagType);
    
    /**
     * 获取标签统计信息
     */
    Map<String, Object> getTagStats();
    
    /**
     * 根据条件查询所有标签（支持分页和筛选）
     */
    Page<JobTagDetailDTO> getAllTagsWithPagination(JobTagQueryDTO queryDTO);
    
    /**
     * 获取系统常用标签（支持分页）
     */
    Page<JobTagDetailDTO> getSystemTagsWithPagination(JobTagQueryDTO queryDTO);
    
    /**
     * 获取所有标签详情（支持筛选条件）
     */
    List<JobTagDetailDTO> getAllTagDetails(JobTagQueryDTO queryDTO);
    
    /**
     * 统计符合条件的标签数量
     */
    Long countTagsByConditions(JobTagQueryDTO queryDTO);

    /**
     * 创建完整标签信息
     */
    JobTag createTagWithDetails(JobTagDTO jobTagDTO);
}
