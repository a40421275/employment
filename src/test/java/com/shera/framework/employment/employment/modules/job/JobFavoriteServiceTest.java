package com.shera.framework.employment.employment.modules.job;

import com.shera.framework.employment.employment.modules.job.entity.JobFavorite;
import com.shera.framework.employment.employment.modules.job.repository.JobFavoriteRepository;
import com.shera.framework.employment.employment.modules.job.service.impl.JobFavoriteServiceImpl;
import com.shera.framework.employment.employment.modules.job.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 岗位收藏服务测试类
 */
@ExtendWith(MockitoExtension.class)
class JobFavoriteServiceTest {
    
    @Mock
    private JobFavoriteRepository jobFavoriteRepository;
    
    @Mock
    private JobService jobService;
    
    @InjectMocks
    private JobFavoriteServiceImpl jobFavoriteService;
    
    private Long userId;
    private Long jobId;
    private JobFavorite jobFavorite;
    
    @BeforeEach
    void setUp() {
        userId = 1L;
        jobId = 100L;
        
        jobFavorite = new JobFavorite();
        jobFavorite.setId(1L);
        jobFavorite.setUserId(userId);
        jobFavorite.setJobId(jobId);
        jobFavorite.setCreateTime(LocalDateTime.now());
    }
    
    @Test
    void testFavoriteJob_Success() {
        // 准备
        when(jobFavoriteRepository.existsByUserIdAndJobId(userId, jobId)).thenReturn(false);
        when(jobFavoriteRepository.save(any(JobFavorite.class))).thenReturn(jobFavorite);
        
        // 执行
        JobFavorite result = jobFavoriteService.favoriteJob(userId, jobId);
        
        // 验证
        assertNotNull(result);
        assertEquals(jobId, result.getJobId());
        assertEquals(userId, result.getUserId());
        verify(jobFavoriteRepository, times(1)).save(any(JobFavorite.class));
    }
    
    @Test
    void testFavoriteJob_AlreadyFavorited() {
        // 准备
        when(jobFavoriteRepository.existsByUserIdAndJobId(userId, jobId)).thenReturn(true);
        when(jobFavoriteRepository.findByUserIdAndJobId(userId, jobId)).thenReturn(jobFavorite);
        
        // 执行
        JobFavorite result = jobFavoriteService.favoriteJob(userId, jobId);
        
        // 验证
        assertNotNull(result);
        assertEquals(jobFavorite, result);
        verify(jobFavoriteRepository, never()).save(any(JobFavorite.class));
    }
    
    @Test
    void testUnfavoriteJob_Success() {
        // 准备
        when(jobFavoriteRepository.findByUserIdAndJobId(userId, jobId)).thenReturn(jobFavorite);
        
        // 执行
        jobFavoriteService.unfavoriteJob(userId, jobId);
        
        // 验证
        verify(jobFavoriteRepository, times(1)).delete(jobFavorite);
    }
    
    @Test
    void testUnfavoriteJob_NotFound() {
        // 准备
        when(jobFavoriteRepository.findByUserIdAndJobId(userId, jobId)).thenReturn(null);
        
        // 执行
        jobFavoriteService.unfavoriteJob(userId, jobId);
        
        // 验证
        verify(jobFavoriteRepository, never()).delete(any(JobFavorite.class));
    }
    
    @Test
    void testIsJobFavorited() {
        // 准备
        when(jobFavoriteRepository.existsByUserIdAndJobId(userId, jobId)).thenReturn(true);
        
        // 执行
        boolean result = jobFavoriteService.isJobFavorited(userId, jobId);
        
        // 验证
        assertTrue(result);
        verify(jobFavoriteRepository, times(1)).existsByUserIdAndJobId(userId, jobId);
    }
    
    @Test
    void testGetUserFavoriteCount() {
        // 准备
        Long expectedCount = 5L;
        when(jobFavoriteRepository.countByUserId(userId)).thenReturn(expectedCount);
        
        // 执行
        Long result = jobFavoriteService.getUserFavoriteCount(userId);
        
        // 验证
        assertEquals(expectedCount, result);
        verify(jobFavoriteRepository, times(1)).countByUserId(userId);
    }
    
    @Test
    void testGetJobFavoriteCount() {
        // 准备
        Long expectedCount = 10L;
        when(jobFavoriteRepository.countByJobId(jobId)).thenReturn(expectedCount);
        
        // 执行
        Long result = jobFavoriteService.getJobFavoriteCount(jobId);
        
        // 验证
        assertEquals(expectedCount, result);
        verify(jobFavoriteRepository, times(1)).countByJobId(jobId);
    }
    
    @Test
    void testBatchCheckFavoriteStatus() {
        // 准备
        List<Long> jobIds = Arrays.asList(100L, 200L, 300L);
        List<Long> favoritedJobIds = Arrays.asList(100L, 300L);
        
        when(jobFavoriteRepository.findFavoriteJobIdsByUserIdAndJobIds(userId, jobIds))
                .thenReturn(favoritedJobIds);
        
        // 执行
        Map<Long, Boolean> result = jobFavoriteService.batchCheckFavoriteStatus(userId, jobIds);
        
        // 验证
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.get(100L));
        assertFalse(result.get(200L));
        assertTrue(result.get(300L));
    }
    
    @Test
    void testGetUserFavoriteJobIds() {
        // 准备
        List<Long> expectedJobIds = Arrays.asList(100L, 200L, 300L);
        when(jobFavoriteRepository.findJobIdsByUserId(userId)).thenReturn(expectedJobIds);
        
        // 执行
        List<Long> result = jobFavoriteService.getUserFavoriteJobIds(userId);
        
        // 验证
        assertNotNull(result);
        assertEquals(expectedJobIds, result);
        verify(jobFavoriteRepository, times(1)).findJobIdsByUserId(userId);
    }
    
    @Test
    void testDeleteAllUserFavorites() {
        // 执行
        jobFavoriteService.deleteAllUserFavorites(userId);
        
        // 验证
        verify(jobFavoriteRepository, times(1)).deleteByUserId(userId);
    }
    
    @Test
    void testDeleteAllJobFavorites() {
        // 执行
        jobFavoriteService.deleteAllJobFavorites(jobId);
        
        // 验证
        verify(jobFavoriteRepository, times(1)).deleteByJobId(jobId);
    }
}
