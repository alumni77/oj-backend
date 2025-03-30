package com.zjedu.pojo.vo;

import com.zjedu.pojo.entity.problem.Tag;
import com.zjedu.pojo.entity.problem.TagClassification;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 14:18
 * @Version 1.0
 * @Description
 */

@Data
public class ProblemTagVO implements Serializable
{
    /**
     * 标签分类
     */
    private TagClassification classification;

    /**
     * 标签列表
     */
    private List<Tag> tagList;

}