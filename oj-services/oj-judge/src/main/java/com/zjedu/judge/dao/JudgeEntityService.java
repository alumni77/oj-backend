package com.zjedu.judge.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.vo.JudgeVO;

/**
 * @Author Zhong
 * @Create 2025/3/25 14:06
 * @Version 1.0
 * @Description 服务类
 */

public interface JudgeEntityService extends IService<Judge>
{
    IPage<JudgeVO> getCommonJudgeList(Integer limit,
                                      Integer currentPage,
                                      String searchPid,
                                      Integer status,
                                      String username,
                                      String uid,
                                      Boolean completeProblemID,
                                      Long gid);
}