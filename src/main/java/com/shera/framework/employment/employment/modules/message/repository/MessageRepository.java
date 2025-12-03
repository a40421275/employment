package com.shera.framework.employment.employment.modules.message.repository;

import com.shera.framework.employment.employment.modules.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息Repository
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * 根据接收者ID和状态查询消息
     */
    Page<Message> findByReceiverIdAndStatus(Long receiverId, Integer status, Pageable pageable);

    /**
     * 根据接收者ID查询消息
     */
    Page<Message> findByReceiverId(Long receiverId, Pageable pageable);

    /**
     * 根据接收者ID和类型查询消息
     */
    Page<Message> findByReceiverIdAndType(Long receiverId, Integer type, Pageable pageable);

    /**
     * 根据接收者ID、类型和状态查询消息
     */
    Page<Message> findByReceiverIdAndTypeAndStatus(Long receiverId, Integer type, Integer status, Pageable pageable);

    /**
     * 根据接收者ID和状态查询消息数量
     */
    Long countByReceiverIdAndStatus(Long receiverId, Integer status);

    /**
     * 根据接收者ID查询消息数量
     */
    Long countByReceiverId(Long receiverId);

    /**
     * 根据接收者ID和状态查询未读消息数量
     */
    default Long countUnreadByReceiverId(Long receiverId) {
        return countByReceiverIdAndStatus(receiverId, Message.Status.UNREAD.getCode());
    }

    /**
     * 根据接收者ID查询最新消息
     */
    List<Message> findTop10ByReceiverIdOrderByCreateTimeDesc(Long receiverId);

    /**
     * 根据接收者ID和优先级查询重要消息
     */
    List<Message> findTop10ByReceiverIdAndPriorityOrderByCreateTimeDesc(Long receiverId, Integer priority);

    /**
     * 根据关键词搜索消息
     */
    @Query("SELECT m FROM Message m WHERE m.receiverId = :receiverId AND " +
           "(m.title LIKE %:keyword% OR m.content LIKE %:keyword%)")
    Page<Message> searchByKeyword(@Param("receiverId") Long receiverId, 
                                 @Param("keyword") String keyword, 
                                 Pageable pageable);

    /**
     * 批量更新消息状态
     */
    @Query("UPDATE Message m SET m.status = :status, m.readTime = :readTime WHERE m.id IN :ids")
    void updateStatusByIds(@Param("ids") List<Long> ids, 
                          @Param("status") Integer status, 
                          @Param("readTime") LocalDateTime readTime);

    /**
     * 更新用户所有消息状态
     */
    @Query("UPDATE Message m SET m.status = :status, m.readTime = :readTime WHERE m.receiverId = :receiverId")
    void updateStatusByReceiverId(@Param("receiverId") Long receiverId, 
                                 @Param("status") Integer status, 
                                 @Param("readTime") LocalDateTime readTime);

    /**
     * 删除用户所有消息
     */
    void deleteByReceiverId(Long receiverId);

    /**
     * 查询过期消息
     */
    List<Message> findByExpireTimeBeforeAndStatusNot(LocalDateTime expireTime, Integer status);

    /**
     * 根据业务ID和业务类型查询消息
     */
    List<Message> findByBusinessIdAndBusinessType(Long businessId, String businessType);
}
