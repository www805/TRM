package com.avst.trm.v1.common.util.poiwork.param;

import java.util.List;

/**
 * 问答实体类
 */
public class Talk {

    private String question;

    private List<Answer> answers;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
