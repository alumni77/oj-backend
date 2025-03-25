package com.zjedu.judge.service.impl;

import com.zjedu.judge.service.SystemConfigService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;

/**
 * @Author Zhong
 * @Create 2025/3/25 20:28
 * @Version 1.0
 * @Description
 */

@Service
public class SystemConfigServiceImpl implements SystemConfigService
{

    public HashMap<String, Object> getSystemConfig()
    {
        HashMap<String, Object> result = new HashMap<String, Object>();

        int cpuCores = Runtime.getRuntime().availableProcessors(); // cpu核数

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

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        long used = heapMemoryUsage.getUsed();
        long max = heapMemoryUsage.getMax();
        double memoryUsageRate = (max > 0) ? ((double) used / max) * 100 : ((double) used / heapMemoryUsage.getCommitted()) * 100;
        String percentMemoryLoad = String.format("%.2f", memoryUsageRate) + "%"; // 内存使用率


        result.put("cpuCores", cpuCores);
        result.put("percentCpuLoad", percentCpuLoad);
        result.put("percentMemoryLoad", percentMemoryLoad);
        return result;
    }

}