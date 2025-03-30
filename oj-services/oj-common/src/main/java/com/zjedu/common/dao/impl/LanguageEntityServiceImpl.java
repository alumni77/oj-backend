package com.zjedu.common.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.common.dao.LanguageEntityService;
import com.zjedu.common.mapper.LanguageMapper;
import com.zjedu.pojo.entity.problem.Language;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/30 14:31
 * @Version 1.0
 * @Description
 */

@Service
public class LanguageEntityServiceImpl extends ServiceImpl<LanguageMapper, Language> implements LanguageEntityService
{
}