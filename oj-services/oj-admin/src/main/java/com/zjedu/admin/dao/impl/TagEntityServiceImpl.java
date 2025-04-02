package com.zjedu.admin.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.admin.dao.TagEntityService;
import com.zjedu.admin.mapper.TagMapper;
import com.zjedu.pojo.entity.problem.Tag;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/2 19:39
 * @Version 1.0
 * @Description
 */

@Service
public class TagEntityServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagEntityService
{

}
