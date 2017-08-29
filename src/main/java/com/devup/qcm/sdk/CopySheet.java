package com.devup.qcm.sdk;

import istat.android.base.tools.ToolKits;

import org.json.JSONException;
import org.json.JSONObject;

import com.devup.qcm.sdk.interfaces.JSONable;
import com.devup.qcm.sdk.utils.QuestionnaireUtils;

public class CopySheet implements JSONable {
    Test mTest;

    public CopySheet(Test test, CopySheetAuthor author) {
        Questionnaire questionnaire = test.getCompletedQuestionnaire();
        questionnaire.setTime(test.timeLeft);
        questionnaire.created_at = ToolKits.Dates.simpleDateTime();
        questionnaire.updated_at = questionnaire.created_at;
        questionnaire.setAuthor(author);
        mTest = new Test(questionnaire);
    }

    private CopySheet(Questionnaire questionnaire) {
        mTest = new Test(questionnaire);
    }

    public static CopySheet newInstance(String json) throws JSONException {
        Questionnaire qcms = QuestionnaireUtils.JSONReader
                .questionnaireFromJSONObject(new JSONObject(json));
        return new CopySheet(qcms);
    }

    public String getPostURL() {
        return getQuestionnaire().getURLSet().qcmPostAnswersURL;
    }

    public Questionnaire getQuestionnaire() {
        return mTest.getQuestionnaire();
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = QuestionnaireUtils.JSONWriter
                .questionnaireToJSONObject(getQuestionnaire());
        //json.remove("info");
        json.remove("meta");
        json.remove("study_levels");
        json.remove("subjects");
        json.remove("urls");
        json.remove("keywords");
        json.remove("title");
        return json;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        try {
            return toJson().toString();
        } catch (Exception e) {
            return super.toString();
        }
    }

    public static class CopySheetAuthor extends QuestionnaireSummary.QuestionnaireAuthor {

        public CopySheetAuthor(String id, String name) {
            super(id, name);
            // TODO Auto-generated constructor stub
        }

    }

    public Test retrieveBaseTestFromOriginalQuestionnaire(Questionnaire baseqcms) {
        Test test = new Test(baseqcms);
        Questionnaire qcms = test.getQuestionnaire();
        Questionnaire mqcms = mTest.getQuestionnaire();
        qcms.setTime(mqcms.getInfo().questionnaireTime);
        qcms.setAuthor(mqcms.getAuthor());
        test.setExercises(mTest.getExercises());
        return test;
    }
}
