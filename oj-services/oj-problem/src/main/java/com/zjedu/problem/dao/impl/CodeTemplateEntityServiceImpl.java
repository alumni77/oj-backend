package com.zjedu.problem.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.problem.CodeTemplate;
import com.zjedu.problem.dao.CodeTemplateEntityService;
import com.zjedu.problem.mapper.CodeTemplateMapper;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/29 15:14
 * @Version 1.0
 * @Description
 */

@Service
public class CodeTemplateEntityServiceImpl extends ServiceImpl<CodeTemplateMapper, CodeTemplate> implements CodeTemplateEntityService
{
}