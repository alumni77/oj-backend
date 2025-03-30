package com.zjedu.common.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.common.dao.TagEntityService;
import com.zjedu.common.mapper.TagMapper;
import com.zjedu.pojo.entity.problem.Tag;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/30 14:08
 * @Version 1.0
 * @Description
 */

@Service
public class TagEntityServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagEntityService
{

}