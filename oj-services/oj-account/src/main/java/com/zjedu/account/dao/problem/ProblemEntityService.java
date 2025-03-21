package com.zjedu.account.dao.problem;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.vo.ProblemVO;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/21 14:27
 * @Version 1.0
 * @Description
 */

public interface ProblemEntityService extends IService<Problem>
{
    Page<ProblemVO> getProblemList(int limit, int currentPage, Long pid, String title,
                                   Integer difficulty, List<Long> tid, String oj);

}
