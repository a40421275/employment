package com.shera.framework.employment.employment.modules.company.controller;

import com.shera.framework.employment.employment.modules.company.dto.CompanyDTO;
import com.shera.framework.employment.employment.modules.company.dto.CompanyOptionDTO;
import com.shera.framework.employment.employment.modules.company.dto.CompanyQueryDTO;
import com.shera.framework.employment.employment.modules.company.service.CompanyService;
import com.shera.framework.employment.employment.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业控制器
 */
@Tag(name = "企业管理", description = "企业台账管理接口")
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    
    private final CompanyService companyService;
    
    @Operation(summary = "创建企业", description = "创建新的企业信息")
    @PostMapping
    public ResponseEntity<?> createCompany(@RequestBody CompanyDTO companyDTO) {
        try {
            CompanyDTO createdCompany = companyService.createCompany(companyDTO);
            return ResponseUtil.success("创建企业成功", createdCompany);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "更新企业信息", description = "根据ID更新企业信息")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(
            @Parameter(description = "企业ID") @PathVariable Long id,
            @RequestBody CompanyDTO companyDTO) {
        try {
            CompanyDTO updatedCompany = companyService.updateCompany(id, companyDTO);
            return ResponseUtil.success("更新企业信息成功", updatedCompany);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "获取企业详情", description = "根据ID获取企业详细信息")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(
            @Parameter(description = "企业ID") @PathVariable Long id) {
        try {
            CompanyDTO company = companyService.getCompanyById(id);
            return ResponseUtil.success("获取企业详情成功", company);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "根据名称获取企业", description = "根据企业名称获取企业信息")
    @GetMapping("/name/{companyName}")
    public ResponseEntity<?> getCompanyByName(
            @Parameter(description = "企业名称") @PathVariable String companyName) {
        try {
            CompanyDTO company = companyService.getCompanyByName(companyName);
            return ResponseUtil.success("获取企业信息成功", company);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }

    @Operation(summary = "删除企业", description = "根据ID删除企业（逻辑删除）")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(
            @Parameter(description = "企业ID") @PathVariable Long id) {
        try {
            companyService.deleteCompany(id);
            return ResponseUtil.success("删除企业成功", null);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "分页查询企业列表", description = "分页获取企业列表")
    @GetMapping
    public ResponseEntity<?> getCompanies(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "createTime") String sort,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") String direction) {
        
        try {
            Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            
            Page<CompanyDTO> companies = companyService.getCompanies(pageable);
            
            return ResponseUtil.successPage(companies, "查询企业列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "统一查询企业列表", description = "支持各种筛选和分页查询企业列表")
    @PostMapping("/query")
    public ResponseEntity<?> queryCompanies(@RequestBody CompanyQueryDTO queryDTO) {
        try {
            Page<CompanyDTO> companies = companyService.queryCompanies(queryDTO);
            
            return ResponseUtil.successPage(companies, "查询企业列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "根据状态查询企业列表", description = "根据状态获取企业列表")
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getCompaniesByStatus(
            @Parameter(description = "状态") @PathVariable Integer status) {
        try {
            List<CompanyDTO> companies = companyService.getCompaniesByStatus(status);
            return ResponseUtil.successList(companies, "查询企业列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "根据认证状态查询企业列表", description = "根据认证状态获取企业列表")
    @GetMapping("/auth-status/{authStatus}")
    public ResponseEntity<?> getCompaniesByAuthStatus(
            @Parameter(description = "认证状态") @PathVariable Integer authStatus) {
        try {
            List<CompanyDTO> companies = companyService.getCompaniesByAuthStatus(authStatus);
            return ResponseUtil.successList(companies, "查询企业列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "根据行业查询企业列表", description = "根据行业获取企业列表")
    @GetMapping("/industry/{industry}")
    public ResponseEntity<?> getCompaniesByIndustry(
            @Parameter(description = "行业") @PathVariable String industry) {
        try {
            List<CompanyDTO> companies = companyService.getCompaniesByIndustry(industry);
            return ResponseUtil.successList(companies, "查询企业列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "根据城市查询企业列表", description = "根据城市获取企业列表")
    @GetMapping("/city/{city}")
    public ResponseEntity<?> getCompaniesByCity(
            @Parameter(description = "城市") @PathVariable String city) {
        try {
            List<CompanyDTO> companies = companyService.getCompaniesByCity(city);
            return ResponseUtil.successList(companies, "查询企业列表成功");
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "搜索企业", description = "根据关键词搜索企业")
    @GetMapping("/search")
    public ResponseEntity<?> searchCompanies(
            @Parameter(description = "搜索关键词") @RequestParam String keyword) {
        try {
            List<CompanyDTO> companies = companyService.searchCompanies(keyword);
            return ResponseUtil.successList(companies, "搜索企业成功");
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "审核企业认证", description = "审核企业认证申请")
    @PostMapping("/{id}/audit")
    public ResponseEntity<?> auditCompany(
            @Parameter(description = "企业ID") @PathVariable Long id,
            @Parameter(description = "认证状态") @RequestParam Integer authStatus,
            @Parameter(description = "认证失败原因") @RequestParam(required = false) String authFailReason,
            @Parameter(description = "操作员ID") @RequestParam Long operatorId) {
        
        try {
            CompanyDTO auditedCompany = companyService.auditCompany(id, authStatus, authFailReason, operatorId);
            return ResponseUtil.success("审核企业认证成功", auditedCompany);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "更新企业状态", description = "更新企业状态")
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateCompanyStatus(
            @Parameter(description = "企业ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Integer status) {
        
        try {
            CompanyDTO updatedCompany = companyService.updateCompanyStatus(id, status);
            return ResponseUtil.success("更新企业状态成功", updatedCompany);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "统计企业数量", description = "根据状态统计企业数量")
    @GetMapping("/count")
    public ResponseEntity<?> countCompanies(
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        try {
            Long count = companyService.countCompanies(status);
            return ResponseUtil.success("统计企业数量成功", count);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "统计认证企业数量", description = "根据认证状态统计企业数量")
    @GetMapping("/count/auth")
    public ResponseEntity<?> countAuthCompanies(
            @Parameter(description = "认证状态") @RequestParam Integer authStatus) {
        try {
            Long count = companyService.countAuthCompanies(authStatus);
            return ResponseUtil.success("统计认证企业数量成功", count);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "检查企业名称是否存在", description = "检查企业名称是否已存在")
    @GetMapping("/check/name")
    public ResponseEntity<?> checkCompanyNameExists(
            @Parameter(description = "企业名称") @RequestParam String companyName) {
        try {
            boolean exists = companyService.isCompanyNameExists(companyName);
            return ResponseUtil.success("检查企业名称成功", exists);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "检查营业执照号是否存在", description = "检查营业执照号是否已存在")
    @GetMapping("/check/license")
    public ResponseEntity<?> checkBusinessLicenseNoExists(
            @Parameter(description = "营业执照号") @RequestParam String businessLicenseNo) {
        try {
            boolean exists = companyService.isBusinessLicenseNoExists(businessLicenseNo);
            return ResponseUtil.success("检查营业执照号成功", exists);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @Operation(summary = "获取企业选项列表", description = "用于用户关联公司时查询公司列表，支持名称搜索")
    @GetMapping("/options")
    public ResponseEntity<?> getCompanyOptions(
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<CompanyOptionDTO> companies = companyService.getCompanyOptions(keyword, pageable);
            
            return ResponseUtil.successPage(companies, "获取企业选项列表成功", keyword);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
}
