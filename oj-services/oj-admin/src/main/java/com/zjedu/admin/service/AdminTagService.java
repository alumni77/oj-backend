package com.zjedu.admin.service;

import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.entity.problem.Tag;
import com.zjedu.pojo.entity.problem.TagClassification;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/2 19:36
 * @Version 1.0
 * @Description
 */

public interface AdminTagService
{
    CommonResult<Tag> addTag(Tag tag);

    CommonResult<Void> updateTag(Tag tag);

    CommonResult<Void> deleteTag(Long id);

    CommonResult<List<TagClassification>> getTagClassification(String oj);

    CommonResult<TagClassification> addTagClassification(TagClassification tagClassification);

    CommonResult<Void> updateTagClassification(TagClassification tagClassification);

    CommonResult<Void> deleteTagClassification(Long id);
}
