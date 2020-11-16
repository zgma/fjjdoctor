package com.qingeng.apilibrary.bean;

import java.util.List;

public class SurveyBean extends BaseBean {

    private String createTime;
    private String modelName;
    private String modelAgreement;
    private int examineState;
    private int rankSort;
    private int id;
    private int userId;
    private List<SurveySubjectBean> surveySubjectList;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelAgreement() {
        return modelAgreement;
    }

    public void setModelAgreement(String modelAgreement) {
        this.modelAgreement = modelAgreement;
    }

    public int getExamineState() {
        return examineState;
    }

    public void setExamineState(int examineState) {
        this.examineState = examineState;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<SurveySubjectBean> getSurveySubjectList() {
        return surveySubjectList;
    }

    public void setSurveySubjectList(List<SurveySubjectBean> surveySubjectList) {
        this.surveySubjectList = surveySubjectList;
    }
}
