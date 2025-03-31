package com.zjedu.file.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.file.dao.LanguageEntityService;
import com.zjedu.file.mapper.LanguageMapper;
import com.zjedu.pojo.entity.problem.Language;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/31 11:05
 * @Version 1.0
 * @Description
 */

@Service
public class LanguageEntityServiceImpl extends ServiceImpl<LanguageMapper, Language> implements LanguageEntityService
{
}