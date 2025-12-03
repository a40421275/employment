package com.shera.framework.employment.employment.modules.resume;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shera.framework.employment.employment.modules.resume.dto.ResumeUserDTO;
import com.shera.framework.employment.employment.modules.resume.entity.Resume;
import com.shera.framework.employment.employment.modules.resume.service.ResumeService;
import com.shera.framework.employment.employment.modules.user.entity.User;
import com.shera.framework.employment.employment.modules.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * 测试简历接口返回的用户信息安全
 */
@SpringBootTest
public class ResumeSecurityTest {

    @Autowired
    private ResumeService resumeService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testResumeUserInfoSecurity() throws Exception {
        // 模拟用户数据
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPhone("13800138000");
        user.setEmail("test@example.com");
        user.setPassword("encrypted_password");
        user.setWxOpenid("wx_openid_123");
        user.setWxUnionid("wx_unionid_456");
        user.setCompanyId(100L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // 创建测试简历
        Resume resume = new Resume();
        resume.setId(1L);
        resume.setUserId(1L);
        resume.setTitle("测试简历");
        resume.setResumeType(2); // 结构化简历
        resume.setStructuredData("{}");

        // 设置安全的用户信息
        ResumeUserDTO safeUser = new ResumeUserDTO();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());

        // 序列化为JSON
        String json = objectMapper.writeValueAsString(safeUser);

        // 验证不包含敏感信息
        assertFalse(json.contains("password"), "不应该包含密码");
        assertFalse(json.contains("wxOpenid"), "不应该包含微信OpenID");
        assertFalse(json.contains("wxUnionid"), "不应该包含微信UnionID");
        assertFalse(json.contains("companyId"), "不应该包含企业ID");
        assertFalse(json.contains("createTime"), "不应该包含创建时间");
        assertFalse(json.contains("updateTime"), "不应该包含更新时间");

        // 验证包含安全信息
        assertTrue(json.contains("username"), "应该包含用户名");
        assertTrue(json.contains("phone"), "应该包含手机号");
        assertTrue(json.contains("email"), "应该包含邮箱");

        System.out.println("序列化后的JSON: " + json);
    }

    @Test
    public void testResumeUserDTOSecurity() throws Exception {
        // 创建安全的用户DTO
        ResumeUserDTO safeUser = new ResumeUserDTO();
        safeUser.setId(1L);
        safeUser.setUsername("testuser");
        safeUser.setPhone("13800138000");
        safeUser.setEmail("test@example.com");

        // 序列化为JSON
        String json = objectMapper.writeValueAsString(safeUser);

        // 验证不包含敏感信息
        assertFalse(json.contains("password"), "不应该包含密码");
        assertFalse(json.contains("wxOpenid"), "不应该包含微信OpenID");
        assertFalse(json.contains("wxUnionid"), "不应该包含微信UnionID");
        assertFalse(json.contains("companyId"), "不应该包含企业ID");
        assertFalse(json.contains("createTime"), "不应该包含创建时间");
        assertFalse(json.contains("updateTime"), "不应该包含更新时间");
        assertFalse(json.contains("idCardFront"), "不应该包含身份证正面");
        assertFalse(json.contains("idCardBack"), "不应该包含身份证背面");

        // 验证包含基本安全信息和用户资料信息
        assertTrue(json.contains("id"), "应该包含ID");
        assertTrue(json.contains("username"), "应该包含用户名");
        assertTrue(json.contains("phone"), "应该包含手机号");
        assertTrue(json.contains("email"), "应该包含邮箱");
        assertTrue(json.contains("realName"), "应该包含真实姓名");
        assertTrue(json.contains("gender"), "应该包含性别");
        assertTrue(json.contains("birthday"), "应该包含生日");
        assertTrue(json.contains("avatar"), "应该包含头像");
        assertTrue(json.contains("education"), "应该包含学历");
        assertTrue(json.contains("workYears"), "应该包含工作年限");
        assertTrue(json.contains("currentSalary"), "应该包含当前薪资");
        assertTrue(json.contains("expectedSalary"), "应该包含期望薪资");
        assertTrue(json.contains("city"), "应该包含城市");
        assertTrue(json.contains("selfIntro"), "应该包含自我介绍");

        System.out.println("安全的用户DTO JSON: " + json);
    }
}
