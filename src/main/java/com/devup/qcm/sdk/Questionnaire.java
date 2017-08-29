package com.devup.qcm.sdk;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.devup.qcm.sdk.utils.QuestionnaireUtils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Questionnaire extends QuestionnaireSummary implements Parcelable,
        Cloneable {
    public final static String EXTRA_TAG = "questionnaire";
    List<Qcm> QCMs = new ArrayList<Qcm>();

    public Questionnaire() {
    }

    public Questionnaire(String id, String title, String create_at,
                         String update_at) {

        super(id, title, create_at, update_at);
    }

    // TODO Finish to implement this constructor
    public Questionnaire(QuestionnaireSummary summary) {
        this(summary.id, summary.title, summary.created_at, summary.updated_at);
        this.setAuthor(summary.getAuthor());
    }

    public QuestionnaireAuthor getAuthor() {
        return author;
    }

    public List<Qcm> getQcms() {
        return QCMs;
    }

    public QuestionnaireConfig getConfig() {
        return config;
    }

    public QuestionnaireURLSet getURLSet() {
        return URLSet;
    }

    public void setQcms(List<Qcm> qCMs) {
        QCMs = qCMs;
    }

    public Subject getSubject() {
        return subject;
    }

    public QuestionnaireInfo getInfo() {
        if (QCMs != null && QCMs.size() > 0)
            info.questionCount = QCMs.size();
        return info;
    }

    public String getCreated_at() {
        return created_at;
    }

    public List<String> getKeyWords() {
        return keyWord;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getTitle() {
        return title;
    }

    public List<StudyLevel> getStudyLevels() {
        return studyLevelCompatibility;
    }

    public String getId() {
        return id;
    }


    public boolean isSummary() {
        return !hasQcms();
    }

    public QuestionnaireSummary getSummary() {
        return (QuestionnaireSummary) this;
    }

    public boolean hasQcms() {
        return QCMs != null && QCMs.size() > 0;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.toString());
    }

    public static final Parcelable.Creator<Questionnaire> CREATOR = new Parcelable.Creator<Questionnaire>() {
        @Override
        public Questionnaire createFromParcel(Parcel source) {
            try {
                return QuestionnaireUtils.JSONReader
                        .questionnaireFromJSONObject(new JSONObject(source
                                .readString()));
            } catch (JSONException e) {

                e.printStackTrace();
                Log.e("Questionnaire", "createFromParcel:ERROR::" + e);
                return null;
            }
        }

        @Override
        public Questionnaire[] newArray(int size) {
            return new Questionnaire[size];
        }
    };

    @Override
    public String toString() {

        return QuestionnaireUtils.questionnaireToString(this);
    }

    public void addQcm(Qcm qcm) {
        if (QCMs != null) {
            QCMs.add(qcm);
        }
    }

    public void eraseQcms() {
        if (QCMs != null) {
            QCMs.clear();
        }
    }


    public Object clone() {
        Object o = null;
        try {
            // On recupere l'instance a renvoyer par l'appel de la
            // methode super.clone()
            o = super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver car nous impl�mentons
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }
        // on renvoie le clone
        return o;
    }

}
