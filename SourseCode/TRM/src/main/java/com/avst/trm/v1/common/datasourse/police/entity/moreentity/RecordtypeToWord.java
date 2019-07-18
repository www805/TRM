package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtype;

import java.util.List;

public class RecordtypeToWord extends Police_recordtype {
    List<WordTemplate> wordTemplates;

    public List<WordTemplate> getWordTemplates() {
        return wordTemplates;
    }

    public void setWordTemplates(List<WordTemplate> wordTemplates) {
        this.wordTemplates = wordTemplates;
    }
}
