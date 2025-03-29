package com.zjedu.problem.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.problem.Tag;
import com.zjedu.problem.dao.TagEntityService;
import com.zjedu.problem.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/29 15:05
 * @Version 1.0
 * @Description
 */

@Service
public class TagEntityServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagEntityService
{

}