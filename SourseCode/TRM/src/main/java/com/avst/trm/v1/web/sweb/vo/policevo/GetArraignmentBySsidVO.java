package com.avst.trm.v1.web.sweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_keyword;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordToProblem;

import java.util.List;

public class GetArraignmentBySsidVO {
    private  List<RecordToProblem> problems;//笔录模板：问题和答案

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

    public List<RecordToProblem> getProblems() {
        return problems;
    }

    public void setProblems(List<RecordToProblem> problems) {
        this.problems = problems;
    }
}
