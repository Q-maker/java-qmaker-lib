package com.devup.qcm.sdk.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.devup.qcm.sdk.Qcm;
import com.devup.qcm.sdk.QcmsFile;
import com.devup.qcm.sdk.Questionnaire;
import com.devup.qcm.sdk.QuestionnaireSummary;
import com.devup.qcm.sdk.StudyLevel;
import com.devup.qcm.sdk.Qcm.Question;
import com.devup.qcm.sdk.QuestionnaireSummary.QuestionnaireAuthor;
import com.devup.qcm.sdk.QuestionnaireSummary.QuestionnaireConfig;
import com.devup.qcm.sdk.QuestionnaireSummary.QuestionnaireInfo;
import com.devup.qcm.sdk.QuestionnaireSummary.QuestionnaireURLSet;
import com.devup.qcm.sdk.Subject;

import istat.android.base.tools.JSON;

public class QuestionnaireUtils {
    private QuestionnaireUtils() {

    }

    private static String createBinary(Questionnaire qcms) {
        qcms.getURLSet()
                .setIconURL(
                        ToolKits.optInternalQcmArchiveImagesRes(qcms
                                .getURLSet().iconURL));
        qcms.getURLSet().setQcmFileURL("");
        qcms.getURLSet()
                .setSoundURL(
                        ToolKits.optInternalQcmArchiveImagesRes(qcms
                                .getURLSet().soundURL));
        List<Qcm> qcmList = qcms.getQcms();
        for (Qcm qcm : qcmList) {
            Question question = qcm.getQuestion();
            question.setAnimationURL(ToolKits
                    .optInternalQcmArchiveImagesRes(question.getAnimationURL()));
            question.setImageURL(ToolKits
                    .optInternalQcmArchiveImagesRes(question.getImageURL()));
            question.setVideoURL(ToolKits
                    .optInternalQcmArchiveImagesRes(question.getVideoURL()));
            question.setFontURL(ToolKits
                    .optInternalQcmArchiveImagesRes(question.getFontURL()));

        }
        return qcms.toString();
    }

    private static String createSourceBinary(Questionnaire qcms) {
        return qcms.toString();
    }

    private static String createSummaryBinary(Questionnaire qcms) {
        qcms.eraseQcms();
        qcms.getURLSet()
                .setIconURL(
                        ToolKits.optInternalQcmArchiveImagesRes(qcms
                                .getURLSet().iconURL));
        qcms.getURLSet().setQcmFileURL("");
        qcms.getURLSet()
                .setSoundURL(
                        ToolKits.optInternalQcmArchiveImagesRes(qcms
                                .getURLSet().soundURL));
        return qcms.toString();
    }

    public static Questionnaire checkRandomShuffle(final Questionnaire qcms) {
        return checkRandomShuffle(qcms, qcms.getInfo().getMaxRandom());
    }

    public static Questionnaire checkRandomShuffle(final Questionnaire qcms,
                                                   int maxRandom) {
        List<Qcm> qcmList = qcms.getQcms();
        int qcmNumber = 0;
        int endSubIndex = 0;
        if (qcms.isRandomEnable()) {
            Collections.shuffle(qcmList);
            qcmNumber = qcmList.size();
            if ((endSubIndex = maxRandom) >= qcmNumber) {
                endSubIndex = qcmNumber;
            }
            List<Qcm> subList = qcmList.subList(0, endSubIndex);
            for (Qcm qcm : subList) {
                QcmUtils.shuffleQcmAnswer(qcm);
            }
            qcms.setQcms(subList);
        }
        return qcms;
    }

    public static Questionnaire customiseQuestionnaire(Questionnaire qcms,
                                                       boolean randomise, boolean randomiseAnswer, int max_question, int min_answer,
                                                       int max_answer, int min_answer_truth, int max_answer_truth) {
        List<Qcm> qcmList = qcms.getQcms();
        int qcmNumber = 0;
        int endSubIndex = 0;
        if (randomise) {
            qcms.getInfo().setMaxRandom(max_question);
            Collections.shuffle(qcmList);
        }
        qcmNumber = qcmList.size();
        if ((endSubIndex = max_question) >= qcmNumber) {
            endSubIndex = qcmNumber;
        }
        List<Qcm> filterResult = new ArrayList<Qcm>();
        for (Qcm qcm : qcmList) {
            if (qcm.getPropositions().size() >= min_answer
                    && QcmUtils.getPropositionsWithTruth(qcm, true).size() >= min_answer_truth) {
                filterResult.add(QcmUtils.customiseQcm(qcm, randomiseAnswer,
                        max_answer, max_answer_truth));
            }
        }
        List<Qcm> subList = filterResult;
        if (filterResult.size() > 0 && endSubIndex < filterResult.size()) {
            subList = filterResult.subList(0, endSubIndex);
        }
        qcms.setQcms(subList);

        return qcms;
    }

    public static Questionnaire mergeQuestionnaires(Questionnaire... qcms) {
        if (qcms.length <= 0)
            return null;
        Questionnaire qcms1 = qcms[0];
        for (int i = 1; i < qcms.length; i++) {
            qcms1.getQcms().addAll(qcms[i].getQcms());
            qcms1.getAuthor().name += qcms[i].getAuthor().name;
            qcms1.getAuthor().id += qcms[i].getAuthor().id;
        }
        qcms1.getAuthor().id = "MERGED::" + qcms1.getAuthor().id.hashCode();
        return qcms1;
    }

    public static QuestionnaireSummary optQuestionnaireSummary(
            Questionnaire qcms) {
        qcms.eraseQcms();
        return qcms;
    }

    public static Questionnaire readBinaryFile(String file, String decode)
            throws JSONException, FileNotFoundException {
        return readBinaryFile(new File(file), decode);
    }

    public static Questionnaire readBinaryFile(File file, String decode)
            throws JSONException, FileNotFoundException {
        String cripted = istat.android.base.tools.ToolKits.Stream.streamToString(new FileInputStream(
                file), "");
        String decripted = cripted;
        JSONObject json = new JSONObject(decripted);
        Questionnaire questionnaire = JSONReader
                .questionnaireFromJSONObject(json);
        questionnaire.getURLSet().iconURL = ToolKits
                .optAbsoluteQuestionnaireResURL(file.getParent(),
                        questionnaire.getURLSet().iconURL);
        questionnaire.getURLSet().soundURL = ToolKits
                .optAbsoluteQuestionnaireResURL(file.getParent(),
                        questionnaire.getURLSet().soundURL);
        questionnaire.getURLSet().qcmFileURL = file.getParent();
        List<Qcm> qcmList = questionnaire.getQcms();
        for (Qcm qcm : qcmList) {
            Question question = qcm.getQuestion();
            question.setAnimationURL(ToolKits.optAbsoluteQuestionnaireResURL(
                    file.getParent(), question.getAnimationURL()));
            question.setImageURL(ToolKits.optAbsoluteQuestionnaireResURL(
                    file.getParent(), question.getImageURL()));
            question.setVideoURL(ToolKits.optAbsoluteQuestionnaireResURL(
                    file.getParent(), question.getVideoURL()));
            question.setFontURL(ToolKits.optAbsoluteQuestionnaireResURL(
                    file.getParent(), question.getFontURL()));

        }
        return questionnaire;
    }

    public static Questionnaire readSourceBinaryFile(String file)
            throws JSONException, FileNotFoundException {
        JSONObject json = new JSONObject(istat.android.base.tools.ToolKits.Stream.streamToString(
                new FileInputStream(file), "UTF_8"));
        return JSONReader.questionnaireFromJSONObject(json);
    }

    public static Questionnaire readSummaryFile(File file)
            throws FileNotFoundException, JSONException {
        Questionnaire questionnaire = readSourceBinaryFile(file);
        questionnaire.getURLSet().iconURL = ToolKits
                .optAbsoluteQuestionnaireResURL(file.getParent(),
                        questionnaire.getURLSet().iconURL);
        questionnaire.getURLSet().soundURL = ToolKits
                .optAbsoluteQuestionnaireResURL(file.getParent(),
                        questionnaire.getURLSet().soundURL);
        questionnaire.getURLSet().qcmFileURL = file.getParent();
        return questionnaire;
    }

    public static Questionnaire readSourceBinaryFile(File file)
            throws JSONException, FileNotFoundException {
        JSONObject json = new JSONObject(istat.android.base.tools.ToolKits.Stream.streamToString(
                new FileInputStream(file), "UTF_8"));
        return JSONReader.questionnaireFromJSONObject(json);
    }

    public static String createQcmSummaryFile(File file, Questionnaire qcms)
            throws IOException {
        FileWriter writer = new FileWriter(file.getAbsoluteFile() + "/"
                + qcms.getTitle() + "." + QcmsFile.FILE_EXTANSION_SUMMARY);
        writer.write(QuestionnaireUtils.createSummaryBinary(qcms));
        writer.flush();
        writer.close();
        return qcms.toString();
    }

    public static String createQcmBinaryFile(File file, Questionnaire qcms)
            throws IOException {
        FileWriter writer = new FileWriter(file.getAbsoluteFile() + "/"
                + qcms.getTitle() + "." + QcmsFile.FILE_EXTANSION_BINARY);
        writer.write(QuestionnaireUtils.createBinary(qcms));
        writer.flush();
        writer.close();
        return qcms.toString();
    }

    public static String createQcmEditableBinaryFile(File file,
                                                     Questionnaire qcms) throws IOException {
        FileWriter writer = new FileWriter(file.getAbsoluteFile() + "/"
                + qcms.getTitle() + "." + QcmsFile.FILE_EXTANSION_SOURCE_CODE);
        writer.write(QuestionnaireUtils.createSourceBinary(qcms));
        writer.flush();
        writer.close();
        return qcms.toString();
    }

    public static String questionnaireToString(Questionnaire qcms) {
        // TODO Auto-generated method stub
        try {
            return JSONWriter.questionnaireToJSONObject(qcms).toString();
        } catch (Exception e) {
            return "" + e;
        }
    }

    // -----------------------------------------------------------------
    public static class JSONWriter {
        public static JSONObject questionnaireToJSONObject(Questionnaire qcms)
                throws JSONException {
            JSONObject json = new JSONObject();
            json.accumulate("id", qcms.getId());
            json.accumulate("title", qcms.getTitle());
            json.accumulate("create_at", qcms.getCreated_at());
            json.accumulate("update_at", qcms.getUpdated_at());
            json.accumulate("author", accumulateAuthorInfo(qcms));
            json.accumulate("info", accumulateInfo(qcms));
            json.accumulate("meta", accumulateMetaInfo(qcms));
            json.accumulate("urls", accumulateURLLink(qcms));
            if (qcms.getSubject() != null) {
                json.putOpt("subjects", qcms.getSubject().toJson());
            }
            if (qcms.getStudyLevels() != null
                    && qcms.getStudyLevels().size() > 0) {
                json.accumulate("study_levels",
                        accumulateStudyLevels(qcms.getStudyLevels()));
            }
            if (qcms.getKeyWords().size() > 0) {
                json.accumulate("keywords",
                        accumulateKeyWords(qcms.getKeyWords()));
            }
            if (qcms.hasQcms()) {
                json.accumulate("qcms",
                        accumulateQcmList(qcms.getQcms()));
            }
            return json;

        }

        private static JSONArray accumulateKeyWords(List<String> keywords) {
            JSONArray array = new JSONArray();
            for (String keyword : keywords) {
                array.put(keyword);
            }
            return array;
        }

        private static JSONObject accumulateInfo(Questionnaire qcms)
                throws JSONException {
            JSONObject json = new JSONObject();
            json.accumulate("description", qcms.getInfo().description);
            json.accumulate("price", qcms.getInfo().price);
            json.accumulate("questionnaire_time",
                    qcms.getInfo().questionnaireTime);
            json.accumulate("question_count", qcms.getInfo().getQuestionCount());
            json.accumulate("language", qcms.getInfo().language);
            // json.accumulate("store_note", qcms.getInfo().storeNote);
            json.accumulate("max_randum", qcms.getInfo().getMaxRandom());
            json.accumulate("auto_correction_enable", qcms.getInfo()
                    .isAutoCorrectionEnable());

            return json;
        }

        private static JSONObject accumulateMetaInfo(Questionnaire qcms)
                throws JSONException {
            JSONObject json = new JSONObject();
            json.accumulate("protection", qcms.getConfig().isProtected());
            json.accumulate("smart_choice", qcms.getConfig()
                    .isSmartChoiceEnable());
            json.accumulate("marks_policy", qcms.getConfig().getMarksPolicy());
            json.accumulate("sdk_version", SDK.VERSION);
            return json;
        }

        private static JSONObject accumulateURLLink(Questionnaire qcms)
                throws JSONException {
            JSONObject json = new JSONObject();
            json.putOpt("url_icon", qcms.getURLSet().iconURL);
            json.putOpt("url_home_sound", qcms.getURLSet().soundURL);
            json.putOpt("url_post_answers", qcms.getURLSet().qcmPostAnswersURL);
            json.putOpt("url_file_questionnaire", qcms.getURLSet().qcmFileURL);
            return json;
        }

        private static JSONObject accumulateAuthorInfo(Questionnaire qcms)
                throws JSONException {
            JSONObject json = new JSONObject();
            json.accumulate("author_name", qcms.getAuthor().name);
            json.accumulate("author_id", qcms.getAuthor().id);
            return json;
        }

        private static JSONArray accumulateStudyLevels(List<StudyLevel> levels)
                throws JSONException {
            JSONArray array = new JSONArray();
            for (StudyLevel level : levels) {
                array.put(level.toJson());
            }
            return array;
        }

        private static JSONArray accumulateQcmList(List<Qcm> qcmList)
                throws JSONException {
            JSONArray array = new JSONArray();
            for (Qcm qcm : qcmList) {
                array.put(QcmUtils.JSONWriter.qcmToJSONObject(qcm));
            }
            return array;
        }
    }

    public static class JSONReader {
        public static Questionnaire questionnaireFromJSONObject(JSONObject json)
                throws JSONException {
            return questionnaireFromJSONObject(json, true);

        }

        public static Questionnaire questionnaireFromJSONObject(
                JSONObject json, boolean loadQcm) throws JSONException {
            Questionnaire qcms = new Questionnaire();
            qcms.setId(json.getString("id"));
            qcms.setTitle(json.getString("title"));
            qcms.setCreated_at(json.getString("create_at"));
            qcms.setUpdated_at(json.getString("update_at"));

            qcms.setAuthor(extractAuthor(json));
            qcms.setInfo(extractInfo(json));
            qcms.setConfig(extractConfig(json));
            if (json.optString("urls") != null) {
                qcms.setURLSet(extractURLSet(json));
            }
            if (json.optJSONArray("keywords") != null) {
                for (String keyword : JSON.JSONArrayToStringList(json
                        .getJSONArray("keywords"))) {
                    if (!TextUtils.isEmpty(keyword)) {
                        qcms.addKeyword(keyword);
                    }
                }
            } else {
                String keyword = json.optString("keywords");
                if (!TextUtils.isEmpty(keyword)) {
                    qcms.addKeyword(keyword);
                }
            }
            // -------
            if (json.optJSONObject("subjects") != null) {
                JSONObject jsonSubject = json.optJSONObject("subjects");
                Subject subject = Subject.fromJson(jsonSubject);
                if (subject != null)
                    qcms.setSubject(subject);
            }
            if (json.optJSONArray("study_levels") != null) {
                for (JSONObject study_level : JSON.JSONArrayToJsonList(json
                        .getJSONArray("study_levels"))) {
                    StudyLevel level = StudyLevel.fromJson(study_level);
                    if (level != null) {
                        qcms.addStudyLevel(level);
                    }
                }
            } else {
                JSONObject study_level = json.optJSONObject("study_levels");
                if (study_level != null) {
                    StudyLevel level = StudyLevel.fromJson(study_level);
                    if (level != null) {
                        qcms.addStudyLevel(level);
                    }
                }
            }
            // -----------
            if (loadQcm) {
                if (json.optJSONArray("qcms") != null) {
                    for (JSONObject qcmJson : JSON.JSONArrayToJsonList(json
                            .getJSONArray("qcms"))) {
                        qcms.addQcm(QcmUtils.JSONReader
                                .qcmFromJSONObject(qcmJson));
                    }
                } else {
                    if (json.optJSONObject("qcms") != null)
                        qcms.addQcm(QcmUtils.JSONReader
                                .qcmFromJSONObject(json.getJSONObject("qcms")));
                }
            }
            return qcms;

        }

        private static QuestionnaireInfo extractInfo(JSONObject jsonO)
                throws JSONException {
            JSONObject json = jsonO.getJSONObject("info");
            QuestionnaireInfo info = new QuestionnaireInfo();
            info.setDescription(json.getString("description"))
                    .setLanguage(json.getString("language"))
                    .setPrice((float) json.getDouble("price"))
                    .setQuestionnaireTime(json.getInt("questionnaire_time"))
                    .setTotalQuestionCount(json.getInt("question_count"))
                    // .setStoreNote(json.getString("store_note"))
                    .setMaxRandom(json.optInt("max_randum"))
                    .setAutoCorrectionEnable(
                            json.optBoolean("auto_correction_enable"));
            return info;
        }

        private static QuestionnaireConfig extractConfig(JSONObject jsonO)
                throws JSONException {
            JSONObject json = jsonO.getJSONObject("meta");
            QuestionnaireConfig config = new QuestionnaireConfig(
                    json.optInt("sdk_version"), json.getBoolean("protection"),
                    json.getBoolean("smart_choice"));
            config.setMarksPolicy(json.getInt("marks_policy"));
            return config;
        }

        private static QuestionnaireURLSet extractURLSet(JSONObject jsonO)
                throws JSONException {
            JSONObject json = jsonO.getJSONObject("urls");
            QuestionnaireURLSet URLSet = new QuestionnaireURLSet(
                    json.optString("url_icon"),
                    json.optString("url_home_sound"),
                    json.optString("url_file_questionnaire"));
            URLSet.qcmPostAnswersURL = json.optString("url_post_answers");
            return URLSet;
        }

        private static QuestionnaireAuthor extractAuthor(JSONObject jsonO)
                throws JSONException {
            JSONObject json = jsonO.getJSONObject("author");
            QuestionnaireAuthor author = new QuestionnaireAuthor(
                    json.getString("author_id"), json.getString("author_name"));
            return author;
        }
    }
}
