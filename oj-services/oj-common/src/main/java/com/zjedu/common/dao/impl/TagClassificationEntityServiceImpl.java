package com.zjedu.common.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.common.dao.TagClassificationEntityService;
import com.zjedu.common.mapper.TagClassificationMapper;
import com.zjedu.pojo.entity.problem.TagClassification;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/30 14:21
 * @Version 1.0
 * @Description
 */

@Service
public class TagClassificationEntityServiceImpl extends ServiceImpl<TagClassificationMapper, TagClassification> implements TagClassificationEntityService
{
}