package com.zjedu.pojo.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/31 13:51
 * @Version 1.0
 * @Description
 */

@Data
public class ExportProblemParamsDTO
{
    private List<HashMap<String, Object>> problemCaseList;
    private HashMap<Long, String> languageMap;
    private HashMap<Long, String> tagMap;
}