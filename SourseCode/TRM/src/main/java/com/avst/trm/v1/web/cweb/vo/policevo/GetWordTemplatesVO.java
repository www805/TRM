package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.WordTemplate;

import java.util.List;

public class GetWordTemplatesVO {
   private  List<WordTemplate> wordTemplates;//集合

    private String default_word;//原始默认

    public String getDefault_word() {
        return default_word;
    }

    public void setDefault_word(String default_word) {
        this.default_word = default_word;
    }

    public List<WordTemplate> getWordTemplates() {
        return wordTemplates;
    }

    public void setWordTemplates(List<WordTemplate> wordTemplates) {
        this.wordTemplates = wordTemplates;
    }
}
