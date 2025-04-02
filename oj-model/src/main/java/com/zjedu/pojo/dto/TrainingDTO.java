package com.zjedu.pojo.dto;

import com.zjedu.pojo.entity.training.Training;
import com.zjedu.pojo.entity.training.TrainingCategory;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author Zhong
 * @Create 2025/4/2 21:33
 * @Version 1.0
 * @Description 后台管理训练的传输类
 */

@Data
@Accessors(chain = true)
public class TrainingDTO
{

    private Training training;

    private TrainingCategory trainingCategory;
}