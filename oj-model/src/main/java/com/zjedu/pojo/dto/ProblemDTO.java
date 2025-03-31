package com.zjedu.pojo.dto;

import com.zjedu.pojo.entity.problem.CodeTemplate;
import com.zjedu.pojo.entity.problem.Language;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.problem.Tag;
import com.zjedu.pojo.entity.problem.ProblemCase;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/31 11:00
 * @Version 1.0
 * @Description
 */

@Data
@Accessors(chain = true)
public class ProblemDTO
{

    private Problem problem;

    private List<ProblemCase> samples;

    private Boolean isUploadTestCase;

    private String uploadTestcaseDir;

    private String judgeMode;

    private Boolean changeModeCode;

    private Boolean changeJudgeCaseMode;

    private List<Language> languages;

    private List<Tag> tags;

    private List<CodeTemplate> codeTemplates;

}