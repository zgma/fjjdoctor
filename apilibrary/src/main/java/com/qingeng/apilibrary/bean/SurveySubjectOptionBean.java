package com.qingeng.apilibrary.bean;

public class SurveySubjectOptionBean extends BaseBean {

    private String optionContent;
    private int rankSort;
    private int id;
    private int subjectId;


    public String getOptionContent() {
        return optionContent;
    }

    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }

    public int getRankSort() {
        return rankSort;
    }

    public void setRankSort(int rankSort) {
        this.rankSort = rankSort;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
