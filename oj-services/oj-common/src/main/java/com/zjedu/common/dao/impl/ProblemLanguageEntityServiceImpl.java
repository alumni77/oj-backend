package com.zjedu.common.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.common.dao.ProblemLanguageEntityService;
import com.zjedu.common.mapper.ProblemLanguageMapper;
import com.zjedu.pojo.entity.problem.ProblemLanguage;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/30 14:36
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemLanguageEntityServiceImpl extends ServiceImpl<ProblemLanguageMapper, ProblemLanguage> implements ProblemLanguageEntityService
{
}