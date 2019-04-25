package com.avst.trm.v1.web.vo.policevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_keyword;
import com.avst.trm.v1.common.datasourse.police.entity.Police_record;
import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtemplate;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;

import java.util.List;

public class GetArraignmentBySsidVO {
    private List<Police_recordtemplate> recordtemplates;//全部模板

    private  List<Problem> problems;//笔录模板：问题和答案

    private Record record;//笔录数据

    private  List<Base_keyword> keywords;//全部关键字

    public List<Base_keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Base_keyword> keywords) {
        this.keywords = keywords;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }

    public List<Police_recordtemplate> getRecordtemplates() {
        return recordtemplates;
    }

    public void setRecordtemplates(List<Police_recordtemplate> recordtemplates) {
        this.recordtemplates = recordtemplates;
    }
}
