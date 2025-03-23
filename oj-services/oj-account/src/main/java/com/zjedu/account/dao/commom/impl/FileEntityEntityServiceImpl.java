package com.zjedu.account.dao.commom.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.account.dao.commom.FileEntityService;
import com.zjedu.account.mapper.FileMapper;
import com.zjedu.pojo.entity.common.File;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/23 21:23
 * @Version 1.0
 * @Description
 */

@Service
public class FileEntityEntityServiceImpl extends ServiceImpl<FileMapper, File> implements FileEntityService
{

    @Resource
    private FileMapper fileMapper;

    @Override
    public List<File> queryCarouselFileList()
    {
        return fileMapper.queryCarouselFileList();
    }
}
