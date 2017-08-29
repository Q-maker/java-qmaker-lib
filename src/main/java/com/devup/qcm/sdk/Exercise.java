package com.devup.qcm.sdk;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.devup.qcm.sdk.utils.QcmUtils;

public class Exercise implements Parcelable {
	public String TAG = "" + Math.random();
	Qcm mQcm = new Qcm();
	List<Qcm.Proposition> answers = new ArrayList<Qcm.Proposition>();
	boolean prospected = false, composed = false;
	public final static int MARKS_POLICY_SUSTRACT_ON_ECHEC = 66,
			MARKS_POLICY_SUSTRACT_FOR_EACH_ON_ECHEC = 67,
			MARKS_POLICY_ANNUL_ON_ECHEC = 68;

	public Exercise(Qcm qcm) {
		mQcm = qcm;
	}

	public int getMarks(int correctionMode) {
		MarksPolicy currentPolicy = marksPolicy_sustract_on_echec;
		switch (correctionMode) {
		case MARKS_POLICY_SUSTRACT_ON_ECHEC:
			currentPolicy = marksPolicy_sustract_on_echec;
			break;
		case MARKS_POLICY_SUSTRACT_FOR_EACH_ON_ECHEC:
			currentPolicy = marksPolicy_sustract_for_each_echec;
			break;
		case MARKS_POLICY_ANNUL_ON_ECHEC:
			currentPolicy = marksPolicy_annul_on_echec;
			break;
		default:
			if (mMarksPolicy != null) {
				currentPolicy = marksPolicy_sustract_on_echec;
			}
		}
		return currentPolicy.getComputedExerciseMark(this, correctionMode);
	}

	public void setAnswers(List<Qcm.Proposition> answers) {
		this.answers = answers;
	}

	public void addAnswer(Qcm.Proposition answer) {
		answers.add(answer);
	}

	public void addAnswers(List<Qcm.Proposition> answers) {
		this.answers.addAll(answers);
	}

	public void removeAllAnswers() {
		answers = new ArrayList<Qcm.Proposition>();
	}

	public List<Qcm.Proposition> getAnswers() {
		return answers;
	}

	public List<Qcm.Proposition> getDefaultAnswers() {
		List<Qcm.Proposition> answers = new ArrayList<Qcm.Proposition>();
		for (Qcm.Proposition answer : mQcm.getPropositions()) {
			Qcm.Proposition tmp = (Qcm.Proposition) answer.clone();
			tmp.setTruth(false);
			answers.add(tmp);
		}
		return answers;
	}

	public int getTotalMarks() {
		return QcmUtils.getPropositionsWithTruth(mQcm, true).size();
	}

	public boolean hasBeenProspected() {
		return prospected;
	}

	public boolean hasBeenComposed() {
		return composed;
	}

	public void setProspected(boolean prospected) {
		this.prospected = prospected;
	}

	public void setComposed(boolean composed) {
		this.composed = composed;
	}

	public Qcm getQcm() {
		return mQcm;
	}

	@Override
	public String toString() {
		
		Qcm tmp = new Qcm(mQcm.question, answers);
		return tmp.toString();
	}

	public Qcm buildQcmGoodReply() {
		
		return new Qcm(mQcm.question, answers);
	}

	@Override
	public int describeContents() {
		
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(mQcm.toString());
		dest.writeString(QcmUtils.JSONWriter.answersToJSONObject(answers)
				.toString());
	}

	public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
		@Override
		public Exercise createFromParcel(Parcel source) {
			Exercise exercise = new Exercise(
					QcmUtils.JSONReader.qcmFromJSONObject(source.readString()));
			exercise.setAnswers(QcmUtils.JSONReader.extractAnswers(source
					.readString()));
			return exercise;
		}

		@Override
		public Exercise[] newArray(int size) {
			return new Exercise[size];
		}
	};

	public void applyMarksPolicyIfNeeded(MarksPolicy mMarksPolicy) {
		this.mMarksPolicy = mMarksPolicy;
	}

	private MarksPolicy mMarksPolicy;

	public static interface MarksPolicy {
		public int getComputedExerciseMark(Exercise exercise, int policy_flag);
	}

	static MarksPolicy marksPolicy_sustract_on_echec = new MarksPolicy() {

		@Override
		public int getComputedExerciseMark(Exercise exercise, int policy_flag) {
			
			Qcm mQcm = exercise.getQcm();
			List<Qcm.Proposition> answers = exercise.getAnswers();
			int marks = 0;
			List<Qcm.Proposition> trueAnswers = QcmUtils
					.getPropositionsWithTruth(mQcm, true);
			List<Qcm.Proposition> proposedAnswers = QcmUtils.getPropositionsWithTruth(
					answers, true);
			for (Qcm.Proposition proposedAnswer : proposedAnswers) {
				boolean match = false;
				for (Qcm.Proposition trueAnswer : trueAnswers) {
					if (proposedAnswer.sameAs(trueAnswer)) {
						marks++;
						match = true;
						break;
					}
				}
				if (!match) {
					return -1;
				}
			}
			return marks;
		}
	};
	static MarksPolicy marksPolicy_sustract_for_each_echec = new MarksPolicy() {

		@Override
		public int getComputedExerciseMark(Exercise exercise, int policy_flag) {
			
			Qcm mQcm = exercise.getQcm();
			List<Qcm.Proposition> answers = exercise.getAnswers();
			int marks = 0;
			List<Qcm.Proposition> trueAnswers = QcmUtils
					.getPropositionsWithTruth(mQcm, true);
			List<Qcm.Proposition> proposedAnswers = QcmUtils.getPropositionsWithTruth(
					answers, true);
			for (Qcm.Proposition proposedAnswer : proposedAnswers) {
				boolean match = false;
				for (Qcm.Proposition trueAnswer : trueAnswers) {
					if (proposedAnswer.sameAs(trueAnswer)) {
						marks++;
						match = true;
						break;
					}
				}
				if (!match) {
					marks--;
				}
			}
			return marks;
		}
	};
	static MarksPolicy marksPolicy_annul_on_echec = new MarksPolicy() {

		@Override
		public int getComputedExerciseMark(Exercise exercise, int policy_flag) {
			
			Qcm mQcm = exercise.getQcm();
			List<Qcm.Proposition> answers = exercise.getAnswers();
			int marks = 0;
			List<Qcm.Proposition> trueAnswers = QcmUtils
					.getPropositionsWithTruth(mQcm, true);
			List<Qcm.Proposition> proposedAnswers = QcmUtils.getPropositionsWithTruth(
					answers, true);
			for (Qcm.Proposition proposedAnswer : proposedAnswers) {
				boolean match = false;
				for (Qcm.Proposition trueAnswer : trueAnswers) {
					if (proposedAnswer.sameAs(trueAnswer)) {
						marks++;
						match = true;
						break;
					}
				}
				if (!match) {
					return 0;
				}
			}
			return marks;
		}
	};

}
