package com.zjedu.judgeserve.judge;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjedu.judgeserve.dao.judge.JudgeServerEntityService;
import com.zjedu.pojo.entity.judge.JudgeServer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/26 21:51
 * @Version 1.0
 * @Description 筛选可用判题机
 */

@Component
@Slf4j
public class ChooseUtils
{

    @Resource
    private NacosDiscoveryProperties discoveryProperties;

    @Value("${service-url.name}")
    private String JudgeServiceName;

    @Resource
    private JudgeServerEntityService judgeServerEntityService;

    /**
     * @MethodName chooseServer
     * @Description 选择可以用调用判题的判题服务器
     * @Return JudgeServer 返回可用的判题服务器
     */
    @Transactional(rollbackFor = Exception.class)
    public JudgeServer chooseServer(Boolean isRemote)
    {
        // 获取该微服务的所有健康实例
        List<Instance> instances = getInstances(JudgeServiceName);
        if (instances.size() <= 0)
        {
            return null;
        }
        List<String> keyList = new ArrayList<>();
        // 获取当前健康实例取出ip和port拼接
        for (Instance instance : instances)
        {
            keyList.add(instance.getIp() + ":" + instance.getPort());
        }

        // 过滤出小于或等于规定最大并发判题任务数的服务实例且健康的判题机
        /*
          如果一个条件无法通过索引快速过滤，存储引擎层面就会将所有记录加锁后返回，
          再由MySQL Server层进行过滤，但在实际使用过程当中，MySQL做了一些改进，
          在MySQL Server过滤条件，发现不满足后，会调用unlock_row方法，
          把不满足条件的记录释放锁 (违背了二段锁协议的约束)。
         */
        boolean isRemoteBool = isRemote != null && isRemote; //避免空值
        int isRemoteInt = isRemoteBool ? 1 : 0; //MySQL tinyint类型 只能用0或1
        QueryWrapper<JudgeServer> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("url", keyList)
                .eq("is_remote", isRemoteInt)
                .orderByDesc("task_number")
                .last("for update"); //开启悲观锁
        List<JudgeServer> judgeServerList = judgeServerEntityService.list(queryWrapper);

        // 获取可用判题机
        for (JudgeServer judgeServer : judgeServerList)
        {
            if (judgeServer.getTaskNumber() < judgeServer.getMaxTaskNumber())
            {
                judgeServer.setTaskNumber(judgeServer.getTaskNumber() + 1);
                boolean isOk = judgeServerEntityService.updateById(judgeServer);
                if (isOk)
                {
                    return judgeServer;
                }
            }
        }

        return null;
    }

    /**
     * @param serviceId 服务ID
     * @MethodName getInstances
     * @Description 根据服务id获取对应的健康实例列表
     * @Return List<Instance> 健康实例列表
     */
    private List<Instance> getInstances(String serviceId)
    {
        // 获取服务发现的相关API
        NamingService namingService = discoveryProperties.namingServiceInstance();
        try
        {
            // 获取该微服务的所有健康实例
            return namingService.selectInstances(serviceId, true);
        } catch (NacosException e)
        {
            log.error("获取微服务健康实例发生异常---------> {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}