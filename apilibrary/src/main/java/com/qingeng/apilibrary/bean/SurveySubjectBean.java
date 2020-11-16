package com.qingeng.apilibrary.bean;

import java.util.List;

public class SurveySubjectBean extends BaseBean {

    private String createTime;
    private String subjectName;
    private int subjectType; //1填空 2单选 3多选
    private int rankSort;
    private int id;
    private int modelId;
    private List<SurveySubjectOptionBean> surveyOptionList;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(int subjectType) {
        this.subjectType = subjectType;
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

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public List<SurveySubjectOptionBean> getSurveyOptionList() {
        return surveyOptionList;
    }

    public void setSurveyOptionList(List<SurveySubjectOptionBean> surveyOptionList) {
        this.surveyOptionList = surveyOptionList;
    }
}
