package com.zjedu.account.dao.judge;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.vo.ProblemCountVO;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/21 14:41
 * @Version 1.0
 * @Description 服务类
 */

public interface JudgeEntityService extends IService<Judge>
{
    List<ProblemCountVO> getProblemListCount(List<Long> pidList);

}
