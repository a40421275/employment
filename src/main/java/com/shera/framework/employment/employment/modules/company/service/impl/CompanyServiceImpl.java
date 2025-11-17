package com.shera.framework.employment.employment.modules.company.service.impl;

import com.shera.framework.employment.employment.modules.company.dto.CompanyDTO;
import com.shera.framework.employment.employment.modules.company.dto.CompanyOptionDTO;
import com.shera.framework.employment.employment.modules.company.dto.CompanyQueryDTO;
import com.shera.framework.employment.employment.modules.company.entity.Company;
import com.shera.framework.employment.employment.modules.company.repository.CompanyRepository;
import com.shera.framework.employment.employment.modules.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 企业服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    
    private final CompanyRepository companyRepository;
    
    @Override
    @Transactional
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        // 验证企业信息
        if (!validateCompany(companyDTO)) {
            throw new IllegalArgumentException("企业信息验证失败");
        }
        
        // 检查企业名称是否已存在
        if (isCompanyNameExists(companyDTO.getCompanyName())) {
            throw new IllegalArgumentException("企业名称已存在");
        }
        
        // 检查营业执照号是否已存在
        if (companyDTO.getBusinessLicenseNo() != null && 
            isBusinessLicenseNoExists(companyDTO.getBusinessLicenseNo())) {
            throw new IllegalArgumentException("营业执照号已存在");
        }
        
        Company company = new Company();
        // 排除统计字段，确保使用实体类的默认值
        BeanUtils.copyProperties(companyDTO, company, "jobCount", "viewCount", "favoriteCount", "applyCount");
        company.setStatus(Company.Status.NORMAL.getCode());
        company.setAuthStatus(Company.AuthStatus.UNAUTHENTICATED.getCode());
        
        Company savedCompany = companyRepository.save(company);
        log.info("创建企业成功，企业ID：{}，企业名称：{}", savedCompany.getId(), savedCompany.getCompanyName());
        
        return convertToDTO(savedCompany);
    }
    
    @Override
    @Transactional
    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("企业不存在"));
        
        // 验证企业信息
        if (!validateCompany(companyDTO)) {
            throw new IllegalArgumentException("企业信息验证失败");
        }
        
        // 检查企业名称是否被其他企业使用
        if (!company.getCompanyName().equals(companyDTO.getCompanyName()) && 
            isCompanyNameExists(companyDTO.getCompanyName())) {
            throw new IllegalArgumentException("企业名称已被其他企业使用");
        }
        
        // 检查营业执照号是否被其他企业使用
        if (companyDTO.getBusinessLicenseNo() != null && 
            !companyDTO.getBusinessLicenseNo().equals(company.getBusinessLicenseNo()) && 
            isBusinessLicenseNoExists(companyDTO.getBusinessLicenseNo())) {
            throw new IllegalArgumentException("营业执照号已被其他企业使用");
        }
        
        BeanUtils.copyProperties(companyDTO, company, "id", "status", "authStatus", "authTime", 
                "authOperatorId", "authFailReason", "jobCount", "viewCount", "favoriteCount", "applyCount");
        
        Company updatedCompany = companyRepository.save(company);
        log.info("更新企业信息成功，企业ID：{}", id);
        
        return convertToDTO(updatedCompany);
    }
    
    @Override
    public CompanyDTO getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("企业不存在"));
        
        // 增加查看次数
        incrementViewCount(id);
        
        return convertToDTO(company);
    }
    
    @Override
    public CompanyDTO getCompanyByName(String companyName) {
        Company company = companyRepository.findByCompanyName(companyName)
                .orElseThrow(() -> new IllegalArgumentException("企业不存在"));
        
        return convertToDTO(company);
    }
    
    @Override
    @Transactional
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("企业不存在"));
        
        company.setStatus(Company.Status.DISABLED.getCode());
        companyRepository.save(company);
        log.info("删除企业成功，企业ID：{}", id);
    }
    
    @Override
    public Page<CompanyDTO> getCompanies(Pageable pageable) {
        // 查询所有正常状态的企业，不限制认证状态
        Page<Company> companies = companyRepository.findByStatus(
                Company.Status.NORMAL.getCode(), 
                pageable
        );
        
        return companies.map(this::convertToDTO);
    }
    
    @Override
    public List<CompanyDTO> getCompaniesByStatus(Integer status) {
        List<Company> companies = companyRepository.findByStatus(status);
        return companies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<CompanyDTO> getCompaniesByAuthStatus(Integer authStatus) {
        List<Company> companies = companyRepository.findByAuthStatus(authStatus);
        return companies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<CompanyDTO> getCompaniesByIndustry(String industry) {
        List<Company> companies = companyRepository.findByIndustry(industry);
        return companies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<CompanyDTO> getCompaniesByCity(String city) {
        List<Company> companies = companyRepository.findByCity(city);
        return companies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<CompanyDTO> searchCompanies(String keyword) {
        List<Company> companies = companyRepository.findByCompanyNameContaining(keyword);
        return companies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public Page<CompanyDTO> queryCompanies(CompanyQueryDTO queryDTO) {
        // 构建分页参数
        Pageable pageable = buildPageable(queryDTO);
        
        // 构建查询条件
        Specification<Company> spec = buildQuerySpecification(queryDTO);
        
        // 执行查询
        Page<Company> companies = companyRepository.findAll(spec, pageable);
        
        return companies.map(this::convertToDTO);
    }
    
    /**
     * 构建分页参数
     */
    private Pageable buildPageable(CompanyQueryDTO queryDTO) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(queryDTO.getDirection()) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(queryDTO.getPage(), queryDTO.getSize(), 
                Sort.by(sortDirection, queryDTO.getSort()));
    }
    
    /**
     * 构建查询条件
     */
    private Specification<Company> buildQuerySpecification(CompanyQueryDTO queryDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 关键词搜索（企业名称、简称）
            if (StringUtils.hasText(queryDTO.getKeyword())) {
                String keyword = "%" + queryDTO.getKeyword() + "%";
                Predicate namePredicate = criteriaBuilder.like(root.get("companyName"), keyword);
                Predicate shortNamePredicate = criteriaBuilder.like(root.get("companyShortName"), keyword);
                predicates.add(criteriaBuilder.or(namePredicate, shortNamePredicate));
            }
            
            // 城市筛选
            if (StringUtils.hasText(queryDTO.getCity())) {
                predicates.add(criteriaBuilder.equal(root.get("city"), queryDTO.getCity()));
            }
            
            // 行业筛选
            if (StringUtils.hasText(queryDTO.getIndustry())) {
                predicates.add(criteriaBuilder.equal(root.get("industry"), queryDTO.getIndustry()));
            }
            
            // 企业类型筛选
            if (queryDTO.getCompanyType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("companyType"), queryDTO.getCompanyType()));
            }
            
            // 企业规模筛选
            if (queryDTO.getScale() != null) {
                predicates.add(criteriaBuilder.equal(root.get("scale"), queryDTO.getScale()));
            }
            
            // 状态筛选
            if (queryDTO.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), queryDTO.getStatus()));
            }
            
            // 认证状态筛选
            if (queryDTO.getAuthStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("authStatus"), queryDTO.getAuthStatus()));
            }
            
            // 省份筛选
            if (StringUtils.hasText(queryDTO.getProvince())) {
                predicates.add(criteriaBuilder.equal(root.get("province"), queryDTO.getProvince()));
            }
            
            // 区县筛选
            if (StringUtils.hasText(queryDTO.getDistrict())) {
                predicates.add(criteriaBuilder.equal(root.get("district"), queryDTO.getDistrict()));
            }
            
            // 默认只查询正常状态的企业
            if (queryDTO.getStatus() == null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), Company.Status.NORMAL.getCode()));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    @Override
    @Transactional
    public CompanyDTO auditCompany(Long id, Integer authStatus, String authFailReason, Long operatorId) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("企业不存在"));
        
        company.setAuthStatus(authStatus);
        company.setAuthOperatorId(operatorId);
        
        if (authStatus.equals(Company.AuthStatus.AUTHENTICATED.getCode())) {
            company.setAuthTime(LocalDateTime.now());
            company.setAuthFailReason(null);
        } else if (authStatus.equals(Company.AuthStatus.UNAUTHENTICATED.getCode())) {
            company.setAuthFailReason(authFailReason);
        }
        
        Company auditedCompany = companyRepository.save(company);
        log.info("审核企业认证成功，企业ID：{}，认证状态：{}", id, authStatus);
        
        return convertToDTO(auditedCompany);
    }
    
    @Override
    @Transactional
    public CompanyDTO updateCompanyStatus(Long id, Integer status) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("企业不存在"));
        
        company.setStatus(status);
        Company updatedCompany = companyRepository.save(company);
        log.info("更新企业状态成功，企业ID：{}，状态：{}", id, status);
        
        return convertToDTO(updatedCompany);
    }
    
    @Override
    @Transactional
    public void incrementViewCount(Long id) {
        companyRepository.findById(id).ifPresent(company -> {
            company.setViewCount(company.getViewCount() + 1);
            companyRepository.save(company);
        });
    }
    
    @Override
    @Transactional
    public void incrementFavoriteCount(Long id) {
        companyRepository.findById(id).ifPresent(company -> {
            company.setFavoriteCount(company.getFavoriteCount() + 1);
            companyRepository.save(company);
        });
    }
    
    @Override
    @Transactional
    public void incrementApplyCount(Long id) {
        companyRepository.findById(id).ifPresent(company -> {
            company.setApplyCount(company.getApplyCount() + 1);
            companyRepository.save(company);
        });
    }
    
    @Override
    @Transactional
    public void updateJobCount(Long id, Integer jobCount) {
        companyRepository.findById(id).ifPresent(company -> {
            company.setJobCount(jobCount);
            companyRepository.save(company);
        });
    }
    
    @Override
    public Long countCompanies(Integer status) {
        return companyRepository.countByStatus(status);
    }
    
    @Override
    public Long countAuthCompanies(Integer authStatus) {
        return companyRepository.countByAuthStatus(authStatus);
    }
    
    @Override
    public boolean validateCompany(CompanyDTO companyDTO) {
        if (companyDTO.getCompanyName() == null || companyDTO.getCompanyName().trim().isEmpty()) {
            return false;
        }
        if (companyDTO.getIndustry() == null || companyDTO.getIndustry().trim().isEmpty()) {
            return false;
        }
        if (companyDTO.getLegalPerson() == null || companyDTO.getLegalPerson().trim().isEmpty()) {
            return false;
        }
        if (companyDTO.getProvince() == null || companyDTO.getProvince().trim().isEmpty()) {
            return false;
        }
        if (companyDTO.getCity() == null || companyDTO.getCity().trim().isEmpty()) {
            return false;
        }
        if (companyDTO.getAddress() == null || companyDTO.getAddress().trim().isEmpty()) {
            return false;
        }
        if (companyDTO.getContactPerson() == null || companyDTO.getContactPerson().trim().isEmpty()) {
            return false;
        }
        if (companyDTO.getContactPhone() == null || companyDTO.getContactPhone().trim().isEmpty()) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean isCompanyNameExists(String companyName) {
        return companyRepository.findByCompanyName(companyName).isPresent();
    }
    
    @Override
    public boolean isBusinessLicenseNoExists(String businessLicenseNo) {
        return companyRepository.findByBusinessLicenseNo(businessLicenseNo).isPresent();
    }
    
    @Override
    public Page<CompanyOptionDTO> getCompanyOptions(String keyword, Pageable pageable) {
        // 构建查询条件
        Specification<Company> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 只查询正常状态的企业
            predicates.add(criteriaBuilder.equal(root.get("status"), Company.Status.NORMAL.getCode()));
            
            // 关键词搜索（企业名称）
            if (StringUtils.hasText(keyword)) {
                String searchKeyword = "%" + keyword + "%";
                Predicate namePredicate = criteriaBuilder.like(root.get("companyName"), searchKeyword);
                predicates.add(namePredicate);
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        // 执行查询，按企业名称排序
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
                Sort.by(Sort.Direction.ASC, "companyName"));
        
        Page<Company> companies = companyRepository.findAll(spec, sortedPageable);
        
        // 转换为CompanyOptionDTO
        return companies.map(company -> new CompanyOptionDTO(company.getId(), company.getCompanyName()));
    }
    
    /**
     * 将实体转换为DTO
     */
    private CompanyDTO convertToDTO(Company company) {
        CompanyDTO dto = new CompanyDTO();
        BeanUtils.copyProperties(company, dto);
        
        // 设置枚举描述
        dto.setCompanyTypeDesc(getCompanyTypeDesc(company.getCompanyType()));
        dto.setScaleDesc(getScaleDesc(company.getScale()));
        dto.setStatusDesc(getStatusDesc(company.getStatus()));
        dto.setAuthStatusDesc(getAuthStatusDesc(company.getAuthStatus()));
        
        return dto;
    }
    
    /**
     * 获取企业类型描述
     */
    private String getCompanyTypeDesc(Integer companyType) {
        if (companyType == null) return null;
        for (Company.CompanyType type : Company.CompanyType.values()) {
            if (type.getCode() == companyType) {
                return type.getDesc();
            }
        }
        return null;
    }
    
    /**
     * 获取企业规模描述
     */
    private String getScaleDesc(Integer scale) {
        if (scale == null) return null;
        for (Company.Scale s : Company.Scale.values()) {
            if (s.getCode() == scale) {
                return s.getDesc();
            }
        }
        return null;
    }
    
    /**
     * 获取企业状态描述
     */
    private String getStatusDesc(Integer status) {
        if (status == null) return null;
        for (Company.Status s : Company.Status.values()) {
            if (s.getCode() == status) {
                return s.getDesc();
            }
        }
        return null;
    }
    
    /**
     * 获取认证状态描述
     */
    private String getAuthStatusDesc(Integer authStatus) {
        if (authStatus == null) return null;
        for (Company.AuthStatus status : Company.AuthStatus.values()) {
            if (status.getCode() == authStatus) {
                return status.getDesc();
            }
        }
        return null;
    }
}
