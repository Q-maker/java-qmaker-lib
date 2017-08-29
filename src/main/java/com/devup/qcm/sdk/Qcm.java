package com.devup.qcm.sdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.devup.qcm.sdk.utils.QcmUtils;

public class Qcm implements Parcelable {
    StudyLevel studyLevel;
    Subject subject;
    Question question;
    List<Proposition> answers = new ArrayList<Proposition>();

    public Qcm() {
    }

    public Qcm(Question question, List<Proposition> answers) {
        this.question = question;
        if (answers != null && answers.size() > 0)
            this.answers = answers;
    }

    public Qcm(Question question, Proposition... answers) {
        this.question = question;
        if (answers != null && answers.length > 0)
            this.answers.addAll(Arrays.asList(answers));
    }

    public boolean hasMultipleAnswer() {
        boolean out = false;
        for (Proposition answer : answers) {
            if (out && answer.truth)
                return true;
            if (answer.truth) {
                out = true;
            }
        }
        return out;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Proposition> getPropositions() {
        return answers;
    }

    public void setPropositions(List<Proposition> answers) {
        this.answers = answers;
    }

    public void setPropositions(Proposition... answers) {
        this.answers = Arrays.asList(answers);
    }

    public void addProposition(Proposition answer) {
        answers.add(answer);
    }

    @Override
    public String toString() {
        
        return QcmUtils.qcmToString(this);
    }

    public static class Question extends QcmEntity {
        public final static int TYPE_AUTO = 0;
        String comment;
        int type = TYPE_AUTO;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        @Override
        public String toString() {
            
            return QcmUtils.questionToString(this);
        }

        @Override
        public Question clone() {
            Question question = (Question) clone(this);
            question.comment = this.comment;
            return question;
        }
    }

    public List<String> getImageURLList() {
        List<String> out = new ArrayList<String>();
        if (question.imageURL != null)
            out.add(question.imageURL);
        for (Proposition answer : answers) {
            if (answer.imageURL != null)
                out.add(answer.imageURL);
        }
        return out;
    }

    public List<String> getSoundURLList() {
        List<String> out = new ArrayList<String>();
        if (question.soundURL != null)
            out.add(question.soundURL);
        for (Proposition answer : answers) {
            if (answer.soundURL != null)
                out.add(answer.soundURL);
        }
        return out;
    }

    public StudyLevel getStudyLevel() {
        return studyLevel;
    }

    public Subject getSubject() {
        return subject;
    }

    public List<String> getVideoURLList() {
        List<String> out = new ArrayList<String>();
        if (question.videoURL != null)
            out.add(question.videoURL);
        for (Proposition answer : answers) {
            if (answer.videoURL != null)
                out.add(answer.videoURL);
        }
        return out;
    }

    public List<String> getAnimationURLList() {
        List<String> out = new ArrayList<String>();
        if (question.animationURL != null)
            out.add(question.animationURL);
        for (Proposition answer : answers) {
            if (answer.animationURL != null)
                out.add(answer.animationURL);
        }
        return out;
    }

    public List<String> getfontURLList() {
        List<String> out = new ArrayList<String>();
        if (question.fontURL != null)
            out.add(question.fontURL);
        for (Proposition answer : answers) {
            if (answer.fontURL != null)
                out.add(answer.fontURL);
        }
        return out;
    }

    public static class Proposition extends QcmEntity implements Cloneable {
        boolean truth = false;

        public Proposition() {
        }

        public Proposition(String label) {
            this.label = label;
        }

        public Proposition(String label, boolean truth) {
            this.label = label;
            this.truth = truth;
        }

        public boolean getTruth() {
            return truth;
        }

        public void setTruth(boolean isTrue) {
            this.truth = isTrue;
        }

        public void setTruth(String isTrue) {
            this.truth = Boolean.valueOf(isTrue);
        }

        @Override
        public String toString() {
            
            return QcmUtils.answerToString(this);
        }

        public boolean sameAs(Proposition answer) {
            return QcmUtils.isEqual(this, answer);
        }
        // @Override
        // public Answer clone() {
        // Answer answer = (Answer) clone(this);
        // //answer.truth = this.truth;
        // return answer;
        // }

    }

    public static abstract class QcmEntity {
        public static int VISUAL_TYPE_IMAGE = 3, VISUAL_TYPE_VIDEO = 4,
                VISUAL_TYPE_ANIMATION = 5;
        long createdAt = System.currentTimeMillis();
        long updatedAt = createdAt;
        String imageURL, soundURL, videoURL, label, fontURL, animationURL;

        public QcmEntity() {
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public QcmEntity(String label) {
            this.label = label;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void setSoundURL(String soundURL) {
            this.soundURL = soundURL;
        }

        public void setVideoURL(String videoURL) {
            this.videoURL = videoURL;
        }

        public String getImageURL() {
            return TextUtils.isEmpty(imageURL) ? null : imageURL;
        }

        public String getLabel() {
            return TextUtils.isEmpty(label) ? null : label;
        }

        public String getSoundURL() {
            return TextUtils.isEmpty(soundURL) ? null : soundURL;
        }

        public String getVideoURL() {
            return TextUtils.isEmpty(videoURL) ? null : videoURL;
        }

        public String getAnimationURL() {
            return TextUtils.isEmpty(animationURL) ? null : animationURL;
        }

        public String getFontURL() {
            return TextUtils.isEmpty(fontURL) ? null : fontURL;
        }

        public void setAnimationURL(String animationURL) {
            this.animationURL = animationURL;
        }

        public void setFontURL(String fontURL) {
            this.fontURL = fontURL;
        }

        public void setVisualURL(String visualURL, int type) {
            if (type == VISUAL_TYPE_IMAGE)
                setImageURL(visualURL);
            if (type == VISUAL_TYPE_VIDEO)
                setVideoURL(visualURL);
            if (type == VISUAL_TYPE_ANIMATION)
                setAnimationURL(visualURL);
        }

        public String getVisualURL() {

            if (videoURL != null)
                return videoURL;
            if (animationURL != null)
                return animationURL;

            return imageURL;
        }

        public QcmEntity clone(QcmEntity entity) {
            entity.animationURL = this.animationURL;
            entity.fontURL = this.fontURL;
            entity.imageURL = this.imageURL;
            entity.label = this.label;
            entity.soundURL = this.soundURL;
            entity.videoURL = this.videoURL;
            return entity;
        }

        public Object clone() {
            Object o = null;
            try {
                // On r�cup�re l'instance � renvoyer par l'appel de la
                // m�thode super.clone()
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

    @Override
    public int describeContents() {
        
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(toString());
    }

    public static final Parcelable.Creator<Qcm> CREATOR = new Parcelable.Creator<Qcm>() {
        @Override
        public Qcm createFromParcel(Parcel source) {

            return QcmUtils.JSONReader.qcmFromJSONObject(source.readString());

        }

        @Override
        public Qcm[] newArray(int size) {
            return new Qcm[size];
        }
    };

}
