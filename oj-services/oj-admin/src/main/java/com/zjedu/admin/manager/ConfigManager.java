package com.zjedu.admin.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.zjedu.admin.dao.FileEntityService;
import com.zjedu.admin.feign.PassportFeignClient;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.config.NacosSwitchConfig;
import com.zjedu.config.SwitchConfig;
import com.zjedu.config.WebConfig;
import com.zjedu.pojo.dto.DBAndRedisConfigDTO;
import com.zjedu.pojo.dto.SwitchConfigDTO;
import com.zjedu.pojo.dto.WebConfigDTO;
import com.zjedu.pojo.entity.common.File;
import com.zjedu.pojo.vo.ConfigVO;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.utils.ConfigUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


/**
 * @Author Zhong
 * @Create 2025/4/6 15:00
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class ConfigManager
{
    @Resource
    private DiscoveryClient discoveryClient;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private HttpServletRequest request;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private NacosSwitchConfig nacosSwitchConfig;

    @Resource
    private FileEntityService fileEntityService;

    @Resource
    private ConfigVO configVo;

    @Resource
    private ConfigUtils configUtils;

    @Value("${spring.application.name}")
    private String currentServiceName;

    @Value("http://${spring.cloud.nacos.discovery.server-addr}")
    private String NACOS_URL;

    @Value("${service-url.name}")
    private String judgeServiceName;

    @Value("${NACOS_CONFIG_USERNAME}")
    private String nacosUsername;

    @Value("${NACOS_CONFIG_PASSWORD}")
    private String nacosPassword;

    @Value("${spring.cloud.nacos.config.prefix}")
    private String prefix;

    @Value("${spring.profiles.active}")
    private String active;

    @Value("${spring.cloud.nacos.config.file-extension}")
    private String fileExtension;

    @Value("${spring.cloud.nacos.discovery.group}")
    private String GROUP;

    @Value("${spring.cloud.nacos.config.type}")
    private String TYPE;


    /**
     * 获取当前服务的相关信息以及当前系统的cpu情况，内存使用情况
     *
     * @return
     */
    public JSONObject getServiceInfo() throws StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限操作!");
        }

        JSONObject result = new JSONObject();

        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(currentServiceName);

        // 获取nacos中心配置所在的机器环境
        String response = restTemplate.getForObject(NACOS_URL + "/nacos/v1/ns/operator/metrics", String.class);

        JSONObject jsonObject = JSONUtil.parseObj(response);
        // 获取当前数据后台所在机器环境
        // 当前机器的cpu核数
        int cores = Runtime.getRuntime().availableProcessors();

        // 获取CPU使用率
        String percentCpuLoad = "0.00%"; // 默认值
        try
        {
            Process process;
            if (System.getProperty("os.name").toLowerCase().contains("windows"))
            {
                // Windows系统
                process = new ProcessBuilder("wmic", "cpu", "get", "loadpercentage").start();
            } else
            {
                // Linux/Unix系统
                process = new ProcessBuilder("sh", "-c", "top -bn1 | grep 'Cpu(s)' | awk '{print $2}'").start();
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    if (line.trim().matches("\\d+(\\.\\d+)?"))
                    {
                        double cpuLoad = Double.parseDouble(line.trim());
                        percentCpuLoad = String.format("%.2f", cpuLoad) + "%";
                        break;
                    }
                }
            }
        } catch (Exception e)
        {
            // 出错时使用默认值
            System.err.println("获取CPU使用率失败: " + e.getMessage());
        }

        // 内存使用率
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        long used = heapMemoryUsage.getUsed();
        long max = heapMemoryUsage.getMax();
        double memoryUsageRate = (max > 0) ? ((double) used / max) * 100 : ((double) used / heapMemoryUsage.getCommitted()) * 100;
        String percentMemoryLoad = String.format("%.2f", memoryUsageRate) + "%";

        result.set("nacos", jsonObject);
        result.set("backupCores", cores);
        result.set("backupService", serviceInstances);
        result.set("backupPercentCpuLoad", percentCpuLoad);
        result.set("backupPercentMemoryLoad", percentMemoryLoad);
        return result;
    }

    public List<JSONObject> getJudgeServiceInfo() throws StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限操作!");
        }

        List<JSONObject> serviceInfoList = new LinkedList<>();
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(judgeServiceName);
        for (ServiceInstance serviceInstance : serviceInstances)
        {
            try
            {
                String uri = "http://192.168.88.101:8020";
                String result = "";
                if (serviceInstance.getUri().toString().equals(uri))
                {
                    result = restTemplate.getForObject("http://47.98.112.208:8020/api/judge/get-sys-config", String.class);
                } else
                {
                    result = restTemplate.getForObject(serviceInstance.getUri() + "/api/judge/get-sys-config", String.class);
                }
                JSONObject jsonObject = JSONUtil.parseObj(result, false);
                jsonObject.set("service", serviceInstance);
                serviceInfoList.add(jsonObject);
            } catch (Exception e)
            {
                log.error("[Admin Dashboard] get judge service info error, uri={}, error={}", serviceInstance.getUri(), e.getMessage());
            }
        }
        return serviceInfoList;
    }

    public WebConfigDTO getWebConfig() throws StatusForbiddenException
    {
        boolean b = checkRootAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限操作!");
        }

        WebConfig webConfig = nacosSwitchConfig.getWebConfig();
        return WebConfigDTO.builder()
                .baseUrl(UnicodeUtil.toString(webConfig.getBaseUrl()))
                .name(UnicodeUtil.toString(webConfig.getName()))
                .shortName(UnicodeUtil.toString(webConfig.getShortName()))
                .description(UnicodeUtil.toString(webConfig.getDescription()))
                .register(webConfig.getRegister())
                .recordName(UnicodeUtil.toString(webConfig.getRecordName()))
                .recordUrl(UnicodeUtil.toString(webConfig.getRecordUrl()))
                .projectName(UnicodeUtil.toString(webConfig.getProjectName()))
                .projectUrl(UnicodeUtil.toString(webConfig.getProjectUrl()))
                .build();

    }

    public void deleteHomeCarousel(Long id) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkRootAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限操作!");
        }

        File imgFile = fileEntityService.getById(id);
        if (imgFile == null)
        {
            throw new StatusFailException("文件id错误，图片不存在");
        }
        boolean isOk = fileEntityService.removeById(id);
        if (isOk)
        {
            FileUtil.del(imgFile.getFilePath());
        } else
        {
            throw new StatusFailException("删除失败！");
        }
    }

    public void setWebConfig(WebConfigDTO config) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkRootAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限操作!");
        }

        WebConfig webConfig = nacosSwitchConfig.getWebConfig();

        if (StringUtils.hasText(config.getBaseUrl()))
        {
            webConfig.setBaseUrl(config.getBaseUrl());
        }
        if (StringUtils.hasText(config.getName()))
        {
            webConfig.setName(config.getName());
        }
        if (StringUtils.hasText(config.getShortName()))
        {
            webConfig.setShortName(config.getShortName());
        }
        if (StringUtils.hasText(config.getDescription()))
        {
            webConfig.setDescription(config.getDescription());
        }
        if (config.getRegister() != null)
        {
            webConfig.setRegister(config.getRegister());
        }
        if (StringUtils.hasText(config.getRecordName()))
        {
            webConfig.setRecordName(config.getRecordName());
        }
        if (StringUtils.hasText(config.getRecordUrl()))
        {
            webConfig.setRecordUrl(config.getRecordUrl());
        }
        if (StringUtils.hasText(config.getProjectName()))
        {
            webConfig.setProjectName(config.getProjectName());
        }
        if (StringUtils.hasText(config.getProjectUrl()))
        {
            webConfig.setProjectUrl(config.getProjectUrl());
        }
        boolean isOk = nacosSwitchConfig.publishWebConfig();
        if (!isOk)
        {
            throw new StatusFailException("修改失败");
        }
    }

    public DBAndRedisConfigDTO getDBAndRedisConfig() throws StatusForbiddenException
    {
        boolean b = checkRootAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限操作!");
        }

        return DBAndRedisConfigDTO.builder()
                .dbName(configVo.getMysqlDBName())
                .dbHost(configVo.getMysqlHost())
                .dbPort(configVo.getMysqlPort())
                .dbUsername(configVo.getMysqlUsername())
                .dbPassword(configVo.getMysqlPassword())
                .redisHost(configVo.getRedisHost())
                .redisPort(configVo.getRedisPort())
                .redisPassword(configVo.getRedisPassword())
                .build();
    }

    public void setDBAndRedisConfig(DBAndRedisConfigDTO config) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkRootAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限操作!");
        }

        if (StringUtils.hasText(config.getDbName()))
        {
            configVo.setMysqlDBName(config.getDbName());
        }

        if (StringUtils.hasText(config.getDbHost()))
        {
            configVo.setMysqlHost(config.getDbHost());
        }
        if (config.getDbPort() != null)
        {
            configVo.setMysqlPort(config.getDbPort());
        }
        if (StringUtils.hasText(config.getDbUsername()))
        {
            configVo.setMysqlUsername(config.getDbUsername());
        }
        if (StringUtils.hasText(config.getDbPassword()))
        {
            configVo.setMysqlPassword(config.getDbPassword());
        }

        if (StringUtils.hasText(config.getRedisHost()))
        {
            configVo.setRedisHost(config.getRedisHost());
        }

        if (config.getRedisPort() != null)
        {
            configVo.setRedisPort(config.getRedisPort());
        }
        if (StringUtils.hasText(config.getRedisPassword()))
        {
            configVo.setRedisPassword(config.getRedisPassword());
        }

        boolean isOk = sendNewConfigToNacos();

        if (!isOk)
        {
            throw new StatusFailException("修改失败");
        }
    }

    public boolean sendNewConfigToNacos()
    {

        Properties properties = new Properties();
        properties.put("serverAddr", NACOS_URL);

        // if need username and password to login
        properties.put("username", nacosUsername);
        properties.put("password", nacosPassword);

        com.alibaba.nacos.api.config.ConfigService configService = null;
        boolean isOK = false;
        try
        {
            configService = NacosFactory.createConfigService(properties);
            isOK = configService.publishConfig(prefix + "-" + active + "." + fileExtension, GROUP, configUtils.getConfigContent(), TYPE);
        } catch (NacosException e)
        {
            log.error("通过nacos修改网站配置异常--------------->{}", e.getMessage());
        }
        return isOK;
    }

    public SwitchConfigDTO getSwitchConfig() throws StatusForbiddenException
    {
        boolean b = checkRootAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限操作!");
        }

        SwitchConfig switchConfig = nacosSwitchConfig.getSwitchConfig();
        SwitchConfigDTO switchConfigDTO = new SwitchConfigDTO();
        BeanUtil.copyProperties(switchConfig, switchConfigDTO);
        return switchConfigDTO;
    }

    public void setSwitchConfig(SwitchConfigDTO config) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkRootAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限操作!");
        }

        SwitchConfig switchConfig = nacosSwitchConfig.getSwitchConfig();

        // 只保留三个必要的配置项
        if (config.getOpenPublicJudge() != null)
        {
            switchConfig.setOpenPublicJudge(config.getOpenPublicJudge());
        }

        if (config.getHideNonContestSubmissionCode() != null)
        {
            switchConfig.setHideNonContestSubmissionCode(config.getHideNonContestSubmissionCode());
        }

        if (config.getDefaultSubmitInterval() != null)
        {
            if (config.getDefaultSubmitInterval() >= 0)
            {
                switchConfig.setDefaultSubmitInterval(config.getDefaultSubmitInterval());
            } else
            {
                switchConfig.setDefaultSubmitInterval(0);
            }
        }

        boolean isOk = nacosSwitchConfig.publishSwitchConfig();
        if (!isOk)
        {
            throw new StatusFailException("修改失败");
        }
    }

    private boolean checkRootAuthority()
    {
        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRoles = passportFeignClient.getUserRoles(userId, "");
        // 是否为超级管理员
        return userRoles.getRoles().stream()
                .anyMatch(role -> "root".equals(role.getRole()));
    }

    private boolean checkAuthority()
    {
        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRoles = passportFeignClient.getUserRoles(userId, "");
        // 是否为超级管理员
        boolean isRoot = userRoles.getRoles().stream()
                .anyMatch(role -> "root".equals(role.getRole()));
        // 是否为admin
        boolean isAdmin = userRoles.getRoles().stream()
                .anyMatch(role -> "admin".equals(role.getRole()));
        // 是否为problem_admin
        boolean isProblemAdmin = userRoles.getRoles().stream()
                .anyMatch(role -> "problem_admin".equals(role.getRole()));

        return isRoot || isAdmin || isProblemAdmin;
    }

}
