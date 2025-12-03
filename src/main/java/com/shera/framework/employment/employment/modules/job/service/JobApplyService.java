package com.shera.framework.employment.employment.modules.job.service;

import com.shera.framework.employment.employment.modules.job.dto.JobApplyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 岗位申请服务接口
 */
public interface JobApplyService {
    
    /**
     * 创建岗位申请
     */
    JobApplyDTO createJobApply(JobApplyDTO jobApplyDTO);
    
    /**
     * 更新岗位申请
     */
    JobApplyDTO updateJobApply(Long id, JobApplyDTO jobApplyDTO);
    
    /**
     * 删除岗位申请
     */
    void deleteJobApply(Long id);
    
    /**
     * 根据ID获取岗位申请
     */
    JobApplyDTO getJobApplyById(Long id);
    
    /**
     * 获取申请详情（包含关联信息）
     */
    JobApplyDTO getJobApplyDetail(Long id);
    
    /**
     * 分页查询岗位申请列表
     */
    Page<JobApplyDTO> getJobApplies(Pageable pageable);
    
    /**
     * 根据用户ID查询岗位申请列表
     */
    List<JobApplyDTO> getJobAppliesByUserId(Long userId);
    
    /**
     * 根据用户ID分页查询岗位申请列表
     */
    Page<JobApplyDTO> getJobAppliesByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据岗位ID查询岗位申请列表
     */
    List<JobApplyDTO> getJobAppliesByJobId(Long jobId);
    
    /**
     * 根据岗位ID分页查询岗位申请列表
     */
    Page<JobApplyDTO> getJobAppliesByJobId(Long jobId, Pageable pageable);
    
    /**
     * 根据简历ID查询岗位申请列表
     */
    List<JobApplyDTO> getJobAppliesByResumeId(Long resumeId);
    
    /**
     * 根据状态查询岗位申请列表
     */
    List<JobApplyDTO> getJobAppliesByStatus(Integer status);
    
    /**
     * 根据用户ID和状态查询岗位申请列表
     */
    List<JobApplyDTO> getJobAppliesByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 根据岗位ID和状态查询岗位申请列表
     */
    List<JobApplyDTO> getJobAppliesByJobIdAndStatus(Long jobId, Integer status);
    
    /**
     * 更新申请状态
     */
    JobApplyDTO updateStatus(Long id, Integer status);
    
    /**
     * 设置面试时间
     */
    JobApplyDTO setInterviewTime(Long id, String interviewTime, String interviewLocation);
    
    /**
     * 添加申请反馈
     */
    JobApplyDTO addFeedback(Long id, String feedback);
    
    /**
     * 计算匹配度
     */
    BigDecimal calculateMatchScore(Long jobId, Long resumeId);
    
    /**
     * 检查用户是否已申请该岗位
     */
    boolean hasUserAppliedJob(Long userId, Long jobId);
    
    /**
     * 统计各状态的申请数量
     */
    Map<Integer, Long> countJobAppliesByStatus();
    
    /**
     * 统计用户申请数量
     */
    Long countJobAppliesByUserId(Long userId);
    
    /**
     * 统计岗位申请数量
     */
    Long countJobAppliesByJobId(Long jobId);
    
    /**
     * 获取热门申请岗位（按申请量排序）
     */
    List<Object[]> getHotAppliedJobs(int limit);
    
    /**
     * 获取用户申请统计
     */
    Map<String, Object> getUserApplyStats(Long userId);
    
    /**
     * 面试通过
     */
    JobApplyDTO passInterview(Long id, String feedback);
    
    /**
     * 拒绝申请
     */
    JobApplyDTO rejectApplication(Long id, String reason);
    
    /**
     * 取消申请
     */
    JobApplyDTO cancelApplication(Long id);
    
    /**
     * 检查是否已申请
     */
    Map<String, Object> checkAppliedStatus(Long userId, Long jobId);
    
    /**
     * 获取最新申请
     */
    List<JobApplyDTO> getLatestApplications(int limit);
}
