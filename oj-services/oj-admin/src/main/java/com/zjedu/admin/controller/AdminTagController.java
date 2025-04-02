package com.zjedu.admin.controller;

import com.zjedu.admin.service.AdminTagService;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.entity.problem.Tag;
import com.zjedu.pojo.entity.problem.TagClassification;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/2 19:36
 * @Version 1.0
 * @Description
 */

@RestController
@RequestMapping("/tag")
public class AdminTagController
{
    @Resource
    private AdminTagService adminTagService;

    @PostMapping("/")
    public CommonResult<Tag> addTag(@RequestBody Tag tag)
    {
        return adminTagService.addTag(tag);
    }

    @PutMapping("/")
    public CommonResult<Void> updateTag(@RequestBody Tag tag)
    {
        return adminTagService.updateTag(tag);
    }

    @DeleteMapping("/")
    public CommonResult<Void> deleteTag(@RequestParam("id") Long id)
    {
        return adminTagService.deleteTag(id);
    }

    @GetMapping("/classification")
    public CommonResult<List<TagClassification>> getTagClassification(@RequestParam(value = "oj", defaultValue = "ME") String oj)
    {
        return adminTagService.getTagClassification(oj);
    }

    @PostMapping("/classification")
    public CommonResult<TagClassification> addTagClassification(@RequestBody TagClassification tagClassification)
    {
        return adminTagService.addTagClassification(tagClassification);
    }

    @PutMapping("/classification")
    public CommonResult<Void> updateTagClassification(@RequestBody TagClassification tagClassification)
    {
        return adminTagService.updateTagClassification(tagClassification);
    }

    @DeleteMapping("/classification")
    public CommonResult<Void> deleteTagClassification(@RequestParam("id") Long id)
    {
        return adminTagService.deleteTagClassification(id);
    }


}
