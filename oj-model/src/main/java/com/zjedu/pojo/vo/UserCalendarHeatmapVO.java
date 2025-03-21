package com.zjedu.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/21 15:42
 * @Version 1.0
 * @Description
 */

@Data
@Schema(name = "用户主页的提交热力图数据类UserCalendarHeatmapVo", description = "")
public class UserCalendarHeatmapVO implements Serializable
{

    @Schema(description = "结尾日期 例如 2022-02-02")
    private String endDate;

    @Schema(description = "日期对应的提交次数数据列表")
    private List<HashMap<String, Object>> dataList;
}