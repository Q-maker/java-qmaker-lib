package com.devup.qcm.sdk;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

public class Test implements Parcelable {
	public final static String EXTRA_TAG = "test";
	Questionnaire mQuestionnaire;
	public int timeLeft = 0;
	List<Exercise> mExercises = new ArrayList<Exercise>();
	boolean valid = true;
	boolean indefinite = false;
	public final static int VALUE_MAX_TIME_TO_BECOME_INFINITE = 1000 * 60 * 60 * 4;

	public Test(Questionnaire questionnaire) {
		mQuestionnaire = questionnaire;
		if (questionnaire != null) {
			timeLeft = questionnaire.getInfo().questionnaireTime;
			buildExercises();
		} else
			valid = false;
		indefinite = timeLeft >= VALUE_MAX_TIME_TO_BECOME_INFINITE;

	}

	public int getTimeLeft() {
		if (timeLeft < 0)
			return 0;
		return timeLeft;
	}

	public boolean isIndefinite() {
		return indefinite;
	}

	public boolean isValid() {
		return valid;
	}

	public boolean isTimeOut() {
		return timeLeft <= 0;
	}

	public void setExercises(List<Exercise> mExercises) {
		this.mExercises = mExercises;
	}

	public void setExercise(int position, Exercise mExercises) {
		this.mExercises.set(position, mExercises);
	}

	public void addExercise(Exercise exercise) {
		mExercises.add(exercise);
	}

	public void eraseExercises() {
		mExercises = new ArrayList<Exercise>();
	}

	public List<Exercise> getExercises() {
		return mExercises;
	}

	public Exercise getExercise(int index) {
		return mExercises.get(index);
	}

	public int getScore() {
		int score = 0;
		for (Exercise exercise : mExercises) {
			int marksPolicy = mQuestionnaire.getConfig().getMarksPolicy();
			score += exercise.getMarks(marksPolicy);
		}
		return score;
	}

	public int getTotalScore() {
		int score = 0;
		for (Exercise exercise : mExercises) {
			score += exercise.getTotalMarks();
		}
		return score;
	}

	public String getAverage() {
		int score = getScore();
		return (score > 0 ? score : 0) + "/" + getTotalScore();
	}

	public int getEchecCount() {
		return mExercises.size() - getSuccessCount();
	}

	public int getProspectedCount() {
		int number = 0;
		for (Exercise exercise : mExercises) {
			if (exercise.hasBeenProspected())
				number++;
		}
		return number;

	}

	public int getNotProspectedCount() {
		return getExerciseCount() - getProspectedCount();
	}

	public int getExerciseCount() {
		if (isValid())
			return mExercises.size();
		else
			return 0;
	}

	// public int getComposedCount() {
	// int number = 0;
	// for (Exercise exercise : mExercises) {
	// if (!QcmManager.JSONWriter
	// .answersToJSONObject(exercise.answers)
	// .toString()
	// .equals(QcmManager.JSONWriter.answersToJSONObject(
	// exercise.getPropositions()).toString()))
	// number++;
	// }
	// return number;
	// }

	public int getComposedCount() {
		int number = 0;
		for (Exercise exercise : mExercises) {
			if (exercise.hasBeenComposed())
				number++;
		}
		return number;
	}

	public int getSuccessCount() {
		int number = 0;
		for (Exercise exercise : mExercises) {
			if (exercise.getMarks(getMarksPolicy()) > 0)
				number++;
		}
		return number;
	}

	public Questionnaire getQuestionnaire() {
		return mQuestionnaire;
	}

	public List<Qcm.Proposition> getAnswers() {
		List<Qcm.Proposition> answers = new ArrayList<Qcm.Proposition>();
		for (Exercise exo : mExercises)
			answers.addAll(exo.getAnswers());

		return answers;
	}

	public int getMarksPolicy() {
		return mQuestionnaire.getConfig().getMarksPolicy();
	}

	// public List<Answer> getBadAnswerList(){
	// List<Answer> answers =new ArrayList<Answer>();
	// for(Exercise exo:mExercises)
	// {
	// if(exo.getMarks(getMarksPolicy())!=exo.getTotalMarks())
	// answers.addAll(exo.getPropositions());
	// }
	//
	// return answers;
	// }
	public List<Exercise> getExerciseWithBadAnswers() {
		List<Exercise> exercises = new ArrayList<Exercise>();
		for (Exercise exo : mExercises) {
			if (exo.getMarks(getMarksPolicy()) != exo.getTotalMarks())
				exercises.add(exo);
		}

		return exercises;
	}

	public List<Exercise> getExerciseWithGoodAnswers() {
		List<Exercise> exercises = new ArrayList<Exercise>();
		for (Exercise exo : mExercises) {
			if (exo.getMarks(getMarksPolicy()) == exo.getTotalMarks())
				exercises.add(exo);
		}

		return exercises;
	}

	public List<Exercise> getExerciseWithNotBadAnswers() {
		List<Exercise> exercises = new ArrayList<Exercise>();
		for (Exercise exo : mExercises) {
			if (exo.getMarks(getMarksPolicy()) != 0)
				exercises.add(exo);
		}

		return exercises;
	}

	private void buildExercises() {
		if (mQuestionnaire.getQcms() == null
				|| mQuestionnaire.getQcms().size() == 0) {
			valid = false;
			return;
		}
		for (Qcm qcm : mQuestionnaire.getQcms()) {
			mExercises.add(new Exercise(qcm));
		}
		indefinite = isIndefinite();
	}

	@Override
	public String toString() {
		
		Questionnaire qcms = new Questionnaire();
		QuestionnaireSummary.QuestionnaireInfo info = new QuestionnaireSummary.QuestionnaireInfo();
		info.setQuestionnaireTime(timeLeft);
		qcms.setInfo(info);
		for (Exercise e : mExercises)
			qcms.addQcm(e.buildQcmGoodReply());
		return qcms.toString();
	}

	public Questionnaire getCompletedQuestionnaire() {
		Questionnaire questionnaire = (Questionnaire) mQuestionnaire.clone();
		List<Qcm> qcms = new ArrayList<Qcm>();
		for (Exercise exercise : getExercises()) {
			Qcm exoQcm = exercise.getQcm();
			Qcm tmp = new Qcm(exoQcm.question, exoQcm.getPropositions());
			qcms.add(tmp);
		}
		questionnaire.setQcms(qcms);
		return questionnaire;
	}

	public CopySheet getCopySheet() {
		return null;
	}

	// private String executeScrip(){
	// return "HELLO WORLD";
	// }
	@Override
	public int describeContents() {
		
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		// Questionnaire mQuestionnaire;
		// public int timeLeft = 0;
		// List<Exercise> mExercises = new ArrayList<Exercise>();
		// boolean valid = true;
		dest.writeParcelable(mQuestionnaire, flags);
		dest.writeString(indefinite + "");
		dest.writeInt(timeLeft);
		dest.writeInt(mExercises.size());
		for (Exercise e : mExercises) {
			dest.writeParcelable(e, flags);
		}

	}

	public static final Parcelable.Creator<Test> CREATOR = new Parcelable.Creator<Test>() {
		@Override
		public Test createFromParcel(Parcel src) {
			Questionnaire tmpQ = src.readParcelable(Questionnaire.class
					.getClassLoader());
			Test test = new Test(tmpQ);
			test.indefinite = Boolean.valueOf(src.readString());
			test.timeLeft = src.readInt();
			List<Exercise> tmpEs = new ArrayList<Exercise>();
			for (int i = 0; i < src.readInt(); i++) {
				Parcelable parcelable = src.readParcelable(Exercise.class
						.getClassLoader());
				tmpEs.add((Exercise) parcelable);

			}
			test.setExercises(tmpEs);
			test.eraseExercises();
			return test;
		}

		@Override
		public Test[] newArray(int size) {
			return new Test[size];
		}
	};
	@Override
	public Object clone() {
		Object o = null;
		try {
			// On récupure l'instance à renvoyer par l'appel de la
			// méthode super.clone()
			o = super.clone();
		} catch (CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implémentons
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		// on renvoie le clone
		return o;
	}

}
