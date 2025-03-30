package com.zjedu.training.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.pojo.entity.training.Training;
import com.zjedu.pojo.vo.TrainingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:45
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface TrainingMapper extends BaseMapper<Training>
{
    List<TrainingVO> getTrainingList(IPage page,
                                     @Param("categoryId") Long categoryId,
                                     @Param("auth") String auth,
                                     @Param("keyword") String keyword);
}
