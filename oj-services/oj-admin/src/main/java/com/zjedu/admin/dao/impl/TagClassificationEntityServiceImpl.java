package com.zjedu.admin.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.admin.dao.TagClassificationEntityService;
import com.zjedu.admin.mapper.TagClassificationMapper;
import com.zjedu.pojo.entity.problem.TagClassification;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/2 20:13
 * @Version 1.0
 * @Description
 */

@Service
public class TagClassificationEntityServiceImpl extends ServiceImpl<TagClassificationMapper, TagClassification> implements TagClassificationEntityService
{
}