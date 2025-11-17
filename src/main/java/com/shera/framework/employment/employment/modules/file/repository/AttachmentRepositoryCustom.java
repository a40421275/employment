package com.shera.framework.employment.employment.modules.file.repository;

import com.shera.framework.employment.employment.modules.file.dto.FileQueryDTO;
import com.shera.framework.employment.employment.modules.file.dto.AttachmentWithFileDTO;
import com.shera.framework.employment.employment.modules.file.entity.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 附件自定义数据访问接口
 */
public interface AttachmentRepositoryCustom {
    
    /**
     * 根据查询条件查找附件列表
     */
    List<Attachment> findByQuery(FileQueryDTO queryDTO);
    
    /**
     * 根据查询条件查找附件列表（分页）
     */
    Page<Attachment> findByQuery(FileQueryDTO queryDTO, Pageable pageable);
    
    /**
     * 根据查询条件查找附件和文件信息列表（分页）
     */
    Page<AttachmentWithFileDTO> findAttachmentWithFileByQuery(FileQueryDTO queryDTO, Pageable pageable);
    
    /**
     * 根据查询条件统计附件数量
     */
    long countByQuery(FileQueryDTO queryDTO);
}
