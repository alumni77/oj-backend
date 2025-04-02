package com.zjedu.admin.service.impl;

import com.zjedu.admin.manager.AdminTagManager;
import com.zjedu.admin.service.AdminTagService;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.pojo.entity.problem.Tag;
import com.zjedu.pojo.entity.problem.TagClassification;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/2 19:36
 * @Version 1.0
 * @Description
 */

@Service
public class AdminTagServiceImpl implements AdminTagService
{
    @Resource
    private AdminTagManager adminTagManager;

    @Override
    public CommonResult<Tag> addTag(Tag tag)
    {
        try
        {
            return CommonResult.successResponse(adminTagManager.addTag(tag));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> updateTag(Tag tag)
    {
        try
        {
            adminTagManager.updateTag(tag);
            return CommonResult.successResponse();
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> deleteTag(Long id)
    {
        try
        {
            adminTagManager.deleteTag(id);
            return CommonResult.successResponse();
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<List<TagClassification>> getTagClassification(String oj)
    {
        try
        {
            return CommonResult.successResponse(adminTagManager.getTagClassification(oj));
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }

    }

    @Override
    public CommonResult<TagClassification> addTagClassification(TagClassification tagClassification)
    {
        try
        {
            return CommonResult.successResponse(adminTagManager.addTagClassification(tagClassification));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> updateTagClassification(TagClassification tagClassification)
    {
        try
        {
            adminTagManager.updateTagClassification(tagClassification);
            return CommonResult.successResponse();
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> deleteTagClassification(Long id)
    {
        try
        {
            adminTagManager.deleteTagClassification(id);
            return CommonResult.successResponse();
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }
}
