package com.zjedu.file.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.file.dao.FileEntityService;
import com.zjedu.file.mapper.FileMapper;
import com.zjedu.pojo.entity.common.File;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/30 20:17
 * @Version 1.0
 * @Description
 */

@Service
public class FileEntityEntityServiceImpl extends ServiceImpl<FileMapper, File> implements FileEntityService
{
    @Resource
    private FileMapper fileMapper;

    @Override
    public int updateFileToDeleteByUidAndType(String uid, String type)
    {
        return fileMapper.updateFileToDeleteByUidAndType(uid, type);
    }
}
