package com.zjedu.common.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.common.dao.CodeTemplateEntityService;
import com.zjedu.common.mapper.CodeTemplateMapper;
import com.zjedu.pojo.entity.problem.CodeTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/30 14:40
 * @Version 1.0
 * @Description
 */

@Service
public class CodeTemplateEntityServiceImpl extends ServiceImpl<CodeTemplateMapper, CodeTemplate> implements CodeTemplateEntityService
{
}