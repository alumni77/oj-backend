package com.zjedu.pojo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/28 16:23
 * @Version 1.0
 * @Description
 */

@Data
public class SubmitIdListDTO
{
    @NotEmpty(message = "查询的提交id列表不能为空")
    private List<Long> submitIds;
}