package com.zjedu.admin.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.admin.dao.FileEntityService;
import com.zjedu.admin.mapper.FileMapper;
import com.zjedu.pojo.entity.common.File;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/6 16:02
 * @Version 1.0
 * @Description
 */

@Service
public class FileEntityEntityServiceImpl extends ServiceImpl<FileMapper, File> implements FileEntityService
{
}
