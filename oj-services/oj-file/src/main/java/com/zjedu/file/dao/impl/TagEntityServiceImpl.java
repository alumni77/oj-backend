package com.zjedu.file.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.file.dao.TagEntityService;
import com.zjedu.file.mapper.TagMapper;
import com.zjedu.pojo.entity.problem.Tag;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/31 11:06
 * @Version 1.0
 * @Description
 */

@Service
public class TagEntityServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagEntityService
{

}