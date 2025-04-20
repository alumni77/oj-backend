package com.zjedu.pojo.dto;

import com.zjedu.pojo.entity.problem.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
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

    public List<Tag> getTags() {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags;
    }

    public List<Language> getLanguages() {
        if (languages == null) {
            languages = new ArrayList<>();
        }
        return languages;
    }

    public List<CodeTemplate> getCodeTemplates() {
        if (codeTemplates == null) {
            codeTemplates = new ArrayList<>();
        }
        return codeTemplates;
    }

}