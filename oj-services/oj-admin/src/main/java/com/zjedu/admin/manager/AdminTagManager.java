package com.zjedu.admin.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjedu.admin.dao.TagClassificationEntityService;
import com.zjedu.admin.dao.TagEntityService;
import com.zjedu.admin.feign.PassportFeignClient;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.pojo.entity.problem.Tag;
import com.zjedu.pojo.entity.problem.TagClassification;
import com.zjedu.pojo.vo.UserRolesVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/2 19:37
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class AdminTagManager
{
    @Resource
    private HttpServletRequest request;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private TagEntityService tagEntityService;

    @Resource
    private TagClassificationEntityService tagClassificationEntityService;

    public Tag addTag(Tag tag) throws StatusForbiddenException, StatusFailException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("权限不足，无法添加标签！");
        }


        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("name", tag.getName())
                .eq("oj", tag.getOj());
        Tag existTag = tagEntityService.getOne(tagQueryWrapper, false);

        if (existTag != null)
        {
            throw new StatusFailException("该标签名称已存在！请勿重复添加！");
        }

        boolean isOk = tagEntityService.save(tag);
        if (!isOk)
        {
            throw new StatusFailException("添加失败");
        }
        return tag;
    }

    public void updateTag(Tag tag) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("权限不足，无法更新标签！");
        }

        if (tag.getId() == null)
        {
            throw new StatusFailException("标签ID不能为空！");
        }

        boolean isOk = tagEntityService.updateById(tag);
        if (!isOk)
        {
            throw new StatusFailException("更新失败");
        }
    }

    public void deleteTag(Long id) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("权限不足，无法删除标签！");
        }

        boolean isOk = tagEntityService.removeById(id);
        if (!isOk)
        {
            throw new StatusFailException("删除失败");
        }
        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRolesVo = passportFeignClient.getUserRoles(userId, "");
        log.info("[{}],[{}],tid:[{}],operatorUid:[{}],operatorUsername:[{}]",
                "Admin_Tag", "Delete", id, userRolesVo.getUid(), userRolesVo.getUsername());
    }

    public List<TagClassification> getTagClassification(String oj) throws StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("权限不足，无法获取标签分类！");
        }

        oj = oj.toUpperCase();
        if (oj.equals("ALL"))
        {
            return tagClassificationEntityService.list();
        } else
        {
            QueryWrapper<TagClassification> tagClassificationQueryWrapper = new QueryWrapper<>();
            tagClassificationQueryWrapper.eq("oj", oj).orderByAsc("`rank`");
            return tagClassificationEntityService.list(tagClassificationQueryWrapper);
        }
    }

    public TagClassification addTagClassification(TagClassification tagClassification) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("权限不足，无法添加标签分类！");
        }

        QueryWrapper<TagClassification> tagClassificationQueryWrapper = new QueryWrapper<>();
        tagClassificationQueryWrapper.eq("name", tagClassification.getName())
                .eq("oj", tagClassification.getOj());
        TagClassification existTagClassification = tagClassificationEntityService.getOne(tagClassificationQueryWrapper, false);

        if (existTagClassification != null)
        {
            throw new StatusFailException("该标签分类名称已存在！请勿重复！");
        }
        boolean isOk = tagClassificationEntityService.save(tagClassification);
        if (!isOk)
        {
            throw new StatusFailException("添加失败");
        }
        return tagClassification;
    }

    public void updateTagClassification(TagClassification tagClassification) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("权限不足，无法更新标签分类！");
        }

        if (tagClassification.getId() == null)
        {
            throw new StatusFailException("标签分类ID不能为空！");
        }

        boolean isOk = tagClassificationEntityService.updateById(tagClassification);
        if (!isOk)
        {
            throw new StatusFailException("更新失败");
        }
    }

    public void deleteTagClassification(Long id) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("权限不足，无法更新标签分类！");
        }

        boolean isOk = tagClassificationEntityService.removeById(id);
        if (!isOk)
        {
            throw new StatusFailException("删除失败");
        }

        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRolesVo = passportFeignClient.getUserRoles(userId, "");
        log.info("[{}],[{}],tcid:[{}],operatorUid:[{}],operatorUsername:[{}]",
                "Admin_Tag_Classification", "Delete", id, userRolesVo.getUid(), userRolesVo.getUsername());
    }

    private boolean checkAuthority()
    {
        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRoles = passportFeignClient.getUserRoles(userId, "");
        // 是否为超级管理员
        boolean isRoot = userRoles.getRoles().stream()
                .anyMatch(role -> "root".equals(role.getRole()));
        // 是否为problem_admin
        boolean isProblemAdmin = userRoles.getRoles().stream()
                .anyMatch(role -> "problem_admin".equals(role.getRole()));

        return isRoot || isProblemAdmin;
    }
}
