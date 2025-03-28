package com.zjedu.problem.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.pojo.vo.ProblemVO;
import com.zjedu.problem.dao.ProblemEntityService;
import com.zjedu.utils.Constants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/28 20:46
 * @Version 1.0
 * @Description
 */

@Component
public class ProblemManager
{
    @Resource
    private ProblemEntityService problemEntityService;

    public Page<ProblemVO> getProblemList(Integer limit, Integer currentPage,
                                          String keyword, List<Long> tagId, Integer difficulty, String oj)
    {
        // 页数，每页题数若为空，设置默认值
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        // 关键词查询不为空
        if (StringUtils.hasText(keyword))
        {
            keyword = keyword.trim();
        }
        if (oj != null && !Constants.RemoteOJ.isRemoteOJ(oj))
        {
            oj = "Mine";
        }
        return problemEntityService.getProblemList(limit, currentPage, null, keyword,
                difficulty, tagId, oj);
    }
}
