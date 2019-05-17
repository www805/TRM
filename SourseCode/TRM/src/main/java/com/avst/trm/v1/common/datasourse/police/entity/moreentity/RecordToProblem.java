package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_answer;
import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtoproblem;

import java.util.List;

/**
 * 笔录题目数据
 */
public class RecordToProblem extends Police_recordtoproblem {
    private List<Police_answer> answers;//答案集合

    public List<Police_answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Police_answer> answers) {
        this.answers = answers;
    }
}
