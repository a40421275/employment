package com.shera.framework.employment.employment.modules.message.repository;

import com.shera.framework.employment.employment.modules.message.entity.MessageTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 消息模板Repository
 */
@Repository
public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long> {

    /**
     * 根据渠道类型查询模板
     */
    List<MessageTemplate> findByChannelType(String channelType);

    /**
     * 根据渠道类型查询模板（分页）
     */
    Page<MessageTemplate> findByChannelType(String channelType, Pageable pageable);

    /**
     * 根据状态查询模板
     */
    List<MessageTemplate> findByStatus(Integer status);

    /**
     * 根据状态查询模板（分页）
     */
    Page<MessageTemplate> findByStatus(Integer status, Pageable pageable);

    /**
     * 根据渠道类型和状态查询模板
     */
    List<MessageTemplate> findByChannelTypeAndStatus(String channelType, Integer status);

    /**
     * 根据渠道类型和状态查询模板（分页）
     */
    Page<MessageTemplate> findByChannelTypeAndStatus(String channelType, Integer status, Pageable pageable);

    /**
     * 根据名称和渠道类型查询模板
     */
    MessageTemplate findByNameAndChannelType(String name, String channelType);

    /**
     * 根据编码和渠道类型和状态查询模板
     */
    Optional<MessageTemplate> findByCodeAndChannelTypeAndStatus(String code, String channelType, Integer status);

    /**
     * 根据业务类型和渠道类型和状态查询模板
     */
    Optional<MessageTemplate> findByBusinessTypeAndChannelTypeAndStatus(String businessType, String channelType, Integer status);

    /**
     * 根据编码和状态查询模板
     */
    Optional<MessageTemplate> findByCodeAndStatus(String code, Integer status);

    /**
     * 根据编码查询模板
     */
    Optional<MessageTemplate> findByCode(String code);
}
